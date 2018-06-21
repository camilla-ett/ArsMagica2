package am2.common.bosses.ai;

import am2.common.bosses.BossActions;
import am2.common.bosses.EntityEnderGuardian;
import am2.common.bosses.IArsMagicaBoss;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAIShadowstep extends AIAnimation{

	private int cooldownTicks = 0;

	public EntityAIShadowstep(IAnimatedEntity entity){
		super(entity);
	}

	@Override
	public int getAnimID(){
		return BossActions.SPINNING.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 9;
	}

	@Override
	public boolean shouldAnimate(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getAttackTarget() == null) return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask(){
		cooldownTicks = 30;
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			Vec3d facing = guardian.getAttackTarget().getLook(1.0f);
			double x = guardian.getAttackTarget().posX - facing.x * 3;
			double y = guardian.getAttackTarget().posY;
			double z = guardian.getAttackTarget().posZ - facing.z * 3;

			guardian.setPosition(x, y, z);
			guardian.lastTickPosX = x;
			guardian.lastTickPosY = y;
			guardian.lastTickPosZ = z;
			guardian.world.playSound(guardian.posX, guardian.posY, guardian.posZ, ((IArsMagicaBoss)guardian).getAttackSound(), SoundCategory.HOSTILE, 1.0f, guardian.getRNG().nextFloat() * 0.5f + 0.5f, false);
		}
		super.resetTask();
	}

	@Override
	public void updateTask(){
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
		}
	}
}
