package am2.api;

import am2.api.skill.Skill;
import am2.api.skill.SkillPoint;
import am2.api.skill.SkillTree;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class SkillRegistry {
	public static void registerSkill (String stringID, ResourceLocation icon, SkillPoint tier, int posX, int posY, SkillTree tree, int ID, String... parents) {
		registerSkill(new Skill(icon, tier, posX, posY, tree, ID, parents).setRegistryName(ArsMagicaAPI.getCurrentModId(), stringID.toLowerCase()));
	}
	
	public static void registerSkill (boolean createEntry, String stringID, ResourceLocation icon, SkillPoint tier, int posX, int posY, SkillTree tree, int ID, String... parents) {
		registerSkill(createEntry, new Skill(icon, tier, posX, posY, tree, ID, parents).setRegistryName(ArsMagicaAPI.getCurrentModId(), stringID.toLowerCase()));
	}
	
	public static void registerSkill (boolean createEntry, Skill skill) {
		ArsMagicaAPI.getSkillRegistry().register(skill);
	}
	
	public static void registerSkill (Skill skill) {
		registerSkill(true, skill);
	}
	
	public static ArrayList<Skill> getSkillsForTree (SkillTree tree) {
		ArrayList<Skill> skillList = new ArrayList<Skill>();
		for (Skill skill : ArsMagicaAPI.getSkillRegistry().getValues()) {
			if (skill != null && skill.getTree() != null && skill.getTree().equals(tree))
				skillList.add(skill);
		}
		return skillList;
	}

	public static Skill getSkillFromName(String str) {
		return ArsMagicaAPI.getSkillRegistry().getValue(new ResourceLocation(str));
	}
	
}
