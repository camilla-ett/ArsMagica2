package am2.common.affinity.abilities;

import am2.api.affinity.AbstractToggledAffinityAbility;
import am2.api.affinity.Affinity;
import am2.common.extensions.AffinityData;
import am2.common.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AbilityLavaFreeze extends AbstractToggledAffinityAbility {

	public AbilityLavaFreeze() {
		super(new ResourceLocation("arsmagica2", "lavafreeze"));
	}

	@Override
	public float getMinimumDepth() {
		return 1F;
	}

	@Override
	public Affinity getAffinity() {
		return Affinity.ICE;
	}

	@Override
	public void applyTick(EntityPlayer player) {
		if (player.world.isRemote) return;
		BlockPos startPos = new BlockPos(player.posX, Math.floor(player.posY), player.posZ);
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockPos newPos = startPos.add(x, -1, z);
				if (player.world.getBlockState(newPos).getBlock() == Blocks.LAVA || player.world.getBlockState(newPos).getBlock() == Blocks.FLOWING_LAVA)
					WorldUtils.freeze(newPos, player.world);
			}
		}
	}

	@Override
	protected boolean isEnabled(EntityPlayer player) {
		return AffinityData.For(player).getAbilityBoolean(AffinityData.ICE_BRIDGE_STATE);
	}

}
