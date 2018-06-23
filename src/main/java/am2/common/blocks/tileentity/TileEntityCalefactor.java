package am2.common.blocks.tileentity;


import am2.ArsMagica2;
import am2.api.blocks.IKeystoneLockable;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleFloatUpward;
import am2.common.blocks.BlockCalefactor;
import am2.common.defs.AMSounds;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemFocusCharge;
import am2.common.items.ItemFocusMana;
import am2.common.items.ItemOre;
import am2.common.packet.AMDataReader;
import am2.common.packet.AMDataWriter;
import am2.common.packet.AMNetHandler;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class TileEntityCalefactor extends TileEntityAMPower implements IInventory, ISidedInventory, IKeystoneLockable<TileEntityCalefactor>, ITileEntityAMBase {

	private ItemStack calefactorItemStacks[];
	private float rotationX, rotationY, rotationZ;
	private float rotationStepX;
	private final short baseCookTime = 220; //default to the same as a standard furnace
	private short timeSpentCooking = 0;
	private final float basePowerConsumedPerTickCooking = 0.85f;
	private int particleCount = 0;
	private boolean isCooking;
	private boolean dirty = false;

	private static final byte PKT_PRG_UPDATE = 1;
	private boolean isFirstTick = true;

	public TileEntityCalefactor(){
		super(100);

		calefactorItemStacks = new ItemStack[getSizeInventory()];

		isCooking = false;
	}

	@Override
	public float particleOffset(int axis){
		EnumFacing facing = world.getBlockState(pos).getValue(BlockCalefactor.FACING);

		if (axis == 0){
			switch (facing){
			case WEST:
				return 0.25f;
			case EAST:
				return 0.75f;
			default:
				return 0.5f;
			}
		}else if (axis == 1){
			switch (facing){
			case UP:
				return 0.75f;
			case DOWN:
				return 0.25f;
			default:
				return 0.5f;
			}
		}else if (axis == 2){
			switch (facing){
			case NORTH:
				return 0.25f;
			case SOUTH:
				return 0.75f;
			default:
				return 0.5f;
			}
		}

		return 0.5f;
	}

	public void incrementRotations(){
		rotationX += rotationStepX;
		rotationY += rotationStepX;
		rotationZ += rotationStepX;

		if (rotationX > 359) rotationX -= 360;
		if (rotationY > 359) rotationY -= 360;
		if (rotationZ > 359) rotationZ -= 360;

		if (rotationX < 0) rotationX += 360;
		if (rotationY < 0) rotationY += 360;
		if (rotationZ < 0) rotationZ += 360;
	}

	public float getRotationX(){
		return this.rotationX;
	}

	public float getRotationY(){
		return this.rotationX;
	}

	public float getRotationZ(){
		return this.rotationX;
	}

	public ItemStack getItemBeingCooked(){
		if (calefactorItemStacks[0] != null){
			return calefactorItemStacks[0];
		}
		return null;
	}

	private boolean canSmelt(){
		if (this.calefactorItemStacks[0] == null){
			return false;
		}else{
			ItemStack var1 = FurnaceRecipes.instance().getSmeltingResult(this.calefactorItemStacks[0]);
			if (var1 == null) return false;
			if (this.calefactorItemStacks[1] == null) return true;
			if (!this.calefactorItemStacks[1].isItemEqual(var1)) return false;
			int result = calefactorItemStacks[1].stackSize + var1.stackSize;
			return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
		}
	}

	public void smeltItem(){
		if (this.canSmelt()){
			ItemStack var1 = FurnaceRecipes.instance().getSmeltingResult(this.calefactorItemStacks[0]);

			ItemStack smeltStack = var1.copy();

			if (this.calefactorItemStacks[0].getItem() instanceof ItemFood || this.calefactorItemStacks[0].getItem() instanceof ItemBlock || this.calefactorItemStacks[0].getItem() == ItemDefs.itemOre){
				if (PowerNodeRegistry.For(world).checkPower(this, PowerTypes.DARK, getCookTickPowerCost()))
					if (PowerNodeRegistry.For(world).checkPower(this, PowerTypes.NEUTRAL, getCookTickPowerCost()))
						if (PowerNodeRegistry.For(world).checkPower(this, PowerTypes.LIGHT, getCookTickPowerCost()))
							smeltStack.stackSize++;
			}

			if (this.calefactorItemStacks[0].getItem() instanceof ItemFood){
				if (smeltStack.stackSize == var1.stackSize && world.rand.nextDouble() < 0.15f){
					smeltStack.stackSize++;
				}
			}

			boolean doSmelt = true;

			if (doSmelt){

				if (this.calefactorItemStacks[1] == null){
					this.calefactorItemStacks[1] = smeltStack.copy();
				}else if (this.calefactorItemStacks[1].isItemEqual(smeltStack)){
					calefactorItemStacks[1].stackSize += smeltStack.stackSize;
					if (calefactorItemStacks[1].stackSize > calefactorItemStacks[1].getMaxStackSize()){
						calefactorItemStacks[1].stackSize = calefactorItemStacks[1].getMaxStackSize();
					}
				}

				if (Math.random() <= 0.25){
					if (calefactorItemStacks[5] == null){
						calefactorItemStacks[5] = new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM);
					}else{
						calefactorItemStacks[5].stackSize++;
						if (calefactorItemStacks[5].stackSize > calefactorItemStacks[5].getMaxStackSize()){
							calefactorItemStacks[5].stackSize = calefactorItemStacks[5].getMaxStackSize();
						}
					}
				}
			}

			--this.calefactorItemStacks[0].stackSize;

			if (this.calefactorItemStacks[0].stackSize <= 0){
				this.calefactorItemStacks[0] = null;
			}
		}
	}

	public void handlePacket(byte[] data){
		if (world.isRemote){
			AMDataReader rdr = new AMDataReader(data);
			switch (rdr.ID){
			case PKT_PRG_UPDATE:
				isCooking = rdr.getByte() == 1;
				if (rdr.getByte() == 1){
					this.calefactorItemStacks[0] = rdr.getItemStack();
				}else{
					this.calefactorItemStacks[0] = null;
				}
				break;
			default:
			}
		}
	}

	protected void sendCookStatusUpdate(boolean isCooking){
		if (this.world.isRemote)
			return;

		AMDataWriter writer = new AMDataWriter();
		writer.add(PKT_PRG_UPDATE);
		writer.add(isCooking ? (byte)1 : (byte)0);
		writer.add(this.calefactorItemStacks[0] != null ? (byte)1 : (byte)0);
		if (this.calefactorItemStacks[0] != null)
			writer.add(this.calefactorItemStacks[0]);

		AMNetHandler.INSTANCE.sendCalefactorCookUpdate(this, writer.generate());
	}

	private short getModifiedCookTime(){
		int foci = this.numFociOfType(ItemFocusCharge.class);
		short base = baseCookTime;
		short modified = (short)(base * Math.pow(0.5, foci));
		return modified;
	}

	private float getCookTickPowerCost(){
		int fociMana = this.numFociOfType(ItemFocusMana.class);
		int fociCharge = this.numFociOfType(ItemFocusCharge.class);
		float base = basePowerConsumedPerTickCooking;
		return (float)(base * Math.pow(2.25, fociCharge) * Math.pow(0.5, fociMana));
	}

	private boolean isSmelting(){
		return this.timeSpentCooking != 0;
	}

	@Override
	public void update(){
		super.update();
		if (isFirstTick) {
			rotationStepX = world.rand.nextFloat() * 0.03f - 0.015f;
			isFirstTick = false;
		}
		if (this.world.isRemote){
			incrementRotations();
			if (this.isCooking){
				particleCount--;
				if (particleCount <= 0){
					particleCount = (int)(Math.random() * 20);
					double rStartX = Math.random() > 0.5 ? this.pos.getX() + 0.01 : this.pos.getX() + 1.01;
					double rStartY = this.pos.getY() + 1.1;
					double rStartZ = Math.random() > 0.5 ? this.pos.getZ() + 0.01 : this.pos.getZ() + 1.01;

					double endX = this.pos.getX() + 0.5f;
					double endY = this.pos.getY() + 0.7f + (world.rand.nextDouble() * 0.5f);
					double endZ = this.pos.getZ() + 0.5f;

					ArsMagica2.proxy.particleManager.BeamFromPointToPoint(world, rStartX, rStartY, rStartZ, endX, endY, endZ, 0xFF8811);
					if (world.rand.nextBoolean()){
						AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "smoke", endX, endY, endZ);
						if (effect != null){
							effect.setIgnoreMaxAge(false);
							effect.setMaxAge(60);
							effect.AddParticleController(new ParticleFloatUpward(effect, 0.02f, 0.01f, 1, false));
						}
					}else{
						AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "explosion_2", endX, endY, endZ);
						if (effect != null){
							effect.setIgnoreMaxAge(false);
							effect.setMaxAge(10);
							effect.setParticleScale(0.04f);
							effect.addVelocity(world.rand.nextDouble() * 0.2f - 0.1f, 0.2f, world.rand.nextDouble() * 0.2f - 0.1f);
							effect.setAffectedByGravity();
							effect.setDontRequireControllers();
						}
					}
				}
			}else{
				particleCount = 0;
			}
		}
		
		boolean powerCheck = PowerNodeRegistry.For(this.world).checkPower(this, getCookTickPowerCost());
		if (this.canSmelt() && this.isSmelting() && powerCheck){
			++this.timeSpentCooking;
			
			if (this.timeSpentCooking >= getModifiedCookTime()){
				if (!this.world.isRemote){
					this.smeltItem();
				}else{
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), AMSounds.CALEFACTOR_BURN, SoundCategory.BLOCKS, 0.2f, 1.0f, true);
				}
				this.timeSpentCooking = 0;
				if (!world.isRemote){
					sendCookStatusUpdate(false);
				}
			}
			if (!world.isRemote){
				if (PowerNodeRegistry.For(world).checkPower(this, PowerTypes.DARK, getCookTickPowerCost()) &&
						PowerNodeRegistry.For(world).checkPower(this, PowerTypes.NEUTRAL, getCookTickPowerCost()) &&
						PowerNodeRegistry.For(world).checkPower(this, PowerTypes.LIGHT, getCookTickPowerCost())){

					PowerNodeRegistry.For(this.world).consumePower(this, PowerTypes.DARK, getCookTickPowerCost());
					PowerNodeRegistry.For(this.world).consumePower(this, PowerTypes.NEUTRAL, getCookTickPowerCost());
					PowerNodeRegistry.For(this.world).consumePower(this, PowerTypes.LIGHT, getCookTickPowerCost());

				}else{
					PowerNodeRegistry.For(this.world).consumePower(this, PowerNodeRegistry.For(this.world).getHighestPowerType(this), getCookTickPowerCost());
				}
			}
		}else if (!this.isSmelting() && this.canSmelt() && powerCheck){
			this.timeSpentCooking = 1;
			if (!world.isRemote){
				sendCookStatusUpdate(true);
			}
		}else if (!this.canSmelt()){
			this.timeSpentCooking = 0;
		}
		this.markDirty();
	}

	public int getCookProgressScaled(int par1){
		return this.timeSpentCooking * par1 / getModifiedCookTime();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (world.getTileEntity(pos) != this){
			return false;
		}
		return entityplayer.getDistanceSqToCenter(pos) <= 64D;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	private int numFociOfType(Class<?> type){
		int count = 0;
		for (int i = 2; i < getSizeInventory(); ++i){
			if (calefactorItemStacks[i] != null && type.isInstance(calefactorItemStacks[i].getItem())){
				count++;
			}
		}
		return count;
	}

	@Override
	public int getSizeInventory(){
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int var1){
		if (var1 >= calefactorItemStacks.length){
			return null;
		}
		return calefactorItemStacks[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (calefactorItemStacks[i] != null){
			if (calefactorItemStacks[i].stackSize <= j){
				ItemStack itemstack = calefactorItemStacks[i];
				calefactorItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = calefactorItemStacks[i].splitStack(j);
			if (calefactorItemStacks[i].stackSize == 0){
				calefactorItemStacks[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (calefactorItemStacks[i] != null){
			ItemStack itemstack = calefactorItemStacks[i];
			calefactorItemStacks[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		calefactorItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()){
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Calefactor";
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
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
		NBTTagList nbttaglist = nbttagcompound.getTagList("CasterInventory", Constants.NBT.TAG_COMPOUND);
		calefactorItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < calefactorItemStacks.length){
				calefactorItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < calefactorItemStacks.length; i++){
			if (calefactorItemStacks[i] != null){
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				calefactorItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("CasterInventory", nbttaglist);
		return nbttagcompound;
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return i == 0;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing var1){
		return new int[]{0, 1, 5};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing j){
		return i == 0;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j){
		return i == 1 || i == 5;
	}

	@Override
	public int getChargeRate(){
		int numFoci = numFociOfType(ItemFocusCharge.class);
		int base = 20;
		if (numFoci > 0){
			base += 27 * numFoci;
		}

		return base;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = calefactorItemStacks[6];
		runes[1] = calefactorItemStacks[7];
		runes[2] = calefactorItemStacks[8];
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
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
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

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void markDirty() {
		markForUpdate();
		super.markDirty();
	}

	@Override
	public void markForUpdate() {
		this.dirty = true;
	}

	@Override
	public boolean needsUpdate() {
		return this.dirty;
	}

	@Override
	public void clean() {
		this.dirty = false;
	}
}
