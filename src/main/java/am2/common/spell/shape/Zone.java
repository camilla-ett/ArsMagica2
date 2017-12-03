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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Zone extends SpellShape{

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){
		if (world.isRemote) return SpellCastResult.SUCCESS;
		int radius = (int)spell.getModifiedValue(2, SpellModifiers.RADIUS, Operation.ADD, world, caster, target); // SpellUtils.getModifiedInt_Add(2, stack, caster, target, world, SpellModifiers.RADIUS);
		double gravity = spell.getModifiedValue(0, SpellModifiers.GRAVITY, Operation.ADD, world, caster, target); // SpellUtils.getModifiedDouble_Add(0, stack, caster, target, world, SpellModifiers.GRAVITY);
		int duration =  (int)spell.getModifiedValue(100, SpellModifiers.DURATION, Operation.ADD, world, caster, target); // SpellUtils.getModifiedInt_Mul(100, stack, caster, target, world, SpellModifiers.DURATION);
		EntitySpellEffect zone = new EntitySpellEffect(world);
		zone.setRadius(radius);
		zone.setTicksToExist(duration);
		zone.setGravity(gravity);
		zone.SetCasterAndStack(caster, spell);
		zone.setPosition(x, y, z);
		world.spawnEntityInWorld(zone);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RADIUS, SpellModifiers.GRAVITY, SpellModifiers.DURATION, SpellModifiers.COLOR, SpellModifiers.TARGET_NONSOLID_BLOCKS);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				BlockDefs.tarmaRoot,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE),
				Items.DIAMOND
		};
	}

	@Override
	public float manaCostMultiplier(){
		return 4.5f;
	}

	@Override
	public boolean isTerminusShape(){
		return false;
	}

	@Override
	public boolean isPrincipumShape(){
		return true;
	}

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

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}
