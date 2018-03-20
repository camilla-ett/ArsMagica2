package am2.api.skill;

import net.minecraft.util.text.TextFormatting;

public class SkillPoint {
	
	public static final SkillPoint SILVER_POINT = new SkillPoint(-1, "Silver", TextFormatting.GRAY, 0x999999, -1, -1).disableRender();
	public static final SkillPoint SKILL_POINT_1 = new SkillPoint(1, "Blue", TextFormatting.BLUE, 0x0000ff, 0, 1);
	public static final SkillPoint SKILL_POINT_2 = new SkillPoint(2, "Green", TextFormatting.GREEN, 0x00ff00, 20, 2);
	public static final SkillPoint SKILL_POINT_3 = new SkillPoint(3, "Red", TextFormatting.RED, 0xff0000, 30, 2);
	public static final SkillPoint SKILL_POINT_4 = new SkillPoint(4, "Yellow", TextFormatting.YELLOW, 0xffff00, 40, 3);
	public static final SkillPoint SKILL_POINT_5 = new SkillPoint(5, "Magenta", TextFormatting.LIGHT_PURPLE, 0xff00ff, 50, 3);
	public static final SkillPoint SKILL_POINT_6 = new SkillPoint(6, "Cyan", TextFormatting.AQUA, 0x00ffff, 60, 4);

	private final int tier;
	private final int color;
	private final int minEarnLevel;
	private final int levelsForPoint;
	private final String name;
	private final TextFormatting chatColor;
	
	private boolean render = true;
	
	public SkillPoint(int tier, String name, TextFormatting chatColor, int color, int minEarnLevel, int levelsForPoint) {
		this.tier = tier;
		this.color = color;
		this.name = name;
		this.minEarnLevel = minEarnLevel;
		this.levelsForPoint = levelsForPoint;
		this.chatColor = chatColor;
	}

	public int getTier() {
		return this.tier;
	}

	public int getColor() {
		return this.color;
	}
	
	public int getLevelsForPoint() {
		return this.levelsForPoint;
	}
	
	public int getMinEarnLevel() {
		return this.minEarnLevel;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public TextFormatting getChatColor() {
		return this.chatColor;
	}
	
	public boolean canRender() {
		return this.render;
	}
	
	public SkillPoint disableRender() {
		this.render = false;
		return this;
	}
}
