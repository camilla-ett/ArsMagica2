package am2.common.extensions;

import am2.api.extensions.IRiftStorage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class RiftStorage implements IRiftStorage, ICapabilityProvider, ICapabilitySerializable<NBTBase> {
	
	private ItemStack[] stacks = new ItemStack[54];

	private int accessLevel;
	
	@CapabilityInject(IRiftStorage.class)
	public static Capability<IRiftStorage> INSTANCE = null;
	
	@Override
	public int getSizeInventory(){
		return 54;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		return this.stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (this.stacks[i] != null){
			if (this.stacks[i].getCount() <= j){
				ItemStack itemstack = this.stacks[i];
				this.stacks[i] = null;
				return itemstack;
			}

			ItemStack itemstack1 = this.stacks[i].splitStack(j);

			if (this.stacks[i].getCount() == 0){
				this.stacks[i] = null;
			}

			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (this.stacks[i] != null){
			ItemStack itemstack = this.stacks[i];
			this.stacks[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		this.stacks[i] = itemstack;
		if (itemstack != null && itemstack.getCount() > this.getInventoryStackLimit()){
			itemstack.getCount() = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Void Storage";
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		//if (accessEntity == null || accessEntity.isDead) return false;
		return true;//entityplayer.getDistanceSqToEntity(accessEntity) < 64;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return true;
	}

	@Override
	public void markDirty(){
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		//this.stacks = new ItemStack[54];
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INSTANCE)
			return (T) this;
		return null;
	}
	
	public static IRiftStorage For(EntityLivingBase thePlayer) {
		return thePlayer.getCapability(INSTANCE, null);
	}
	
	@Override
	public NBTBase serializeNBT() {
		return new IRiftStorage.Storage().writeNBT(INSTANCE, this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		new IRiftStorage.Storage().readNBT(INSTANCE, this, null, nbt);
	}
	
	@Override
	public int getAccessLevel() {
		return this.accessLevel;
	}
	
	@Override
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
}
