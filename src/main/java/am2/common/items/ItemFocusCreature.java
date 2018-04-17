package am2.common.items;

import am2.api.items.ItemFilterFocus;
import am2.common.defs.ItemDefs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ItemFocusCreature extends ItemFilterFocus{

	public ItemFocusCreature(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				" P ",
				"LFT",
				" W ",
				'P', Items.PORKCHOP,
				'B', Items.LEATHER,
				'F', ItemDefs.standardFocus,
				'T', Items.FEATHER,
				'W', Blocks.WOOL,
		};
	}

	@Override
	public String getInGameName(){
		return "Creature Focus";
	}

	@Override
	public Class<? extends Entity> getFilterClass(){
		return EntityCreature.class;
	}

}
