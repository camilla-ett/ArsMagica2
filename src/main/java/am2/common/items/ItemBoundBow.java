package am2.common.items;

import javax.annotation.Nullable;

import am2.api.IBoundItem;
import am2.common.defs.ItemDefs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBoundBow extends ItemBow implements IBoundItem {

	public ItemBoundBow() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.setCreativeTab(null);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() == ItemDefs.BoundBow ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (!stack.hasTagCompound())
			return;
		ItemStack copiedStack = stack.copy();
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, true);
			if (i < 0)
				return;

			if (true) {

				float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					if (!worldIn.isRemote) {
						ItemArrow itemarrow = ItemDefs.BoundArrow;
						EntityArrow entityarrow = itemarrow.createArrow(worldIn, copiedStack, entityplayer);
						entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						if (f == 1.0F) {
							entityarrow.setIsCritical(true);
						}

						stack.damageItem(1, entityplayer);
						entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

						worldIn.spawnEntity(entityarrow);
					}

					worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}


	@SuppressWarnings("deprecation")
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		item.setItem(ItemDefs.spell);
		return false;
	}

	@Override
	public float maintainCost(EntityPlayer player, ItemStack stack) {
		return normalMaintain;
	}

	public ItemBoundBow registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		return this;
	}

}
