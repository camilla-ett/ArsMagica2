package am2.common.container;

import am2.common.ObeliskFuelHelper;
import am2.common.blocks.tileentity.TileEntityObelisk;
import am2.common.container.slot.SlotObelisk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerObelisk extends Container{

	private TileEntityObelisk obelisk;

	private int INVENTORY_STORAGE_START = 0;
	private int PLAYER_INVENTORY_START = 1;
	private int PLAYER_ACTION_BAR_START = 28;
	private int PLAYER_ACTION_BAR_END = 37;

	public ContainerObelisk(TileEntityObelisk obelisk, EntityPlayer player){

		this.obelisk = obelisk;

		addSlotToContainer(new SlotObelisk(obelisk, 0, 79, 47));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < INVENTORY_STORAGE_START){
				if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_ACTION_BAR_END, true)){
					return null;
				}
			}else if (i >= INVENTORY_STORAGE_START && i < PLAYER_INVENTORY_START) //from player inventory
			{
				if (ObeliskFuelHelper.instance.getFuelBurnTime(itemstack) > 0){
					if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)){
						return null;
					}
				}
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) //from player inventory
			{
				if (ObeliskFuelHelper.instance.getFuelBurnTime(itemstack) > 0){
					if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_INVENTORY_START, false)){
						if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)){
							return null;
						}
					}
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END){
				if (ObeliskFuelHelper.instance.getFuelBurnTime(itemstack) > 0){
					if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_ACTION_BAR_START - 1, false)){
						return null;
					}
				}
			}else if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)){
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

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return obelisk.isUseableByPlayer(entityplayer);
	}

}
