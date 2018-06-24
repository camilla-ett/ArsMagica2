package am2.common.bosses.ai;

import java.util.List;

import am2.api.DamageSources;
import am2.common.bosses.BossActions;
import am2.common.bosses.IArsMagicaBoss;
import am2.common.entity.EntityShockwave;
import am2.common.packet.AMNetHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class EntityAISmash extends EntityAIBase{

	EntityLiving host;
	private EntityLivingBase target;
	private final float moveSpeed;
	private int cooldownTicks = 0;
	private final DamageSources.DamageSourceTypes damageType;

	public EntityAISmash(IArsMagicaBoss host, float moveSpeed, DamageSources.DamageSourceTypes damageType){
		this.host = (EntityLiving)host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
		this.damageType = damageType;
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss)host).getCurrentAction() != BossActions.IDLE || !((IArsMagicaBoss)host).isActionValid(BossActions.SMASH))
			return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		if (AITarget != null && host.getDistanceSqToEntity(AITarget) > 4D){
			if (!host.getNavigator().tryMoveToEntityLiving(AITarget, moveSpeed))
				return false;
		}
		this.target = AITarget;
		return true;
	}

	@Override
	public boolean continueExecuting(){
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget != null && host.getDistanceSqToEntity(AITarget) > 4D){
			if (host.onGround)
				return host.getNavigator().tryMoveToEntityLiving(AITarget, moveSpeed);
		}
		if (AITarget == null || AITarget.isDead || (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SMASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() > ((IArsMagicaBoss)host).getCurrentAction().getMaxActionTime())){
			((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
			cooldownTicks = 100;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30, 30);
		host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		if (host.getDistanceSqToEntity(target) < 16)
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.SMASH)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.SMASH);

		if (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SMASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() == 18){

			if (!host.world.isRemote)
				host.world.playSound(host.posX, host.posY, host.posZ, ((IArsMagicaBoss)host).getAttackSound(), SoundCategory.HOSTILE, 1.0f, 1.0f, false);

			List<EntityLivingBase> aoeEntities = host.world.getEntitiesWithinAABB(EntityLivingBase.class, host.getEntityBoundingBox().expand(4, 2, 4));
			for (EntityLivingBase ent : aoeEntities){
				if (ent == host) continue;
				ent.attackEntityFrom(DamageSources.causeDamage(damageType, host, true), 8);
				if (ent instanceof EntityPlayer){
					AMNetHandler.INSTANCE.sendVelocityAddPacket(host.world, ent, 0, 1.3f, 0);
				}else{
					ent.addVelocity(0, 1.4f, 0);
				}
			}
			if (!host.world.isRemote){
				for (int i = 0; i < 4; ++i){
					EntityShockwave shockwave = new EntityShockwave(host.world);
					shockwave.setPosition(host.posX, host.posY, host.posZ);
					shockwave.setMoveSpeedAndAngle(0.5f, MathHelper.wrapDegrees(host.rotationYaw + (90 * i)));
					host.world.spawnEntity(shockwave);
				}
			}
		}
	}
}
