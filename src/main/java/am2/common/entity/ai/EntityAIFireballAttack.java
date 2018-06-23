package am2.common.entity.ai;

import am2.api.extensions.ISpellCaster;
import am2.common.spell.SpellCaster;
import am2.common.utils.NPCSpells;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityAIFireballAttack extends EntityAIBase{
	World world;

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
	 * The ID of this ranged attack AI. This chooses which entity is to be used as a ranged attack.
	 */
	int rangedAttackID;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	int maxRangedAttackTime;

	public EntityAIFireballAttack(EntityCreature par1EntityLiving, float par2, int par3, int par4){
		rangedAttackTime = 0;
		field_48367_f = 0;
		entityHost = par1EntityLiving;
		world = par1EntityLiving.world;
		field_48370_e = par2;
		rangedAttackID = par3;
		maxRangedAttackTime = par4;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute(){
		EntityLivingBase entityliving = entityHost.getAttackTarget();

		if (entityliving == null){
			return false;
		}else{
			attackTarget = entityliving;
			if (this.entityHost.getDistanceSqToEntity(attackTarget) < 4) return false;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting(){
		return shouldExecute() || !entityHost.getNavigator().noPath();
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
			if (!entityHost.getNavigator().tryMoveToEntityLiving(attackTarget, field_48370_e)){
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
		ISpellCaster caster = NPCSpells.instance.fireBolt.getCapability(SpellCaster.INSTANCE, null);
		if (caster != null) {
			caster.cast(NPCSpells.instance.fireBolt, world, entityHost);
		}
	}
}
