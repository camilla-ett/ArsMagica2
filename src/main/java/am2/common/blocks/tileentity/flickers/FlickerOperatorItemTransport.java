package am2.common.blocks.tileentity.flickers;

import java.util.ArrayList;
import java.util.HashMap;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.AbstractFlickerFunctionality;
import am2.api.math.AMVector3;
import am2.common.blocks.BlockCrystalMarker;
import am2.common.blocks.tileentity.TileEntityCrystalMarker;
import am2.common.blocks.tileentity.TileEntityFlickerHabitat;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemOre;
import am2.common.utils.GetFirstStackStartingFromSlotResult;
import am2.common.utils.InventoryUtilities;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlickerOperatorItemTransport extends AbstractFlickerFunctionality{
	private static final float BASE_POWERED_ACTIVATION_TIME = 10.0f;
	private static final float TIME_DISCOUNT_PERCENTAGE = 0.33f;

	public static final FlickerOperatorItemTransport instance = new FlickerOperatorItemTransport();

	@Override
	public boolean RequiresPower(){
		return false;
	}

	@Override
	public int PowerPerOperation(){
		return 10;
	}

	@Override
	public boolean DoOperation(World world, IFlickerController<?> controller, boolean powered){
		Affinity[] emptyFlickerList = new Affinity[6];
		this.DoOperation(world, controller, powered, emptyFlickerList);
		return false;
	}

	@Override
	public boolean DoOperation(World world, IFlickerController<?> controller, boolean powered, Affinity[] flickers){
		if (world.isRemote){
			return false;
		}

		//Gets the habitat running this operation
		TileEntityFlickerHabitat habitat = null;
		if (controller instanceof TileEntityFlickerHabitat){
			habitat = (TileEntityFlickerHabitat)controller;
		}else{
			//if the habitat couldn't be found, bail
			return false;
		}

		int toMove = 0;

		//iterate through modifiers and adjust params
		//if(powered){
		for (int i = 0; i < 6; i++){
			if (flickers[i] == Affinity.ARCANE){
				toMove += 32;
			}
		}
		//}

		//ensure toMove is at least 1
		toMove = Math.max(toMove, 1);


		ArrayList<AMVector3> removeFromInList = new ArrayList<AMVector3>();
		boolean itemFound = false;

		//if the in list position has exceeded the size of the list reset it
		if (habitat.getInListPosition() >= habitat.getInListSize()){
			habitat.setInListPosition(0);
		}

		for (int inIterator = habitat.getInListPosition(); inIterator < habitat.getInListSize(); ++inIterator){
			//Gets the tile entity for the attached crystal marker at the specified location
			AMVector3 vector = habitat.getInListAt(inIterator);
			TileEntity te = null;
			TileEntityCrystalMarker crystalMarkerTE = this.GetCrystalMarkerTileEntity(world, vector.toBlockPos());

			if (crystalMarkerTE == null){
				//crystal marker no longer exists, remove it from the list
				removeFromInList.add(vector);
				break;
			}

			te = this.GetAttachedCrystalMarkerTileEntity(world, crystalMarkerTE, vector);

			if (te != null && te instanceof IInventory){
				//if the crystal is attached to an inventory
				itemFound = this.moveItem(world, (IInventory)te, crystalMarkerTE, habitat, toMove);

				if (itemFound){
					//if an item was found the break out of the current loop
					habitat.setInListPosition(inIterator + 1);
					break;
				}else if (te instanceof TileEntityChest){
					//This is to handle the special case of double chests
					TileEntityChest adjacent = InventoryUtilities.getAdjacentChest((TileEntityChest)te);

					if (adjacent != null){
						itemFound = this.moveItem(world, adjacent, crystalMarkerTE, habitat, toMove);
						if (itemFound){
							habitat.setInListPosition(inIterator + 1);
						}
					}
				}
			}
		}

		if (!itemFound){
			//if no items were found to move then reset the input list position
			habitat.setInListPosition(0);
		}

		for (AMVector3 vector : removeFromInList){
			//remove the invalid in list positions from the maintained in list
			habitat.removeInListAt(vector);
		}

		return itemFound;
	}

	private boolean moveItem(World world, IInventory inventory, TileEntityCrystalMarker crystalMarkerTE, TileEntityFlickerHabitat habitat, int toMove){
		boolean itemFound = false;
		ItemStack orgStack = null;
		int currentSlot = 0;
		GetFirstStackStartingFromSlotResult result = InventoryUtilities.getFirstStackStartingFromSlot(inventory, orgStack, currentSlot, crystalMarkerTE.getFacing());
		orgStack = result.stack;
		currentSlot = result.slot;
		while (orgStack != null){
			if (this.InputCanMove(crystalMarkerTE, orgStack, inventory)){
				ItemStack stackCopy = orgStack.copy();
				int amountToMove = Math.min(toMove, stackCopy.stackSize);
				ItemStack mergeStack = stackCopy.splitStack(amountToMove);
				if (this.FindOutput(world, habitat, mergeStack, inventory)){
					//if a valid item was found in input, and a valid output for it was found remove the item from the source
					InventoryUtilities.deductFromInventory(inventory, orgStack, amountToMove - mergeStack.stackSize, crystalMarkerTE.getFacing());

					itemFound = true;
					break;
				}else{
					currentSlot++;
				}
			}else{
				currentSlot++;
			}

			result = InventoryUtilities.getFirstStackStartingFromSlot(inventory, orgStack, currentSlot, crystalMarkerTE.getFacing());
			orgStack = result.stack;
			currentSlot = result.slot;
			if (currentSlot == -1){
				orgStack = null;
			}
		}

		return itemFound;
	}


	/**
	 * Ensures that the stack can be moved through the crystalMarker from the inventory
	 *
	 * @param crystalMarker The crystal marker doing the move
	 * @param stack         The ItemStack to move
	 * @param inventory     The Inventory the item is being moved from
	 * @return true if the item can be moved, false otherwise
	 */
	private boolean InputCanMove(TileEntityCrystalMarker crystalMarker, ItemStack stack, IInventory inventory){
		switch (crystalMarker.getMarkerType()){
		case BlockCrystalMarker.META_IN:
			return true;
		case BlockCrystalMarker.META_REGULATE_MULTI:
			return crystalMarker.filterHasItem(stack) && crystalMarker.getFilterCount(stack) < InventoryUtilities.getLikeItemCount(inventory, stack);
		case BlockCrystalMarker.META_SET_IMPORT:
			return crystalMarker.filterHasItem(stack);
		case BlockCrystalMarker.META_SPELL_EXPORT:
			return !crystalMarker.filterHasItem(stack);
		}

		return false;
	}


	/**
	 * Will try to find a place to move the item stack
	 *
	 * @param stack The item stack to move
	 * @return Returns true if the item can be moved, returns false otherwise
	 */
	private boolean FindOutput(World world, TileEntityFlickerHabitat attuner, ItemStack stack, IInventory source){
		HashMap<Integer, ArrayList<AMVector3>> removeFromOutList = new HashMap<Integer, ArrayList<AMVector3>>();
		boolean itemMoved = false;

		for (int priority = 0; priority <= TileEntityFlickerHabitat.PRIORITY_FINAL; ++priority){

			if (attuner.getOutListPosition(priority) >= attuner.getOutListSize(priority)){
				//if the out list position has gone outside the list size reset it to 0
				attuner.setOutListPosition(priority, 0);
			}

			int start = attuner.getOutListPosition(priority);
			int pos = start;
			boolean fullLoop = false;

			while (!fullLoop){
				//get the crystal marker tile entity for the specified position
				AMVector3 vector = attuner.getOutListAt(priority, pos);
				TileEntity te = null;
				TileEntityCrystalMarker crystalMarkerTE = this.GetCrystalMarkerTileEntity(world, vector.toBlockPos());

				if (crystalMarkerTE == null){
					//crystal marker no longer exists, remove it from the list
					if (!removeFromOutList.containsKey(priority))
						removeFromOutList.put(priority, new ArrayList<AMVector3>());
					removeFromOutList.get(priority).add(vector);
					break;
				}

				te = this.GetAttachedCrystalMarkerTileEntity(world, crystalMarkerTE, vector);
				int markerType = world.getBlockState(vector.toBlockPos()).getValue(BlockCrystalMarker.TYPE);

				if (te != null && te instanceof IInventory){
					IInventory inventory = (IInventory)te;
					itemMoved = this.outputItem(markerType, new IInventory[]{inventory}, stack, crystalMarkerTE, new IInventory[]{source});

					if (itemMoved){
						attuner.setOutListPosition(priority, pos + 1);
					}
				}

				if (!itemMoved && te instanceof TileEntityChest){
					//This handles the special case of double chests
					TileEntityChest adjacent = InventoryUtilities.getAdjacentChest((TileEntityChest)te);

					if (adjacent != null){
						IInventory inventory = adjacent;
						itemMoved = this.outputItem(markerType, new IInventory[]{inventory, (IInventory)te}, stack, crystalMarkerTE, new IInventory[]{source});

						if (itemMoved){
							attuner.setOutListPosition(priority, pos + 1);
						}
					}
				}

				if (itemMoved)
					break;

				pos++;
				pos %= attuner.getOutListSize(priority);
				if (pos == start)
					fullLoop = true;
			}

			for (int i : removeFromOutList.keySet()){
				for (AMVector3 vector : removeFromOutList.get(i)){
					attuner.removeOutListAt(i, vector);
				}
			}

			if (!itemMoved){
				attuner.setOutListPosition(priority, 0);
			}else{
				break;
			}
		}

		return itemMoved;
	}


	/**
	 * Attempts to move an item into the specified inventory
	 *
	 * @param markerType The type of marker attached to the inventory we are trying to move into
	 * @param inventory  The inventory we are trying to move into
	 * @param stack      The item stack we are trying to move
	 * @return Returns true if successful, false otherwise
	 */
	private boolean outputItem(int markerType, IInventory[] outputs, ItemStack stack, TileEntityCrystalMarker crystalMarkerTE, IInventory[] sources){
		EnumFacing side = crystalMarkerTE.getFacing();
		switch (markerType){
		case BlockCrystalMarker.META_OUT:
		case BlockCrystalMarker.META_FINAL_DEST:
			//Inventory must have room for the item
			for (IInventory inventory : outputs){
				if (InventoryUtilities.inventoryHasRoomFor(inventory, stack, 1, side) && this.isDestinationDifferentFromSource(outputs, sources)){
					return InventoryUtilities.mergeIntoInventory(inventory, stack, stack.stackSize, side);
				}
			}

			break;

		case BlockCrystalMarker.META_LIKE_EXPORT:
			//One of the destination inventories must already contain at least 1 of the item we are trying to move and
			//must have room for the item
			boolean atLeastOneContains = false;
			for (IInventory inventory : outputs){
				if (InventoryUtilities.inventoryHasItem(inventory, stack, 1, side)){
					atLeastOneContains = true;
					break;
				}
			}

			for (IInventory inventory : outputs){
				if (atLeastOneContains && InventoryUtilities.inventoryHasRoomFor(inventory, stack, 1, side) && this.isDestinationDifferentFromSource(outputs, sources)){
					return InventoryUtilities.mergeIntoInventory(inventory, stack, stack.stackSize, side);
				}
			}
			break;
		case BlockCrystalMarker.META_SET_EXPORT:
			//Check that the item exists in the item filter and that the inventory has room for the item
			for (IInventory inventory : outputs){
				if (crystalMarkerTE.filterHasItem(stack) && InventoryUtilities.inventoryHasRoomFor(inventory, stack, 1, side) && this.isDestinationDifferentFromSource(outputs, sources)){
					return InventoryUtilities.mergeIntoInventory(inventory, stack, stack.stackSize, side);
				}
			}
			break;

		case BlockCrystalMarker.META_REGULATE_MULTI:
		case BlockCrystalMarker.META_SPELL_EXPORT:
		case BlockCrystalMarker.META_REGULATE_EXPORT:
			//Check that the item exists in the item filter and that the inventory doesn't have more then the allocated amount already
			//and that the inventory has room for the item
			for (IInventory inventory : outputs){
				if (crystalMarkerTE.filterHasItem(stack) && InventoryUtilities.getLikeItemCount(inventory, stack, side) < crystalMarkerTE.getFilterCount(stack) && InventoryUtilities.inventoryHasRoomFor(inventory, stack, 1, side) && this.isDestinationDifferentFromSource(outputs, sources)){
					return InventoryUtilities.mergeIntoInventory(inventory, stack, stack.stackSize, side);
				}
			}
		}

		return false;
	}

	private boolean isDestinationDifferentFromSource(IInventory[] destinations, IInventory[] sources){
		boolean destinationDifferent = true;
		for (IInventory destination : destinations){
			for (IInventory source : sources){
				if (destination instanceof ISidedInventory == false && destination.equals(source)){
					destinationDifferent = false;
					break;
				}
			}
		}

		return destinationDifferent;
	}


	/**
	 * Gets the tile entity for the crystal marker at the specified co-ordinates
	 *
	 * @param world The world object to search through
	 * @param xy
	 * @param y
	 * @param z
	 * @return
	 */
	private TileEntityCrystalMarker GetCrystalMarkerTileEntity(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);

		if (te != null && te instanceof TileEntityCrystalMarker){
			return (TileEntityCrystalMarker)te;
		}else{
			return null;
		}
	}

	private TileEntity GetAttachedCrystalMarkerTileEntity(World world, TileEntityCrystalMarker crystalMarkerTE, AMVector3 cmVector){
		TileEntity te = null;

		//Get the inventory tile entity
		te = world.getTileEntity(cmVector.toBlockPos().offset(crystalMarkerTE.getFacing()));

		return te;
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		if (powered){
			float rechargeTime = BASE_POWERED_ACTIVATION_TIME;

			for (int i = 0; i < 6; i++){
				if (flickers[i] == Affinity.LIGHTNING){
					rechargeTime = rechargeTime - (rechargeTime * TIME_DISCOUNT_PERCENTAGE);
				}
			}

			return Math.round(rechargeTime);

		}else{
			return 20;
		}
	}

	@Override
	public void RemoveOperator(World world,
							   IFlickerController<?> controller, boolean powered){
	}

	@Override
	public void RemoveOperator(World world,
							   IFlickerController<?> controller, boolean powered,
							   Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				" B ",
				"CAC",
				" B ",
				Character.valueOf('A'), new ItemStack(ItemDefs.flickerJar, 1, GameRegistry.findRegistry(Affinity.class).getId(Affinity.AIR)),
				Character.valueOf('C'), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE),
				Character.valueOf('B'), new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ)
		};
	}
	
	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation("arsmagica2", "FlickerOperatorItemTransport");
	}

	@Override
	public Affinity[] getMask() {
		return new Affinity[]{Affinity.AIR};
	}

}
