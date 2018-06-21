package am2.common.blocks.tileentity;

import am2.ArsMagica2;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.ISpellFocus;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleFadeOut;
import am2.client.particles.ParticleHoldPosition;
import am2.client.particles.ParticleOrbitPoint;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import am2.common.utils.DimensionUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class TileEntityAstralBarrier extends TileEntityAMPower implements IInventory, IKeystoneLockable<TileEntityAstralBarrier>{

	private ItemStack[] inventory;
	private boolean displayAura;
	private int particleTickCounter;

	public static int keystoneSlot = 0;

	public TileEntityAstralBarrier(){
		super(250);
		inventory = new ItemStack[getSizeInventory()];
		displayAura = false;
		particleTickCounter = 0;
	}

	public void ToggleAuraDisplay(){
		this.displayAura = !this.displayAura;
	}

	public int getRadius(){
		if (this.inventory[0] != null && this.inventory[0].getItem() instanceof ISpellFocus){
			ISpellFocus focus = (ISpellFocus)this.inventory[0].getItem();
			return (focus.getFocusLevel() + 1) * 5;
		}
		return 0;
	}

	public boolean IsActive(){
		return PowerNodeRegistry.For(this.world).checkPower(this, 0.35f * getRadius()) && worldObj.isBlockIndirectlyGettingPowered(pos) > 0 && getRadius() > 0;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, 0, compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public void setWorldObj(World par1World) {
		super.setWorldObj(par1World);
		DimensionUtilities.registerAstralBarrier(this);
	}
	
	@Override
	public void invalidate() {
		DimensionUtilities.invalidateAstralBarrier(this);
		super.invalidate();
	}

	@Override
	public void update(){
		super.update();

		int radius = getRadius();

		if (IsActive()){
			PowerNodeRegistry.For(this.world).consumePower(this, PowerNodeRegistry.For(worldObj).getHighestPowerType(this), 0.35f * radius);
		}

		if (worldObj.isRemote){
			if (IsActive()){
				if (displayAura){
					AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(worldObj, "symbols", pos.getX(), pos.getY() + 0.5, pos.getZ());
					if (effect != null){
						effect.setIgnoreMaxAge(false);
						effect.setMaxAge(100);
						effect.setParticleScale(0.5f);
						effect.AddParticleController(new ParticleOrbitPoint(effect, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, false).SetOrbitSpeed(0.03).SetTargetDistance(radius));
					}
				}

				particleTickCounter++;

				if (particleTickCounter >= 15){

					particleTickCounter = 0;

					AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(worldObj, "sparkle", pos.getX() + 0.5, pos.getY() + 0.1 + worldObj.rand.nextDouble() * 0.5, pos.getZ() + 0.5);
					if (effect != null){
						effect.setIgnoreMaxAge(false);
						effect.setMaxAge(100);
						effect.setParticleScale(0.5f);
						effect.AddParticleController(new ParticleOrbitPoint(effect, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, false).SetOrbitSpeed(0.005).SetTargetDistance(worldObj.rand.nextDouble() * 0.6 - 0.3));
						effect.AddParticleController(new ParticleHoldPosition(effect, 80, 2, true));
						effect.AddParticleController(new ParticleFadeOut(effect, 3, false).setFadeSpeed(0.05f));
					}
				}
			}
		}
	}

	public void onEntityBlocked(EntityLivingBase entity){
		if (this.world.isRemote){
			if (PowerNodeRegistry.For(worldObj).checkPower(this, PowerTypes.DARK, 50)){
				entity.attackEntityFrom(DamageSource.magic, 5);
				PowerNodeRegistry.For(worldObj).consumePower(this, PowerTypes.DARK, 50);
			}
		}
	}

	@Override
	public int getSizeInventory(){
		return 4;
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
		return "Astral Barrier";
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (worldObj.getTileEntity(pos) != this){
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
		NBTTagList nbttaglist = nbttagcompound.getTagList("AstralBarrierInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
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

		nbttagcompound.setTag("AstralBarrierInventory", nbttaglist);
		return nbttagcompound;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

//	private void writeInventory(AMDataWriter writer){
//		for (ItemStack stack : inventory){
//			if (stack == null){
//				writer.add(false);
//				continue;
//			}else{
//				writer.add(true);
//				writer.add(stack);
//			}
//		}
//	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 50;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[1];
		runes[1] = inventory[2];
		runes[2] = inventory[3];
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
