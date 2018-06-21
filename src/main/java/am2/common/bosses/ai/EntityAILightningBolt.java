package am2.common.bosses.ai;

import am2.ArsMagica2;
import am2.api.DamageSources;
import am2.common.bosses.BossActions;
import am2.common.bosses.EntityLightningGuardian;
import am2.common.defs.AMSounds;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAILightningBolt extends AIAnimation{

	private int cooldownTicks = 0;

	public EntityAILightningBolt(IAnimatedEntity entity){
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldAnimate(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getAttackTarget() == null || !living.getEntitySenses().canSee(living.getAttackTarget()))
			return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public int getAnimID(){
		return BossActions.STRIKE.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 15;
	}

	@Override
	public void resetTask(){
		cooldownTicks = 3;
		super.resetTask();
	}

	@Override
	public void updateTask(){
		EntityLightningGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
			if (guardian.getTicksInCurrentAction() == 7){
				doStrike();
				if (!guardian.world.isRemote)
					guardian.world.playSound(guardian.posX, guardian.posY, guardian.posZ, AMSounds.LIGHTNING_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1.0f, (float)(0.5 + guardian.getRNG().nextDouble() * 0.5f), false);
			}
		}
	}

	private void doStrike(){
		EntityLightningGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null && guardian.getEntitySenses().canSee(guardian.getAttackTarget())){
			if (guardian.getDistanceSqToEntity(guardian.getAttackTarget()) > 400){
				guardian.getNavigator().tryMoveToEntityLiving(guardian.getAttackTarget(), 0.5f);
				return;
			}
			guardian.getNavigator().clearPathEntity();
			if (guardian.getRNG().nextDouble() > 0.2f){
				ArsMagica2.proxy.particleManager.BoltFromEntityToEntity(guardian.world, guardian, guardian, guardian.getAttackTarget(), 0);
				guardian.getAttackTarget().attackEntityFrom(DamageSources.causeLightningDamage(guardian), 3);
				if (guardian.getAttackTarget() instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)guardian.getAttackTarget();
					if (player.capabilities.isFlying)
						player.capabilities.isFlying = false;
					if (player.isRiding())
						player.dismountEntity(player.getRidingEntity());
				}
			}else{
				ArsMagica2.proxy.particleManager.BoltFromEntityToPoint(guardian.world, guardian, guardian.getAttackTarget().posX - 0.5 + guardian.getRNG().nextDouble(), guardian.getAttackTarget().posY - 0.5 + guardian.getRNG().nextDouble() + guardian.getAttackTarget().getEyeHeight(), guardian.getAttackTarget().posZ - 0.5 + guardian.getRNG().nextDouble());
			}
		}
	}

}
