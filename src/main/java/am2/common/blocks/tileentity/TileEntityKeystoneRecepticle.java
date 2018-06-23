package am2.common.blocks.tileentity;

import static net.minecraft.block.BlockStairs.FACING;
import static net.minecraft.block.BlockStairs.HALF;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.blocks.IKeystoneLockable;
import am2.api.blocks.IMultiblock;
import am2.api.blocks.IMultiblockController;
import am2.api.blocks.Multiblock;
import am2.api.blocks.MultiblockGroup;
import am2.api.blocks.TypedMultiblockGroup;
import am2.api.math.AMVector3;
import am2.common.AMChunkLoader;
import am2.common.blocks.BlockKeystoneReceptacle;
import am2.common.defs.AMSounds;
import am2.common.defs.BlockDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class TileEntityKeystoneRecepticle extends TileEntityAMPower implements IInventory, IMultiblockController, IKeystoneLockable<TileEntityKeystoneRecepticle>{

	private boolean isActive;
	private long key;
	private int surroundingCheckTicks = 20;

	private final IMultiblock primary = new Multiblock("gateways_alt");
	private final IMultiblock secondary = new Multiblock("gateways");

	public static int keystoneSlot = 0;

	private ItemStack[] inventory;

	public TileEntityKeystoneRecepticle(){
		super(250000);
		this.isActive = false;
		inventory = new ItemStack[getSizeInventory()];
		initMultiblock();
	}

	@SuppressWarnings("unchecked")
	public void initMultiblock(){
		HashMap<Integer, IBlockState> map = new HashMap<>();
		map.put(3, Blocks.STONE_BRICK_STAIRS.getDefaultState());
		map.put(2, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		map.put(1, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.WEST));
		map.put(0, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.EAST));
		map.put(7, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(HALF, EnumHalf.TOP));
		map.put(6, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(HALF, EnumHalf.TOP));
		map.put(5, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.WEST).withProperty(HALF, EnumHalf.TOP));
		map.put(4, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(FACING, EnumFacing.EAST).withProperty(HALF, EnumHalf.TOP));
		map.put(8, Blocks.STONEBRICK.getDefaultState());
		map.put(9, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
		MultiblockGroup keystone = new MultiblockGroup("keystoneRecepticle", Lists.newArrayList(
				BlockDefs.keystoneRecepticle.getDefaultState().withProperty(BlockKeystoneReceptacle.FACING, EnumFacing.EAST),
				BlockDefs.keystoneRecepticle.getDefaultState().withProperty(BlockKeystoneReceptacle.FACING, EnumFacing.WEST)), false);
		TypedMultiblockGroup struct = new TypedMultiblockGroup("struct", Lists.newArrayList(map), false);
		//primary
		//row 0
		keystone.addBlock(BlockPos.ORIGIN);
		struct.addBlock(new BlockPos (0, 0, -1), 2);
		struct.addBlock(new BlockPos (0, 0, 1), 3);
		struct.addBlock(new BlockPos (0, 0, -1), 8);
		struct.addBlock(new BlockPos (0, 0, 1), 8);
		//row 1
		struct.addBlock(new BlockPos (0, -1, -1), 7);
		struct.addBlock(new BlockPos (0, -1, -2), 2);
		struct.addBlock(new BlockPos (0, -1, 1), 6);
		struct.addBlock(new BlockPos (0, -1, 2), 3);
		struct.addBlock(new BlockPos (0, -1, 2), 8);
		struct.addBlock(new BlockPos (0, -1, -2), 8);
		//row 2
		struct.addBlock(new BlockPos (0, -2, -2), 8);
		struct.addBlock(new BlockPos (0, -2, 2), 8);
		//row 3
		struct.addBlock(new BlockPos (0, -3, -2), 8);
		struct.addBlock(new BlockPos (0, -3, 2), 8);
		struct.addBlock(new BlockPos (0, -3, -1), 3);
		struct.addBlock(new BlockPos (0, -3, 1), 2);
		//row 4
		struct.addBlock(new BlockPos (0, -4, -2), 8);
		struct.addBlock(new BlockPos (0, -4, -1), 8);
		struct.addBlock(new BlockPos (0, -4, 0), 9);
		struct.addBlock(new BlockPos (0, -4, 1), 8);
		struct.addBlock(new BlockPos (0, -4, 2), 8);
		
		primary.addGroup(struct);
		primary.addGroup(keystone);

		//secondary
		//row 0
		MultiblockGroup keystone_alt = new MultiblockGroup("keystoneRecepticle_alt", Lists.newArrayList(
				BlockDefs.keystoneRecepticle.getDefaultState().withProperty(BlockKeystoneReceptacle.FACING, EnumFacing.NORTH),
				BlockDefs.keystoneRecepticle.getDefaultState().withProperty(BlockKeystoneReceptacle.FACING, EnumFacing.SOUTH)), false);
		TypedMultiblockGroup struct_alt = new TypedMultiblockGroup("struct_alt", Lists.newArrayList(map), false);
		keystone_alt.addBlock(BlockPos.ORIGIN);
		struct_alt.addBlock(new BlockPos (-1, 0, 0), 0);
		struct_alt.addBlock(new BlockPos (1, 0, 0), 1);
		struct_alt.addBlock(new BlockPos (-1, 0, 0), 8);
		struct_alt.addBlock(new BlockPos (1, 0, 0), 8);
		//row 1
		struct_alt.addBlock(new BlockPos (-1, -1, 0), 5);
		struct_alt.addBlock(new BlockPos (-2, -1, 0), 0);
		struct_alt.addBlock(new BlockPos (1, -1, 0), 4);
		struct_alt.addBlock(new BlockPos (2, -1, 0), 1);
		struct_alt.addBlock(new BlockPos (2, -1, 0), 8);
		struct_alt.addBlock(new BlockPos (-2, -1, 0), 8);
		//row 2
		struct_alt.addBlock(new BlockPos (-2, -2, 0), 8);
		struct_alt.addBlock(new BlockPos (2, -2, 0), 8);
		//row 3
		struct_alt.addBlock(new BlockPos (-2, -3, 0), 8);
		struct_alt.addBlock(new BlockPos (2, -3, 0), 8);
		struct_alt.addBlock(new BlockPos (-1, -3, 0), 1);
		struct_alt.addBlock(new BlockPos (1, -3, 0), 0);
		//row 4
		struct_alt.addBlock(new BlockPos (-2, -4, 0), 8);
		struct_alt.addBlock(new BlockPos (-1, -4, 0), 8);
		struct_alt.addBlock(new BlockPos (0, -4, 0), 9);
		struct_alt.addBlock(new BlockPos (1, -4, 0), 8);
		struct_alt.addBlock(new BlockPos (2, -4, 0), 8);
		secondary.addGroup(struct_alt);
		secondary.addGroup(keystone_alt);
	}

	public void onPlaced(){
		if (!world.isRemote){
			AMChunkLoader.INSTANCE.requestStaticChunkLoad(this.getClass(), this.pos, this.world);
		}
	}

	@Override
	public void invalidate(){
		ArsMagica2.proxy.blocks.removeKeystonePortal(pos, world.provider.getDimension());

		if (!world.isRemote){
			AMChunkLoader.INSTANCE.releaseStaticChunkLoad(this.getClass(), pos, this.world);
		}

		super.invalidate();
	}

	public void setActive(long key){
		this.isActive = true;
		this.key = key;

		if (!this.world.isRemote){
			for (Object player : this.world.playerEntities){
				if (player instanceof EntityPlayerMP && new AMVector3((EntityPlayerMP)player).distanceSqTo(new AMVector3(this)) <= 4096){
					((EntityPlayerMP)player).connection.sendPacket(getUpdatePacket());
				}
			}
		}else{
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), AMSounds.GATEWAY_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
		}
	}

	public boolean isActive(){
		return this.isActive;
	}

	@Override
	public void update(){
		if (world.isRemote)
			return;
		super.update();

		AxisAlignedBB bb = new AxisAlignedBB(pos.getX() + 0.3, pos.getY() - 3, pos.getZ() + 0.3, pos.getX() + 0.7, pos.getY(), pos.getZ() + 0.7);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);

		if (this.isActive){
			surroundingCheckTicks--;
			if (surroundingCheckTicks <= 0){
				surroundingCheckTicks = 20;
				checkSurroundings();
			}
			if (entities.size() == 1){
				doTeleport(entities.get(0));
			}
		}else{
			if (entities.size() == 1 && world.canBlockSeeSky(pos)){
				Entity entity = entities.get(0);
				if (entity instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)entity;
					if (player.isPotionActive(PotionEffectsDefs.HASTE) && player.isPotionActive(Potion.getPotionFromResourceLocation("speed")) && player.isSprinting()){
						this.key = 0;
						if (!world.isRemote){
							EntityLightningBolt elb = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
							world.spawnEntityInWorld(elb);
						}
						doTeleport(player);
					}

				}
			}
		}
	}

	public boolean canActivate(){
		boolean allGood = true;
		allGood &= world.isAirBlock(pos.down());
		allGood &= world.isAirBlock(pos.down(2));
		allGood &= world.isAirBlock(pos.down(3));
		allGood &= checkStructure();
		allGood &= PowerNodeRegistry.For(this.world).checkPower(this);
		allGood &= !this.isActive;
		return allGood;
	}

	private void checkSurroundings(){
		if (!checkStructure()){
			deactivate();
		}
	}
	
	@Override
	public boolean isStructureValid() {
		return this.checkStructure();
	}

	private boolean checkStructure(){
		boolean remainActive = true;

		switch (world.getBlockState(pos).getValue(BlockKeystoneReceptacle.FACING)){
		case NORTH:
		case SOUTH:
			remainActive &= secondary.matches(world, pos);
			break;
		case EAST:
		default:
			remainActive &= primary.matches(world, pos);
			break;
		}
		return remainActive;
	}

	public void deactivate(){
		this.isActive = false;
		if (!this.world.isRemote){
			world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}

	private void doTeleport(Entity entity){
		deactivate();

		AMVector3 newLocation = ArsMagica2.proxy.blocks.getNextKeystonePortalLocation(this.world, pos, false, this.key);
		AMVector3 myLocation = new AMVector3(pos);
		double distance = myLocation.distanceTo(newLocation);
		float essenceCost = (float)(distance * distance * 0.00175f);

		EnumFacing facing = world.getBlockState(newLocation.toBlockPos()).getValue(BlockKeystoneReceptacle.FACING);

		if (ArsMagica2.config.getHazardousGateways()){
			//uh-oh!  Not enough power!  The teleporter will still send you though, but I wonder where...
			float charge = PowerNodeRegistry.For(this.world).getHighestPower(this);
			if (charge < essenceCost){
				essenceCost = charge;
				//get the distance that our charge *will* take us towards the next point
				double distanceWeCanGo = MathHelper.sqrt(charge / 0.00175);
				//get the angle between the 2 vectors
				double deltaZ = newLocation.z - myLocation.z;
				double deltaX = newLocation.x - myLocation.x;
				double angleH = Math.atan2(deltaZ, deltaX);
				//interpolate the distance at that angle - this is the new position
				double newX = myLocation.x + (Math.cos(angleH) * distanceWeCanGo);
				double newZ = myLocation.z + (Math.sin(angleH) * distanceWeCanGo);
				double newY = myLocation.y;
				
				while (!world.isAirBlock(new BlockPos(newX, newY, newZ))){
					newY++;
				}

				newLocation = new AMVector3(newX, newY, newZ);
			}
		}else{
			//this.world.playSound(newLocation.x, newLocation.y, newLocation.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
			//return;
		}


		float newRotation = 0;
		switch (facing){
		case EAST:
			newRotation = 270;
			break;
		case NORTH:
			newRotation = 180;
			break;
		case WEST:
			newRotation = 90;
			break;
		case SOUTH:
			newRotation = 0;
			break;
		default:
			break;
		}
		//entity.setPositionAndRotation(newLocation.x + 0.5, newLocation.y - entity.height, newLocation.z + 0.5, newRotation, entity.rotationPitch);
		if (entity instanceof EntityPlayer)
			((EntityPlayer) entity).addStat(StatList.getObjectUseStats(Item.getItemFromBlock(blockType)));

		PowerNodeRegistry.For(this.world).consumePower(this, PowerNodeRegistry.For(this.world).getHighestPowerType(this), essenceCost);
		
		entity.rotationYaw = newRotation;
		entity.setPositionAndUpdate(newLocation.x + 0.5F, newLocation.y - entity.height, newLocation.z + 0.5);
		this.world.playSound(myLocation.x, myLocation.y, myLocation.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
		this.world.playSound(newLocation.x, newLocation.y, newLocation.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
	}

	@Override
	public int getSizeInventory(){
		return 3;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[0];
		runes[1] = inventory[1];
		runes[2] = inventory[2];
		return runes;
	}

	@Override
	public boolean keystoneMustBeHeld(){
		return false;
	}

	@Override
	public boolean keystoneMustBeInActionBar(){
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		if (slot >= inventory.length)
			return null;
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (inventory[i] != null){
			if (inventory[i].stackSize <= j){
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0){
				inventory[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (inventory[i] != null){
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Keystone Recepticle";
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (world.getTileEntity(pos) != this){
			return false;
		}
		return entityplayer.getDistanceSqToCenter(pos) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("KeystoneRecepticleInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		ArsMagica2.proxy.blocks.registerKeystonePortal(pos, nbttagcompound.getInteger("keystone_receptacle_dimension_id"));

		this.isActive = nbttagcompound.getBoolean("isActive");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++){
			if (inventory[i] != null){
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setInteger("keystone_receptacle_dimension_id", world.provider.getDimension());
		nbttagcompound.setTag("KeystoneRecepticleInventory", nbttaglist);
		nbttagcompound.setBoolean("isActive", this.isActive);
		return nbttagcompound;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, getBlockMetadata(), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}
	
	@Override
	public IMultiblock getMultiblockStructure() {
		return secondary;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return new AxisAlignedBB(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3);
	}

	@Override
	public int getChargeRate(){
		return 200; //A whole lot of nope.
	}

	@Override
	public int getRequestInterval(){
		return 0;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
