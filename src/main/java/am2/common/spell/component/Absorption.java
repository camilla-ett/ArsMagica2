package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.blocks.IMultiblock;
import am2.api.rituals.IRitualInteraction;
import am2.api.rituals.RitualShapeHelper;
import am2.api.spell.Operation;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleOrbitEntity;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Absorption extends SpellComponent implements IRitualInteraction {
	@Override
	public float manaCost() {
		return 100;
	}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target) {
		if (target instanceof EntityLivingBase) {
			int duration = (int) spell.getModifiedValue(PotionEffectsDefs.DEFAULT_BUFF_DURATION, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);

			if (RitualShapeHelper.instance.matchesRitual(this, world, target.getPosition())) {
				duration += (3600 * (spell.getModifierCount(SpellModifiers.BUFF_POWER) + 1));
				RitualShapeHelper.instance.consumeReagents(this, world, target.getPosition());
			}

			if (!world.isRemote)
				((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:absorption"), duration, spell.getModifierCount(SpellModifiers.BUFF_POWER)));

			return true;
		}
		return false;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target,
			Random rand, int colorModifier) {
		for (int i = 0; i < 15; ++i) {
			AMParticle particle = (AMParticle) ArsMagica2.proxy.particleManager.spawn(world, "lens_flare", x, y, z);
			if (particle != null) {
				particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.1f, 1, false)
						.SetTargetDistance(rand.nextDouble() + 0.5));
				particle.setMaxAge(25 + rand.nextInt(10));
				particle.setRGBColorF(244, 200, 60);
				if (colorModifier > -1) {
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f,
							((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.LIFE);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.05F;
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()),
				new ItemStack(Items.GOLDEN_APPLE, 1), new ItemStack(Items.SHIELD) };
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {

	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.DURATION, SpellModifiers.BUFF_POWER);
	}

	@Override
	public IMultiblock getRitualShape() {
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getRitualReagents() {
		return new ItemStack[] { new ItemStack(Items.APPLE), new ItemStack(Items.GOLD_NUGGET) };
	}

	@Override
	public int getRitualReagentSearchRadius() {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getResult() {
		return null;
	}
}
