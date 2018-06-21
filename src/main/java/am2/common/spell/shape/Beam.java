package am2.common.spell.shape;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.Operation;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.client.particles.AMBeam;
import am2.client.particles.AMParticle;
import am2.client.particles.AMParticleDefs;
import am2.client.particles.ParticleMoveOnHeading;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.defs.SoundDefs;
import am2.common.items.ItemOre;
import am2.common.power.PowerTypes;
import am2.common.spell.SpellCastResult;
import am2.common.utils.MathUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.HashMap;

public class Beam extends SpellShape {

	private final HashMap<Integer, AMBeam> beams;

	public Beam() {
		beams = new HashMap<>();
	}

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		boolean shouldApplyEffectBlock = useCount % 5 == 0;
		boolean shouldApplyEffectEntity = useCount % 10 == 0;

		double range = spell.getModifiedValue(SpellModifiers.RANGE, Operation.ADD, world, caster, target);//.getModifiedDouble_Add(stack, caster, target, world, SpellModifiers.RANGE);
		boolean targetWater = spell.isModifierPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS);
		RayTraceResult mop = spell.raytrace(caster, world, range, true, targetWater);

		SpellCastResult result = null;
		Vec3d beamHitVec = null;
		Vec3d spellVec = null;

		if (mop == null) {
			beamHitVec = MathUtilities.extrapolateEntityLook(world, caster, range);
			spellVec = beamHitVec;
		} else if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
			if (shouldApplyEffectEntity && !world.isRemote) {
				Entity e = mop.entityHit;
				if (e instanceof EntityDragonPart && ((EntityDragonPart) e).entityDragonObj instanceof EntityLivingBase)
					e = (EntityLivingBase) ((EntityDragonPart) e).entityDragonObj;
				result = spell.applyComponentsToEntity(world, caster, e);
				if (result != SpellCastResult.SUCCESS) {
					return result;
				}
			}
			float rng = (float) mop.hitVec.distanceTo(new Vec3d(caster.posX, caster.posY, caster.posZ));
			beamHitVec = MathUtilities.extrapolateEntityLook(world, caster, rng);
			spellVec = beamHitVec;
		} else {
			if (shouldApplyEffectBlock && !world.isRemote) {
				result = spell.applyComponentsToGround(world, caster, mop.getBlockPos(), mop.sideHit, mop.hitVec.x, mop.hitVec.y, mop.hitVec.z);
				if (result != SpellCastResult.SUCCESS) {
					return result;
				}
			}
			beamHitVec = mop.hitVec;
			spellVec = new Vec3d(mop.getBlockPos());
		}

		if (world.isRemote && beamHitVec != null) {
			AMBeam beam = beams.get(caster.getEntityId());
			double startX = caster.posX;
			double startY = caster.posY + caster.getEyeHeight() - 0.2f;
			double startZ = caster.posZ;
			Affinity affinity = spell.getMainShift();

			int color = spell.getColor(world, caster, target);

			if (beam != null) {
				if (!beam.isAlive() || caster.getDistanceSq(beam.getPosX(), beam.getPosY(), beam.getPosZ()) > 4) {
					beams.remove(caster.getEntityId());
				} else {
					beam.setBeamLocationAndTarget(startX, startY, startZ, beamHitVec.x, beamHitVec.y, beamHitVec.z);
				}
			} else {
				if (affinity.equals(Affinity.LIGHTNING)) {
					ArsMagica2.proxy.particleManager.BoltFromEntityToPoint(world, caster, beamHitVec.x, beamHitVec.y, beamHitVec.z, 1, color == -1 ? affinity.getColor() : color);
				} else {
					beam = (AMBeam) ArsMagica2.proxy.particleManager.BeamFromEntityToPoint(world, caster, beamHitVec.x, beamHitVec.y, beamHitVec.z, color == -1 ? affinity.getColor() : color);
					if (beam != null) {
						if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
							beam.setFirstPersonPlayerCast();
						beams.put(caster.getEntityId(), beam);
					}
				}
			}
			for (int i = 0; i < ArsMagica2.config.getGFXLevel() + 1; ++i) {
				AMParticle particle = (AMParticle) ArsMagica2.proxy.particleManager.spawn(world, AMParticleDefs.getParticleForAffinity(affinity), beamHitVec.x, beamHitVec.y, beamHitVec.z);
				if (particle != null) {
					particle.setMaxAge(2);
					particle.setParticleScale(0.1f);
					particle.setIgnoreMaxAge(false);
					if (color != -1)
						particle.setRGBColorI(color);
					particle.AddParticleController(new ParticleMoveOnHeading(particle, world.rand.nextDouble() * 360, world.rand.nextDouble() * 360, world.rand.nextDouble() * 0.2 + 0.02f, 1, false));
				}
			}
		}

		if (result != null && spellVec != null && (mop.typeOfHit == RayTraceResult.Type.ENTITY ? shouldApplyEffectEntity : shouldApplyEffectBlock)) {
			return spell.execute(world, caster, target, spellVec.x, spellVec.y, spellVec.z, mop != null ? mop.sideHit : null);
		} else {
			return SpellCastResult.SUCCESS;
		}
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RANGE, SpellModifiers.TARGET_NONSOLID_BLOCKS);
	}

	@Override
	public boolean isChanneled() {
		return true;
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				ItemDefs.standardFocus,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM),
				BlockDefs.aum,
				"E:" + PowerTypes.NEUTRAL.ID(), 500
		};
	}

	@Override
	public float manaCostMultiplier() {
		//FIXME
//		int stages = SpellUtils.numStages(spellStack);
//		for (int i = SpellUtils.currentStage(spellStack); i < stages; ++i){
//			SpellShape shape = SpellUtils.getShapeForStage(spellStack, i);
//			if (!shape.equals(this)) continue;
//			
//			// return 1 when multiple beams
//			if (shape.getClass() == Beam.class) {
//				return 1.0f;
//			}
//		}
		return 0.2f;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return false;
	}

	@Override
	public SoundEvent getSoundForAffinity(Affinity affinity, SpellData stack, World world) {
		return SoundDefs.LOOP_MAP.get(affinity);
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}
}
