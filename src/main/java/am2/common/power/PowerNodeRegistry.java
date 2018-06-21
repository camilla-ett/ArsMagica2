package am2.common.power;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import am2.api.power.IPowerNode;
import am2.common.LogHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

public class PowerNodeRegistry{

	//private static final HashMap<Integer, PowerNodeRegistry> dimensionPowerManagers = new HashMap<Integer, PowerNodeRegistry>();
	private static final HashMap<Integer, PowerNodeRegistry> serverDimensionPowerManagers = new HashMap<Integer, PowerNodeRegistry>();
	private static final HashMap<Integer, PowerNodeRegistry> clientDimensionPowerManagers = new HashMap<Integer, PowerNodeRegistry>();
	private static final PowerNodeRegistry dummyRegistry = new PowerNodeRegistry();

	static final int POWER_SEARCH_RADIUS = 10; //The literal power search radius
	static final int POWER_SEARCH_RADIUS_SQ = 100; //The power search radius squared.  This is used for faster vector math.
	static final int MAX_POWER_SEARCH_RADIUS = 10000; //The maximum power search radius (squared)

	public static final PowerNodeRegistry For(World world){
		if (world == null)
			return dummyRegistry;
		HashMap<Integer, PowerNodeRegistry> dimensionPowerManagers = world.isRemote ? clientDimensionPowerManagers : serverDimensionPowerManagers;

		if (dimensionPowerManagers.containsKey(world.provider.getDimension())){
			return dimensionPowerManagers.get(world.provider.getDimension());
		}else{
			PowerNodeRegistry reg = new PowerNodeRegistry();
			dimensionPowerManagers.put(world.provider.getDimension(), reg);
			return reg;
		}

	}

	private PowerNodeRegistry(){
		ChunkCoordComparator comparator = new ChunkCoordComparator();
		powerNodes = new TreeMap<ChunkPos, HashMap<BlockPos, PowerNodeEntry>>(comparator);
	}

	private TreeMap<ChunkPos, HashMap<BlockPos, PowerNodeEntry>> powerNodes;

	public void registerPowerNode(IPowerNode<?> node){
		registerPowerNodeInternal(node);
	}

	PowerNodeEntry registerPowerNodeInternal(IPowerNode<?> node){
		ChunkPos chunk = getChunkFromNode(node);
		HashMap<BlockPos, PowerNodeEntry> nodeList;
		TileEntity te = ((TileEntity)node);
		World world = te.getWorld();
		if (powerNodes.containsKey(chunk)){
			nodeList = powerNodes.get(chunk);
			LogHelper.trace(String.format("Located Power Node list for chunk %d, %d", chunk.chunkXPos, chunk.chunkZPos));
		}else{
			LogHelper.trace("Node list not found.  Checking cache/files for prior data");
			NBTTagCompound compound = PowerNodeCache.instance.getNBTForChunk(world, chunk);
			nodeList = new HashMap<BlockPos, PowerNodeEntry>();
			if (compound == null || !compound.hasKey("AM2PowerData")){
				powerNodes.put(chunk, nodeList);
				LogHelper.trace("Prior node list not found.  Created Power Node list for chunk %d, %d", chunk.chunkXPos, chunk.chunkZPos);
			}else{
				LoadChunkFromNBT(chunk, compound);
				nodeList = powerNodes.get(chunk);
				//sanity check
				if (nodeList == null) {
					nodeList = new HashMap<BlockPos, PowerNodeEntry>();
					powerNodes.put(chunk, nodeList);
				}
				LogHelper.trace(String.format("Loaded power data for chunk %d, %d", chunk.chunkXPos, chunk.chunkZPos));
			}
		}

		BlockPos nodeLoc = ((TileEntity)node).getPos();

		//prevent duplicate registrations
		if (nodeList.containsKey(nodeLoc))
			return nodeList.get(nodeLoc);

		PowerNodeEntry pnd = new PowerNodeEntry();

		nodeList.put(nodeLoc, pnd);
		LogHelper.trace("Successfully registered power node at {%d, %d, %d}", ((TileEntity)node).getPos().getX(), ((TileEntity)node).getPos().getY(), ((TileEntity)node).getPos().getZ());

		return pnd;
	}

	public void removePowerNode(IPowerNode<?> node){
		ChunkPos chunk = getChunkFromNode(node);
		removePowerNode(chunk, ((TileEntity)node).getPos());
	}

	/**
	 * Attempts to pair the source and destination nodes by either a direct link or by going through conduits.
	 *
	 * @param powerSource The power source
	 * @param destination The destination point
	 * @return A localized message to return to the entity attempting to pair the nodes, either of success or why it failed.
	 */
	public String tryPairNodes(IPowerNode<?> powerSource, IPowerNode<?> destination){
		//some simple validation

		if (powerSource == destination){
			return I18n.format("am2.tooltip.nodePairToSelf");
		}

		//Can the power source provide any of the valid power types for the destination?
		ArrayList<PowerTypes> typesProvided = new ArrayList<PowerTypes>();
		for (PowerTypes type : destination.getValidPowerTypes()){
			if (powerSource.canProvidePower(type)){
				typesProvided.add(type);
			}
		}
		if (typesProvided.size() == 0){
			//no valid power types can be provided
			return I18n.format("am2.tooltip.noSupportedPowertypes");
		}

		//set up vectors and calculate distance for pathing purposes
		BlockPos sourceLocation = ((TileEntity)powerSource).getPos();
		BlockPos destLocation = ((TileEntity)destination).getPos();
		double rawDist = sourceLocation.distanceSq(destLocation);

		if (rawDist > MAX_POWER_SEARCH_RADIUS){
			return I18n.format("am2.tooltip.nodesTooFar");
		}

		//construct a list of all valid power types common between the source and destination
		int successes = 0;

		for (PowerTypes type : typesProvided){
			LinkedList<BlockPos> powerPath = new LinkedList<BlockPos>();
			PowerNodePathfinder pathfinder = new PowerNodePathfinder(((TileEntity)powerSource).getWorld(), sourceLocation, destLocation, type);
			List<BlockPos> path = pathfinder.compute(sourceLocation);
			if (path == null)
				continue;
			for (BlockPos vec : path){
				powerPath.addFirst(vec);
			}
			successes++;
			getPowerNodeData(destination).registerNodePath(type, powerPath);
		}

		//are the nodes too far apart?
		if (successes == 0){
			return I18n.format("am2.tooltip.noPathFound");
		}

		if (successes == typesProvided.size())
			return I18n.format("am2.tooltip.success");
		return I18n.format("am2.tooltip.partialSuccess");
	}

	/**
	 * Attempts to disconnect all sources powering the passed-in block
	 */
	public void tryDisconnectAllNodes(IPowerNode<?> node){
		getPowerNodeData(node).clearNodePaths();
	}

	private void removePowerNode(ChunkPos chunk, BlockPos location){
		HashMap<BlockPos, PowerNodeEntry> nodeList;
		if (powerNodes.containsKey(chunk)){
			nodeList = powerNodes.get(chunk);
			nodeList.remove(location);

			LogHelper.trace("Successfully removed a node from chunk %d, %d", chunk.chunkXPos, chunk.chunkZPos);
			if (nodeList.size() == 0){
				powerNodes.remove(chunk);
				LogHelper.trace("No more nodes exist in chunk.  Removing tracking data for chunk.");
			}
		}else{
			LogHelper.error("Power Node removal requested in a non-tracked chunk (%d, %d)!", chunk.chunkXPos, chunk.chunkZPos);
		}
	}

	public float requestPower(IPowerNode<?> destination, PowerTypes type, float amount){
		PowerNodeEntry data = getPowerNodeData(destination);

		if (data == null){
			return 0;
		}

		float requested = data.requestPower(((TileEntity)destination).getWorld(), type, amount, destination.getCapacity());

		return requested;
	}

	public float consumePower(IPowerNode<?> consumer, PowerTypes type, float amount){
		PowerNodeEntry data = getPowerNodeData(consumer);

		if (data == null){
			return 0;
		}

		float availablePower = data.getPower(type);
		if (availablePower < amount)
			amount = availablePower;
		data.setPower(type, availablePower - amount);
		return amount;
	}

	public float insertPower(IPowerNode<?> destination, PowerTypes type, float amount){
		PowerNodeEntry data = getPowerNodeData(destination);
		if (data == null){
			return 0;
		}
		float curPower = data.getPower(type);
		if (curPower + amount > destination.getCapacity())
			amount = destination.getCapacity() - curPower;
		data.setPower(type, curPower + amount);
		
		return amount;
	}

	public void setPower(IPowerNode<?> destination, PowerTypes type, float amount){
		PowerNodeEntry data = getPowerNodeData(destination);

		if (data == null){
			return;
		}
		
		if (amount > destination.getCapacity())
			amount = destination.getCapacity();
		
		data.setPower(type, amount);
	}

	public boolean checkPower(IPowerNode<?> node, PowerTypes type, float amount){
		PowerNodeEntry data = getPowerNodeData(node);

		if (data == null){
			return false;
		}

		return data.getPower(type) >= amount;
	}

	public boolean checkPower(IPowerNode<?> node, float amount){
		for (PowerTypes type : PowerTypes.all())
			if (checkPower(node, type, amount))
				return true;
		return false;
	}

	public boolean checkPower(IPowerNode<?> node){
		return getHighestPower(node) > 0;
	}

	public float getPower(IPowerNode<?> node, PowerTypes type){
		PowerNodeEntry data = getPowerNodeData(node);

		if (data == null){
			return 0;
		}

		return data.getPower(type);
	}

	public float getHighestPower(IPowerNode<?> node){
		PowerNodeEntry data = getPowerNodeData(node);

		if (data == null){
			return 0;
		}

		return data.getHighestPower();
	}

	public PowerTypes getHighestPowerType(IPowerNode<?> node){
		PowerNodeEntry data = getPowerNodeData(node);

		if (data == null){
			return PowerTypes.NONE;
		}

		return data.getHighestPowerType();
	}

	PowerNodeEntry getPowerNodeData(IPowerNode<?> node){
		ChunkPos pair = getChunkFromNode(node);
		if (pair != null && powerNodes.containsKey(pair)){
			PowerNodeEntry pnd = powerNodes.get(pair).get(((TileEntity)node).getPos());
			if (pnd != null)
				return pnd;
		}

		return registerPowerNodeInternal(node);
	}

	/**
	 * Returns all power nodes within POWER_SEARCH_RADIUS
	 *
	 * @param location The center point to search from
	 * @param power    The power type that the provider needs to supply or accept to be considered valid
	 * @return An array of IPowerNodes
	 */
	public IPowerNode<?>[] getAllNearbyNodes(World world, BlockPos location, PowerTypes power){
		//get the list of chunks we'll need to search in
		ChunkPos[] search = getSearchChunks(location);

		//build the list of nodes from that chunk
		HashMap<BlockPos, PowerNodeEntry> nodesToSearch = new HashMap<BlockPos, PowerNodeEntry>();
		for (ChunkPos pair : search){
			HashMap<BlockPos, PowerNodeEntry> nodesInChunk = powerNodes.get(pair);
			if (nodesInChunk != null){
				//Add only vectors that are less than or equal to POWER_SEARCH_RADIUS_SQ away
				for (BlockPos vector : nodesInChunk.keySet()){
					System.out.println(vector);
					if (location.distanceSq(vector) <= POWER_SEARCH_RADIUS_SQ && !vector.equals(location)){
						nodesToSearch.put(vector, nodesInChunk.get(vector));
					}
				}
			}
		}

		//spin through and create our list of providers
		ArrayList<IPowerNode<?>> nodes = new ArrayList<IPowerNode<?>>();
		int deadNodesRemoved = 0;
		for (BlockPos vector : nodesToSearch.keySet()){
			if (world.getChunkFromBlockCoords(vector) == null){
				continue;
			}
			Chunk chunk = world.getChunkFromBlockCoords(vector);
			if (!chunk.isLoaded())
				continue;
			TileEntity te = world.getTileEntity(vector);
			if (te == null || !(te instanceof IPowerNode)){
				//opportune time to remove dead power nodes
				removePowerNode(chunk.getChunkCoordIntPair(), vector);
				deadNodesRemoved++;
				continue;
			}
			IPowerNode<?> node = (IPowerNode<?>)te;
			nodes.add(node);
		}

		if (deadNodesRemoved > 0)
			LogHelper.trace("Removed %d dead power nodes", deadNodesRemoved);

		IPowerNode<?>[] nodeArray = nodes.toArray(new IPowerNode[nodes.size()]);

		LogHelper.trace("Located %d nearby power providers", nodeArray.length);

		return nodeArray;
	}

	private ChunkPos[] getSearchChunks(BlockPos location){
		//Calculate the chunk X/Z location of the search center
		int chunkX = location.getX() >> 4;
		int chunkZ = location.getZ() >> 4;

		ArrayList<ChunkPos> searchChunks = new ArrayList<ChunkPos>();
		//always search the chunk you're already in!
		searchChunks.add(new ChunkPos(chunkX, chunkZ));

		for (int i = -1; i <= 1; ++i){
			for (int j = -1; j <= 1; ++j){
				//don't include the home chunk, this has already been added.  We're only concerned with neighboring chunks.
				if (i == 0 && j == 0) continue;
				//create the new CCIP at the offset location
				ChunkPos newPair = new ChunkPos(chunkX + i, chunkZ + j);
				//offset the x/z locations by POWER_SEARCH_RADIUS * i/j respectively and calculate the chunk that would fall in.
				//If it matches the new chunk coordinates, we add that chunk to the list of chunks to search.
				if (((int)location.getX() + (POWER_SEARCH_RADIUS * i)) >> 4 == newPair.chunkXPos && ((int)location.getZ() + (POWER_SEARCH_RADIUS * j)) >> 4 == newPair.chunkZPos){
					searchChunks.add(newPair);
				}
			}
		}

		return searchChunks.toArray(new ChunkPos[searchChunks.size()]);
	}

	private ChunkPos getChunkFromNode(IPowerNode<?> node){
		TileEntity te = (TileEntity)node;
//		if (te.getWorld() == null)
//			return null;
//		if (te.getWorld().getChunkFromBlockCoords(te.getPos()) == null)
		return new ChunkPos(te.getPos().getX() >> 4, te.getPos().getZ() >> 4);
//		return te.getWorld().getChunkFromBlockCoords(te.getPos()).getChunkCoordIntPair();
	}

//	private ChunkPos getChunkFromPosition(World world, BlockPos location){
//		return new ChunkPos((int)location.x >> 4, (int)location.z >> 4);
//	}

	public void SaveChunkToNBT(ChunkPos chunk, NBTTagCompound compound){
		HashMap<BlockPos, PowerNodeEntry> nodeData = powerNodes.get(chunk);
		if (nodeData == null){
			//we're not tracking anything in this chunk
			return;
		}
		NBTTagList powerNodeTagList = new NBTTagList();
		for (BlockPos location : nodeData.keySet()){
			NBTTagCompound nodeCompound = new NBTTagCompound();
			nodeCompound.setInteger("xCoord", location.getX());
			nodeCompound.setInteger("yCoord", location.getY());
			nodeCompound.setInteger("zCoord", location.getZ());
			PowerNodeEntry pnd = nodeData.get(location);
			nodeCompound.setTag("nodeData", pnd.saveToNBT());

			powerNodeTagList.appendTag(nodeCompound);
		}

		LogHelper.trace("Saved %d power node entries", powerNodeTagList.tagCount());

		compound.setTag("AM2PowerData", powerNodeTagList);
	}

	public void LoadChunkFromNBT(ChunkPos chunk, NBTTagCompound compound){
		if (!compound.hasKey("AM2PowerData"))
			return; //nothing was saved in this chunk for the power system
		NBTTagList powerNodeTagList = compound.getTagList("AM2PowerData", Constants.NBT.TAG_COMPOUND);
		HashMap<BlockPos, PowerNodeEntry> chunkPowerData = new HashMap<BlockPos, PowerNodeEntry>();
		for (int i = 0; i < powerNodeTagList.tagCount(); ++i){
			NBTTagCompound nodeCompound = powerNodeTagList.getCompoundTagAt(i);
			BlockPos nodeLocation = new BlockPos(nodeCompound.getInteger("xCoord"), nodeCompound.getInteger("yCoord"), nodeCompound.getInteger("zCoord"));
			PowerNodeEntry pnd = new PowerNodeEntry();
			pnd.readFromNBT(nodeCompound.getCompoundTag("nodeData"));

			chunkPowerData.put(nodeLocation, pnd);
		}

		LogHelper.trace("Loaded %d power node entries", chunkPowerData.size());

		powerNodes.put(chunk, chunkPowerData);
	}

	public void unloadChunk(Chunk chunk){
		powerNodes.remove(chunk.getChunkCoordIntPair());
	}

	public boolean hasDataForChunk(Chunk chunk){
		return powerNodes.containsKey(chunk.getChunkCoordIntPair());
	}

	private class ChunkCoordComparator implements Comparator<ChunkPos>{

		@Override
		public int compare(ChunkPos a, ChunkPos b){
			if (a.chunkXPos == b.chunkXPos && a.chunkZPos == b.chunkZPos)
				return 0;

			if (a.chunkXPos > b.chunkXPos)
				return 1;
			else if (a.chunkZPos > b.chunkZPos)
				return 1;
			return -1;
		}

	}

//	private class IPowerNodeComparer implements Comparator<IPowerNode<?>>{
//
//		@Override
//		public int compare(IPowerNode<?> o1, IPowerNode<?> o2){
//			BlockPos te1 = new BlockPos(((TileEntity)o1).getPos());
//			BlockPos te2 = new BlockPos(((TileEntity)o2).getPos());
//
//			if (te1.x == te2.x && te1.z == te2.z && te1.y == te2.y)
//				return 0;
//
//			if (te1.x > te2.x){
//				return 1;
//			}else{
//				if (te1.z > te2.z){
//					return 1;
//				}else{
//					if (te1.y > te2.y){
//						return 1;
//					}else{
//						return -1;
//					}
//				}
//			}
//		}
//
//	}

	public NBTTagCompound getDataCompoundForNode(IPowerNode<?> node){
		PowerNodeEntry pnd = getPowerNodeData(node);
		if (pnd == null)
			return null;
		return pnd.saveToNBT();
	}

	public void setDataCompoundForNode(IPowerNode<?> node, NBTTagCompound compound){
		PowerNodeEntry pnd = getPowerNodeData(node);
		if (pnd == null)
			return;
		pnd.readFromNBT((NBTTagCompound)compound.copy());
	}

	public PowerNodeEntry parseFromNBT(NBTTagCompound compound){
		PowerNodeEntry pnd = new PowerNodeEntry();
		pnd.readFromNBT((NBTTagCompound)compound.copy());
		return pnd;
	}

	public HashMap<ChunkPos, NBTTagCompound> saveAll(){
		HashMap<ChunkPos, NBTTagCompound> allData = new HashMap<ChunkPos, NBTTagCompound>();
		for (ChunkPos pair : this.powerNodes.keySet()){
			NBTTagCompound chunkCompound = new NBTTagCompound();
			SaveChunkToNBT(pair, chunkCompound);
			allData.put(pair, chunkCompound);
		}

		return allData;
	}

	public void unloadAll(){
		powerNodes.clear();
	}
}
