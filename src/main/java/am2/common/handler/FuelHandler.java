package am2.common.handler;

import am2.common.defs.BlockDefs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Orinion on 09.01.2017.
 */
public class FuelHandler {

    @SubscribeEvent
    public int getBurnTime(FurnaceFuelBurnTimeEvent event) {
        Item fuelItem = event.getItemStack().getItem();
        Block fuelBlock = Block.getBlockFromItem(fuelItem);
        if (fuelBlock == BlockDefs.witchwoodPlanks) event.setBurnTime(300);
        if (fuelBlock == BlockDefs.witchwoodLog) event.setBurnTime(300);
        if (fuelBlock == BlockDefs.witchwoodSapling) event.setBurnTime(100);
        if (fuelBlock == BlockDefs.witchwoodStairs) event.setBurnTime(300);

        return 0;
    }
}
