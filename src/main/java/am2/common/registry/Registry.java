package am2.common.registry;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class Registry {

    private static List<Item> itemsToRegister;
    private static List<Block> blocksToRegister;
    private static List<SoundEvent> soundsToRegister;
    private static List<Potion> potionsToRegister;
    private static List <Biome> biomesToRegister;
    private static List <Enchantment> enchantmentsToRegister;

    public static List<Item> GetItemsToRegister() {
        if (itemsToRegister == null) itemsToRegister = new ArrayList<>();
        return itemsToRegister;
    }

    public static List<Block> GetBlocksToRegister() {
        if (blocksToRegister == null) blocksToRegister = new ArrayList<>();
        return blocksToRegister;
    }

    public static List<SoundEvent> GetSoundsToRegister() {
        if (soundsToRegister == null) soundsToRegister = new ArrayList<>();
        return soundsToRegister;
    }

    public static List<Potion> GetPotionsToRegister() {
        if (potionsToRegister == null) potionsToRegister = new ArrayList<>();
        return potionsToRegister;
    }

    public static List <Biome> GetBiomesToRegister ( ) {
        if ( biomesToRegister == null ) biomesToRegister = new ArrayList <> ( );
        return biomesToRegister;
    }

    public static List <Enchantment> GetEnchantmentsToRegister ( ) {
        if ( enchantmentsToRegister == null ) enchantmentsToRegister = new ArrayList <> ( );
        return enchantmentsToRegister;
    }
}
