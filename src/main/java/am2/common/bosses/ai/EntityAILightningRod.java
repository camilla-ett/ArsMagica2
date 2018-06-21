package am2.common.bosses.ai;

import am2.ArsMagica2;
import am2.api.DamageSources;
import am2.api.math.AMVector3;
import am2.common.bosses.BossActions;
import am2.common.bosses.EntityLightningGuardian;
import am2.common.defs.AMSounds;
import am2.common.extensions.EntityExtension;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.SoundCategory;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAILightningRod extends AIAnimation{

	private int cooldownTicks = 0;
	private AMVector3 startPos;
	private boolean hasThrown = false;
	private boolean hasBolted = false;
	EntityLivingBase target;

	public EntityAILightningRod(IAnimatedEntity entity){
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public int getAnimID(){
		return BossActions.LONG_CASTING.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 173;
	}

	@Override
	public boolean shouldAnimate(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getHealth() > living.getMaxHealth() * 0.75f || living.getAttackTarget() == null || !living.getEntitySenses().canSee(living.getAttackTarget()))
			return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask(){
		startPos = null;
		hasThrown = false;
		hasBolted = false;

		if (target != null){
			EntityExtension.For(target).setDisableGravity(false);
		}
		target = null;

		super.resetTask();
	}

	@Override
	public void updateTask(){
		EntityLightningGuardian guardian = getEntity();

		float factor = ((guardian.getHealth() / guardian.getMaxHealth()) + 0.1f);
		cooldownTicks = (int)(500 * factor);

		int ticks = guardian.getTicksInCurrentAction();
		if (ticks <= 25){
			target = guardian.getAttackTarget();
			if (target != null)
				startPos = new AMVector3(target);
		}else{

			if (target == null || target.isDead || startPos == null){
				resetTask();
				return;
			}

			guardian.getLookHelper().setLookPositionWithEntity(target, 30, 30);

			if (ticks > 85 && ticks <= 150){
				if (!guardian.world.isRemote && ticks % 20 == 0)
					guardian.world.playSound(guardian.posX, guardian.posY, guardian.posZ, AMSounds.LIGHTNING_GUARDIAN_LIGHTNING_ROD_1, SoundCategory.HOSTILE, 1.0f, guardian.getRNG().nextFloat() * 0.5f + 0.5f, false);
			}

			if (ticks > 25 && ticks <= 85){
				forcePosition(target, startPos.x, startPos.y + ((ticks - 25) * 0.1), startPos.z);
				EntityExtension.For(target).setDisableGravity(true);
				if (!guardian.world.isRemote && ticks == 30)
					guardian.world.playSound(guardian.posX, guardian.posY, guardian.posZ, AMSounds.LIGHTNING_GUARDIAN_LIGHTNING_ROD_START, SoundCategory.HOSTILE, 1.0f, guardian.getRNG().nextFloat() * 0.5f + 0.5f, false);
			}else if (ticks > 85 && ticks <= 105){
				forcePosition(target, startPos.x, startPos.y + 6, startPos.z);
			}else if (ticks > 105 && ticks <= 150){
				forcePosition(target, startPos.x, startPos.y + 6, startPos.z);
				if (ticks > 115){
					target.attackEntityFrom(DamageSources.causeLightningDamage(guardian), 3);
				}
				ArsMagica2.proxy.particleManager.BoltFromEntityToEntity(guardian.world, guardian, guardian, target, 0);
				if (!guardian.world.isRemote && ticks % 20 == 0)
					guardian.world.playSound(guardian.posX, guardian.posY, guardian.posZ, AMSounds.LIGHTNING_GUARDIAN_IDLE, SoundCategory.HOSTILE, 1.0f, guardian.getRNG().nextFloat() * 0.5f + 0.5f, false);
			}else if (ticks > 150 && ticks <= 158){
				if (!hasThrown){
					target.addVelocity(0, -3, 0);
					target.fallDistance = 5;
					EntityExtension.For(target).setDisableGravity(false);
					hasThrown = true;
				}
			}else if (ticks > 165){
				if (!hasBolted){
					hasBolted = true;
					EntityLightningBolt bolt = new EntityLightningBolt(guardian.world, target.posX, target.posY, target.posZ, false);
					bolt.setPosition(target.posX, target.posY, target.posZ);
					guardian.world.addWeatherEffect(bolt);
				}
			}
		}
	}

	private void forcePosition(EntityLivingBase target, double x, double y, double z){
		target.setPositionAndUpdate(x, y, z);
	}
}
