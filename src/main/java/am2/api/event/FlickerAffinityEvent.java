package am2.api.event;

import am2.common.entity.EntityFlicker;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;

public class FlickerAffinityEvent extends Event {
	
	private final ArrayList<Integer> validAffinity;
	private final EntityFlicker flicker;
	private final Biome biome;

	//How to go back to 1.7.10. INTEGER ID FOR EVERYTHING
	
	public FlickerAffinityEvent(ArrayList<Integer> validAffinity, EntityFlicker flicker, Biome biome) {
		this.validAffinity = validAffinity;
		this.flicker = flicker;
		this.biome = biome;
	}
	
	public Biome getBiome() {
		return biome;
	}
	
	public EntityFlicker getFlicker() {
		return flicker;
	}
	
	public ArrayList<Integer> getValidAffinity() {
		return validAffinity;
	}
}
