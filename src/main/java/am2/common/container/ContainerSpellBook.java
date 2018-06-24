package am2.common.container;

import am2.common.container.slot.SlotOneItemClassOnly;
import am2.common.items.ItemSpellBase;
import am2.common.items.ItemSpellBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpellBook extends Container{
	private ItemStack bookStack;
	private InventorySpellBook spellBookStack;
//	private int bookSlot;
	public int specialSlotIndex;

	public ContainerSpellBook(InventoryPlayer inventoryplayer, ItemStack bookStack, InventorySpellBook inventoryspellbook){
		//addSlot(new Slot(spellBook,0, 21, 36)); //inventory, index, x, y
		this.spellBookStack = inventoryspellbook;
		this.bookStack = bookStack;
//		this.bookSlot = inventoryplayer.currentItem;

		int slotIndex = 0;
		//Spell Book Pages - active spells
		for (int i = 0; i < 8; ++i){
			addSlotToContainer(new SlotOneItemClassOnly(spellBookStack, slotIndex++, 18, 5 + (i * 18), ItemSpellBase.class, 1));
		}

		//Spell Book Pages - reserve spells
		for (int i = 0; i < 4; ++i){
			for (int k = 0; k < 8; k++){
				addSlotToContainer(new SlotOneItemClassOnly(spellBookStack, slotIndex++, 138 + (i * 26), 5 + (k * 18), ItemSpellBase.class, 1));
			}
		}

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 48 + k * 18, 171 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			if (inventoryplayer.getStackInSlot(j1) == bookStack){
				specialSlotIndex = j1 + 67;
				continue;
			}
			addSlotToContainer(new Slot(inventoryplayer, j1, 48 + j1 * 18, 229));
		}

	}

	public ItemStack[] GetActiveSpells(){
		ItemStack[] itemStack = new ItemStack[7];
		for (int i = 0; i < 7; ++i){
			itemStack[i] = spellBookStack.getStackInSlot(i);
		}
		return itemStack;
	}

	public ItemStack[] GetFullInventory(){
		ItemStack[] stack = new ItemStack[40];
		for (int i = 0; i < 40; ++i){
			stack[i] = ((Slot)inventorySlots.get(i)).getStack();
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer){
		World world = entityplayer.world;

		if (!world.isRemote){
			ItemStack spellBookItemStack = bookStack;
			ItemSpellBook spellBook = (ItemSpellBook)spellBookItemStack.getItem();
			ItemStack[] items = GetFullInventory();
			spellBook.UpdateStackTagCompound(spellBookItemStack, items);
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, spellBookItemStack);
		}

		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return spellBookStack.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < 40){
				if (!mergeItemStack(itemstack1, 40, 75, true)){
					return null;
				}
			}else if (i >= 40 && i < 67) //range 27 - player inventory
			{
				if (itemstack.getItem() instanceof ItemSpellBase){
					for (int n = 0; n < 40; n++){
						Slot scrollSlot = (Slot)inventorySlots.get(n);
						if (scrollSlot.getHasStack()) continue;

						ItemStack newStack = itemstack1.copy();

						scrollSlot.putStack(newStack);
						scrollSlot.onSlotChanged();
						itemstack1.setCount(itemstack1.getCount()-1);
						if (itemstack1.getCount() == 0){
							slot.putStack(null);
							slot.onSlotChanged();
						}
						return null;
					}
				}
				if (!mergeItemStack(itemstack1, 67, 75, false)){
					return null;
				}
			}else if (i >= 67 && i < 76) //range 9 - player action bar
			{
				if (itemstack.getItem() instanceof ItemSpellBase){
					for (int n = 0; n < 40; n++){
						Slot scrollSlot = (Slot)inventorySlots.get(n);
						if (scrollSlot.getHasStack()) continue;

						ItemStack newStack = itemstack1.copy();

						scrollSlot.putStack(newStack);
						scrollSlot.onSlotChanged();
						itemstack1.setCount(itemstack1.getCount()-1);
						if (itemstack1.getCount() == 0){
							slot.putStack(null);
							slot.onSlotChanged();
						}
						return null;
					}
				}
				if (!mergeItemStack(itemstack1, 40, 67, false)){
					return null;
				}
			}else if (!mergeItemStack(itemstack1, 40, 75, false)){
				return null;
			}
			if (itemstack1.getCount() == 0){
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() != itemstack.getCount()){
				slot.onSlotChange(itemstack1, itemstack);
			}else{
				return null;
			}
		}
		return itemstack;
	}

	/*@Override
	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
	{
		boolean var5 = false;
		int var6 = par2;

		if (par4)
		{
			var6 = par3 - 1;
		}

		Slot var7;
		ItemStack var8;

		if (par1ItemStack.isStackable())
		{
			while (par1ItemStack.getCount() > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2))
			{
				if (var6 != this.specialSlotIndex){
					var7 = (Slot)this.inventorySlots.get(var6);
					var8 = var7.getStack();

					if (var8 != null && var8.itemID == par1ItemStack.itemID && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStacksEqual(par1ItemStack, var8))
					{
						int var9 = var8.getCount() + par1ItemStack.getCount();

						if (var9 <= par1ItemStack.getMaxStackSize())
						{
							par1ItemStack.getCount() = 0;
							var8.getCount() = var9;
							var7.onSlotChanged();
							var5 = true;
						}
						else if (var8.getCount() < par1ItemStack.getMaxStackSize())
						{
							par1ItemStack.getCount() -= par1ItemStack.getMaxStackSize() - var8.getCount();
							var8.getCount() = par1ItemStack.getMaxStackSize();
							var7.onSlotChanged();
							var5 = true;
						}
					}
				}

				if (par4)
				{
					--var6;
				}
				else
				{
					++var6;
				}
			}
		}

		if (par1ItemStack.getCount() > 0)
		{
			if (par4)
			{
				var6 = par3 - 1;
			}
			else
			{
				var6 = par2;
			}

			while (!par4 && var6 < par3 || par4 && var6 >= par2)
			{
				if (var6 != this.specialSlotIndex){
					var7 = (Slot)this.inventorySlots.get(var6);
					var8 = var7.getStack();

					if (var8 == null)
					{
						var7.putStack(par1ItemStack.copy());
						var7.onSlotChanged();
						par1ItemStack.getCount() = 0;
						var5 = true;
						break;
					}
				}

				if (par4)
				{
					--var6;
				}
				else
				{
					++var6;
				}
			}
		}

		return var5;
	}*/
}









