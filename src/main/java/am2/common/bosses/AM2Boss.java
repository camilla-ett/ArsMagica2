package am2.common.bosses;

import am2.ArsMagica2;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.entity.EntityLightMage;
import am2.common.extensions.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class AM2Boss extends EntityMob implements IEntityMultiPart, IArsMagicaBoss {

	protected BossActions currentAction = BossActions.IDLE;
	protected int ticksInCurrentAction;
	protected EntityDragonPart[] parts;

	public boolean playerCanSee = false;
	private BossInfoServer bossInfo = null;

	public AM2Boss(World par1World) {
		super(par1World);
		if (par1World != null)
			this.bossInfo = new BossInfoServer(this.getDisplayName(), this.getBarColor(), BossInfo.Overlay.PROGRESS);
		this.stepHeight = 1.02f;
		EntityExtension.For(this).setMagicLevelWithMana(50);
		this.initAI();
	}

	//Bosses should be able to follow players through doors and hallways, so setSize is overridden to instead add a
	//damageable entity based bounding box of the specified size, unless a boss already uses parts.
	@Override
	public void setSize(float width, float height) {
		if (this.parts == null) {
			this.parts = new EntityDragonPart[]{new EntityDragonPart(this, "defaultBody", width, height) {

				@Override
				public void onUpdate() {
					super.onUpdate();
					this.isDead = ((Entity) this.entityDragonObj).isDead;
				}

				@Override
				public boolean shouldRenderInPass(int pass) {
					return false;
				}
			}};
		} else {
			super.setSize(width, height);
		}
	}

	@Override
	public boolean isAIDisabled() {
		return false;
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48);
	}

	/**
	 * This contains the default AI tasks.  To add new ones, override {@link #initSpecificAI()}
	 */
	protected void initAI() {
		//TODO this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityLightMage.class, 32.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityLightMage.class, true));

		this.initSpecificAI();
	}

	/**
	 * Initializer for class-specific AI
	 */
	protected abstract void initSpecificAI();

	protected abstract BossInfo.Color getBarColor();

	@Override
	public BossActions getCurrentAction() {
		return this.currentAction;
	}

	@Override
	public void setCurrentAction(BossActions action) {
		this.currentAction = action;
		this.ticksInCurrentAction = 0;
	}

	@Override
	public int getTicksInCurrentAction() {
		return this.ticksInCurrentAction;
	}

	@Override
	public boolean isActionValid(BossActions action) {
		return true;
	}

	@Override
	public abstract SoundEvent getAttackSound();

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public Entity[] getParts() {
		return this.parts;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {

		if (par1DamageSource == DamageSource.IN_WALL) {
			if (!this.world.isRemote) {// dead code? (calling canSnowAt() without using the result) could it be a buggy upgrade to 1.7.10?
				for (int i = -1; i <= 1; ++i) {
					for (int j = 0; j < 3; ++j) {
						for (int k = -1; k <= 1; ++k) {
							this.world.destroyBlock(this.getPosition().add(i, j, k), true);
						}
					}
				}
			}
			return false;
		}

		if (par1DamageSource.getTrueSource() != null) {

			if (par1DamageSource.getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) par1DamageSource.getTrueSource();
				if (player.capabilities.isCreativeMode && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ItemDefs.woodenLeg) {
					if (!this.world.isRemote)
						this.setDead();
					return false;
				}
			} else if (par1DamageSource.getTrueSource() instanceof EntityArrow) {
				Entity shooter = ((EntityArrow) par1DamageSource.getTrueSource()).shootingEntity;
				if (shooter != null && this.getDistanceSqToEntity(shooter) > 900) {
					this.setPositionAndUpdate(shooter.posX, shooter.posY, shooter.posZ);
				}
				return false;
			} else if (this.getDistanceSqToEntity(par1DamageSource.getTrueSource()) > 900) {
				Entity shooter = (par1DamageSource.getTrueSource());
				if (shooter != null) {
					this.setPositionAndUpdate(shooter.posX, shooter.posY, shooter.posZ);
				}
			}
		}

		if (par2 > 7 && !par1DamageSource.damageType.equals(DamageSource.OUT_OF_WORLD.damageType)) par2 = 7;

		par2 = this.modifyDamageAmount(par1DamageSource, par2);

		if (par2 <= 0) {
			this.heal(-par2);
			return false;
		}

		if (super.attackEntityFrom(par1DamageSource, par2)) {
			this.hurtResistantTime = 40;
			return true;
		}
		return false;
	}

	protected abstract float modifyDamageAmount(DamageSource source, float damageAmt);

	@Override
	public boolean attackEntityFromPart(EntityDragonPart part, DamageSource source, float damage) {
		return this.attackEntityFrom(source, damage);
	}

	@Override
	public void onUpdate() {

		if (this.parts != null && this.parts[0] != null && this.parts[0].partName == "defaultBody") {
			this.parts[0].setPosition(this.posX, this.posY, this.posZ);
			if (this.world.isRemote) {
				this.parts[0].setVelocity(this.motionX, this.motionY, this.motionZ);
			}
			if (!this.parts[0].addedToChunk) {
				this.world.spawnEntity(this.parts[0]);
			}
		}

		this.ticksInCurrentAction++;
		if (this.ticksInCurrentAction > this.getCurrentAction().getMaxActionTime()) {
			this.setCurrentAction(BossActions.IDLE);
		}

		if (this.world.isRemote) {
			this.playerCanSee = ArsMagica2.proxy.getLocalPlayer().canEntityBeSeen(this);
			this.ignoreFrustumCheck = ArsMagica2.proxy.getLocalPlayer().getDistanceToEntity(this) < 32;
		}

		if (this.bossInfo != null)
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		super.onUpdate();
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		if (effect.getPotion() == PotionEffectsDefs.SILENCE)
			return;
		super.addPotionEffect(effect);
	}

	@Override
	public World getWorld() {
		return this.getEntityWorld();
	}

	/**
	 * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
	 * order to view its associated boss bar.
	 */
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		if (this.bossInfo != null)
			this.bossInfo.addPlayer(player);
	}

	/**
	 * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
	 * more information on tracking.
	 */
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if (this.bossInfo != null)
			this.bossInfo.removePlayer(player);
	}
}
