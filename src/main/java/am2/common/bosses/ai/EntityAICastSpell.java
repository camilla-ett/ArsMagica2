package am2.common.bosses.ai;

import am2.api.extensions.ISpellCaster;
import am2.common.bosses.BossActions;
import am2.common.bosses.IArsMagicaBoss;
import am2.common.spell.SpellCaster;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class EntityAICastSpell<T extends EntityLiving & IArsMagicaBoss> extends EntityAIBase{

	private final T host;
	private int cooldownTicks = 0;
	private boolean hasCasted = false;
	private int castTicks = 0;

	private ItemStack stack;
	private int castPoint;
	private int duration;
	private int cooldown;
	private BossActions activeAction;
	private ISpellCastCallback<T> callback;

	public EntityAICastSpell(T host, ItemStack spell, int castPoint, int duration, int cooldown, BossActions activeAction){
		this.host = host;
		this.stack = spell;
		this.castPoint = castPoint;
		this.duration = duration;
		this.cooldown = cooldown;
		this.activeAction = activeAction;
		this.callback = null;
		this.setMutexBits(3);
	}

	public EntityAICastSpell(T host, ItemStack spell, int castPoint, int duration, int cooldown, BossActions activeAction, ISpellCastCallback<T> callback){
		this.host = host;
		this.stack = spell;
		this.castPoint = castPoint;
		this.duration = duration;
		this.cooldown = cooldown;
		this.activeAction = activeAction;
		this.callback = callback;
	}

	@Override
	public boolean shouldExecute(){
		boolean execute = this.host.getCurrentAction() == BossActions.IDLE && this.host.getAttackTarget() != null && --this.cooldownTicks <= 0;
		if (execute){
			if (this.callback == null || this.callback.shouldCast(this.host, this.stack))
				this.hasCasted = false;
			else
				execute = false;
		}
		return execute;
	}

	@Override
	public boolean continueExecuting(){
		return !this.hasCasted && this.host.getAttackTarget() != null && !this.host.getAttackTarget().isDead;
	}

	@Override
	public void resetTask(){
		this.host.setCurrentAction(BossActions.IDLE);
		this.cooldownTicks = this.cooldown;
		this.hasCasted = true;
		this.castTicks = 0;
	}

	@Override
	public void updateTask(){
		if (this.host.getAttackTarget() == null) {
			resetTask();
			return;
		}

		//this.host.getLookHelper().setLookPositionWithEntity(this.host.getAttackTarget(), 30, 30);
		if (this.host.getDistanceSqToEntity(this.host.getAttackTarget()) > 64){

			double deltaZ = this.host.getAttackTarget().posZ - this.host.posZ;
			double deltaX = this.host.getAttackTarget().posX - this.host.posX;

			double angle = -Math.atan2(deltaZ, deltaX);

			double newX = this.host.getAttackTarget().posX + (Math.cos(angle) * 6);
			double newZ = this.host.getAttackTarget().posZ + (Math.sin(angle) * 6);

			this.host.getNavigator().tryMoveToXYZ(newX, this.host.getAttackTarget().posY, newZ, 0.5f);
			//this.host.getNavigator().tryMoveToEntityLiving(this.host.getAttackTarget(), 0.5f);
		}else if (!this.host.canEntityBeSeen(this.host.getAttackTarget())){
			this.host.getNavigator().tryMoveToEntityLiving(this.host.getAttackTarget(), 0.5f);
		}else{
			if (this.host.getCurrentAction() != this.activeAction)
				this.host.setCurrentAction(this.activeAction);

			if (++this.castTicks == this.castPoint){
				if (!this.host.world.isRemote)
					this.host.world.playSound(this.host.posX, this.host.posY, this.host.posZ, this.host.getAttackSound(), SoundCategory.HOSTILE, 1.0f, 1.0f, false);
				this.host.faceEntity(this.host.getAttackTarget(), 180, 180);
				ISpellCaster spell = this.stack.getCapability(SpellCaster.INSTANCE, null);
				if (spell != null) {
					spell.cast(this.stack, this.host.world, this.host);
				}
			}
		}
		if (this.castTicks >= this.duration){
			this.resetTask();
		}
	}
}
