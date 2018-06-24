package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.items.ItemFilterFocus;
import am2.common.defs.ItemDefs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFocusPlayer extends ItemFilterFocus{

	public ItemFocusPlayer(){
		super();
	}

	@Override
	public Class<? extends Entity> getFilterClass(){
		return EntityPlayer.class;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"L",
				"F",
				Character.valueOf('L'), new ItemStack(ItemDefs.essence, 1, GameRegistry.findRegistry(Affinity.class).getKey(Affinity.LIFE)),
				Character.valueOf('F'), ItemDefs.standardFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Player Focus";
	}
}
