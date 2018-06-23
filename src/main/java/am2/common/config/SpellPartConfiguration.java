package am2.common.config;

import java.io.File;
import java.util.ArrayList;

import am2.api.ArsMagicaAPI;
import am2.api.spell.AbstractSpellPart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SpellPartConfiguration extends Configuration{
	
	private final ArrayList<String> disabled = new ArrayList<>();
	
	public SpellPartConfiguration(File file){
		super(file);
	}
	
	public ArrayList<String> getDisabledSkills(boolean reload) {
		
		if (reload) {
			load();
			disabled.clear();
			for (ResourceLocation rl : GameRegistry.findRegistry(AbstractSpellPart.class).getKeys()) {
				String name = rl.toString();
				if (name.startsWith("arsmagica2:")) name = rl.getResourcePath();
				Property prop = this.get("enabled_spell_part", name, true);
				prop.setRequiresWorldRestart(true);
				if (!prop.getBoolean())
					disabled.add(name);
			}
			save();
		}
		
		return disabled;
	}
	
	public boolean isSkillDisabled(String name) {
		name = name.replaceAll("arsmagica2:", "");
		for (String str : disabled) {
			if (str.equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public int[] getDisabledSkillIDs() {
		ArrayList<Integer> intArray = new ArrayList<>();
		for (String disabled : disabled) {
			ResourceLocation rl = new ResourceLocation(disabled);
			if (!disabled.contains(":")) rl = new ResourceLocation("arsmagica2:" + disabled);
			intArray.add(GameRegistry.findRegistry(Skill.class).getId(rl));
		}
		int[] ret = new int[intArray.size()];
		for (int i = 0; i < intArray.size(); i++) {
			int d = intArray.get(i).intValue();
			ret[i] = d;
		}
		return ret;
	}

	public void disableAllSkillsIn(int[] disabledSkills) {
		disabled.clear();
		for (int i : disabledSkills) {
			ResourceLocation rl = GameRegistry.findRegistry(Skill.class).getObjectById(i).getRegistryName();
			String name = rl.toString();
			if (name.startsWith("arsmagica2:")) name = rl.getResourcePath();
			disabled.add(name);
		}
	}
}
