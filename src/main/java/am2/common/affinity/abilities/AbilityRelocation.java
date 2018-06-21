package am2.common.affinity.abilities;

import am2.ArsMagica2;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.common.defs.BindingsDefs;
import am2.common.extensions.AffinityData;
import am2.common.extensions.EntityExtension;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AbilityRelocation extends AbstractAffinityAbility {

	public AbilityRelocation() {
		super(new ResourceLocation("arsmagica2", "relocation"));
	}

	@Override
	public float getMinimumDepth() {
		return 0.75f;
	}

	@Override
	public Affinity getAffinity() {
		return Affinity.ENDER;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public KeyBinding getKey() {
		return BindingsDefs.ENDER_TP;
	}
	
	@Override
	public boolean canApply(EntityPlayer player) {
		return super.canApply(player);
	}
	
	@Override
	public void applyKeyPress(EntityPlayer player) {
		if (AffinityData.For(player).getCooldown("EnderTP") > 0) {
			if (!player.world.isRemote)
				player.addChatMessage(new TextComponentString(I18n.format("am2.chat.relocation_cooldown")));
			return;
		}
	
		Vec3d playerPos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		RayTraceResult result = player.world.rayTraceBlocks(playerPos, playerPos.add(new Vec3d(player.getLookVec().xCoord * 32, player.getLookVec().yCoord * 32, player.getLookVec().zCoord * 32)));
		if (result == null)
			result = new RayTraceResult(playerPos.add(new Vec3d(player.getLookVec().xCoord * 32, player.getLookVec().yCoord * 32, player.getLookVec().zCoord * 32)), null);
		EnderTeleportEvent event = new EnderTeleportEvent(player, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, 0.0f);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			if (!player.world.isRemote)
				player.addChatMessage(new TextComponentString(I18n.format("am2.chat.relocation_failed")));
			return;
		}
		double posY = event.getTargetY();
		while (!player.world.isAirBlock(new BlockPos(event.getTargetX(), posY, event.getTargetZ())) || !player.world.isAirBlock(new BlockPos(event.getTargetX(), posY + 1, event.getTargetZ())))
			posY++;
		if (player.getDistanceSq(event.getTargetX(), posY, event.getTargetZ()) > 1024) {
			if (!player.world.isRemote)
				player.addChatMessage(new TextComponentString(I18n.format("am2.chat.relocation_out_of_range")));
			return;
		}
		player.setPositionAndUpdate(event.getTargetX(), posY, event.getTargetZ());

		player.fallDistance = 0;
		EntityExtension.For(player).setFallProtection(20000);
		AffinityData.For(player).addCooldown("EnderTP", ArsMagica2.config.getEnderAffinityAbilityCooldown());
	}

}
