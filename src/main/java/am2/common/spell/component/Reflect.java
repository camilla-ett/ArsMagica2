package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.Operation;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleHoldPosition;
import am2.common.buffs.BuffEffectSpellReflect;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Reflect extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){

			if (((EntityLivingBase)target).isPotionActive(PotionEffectsDefs.MAGIC_SHIELD)){
				return true;
			}

			int duration = (int) spell.getModifiedValue(PotionEffectsDefs.DEFAULT_BUFF_DURATION, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);
			//duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);
			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectSpellReflect(duration, spell.getModifierCount(SpellModifiers.BUFF_POWER)));
			return true;
		}
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.BUFF_POWER, SpellModifiers.DURATION);
	}

	@Override
	public float manaCost(){
		return 1440;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "lens_flare", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
				particle.setMaxAge(20);
				particle.setParticleScale(0.2f);
				particle.setRGBColorF(0.5f + rand.nextFloat() * 0.5f, 0.1f, 0.5f + rand.nextFloat() * 0.5f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.ARCANE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()),
				Blocks.GLASS,
				Blocks.IRON_BLOCK,
				//TODO BlocksCommonProxy.witchwoodLog
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}
