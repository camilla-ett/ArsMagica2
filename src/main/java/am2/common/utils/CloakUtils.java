package am2.common.utils;

import java.io.File;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.client.models.ModelCloaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

public class CloakUtils{
	private static TreeMap<String, ThreadDownloadImageData> downloadedCloaks = new TreeMap<String, ThreadDownloadImageData>();
	private static TreeMap<String, ResourceLocation> downloadedCloakLocations = new TreeMap<String, ResourceLocation>();

	public static ModelCloaks cloak = new ModelCloaks();

	public static void renderCloakModel(EntityPlayer player, ModelBiped mainModel, float partialRenderTick){
		if (!ArsMagica2.proxy.playerTracker.hasCLDM(player.getUniqueID().toString()))
			return;
		
		if (player.isWearing(EnumPlayerModelParts.CAPE)) return; //cloaks obey the inverse of show cape
		
		int dm = ArsMagica2.proxy.playerTracker.getCLDM(player.getUniqueID().toString());
		ResourceLocation capeLoc = getCapeLocation(player.getUniqueID().toString());
		@SuppressWarnings("unused")
		ThreadDownloadImageData capeImg = downloadCapeTexture(capeLoc, player.getUniqueID().toString());

		EntityPlayer localPlayer = Minecraft.getMinecraft().player;

		GL11.glPushMatrix();

		double dx = (player.prevPosX + (player.posX - player.prevPosX) * partialRenderTick) - (localPlayer.prevPosX + (localPlayer.posX - localPlayer.prevPosX) * partialRenderTick);
		double dy = (player.prevPosY + (player.posY - player.prevPosY) * partialRenderTick) - (localPlayer.prevPosY + (localPlayer.posY - localPlayer.prevPosY) * partialRenderTick);
			dy += player.height - player.renderOffsetY - 0.13f;
		double dz = (player.prevPosZ + (player.posZ - player.prevPosZ) * partialRenderTick) - (localPlayer.prevPosZ + (localPlayer.posZ - localPlayer.prevPosZ) * partialRenderTick);


		GL11.glTranslated(dx, dy, dz);

		cloak.render(player, mainModel, 0.0625f, partialRenderTick, capeLoc, dm);
		GL11.glPopMatrix();
	}

	private static ResourceLocation getCapeLocation(String userName){
		ResourceLocation resourceLocation = downloadedCloakLocations.get(userName);
		if (resourceLocation == null){
			resourceLocation = new ResourceLocation("am2cloak/" + userName);
			downloadedCloakLocations.put(userName, resourceLocation);
		}
		return resourceLocation;
	}

	private static ThreadDownloadImageData downloadCapeTexture(ResourceLocation resourceLocation, String uuid){
		ThreadDownloadImageData data = downloadedCloaks.get(uuid);

		if (data == null){
			TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
			Object object = new ThreadDownloadImageData((File)null, ArsMagica2.proxy.playerTracker.getCLF(uuid), null, null);
			texturemanager.loadTexture(resourceLocation, (ITextureObject)object);
			data = (ThreadDownloadImageData)object;
			downloadedCloaks.put(uuid, data);
		}

		return data;
	}
}