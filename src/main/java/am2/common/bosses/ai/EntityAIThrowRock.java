package am2.common.bosses.ai;

import am2.common.bosses.BossActions;
import am2.common.bosses.IArsMagicaBoss;
import am2.common.entity.EntityThrownRock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.SoundCategory;

public class EntityAIThrowRock extends EntityAIBase{
	private final EntityLiving host;
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;

	public EntityAIThrowRock(IArsMagicaBoss host, float moveSpeed){
		this.host = (EntityLiving)host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss)host).getCurrentAction() != BossActions.IDLE) return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		this.target = AITarget;
		return true;
	}

	@Override
	public boolean continueExecuting(){
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead || (((IArsMagicaBoss)host).getCurrentAction() == BossActions.THROWING_ROCK && ((IArsMagicaBoss)host).getTicksInCurrentAction() > ((IArsMagicaBoss)host).getCurrentAction().getMaxActionTime())){
			((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
			cooldownTicks = 50;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		if (host.getDistanceSqToEntity(target) > 100){
			host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		}else{
			host.getNavigator().clearPathEntity();
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.THROWING_ROCK)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.THROWING_ROCK);

			if (((IArsMagicaBoss)host).getTicksInCurrentAction() == 27){

				if (!host.world.isRemote)
					host.world.playSound(host.posX, host.posY, host.posZ, ((IArsMagicaBoss)host).getAttackSound(), SoundCategory.HOSTILE, 1.0f, 1.0f, false);

				host.faceEntity(target, 180, 180);
				if (!host.world.isRemote){
					EntityThrownRock projectile = new EntityThrownRock(host.world, host, 2.0f);
					host.world.spawnEntity(projectile);
				}
			}
		}
	}
}
