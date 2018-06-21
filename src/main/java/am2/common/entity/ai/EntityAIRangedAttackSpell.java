package am2.common.entity.ai;

import am2.api.extensions.ISpellCaster;
import am2.common.spell.SpellCaster;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIRangedAttackSpell extends EntityAIBase{
	World worldObj;

	/**
	 * The entity the AI instance has been applied to
	 */
	EntityCreature entityHost;
	EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	int rangedAttackTime;
	float field_48370_e;
	int field_48367_f;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	int maxRangedAttackTime;

	ItemStack spellStack;

	public EntityAIRangedAttackSpell(EntityCreature host, float moveSpeed, int cooldown, ItemStack spellStack){
		rangedAttackTime = 0;
		field_48367_f = 0;
		entityHost = host;
		worldObj = host.world;
		field_48370_e = moveSpeed;
		maxRangedAttackTime = cooldown;
		this.spellStack = spellStack;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute(){
		if (entityHost == null)
			return false;
		EntityLivingBase entityliving = entityHost.getAttackTarget();

		if (entityliving == null){
			return false;
		}else{
			attackTarget = entityliving;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting(){
		return shouldExecute() && !entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask(){
		attackTarget = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask(){
		double d = 225D;
		double d1 = entityHost.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY, attackTarget.posZ);
		boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

		if (flag){
			field_48367_f++;
		}else{
			field_48367_f = 0;
		}

		if (d1 > d || field_48367_f > 20){
			double deltaZ = attackTarget.posZ - entityHost.posZ;
			double deltaX = attackTarget.posX - entityHost.posX;

			double angle = -Math.atan2(deltaZ, deltaX);

			double newX = attackTarget.posX + (Math.cos(angle) * 6);
			double newZ = attackTarget.posZ + (Math.sin(angle) * 6);

			if (!entityHost.getNavigator().tryMoveToXYZ(newX, attackTarget.posY, newZ, 0.5f)){
				entityHost.getNavigator().clearPathEntity();
				entityHost.setAttackTarget(null);
			}
		}else{
			entityHost.getNavigator().clearPathEntity();
		}

		entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);

		rangedAttackTime = Math.max(rangedAttackTime - 1, 0);

		if (rangedAttackTime > 0){
			return;
		}

		if (d1 > d || !flag){
			return;
		}else{
			doRangedAttack();
			rangedAttackTime = maxRangedAttackTime;
			return;
		}
	}

	/**
	 * Performs a ranged attack according to the AI's rangedAttackID.
	 */
	private void doRangedAttack(){
		if (entityHost.getAttackTarget() == null)
			return;
		entityHost.faceEntity(entityHost.getAttackTarget(), 180, 180);
		ISpellCaster caster = spellStack.getCapability(SpellCaster.INSTANCE, null);
		if (caster != null) {
			caster.cast(spellStack, worldObj, entityHost);
		}
		entityHost.swingArm(EnumHand.MAIN_HAND);
	}
}
