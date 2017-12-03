package am2.common.spell.shape;

import java.util.EnumSet;

import am2.api.spell.Operation;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.entity.EntitySpellEffect;
import am2.common.items.ItemOre;
import am2.common.spell.SpellCastResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Wall extends SpellShape{

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
				BlockDefs.magicWall,
				Blocks.COBBLESTONE_WALL,
				Blocks.OAK_FENCE,
				BlockDefs.magicWall,
				"E:*", 2500
		};
	}

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){
		if (world.isRemote) return SpellCastResult.SUCCESS;
		int radius = (int) spell.getModifiedValue(3, SpellModifiers.RADIUS, Operation.MULTIPLY, world, caster, target);
		double gravity = spell.getModifiedValue(1, SpellModifiers.GRAVITY, Operation.ADD, world, caster, target);
		int duration = (int) spell.getModifiedValue(100, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);

		EntitySpellEffect wall = new EntitySpellEffect(world);
		wall.setRadius(radius);
		wall.setTicksToExist(duration);
		wall.setGravity(gravity);
		wall.SetCasterAndStack(caster, spell);
		wall.setPosition(x, y, z);
		wall.setWall(caster.rotationYaw);
		world.spawnEntityInWorld(wall);
		return SpellCastResult.SUCCESS;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RADIUS, SpellModifiers.GRAVITY, SpellModifiers.DURATION, SpellModifiers.COLOR, SpellModifiers.TARGET_NONSOLID_BLOCKS);
	}


	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public float manaCostMultiplier(){
		return 2.5f;
	}

	@Override
	public boolean isTerminusShape(){
		return false;
	}

	@Override
	public boolean isPrincipumShape(){
		return true;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}

//	@Override
//	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world){
//		switch (affinity){
//		case AIR:
//			return "arsmagica2:spell.cast.air";
//		case ARCANE:
//			return "arsmagica2:spell.cast.arcane";
//		case EARTH:
//			return "arsmagica2:spell.cast.earth";
//		case ENDER:
//			return "arsmagica2:spell.cast.ender";
//		case FIRE:
//			return "arsmagica2:spell.cast.fire";
//		case ICE:
//			return "arsmagica2:spell.cast.ice";
//		case LIFE:
//			return "arsmagica2:spell.cast.life";
//		case LIGHTNING:
//			return "arsmagica2:spell.cast.lightning";
//		case NATURE:
//			return "arsmagica2:spell.cast.nature";
//		case WATER:
//			return "arsmagica2:spell.cast.water";
//		case NONE:
//		default:
//			return "arsmagica2:spell.cast.none";
//		}
//	}
}
