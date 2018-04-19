package am2.common.items;

import am2.api.IBoundItem;
import am2.api.extensions.ISpellCaster;
import am2.common.defs.ItemDefs;
import am2.common.registry.Registry;
import am2.common.spell.SpellCaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBoundShovel extends ItemSpade implements IBoundItem {

	public ItemBoundShovel() {
		super(ItemDefs.BOUND);
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.setCreativeTab(null);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (!stack.hasTagCompound())
			return true;
		ItemStack copiedStack = stack.copy();
		ISpellCaster caster = stack.copy().getCapability(SpellCaster.INSTANCE, null);
		if (caster != null)
			caster.createSpellData(copiedStack).execute(worldIn, entityLiving, null, pos.getX(), pos.getY(), pos.getZ(), null);
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		item = new ItemStack(ItemDefs.spell);
		return false;
	}

	@Override
	public float maintainCost(EntityPlayer player, ItemStack stack) {
		return normalMaintain;
	}

	public ItemBoundShovel registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		this.setRegistryName(new ResourceLocation("arsmagica2", name));
		Registry.GetItemsToRegister().add(this);
		return this;
	}

}
