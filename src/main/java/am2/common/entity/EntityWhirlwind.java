package am2.common.entity;

import java.util.HashMap;

import am2.api.DamageSources;
import am2.api.math.AMVector3;
import am2.common.navigation.PathNavigator;
import am2.common.packet.AMNetHandler;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityWhirlwind extends EntityFlying{

	private final HashMap<EntityPlayer, Integer> cooldownList;
	private AMVector3 currentTarget;
	private final PathNavigator nav;

	public EntityWhirlwind(World par1World){
		super(par1World);
		cooldownList = new HashMap<EntityPlayer, Integer>();
		nav = new PathNavigator(this);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player){
		if (!world.isRemote){
			Integer cd = cooldownList.get(player);
			if (cd == null || cd <= 0){
				if (!world.isRemote && rand.nextInt(100) < 10){
					int slot = player.inventory.mainInventory.length + rand.nextInt(4);
					if (player.inventory.getStackInSlot(slot) != null){
						ItemStack armorStack = player.inventory.getStackInSlot(slot).copy();
						if (!player.inventory.addItemStackToInventory(armorStack)){
							EntityItem item = new EntityItem(world);
							item.setPosition(player.posX, player.posY, player.posZ);
							item.setVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2 - 0.1);
							world.spawnEntityInWorld(item);
						}
						player.inventory.setInventorySlotContents(slot, null);
					}
				}
				player.attackEntityFrom(DamageSources.causeWindDamage(this), 2);
				float velX = world.rand.nextFloat() * 0.2f;
				float veZ = world.rand.nextFloat() * 0.2f;
				player.addVelocity(velX, 0.8, veZ);
				AMNetHandler.INSTANCE.sendVelocityAddPacket(world, player, velX, 0.8, veZ);
				player.fallDistance = 0;
				setCooldownFor(player);
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		return false;
	}

	@Override
	public void onUpdate(){
		if (currentTarget == null || new AMVector3(this).distanceSqTo(currentTarget) < 2)
			generateNewTarget();

		nav.tryMoveFlying(world, this);

		if (this.ticksExisted > 140 && !this.world.isRemote)
			this.setDead();


		tickCooldowns();

		super.onUpdate();
	}

	private void generateNewTarget(){
		EntityPlayer closest = null;
		for (Object player : this.world.playerEntities){
			if (closest == null || ((EntityPlayer)player).getDistanceSqToEntity(this) < this.getDistanceSqToEntity(closest)){
				closest = (EntityPlayer)player;
			}
		}
		if (closest != null && this.getDistanceSqToEntity(closest) < 64D)
			currentTarget = new AMVector3(closest);
		else
			currentTarget = new AMVector3(this).add(new AMVector3(world.rand.nextInt(10) - 5, 0, world.rand.nextInt(10) - 5));

		nav.SetWaypoint(world, currentTarget.toBlockPos(), this);
	}

	private void setCooldownFor(EntityPlayer player){
		cooldownList.put(player, 20);
	}

	private void tickCooldowns(){
		for (EntityPlayer player : cooldownList.keySet()){
			Integer current = cooldownList.get(player);
			if (current <= 0) continue;
			cooldownList.put(player, --current);
		}
	}
	
	@Override
	public ItemStack getHeldItem(EnumHand hand) {
		return null;
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
	}

	@Override
	public boolean canBePushed(){
		return false;
	}

}
