package am2.common.power;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import am2.ArsMagica2;
import am2.common.LogHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PowerNodeCache{

	public static String extension = ".amc";
	public static String folder = "AM2PowerData";
	private static final String pndID = "pnd_%d_%d";

	private static HashMap<Integer, File> saveDirs = new HashMap<Integer, File>();
	private static HashMap<String, File> saveFilesCached = new HashMap<String, File>();
	private static HashMap<RegionCoordinates, NBTTagCompound> dataCache = new HashMap<RegionCoordinates, NBTTagCompound>();

	public static final PowerNodeCache instance = new PowerNodeCache();

	private File getFileFromChunk(World world, ChunkPos chunk, boolean createNew){

		File saveFolder = saveDirs.get(world.provider.getDimension());
		if (saveFolder == null){
			ISaveHandler handler = world.getSaveHandler();
			if (handler instanceof SaveHandler){
				saveFolder = new File(((SaveHandler)handler).getWorldDirectory(), folder);
				saveFolder.mkdirs();
				saveFolder = new File(saveFolder, String.format("DIM%d", world.provider.getDimension()));
				saveFolder.mkdirs();
				saveDirs.put(world.provider.getDimension(), saveFolder);
			}else{
				return null;
			}
		}

		int rX = (int)Math.floor(chunk.x / 32);
		int rZ = (int)Math.floor(chunk.z / 32);

		String fileName = String.format("%d_%d%s", rX, rZ, extension);

		File file = saveFilesCached.get(fileName);

		if (file != null)
			return file;


		file = new File(saveFolder, fileName);

		if (!file.exists()){
			if (createNew){
				try{
					file.createNewFile();
				}catch (Throwable t){
					t.printStackTrace();
				}
			}else{
				return null;
			}
		}

		saveFilesCached.put(fileName, file);

		return file;
	}

	public NBTTagCompound getNBTForChunk(World world, ChunkPos chunk){
		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.getDimension());
		if (dataCache.containsKey(rc)){
			NBTTagCompound compound = dataCache.get(rc);
			if (compound.hasKey("AM2PowerData"))
				return compound;
			dataCache.remove(rc);
		}
		return LoadNBTFromFile(world, chunk);
	}

	private void SaveNBTToFile(World world, ChunkPos chunk, NBTTagCompound compound, boolean flushImmediate){

		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.getDimension());

		NBTTagCompound dataCompound = dataCache.get(rc);

		if (dataCompound == null){
			File file = getFileFromChunk(world, chunk, true);
			if (file == null || (!file.canWrite() && !file.setWritable(true)) || (!file.canRead() && !file.setReadable(true))){
				LogHelper.error("Unable to obtain file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read/write access to the Minecraft instance folder.", chunk.x, chunk.z);
				return;
			}
			try{
				//read the existing data out
				dataCompound = CompressedStreamTools.read(file);
			}catch (Throwable e){
				//recover
				dataCompound = new NBTTagCompound();
			}
		}

		//set the new compound in the NBT
		dataCompound.setTag(getPNDIdentifier(chunk), compound);

		if (flushImmediate){
			File file = getFileFromChunk(world, chunk, true);
			if (file == null || (!file.canWrite() && !file.setWritable(true)) || (!file.canRead() && !file.setReadable(true))){
				LogHelper.error("Unable to obtain file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read/write access to the Minecraft instance folder.", chunk.x, chunk.z);
				return;
			}
			try{
				//write the modified compound back to the file
				CompressedStreamTools.write(dataCompound, file);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private NBTTagCompound LoadNBTFromFile(World world, ChunkPos chunk){

		RegionCoordinates rc = new RegionCoordinates(chunk, world.provider.getDimension());

		NBTTagCompound dataCompound = dataCache.get(rc);

		if (dataCompound == null){
			File file = getFileFromChunk(world, chunk, false);
			if (file == null){
				return null;
			}
			if ((!file.canRead() && !file.setReadable(true))){
				LogHelper.error("Unable to obtain readable file handle!  The power system data for the chunk at %d, %d will NOT be saved!  To fix this, make sure you have read access to the Minecraft instance folder.", chunk.x, chunk.z);
				return null;
			}

			try{
				//read the existing data out
				dataCompound = CompressedStreamTools.read(file);
			}catch (Throwable e){
				//recover
				dataCompound = new NBTTagCompound();
			}

			dataCache.put(rc, dataCompound);
		}

		if (dataCompound == null){
			dataCompound = new NBTTagCompound();
			dataCache.put(rc, dataCompound);
		}

		NBTTagCompound innerCompound = dataCompound.getCompoundTag(getPNDIdentifier(chunk));
		return innerCompound;
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event){
		if (!event.getWorld().isRemote && PowerNodeRegistry.For(event.getWorld()).hasDataForChunk(event.getChunk())){
			NBTTagCompound dataCompound = new NBTTagCompound();
			PowerNodeRegistry.For(event.getWorld()).SaveChunkToNBT(event.getChunk().getChunkCoordIntPair(), dataCompound);
			PowerNodeRegistry.For(event.getWorld()).unloadChunk(event.getChunk());
			SaveNBTToFile(event.getWorld(), event.getChunk().getChunkCoordIntPair(), dataCompound, false);
		}
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		NBTTagCompound dataCompound = LoadNBTFromFile(event.getWorld(), event.getChunk().getChunkCoordIntPair());
		if (dataCompound != null)
			PowerNodeRegistry.For(event.getWorld()).LoadChunkFromNBT(event.getChunk().getChunkCoordIntPair(), dataCompound);
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event){
		World world = event.getWorld();

		if (world.isRemote)
			return;

		HashMap<ChunkPos, NBTTagCompound> saveData = PowerNodeRegistry.For(world).saveAll();
		for (ChunkPos pair : saveData.keySet()){
			SaveNBTToFile(world, pair, saveData.get(pair), ArsMagica2.config.savePowerDataOnWorldSave());
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event){
		World world = event.getWorld();
		saveWorldToFile(world);
	}

//	private void cacheToFile(World world, RegionCoordinates coords){
//		NBTTagCompound cachedRegion = dataCache.get(coords);
//		if (!cachedRegion.hasKey("AM2PowerData"))
//			return;
//		int xBase = coords.x * 32;
//		int zBase = coords.z * 32;
//		for (int x = 0; x < 32; ++x){
//			for (int z = 0; z < 32; ++z){
//				ChunkPos pair = new ChunkPos(xBase + x, zBase + z);
//				NBTTagCompound compound = cachedRegion.getCompoundTag(getPNDIdentifier(pair));
//				if (compound != null){
//					SaveNBTToFile(world, pair, compound, true);
//				}
//			}
//		}
//	}

	public void saveWorldToFile(World world){
		if (world.isRemote)
			return;

		LogHelper.trace("Saving all cached power data for DIM %d to disk", world.provider.getDimension());

		//cached data to file
		Iterator<RegionCoordinates> it = dataCache.keySet().iterator();
		while (it.hasNext()){
			RegionCoordinates rc = it.next();
			if (rc.dimension == world.provider.getDimension()){
				it.remove();
			}
		}

		//live data to file (may override cache, but that's what we want as live would be newer)
		HashMap<ChunkPos, NBTTagCompound> saveData = PowerNodeRegistry.For(world).saveAll();
		for (ChunkPos pair : saveData.keySet()){
			SaveNBTToFile(world, pair, saveData.get(pair), true);
		}
		PowerNodeRegistry.For(world).unloadAll();
		saveDirs.remove(world.provider.getDimension());
	}

	private String getPNDIdentifier(ChunkPos chunk){
		return String.format(pndID, chunk.x, chunk.z);
	}
}
