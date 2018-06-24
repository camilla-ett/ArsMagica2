package am2.common.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import am2.api.blocks.IKeystoneLockable;
import am2.api.extensions.ISpellCaster;
import am2.api.spell.AbstractSpellPart;
import am2.api.spell.SpellComponent;
import am2.common.defs.BlockDefs;
import am2.common.spell.SpellCaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class TileEntitySpellSealedDoor extends TileEntity implements ITickable, IInventory, IKeystoneLockable<TileEntitySpellSealedDoor>{

	private ItemStack[] inventory;

	private int lastAppliedTime = -1;
	private int closeTime = -1;
	private int curTime = 0;
	private int opentime = 40;

	private ArrayList<SpellComponent> appliedParts;
	private ArrayList<SpellComponent> key;

	public TileEntitySpellSealedDoor(){
		inventory = new ItemStack[getSizeInventory()];
		appliedParts = new ArrayList<SpellComponent>();
		key = new ArrayList<SpellComponent>();
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
			if (inventory[i].getCount() <= j){
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].getCount() == 0){
				inventory[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return false;
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
		if (itemstack != null && itemstack.getCount() > getInventoryStackLimit()){
			itemstack.getCount() = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Spell Sealed Door";
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
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
		analyzeSpellForKey();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("SpellSealedDoorInventory", Constants.NBT.TAG_COMPOUND);
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

		nbttagcompound.setTag("SpellSealedDoorInventory", nbttaglist);
		return nbttagcompound;
	}

	@Override
	public void update(){

		if (!world.isRemote){
			curTime++;

			if (closeTime == -1 && lastAppliedTime != -1){
				if (curTime > lastAppliedTime + 10){
					clearAppliedParts();
					return;
				}
				if (checkKey()){
					clearAppliedParts();
					setOpenState(true);
					this.closeTime = curTime + opentime;
				}
			}

			if (closeTime != -1 && curTime > closeTime){
				clearAppliedParts();
				setOpenState(false);
				closeTime = -1;
			}
		}
	}

	private void setOpenState(boolean open){
		BlockDefs.spellSealedDoor.toggleDoor(world, pos, open);
	}

	public void addPartToCurrentKey(SpellComponent component){
		this.appliedParts.add(component);
		this.lastAppliedTime = curTime;
	}

	private boolean checkKey(){
		if (key.size() != appliedParts.size()) return false;
		if (key.equals(appliedParts)) return true;
		return false;
	}

	private void clearAppliedParts(){
		appliedParts.clear();
		lastAppliedTime = -1;
	}

	public void analyzeSpellForKey(){
		ItemStack spell = this.inventory[3];

		if (spell == null) return;

		//if we're here, we have a spell to analyze!
		key.clear();
		ISpellCaster caster = spell.getCapability(SpellCaster.INSTANCE, null);
		if (caster != null) {
			for (List<AbstractSpellPart> parts : caster.getSpellCommon()) {
				for (AbstractSpellPart part : parts) {
					if (part instanceof SpellComponent) {
						key.add((SpellComponent) part);
					}
				}
			}
		}
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
