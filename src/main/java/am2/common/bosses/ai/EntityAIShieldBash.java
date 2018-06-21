package am2.common.bosses.ai;

import java.util.List;

import am2.common.bosses.BossActions;
import am2.common.bosses.IArsMagicaBoss;
import am2.common.packet.AMNetHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;

public class EntityAIShieldBash extends EntityAIBase{
	private final EntityLiving host;
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;

	public EntityAIShieldBash(IArsMagicaBoss host, float moveSpeed){
		this.host = ((EntityLiving)host);
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss)host).getCurrentAction() != BossActions.IDLE || !((IArsMagicaBoss)host).isActionValid(BossActions.SHIELD_BASH))
			return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		this.target = AITarget;
		return true;
	}

	@Override
	public boolean continueExecuting(){
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead || (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SHIELD_BASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() > ((IArsMagicaBoss)host).getCurrentAction().getMaxActionTime())){
			((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
			cooldownTicks = 10;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);

		if (host.getDistanceSqToEntity(target) < 16)
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.SHIELD_BASH)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.SHIELD_BASH);

		if (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SHIELD_BASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() > 12){
			if (!host.world.isRemote)
				host.world.playSound(host.posX, host.posY, host.posZ, ((IArsMagicaBoss)host).getAttackSound(), SoundCategory.HOSTILE, 1.0f, 1.0f, false);

			double offsetX = Math.cos(host.rotationYaw) * 2;
			double offsetZ = Math.sin(host.rotationYaw) * 2;
			List<EntityLivingBase> aoeEntities = host.world.getEntitiesWithinAABB(EntityLivingBase.class, host.getEntityBoundingBox().offset(offsetX, 0, offsetZ).expand(2.5, 2, 2.5));
			for (EntityLivingBase ent : aoeEntities){
				if (ent == host) continue;

				double speed = 4;
				double vertSpeed = 0.325;

				double deltaZ = ent.posZ - host.posZ;
				double deltaX = ent.posX - host.posX;
				double angle = Math.atan2(deltaZ, deltaX);

				double radians = angle;

				if (ent instanceof EntityPlayer){
					AMNetHandler.INSTANCE.sendVelocityAddPacket(host.world, ent, speed * Math.cos(radians), vertSpeed, speed * Math.sin(radians));
				}
				ent.motionX = (speed * Math.cos(radians));
				ent.motionZ = (speed * Math.sin(radians));
				ent.motionY = vertSpeed;

				ent.attackEntityFrom(DamageSource.causeMobDamage(host), 2);
			}
		}
	}
}
