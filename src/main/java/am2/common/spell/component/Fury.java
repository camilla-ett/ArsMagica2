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
import am2.client.particles.ParticleFloatUpward;
import am2.client.particles.ParticleOrbitEntity;
import am2.common.buffs.BuffEffectFury;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.items.ItemOre;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Fury extends SpellComponent implements IRitualInteraction{

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				Items.FISH,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE)
		};
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			int duration = (int) spell.getModifiedValue(PotionEffectsDefs.DEFAULT_BUFF_DURATION, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);
			//duration = SpellUtils.modifyDurationBasedOnArmor(caster, duration);
			if (RitualShapeHelper.instance.matchesRitual(this, world, target.getPosition())){
				duration += (3600 * (spell.getModifierCount(SpellModifiers.BUFF_POWER) + 1));
				RitualShapeHelper.instance.consumeReagents(this, world, target.getPosition());
			}

			if (!world.isRemote){
				((EntityLivingBase)target).addPotionEffect(new BuffEffectFury(duration, 0));
			}
			return true;
		}
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RADIUS, SpellModifiers.BUFF_POWER);
	}

	
	@Override
	public float manaCost(){
		return 261;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5 * ArsMagica2.config.getGFXLevel(); ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "pulse", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.setRGBColorF(1, 0, 0);
				particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.15f, 1, false).SetTargetDistance(world.rand.nextDouble() + 1f).setIgnoreYCoordinate(true));
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
				particle.setMaxAge(10);
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.FIRE, Affinity.LIGHTNING);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public IMultiblock getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getRitualReagents(){
		return new ItemStack[]{
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SWIFTNESS),
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRENGTH)
		};
	}

	@Override
	public int getRitualReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getResult() {
		return null;
	}

}
