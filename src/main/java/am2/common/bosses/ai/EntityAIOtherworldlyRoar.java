package am2.common.bosses.ai;

import am2.api.extensions.ISpellCaster;
import am2.common.bosses.BossActions;
import am2.common.bosses.EntityEnderGuardian;
import am2.common.spell.SpellCaster;
import am2.common.utils.NPCSpells;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAIOtherworldlyRoar extends AIAnimation {

	private int cooldownTicks = 0;

	public EntityAIOtherworldlyRoar(IAnimatedEntity entity) {
		super(entity);
	}

	@Override
	public int getAnimID() {
		return BossActions.LONG_CASTING.ordinal();
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	public int getDuration() {
		return 63;
	}

	@Override
	public boolean shouldAnimate() {
		// accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		// must have an attack target
		if (living.getAttackTarget() == null)
			return false;

		if (living.world.getEntitiesWithinAABB(EntityLivingBase.class, living.getEntityBoundingBox().expand(9, 3, 9))
				.size() < 2) {
			return false;
		}

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask() {
		cooldownTicks = 100;
		super.resetTask();
	}

	@Override
	public void updateTask() {
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null) {
			if (guardian.getTicksInCurrentAction() == 33) {
				guardian.faceEntity(guardian.getAttackTarget(), 180, 180);
				ISpellCaster spell = NPCSpells.instance.enderGuardian_otherworldlyRoar.getCapability(SpellCaster.INSTANCE, null);
				if (spell != null) {
					spell.cast(NPCSpells.instance.enderGuardian_otherworldlyRoar, guardian.world, guardian);
				}
			} else {
				guardian.faceEntity(guardian.getAttackTarget(), 180, 180);
			}
		}
	}

}
