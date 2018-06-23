package am2.client.particles.codechicken;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.api.math.AMVector3;
import am2.client.particles.codechicken.LightningBoltCommon.Segment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LightningBolt extends Particle{
	private int type = 0;
	private int overrideColor = -1;
	private LightningBoltCommon main;

	public LightningBolt(World world, AMVector3 jammervec, AMVector3 targetvec, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, jammervec, targetvec, seed);
		setupFromMain();
	}

	public LightningBolt(World world, Entity detonator, Entity target, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed);
		setupFromMain();
	}

	public LightningBolt(World world, Entity detonator, Entity target, long seed, int speed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed, speed);
		setupFromMain();
	}

	public LightningBolt(World world, TileEntity detonator, Entity target, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, target, seed);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
		setupFromMain();
	}

	public LightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, 1.0F);
		setupFromMain();
	}

	public LightningBolt(World world, TileEntity detonator, double x, double y, double z, long seed){
		super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.main = new LightningBoltCommon(world, detonator, x, y, z, seed);
		setupFromMain();
	}

	private void setupFromMain(){
		this.particleAge = this.main.particleMaxAge;
		setPosition(this.main.start.x, this.main.start.y, this.main.start.z);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
	}

	public void defaultFractal(){
		this.main.defaultFractal();
	}

	public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle){
		this.main.fractal(splits, amount, splitchance, splitlength, splitangle);
	}

	public void finalizeBolt(){
		this.main.finalizeBolt();
		Minecraft.getMinecraft().effectRenderer.addEffect(this);
	}

	public void setOverrideColor(int overrideColor){
		this.overrideColor = overrideColor;
	}

	public void setSourceEntity(Entity entity){
		this.main.wrapper = entity;
	}

	public void setType(int type){
		this.type = type;
		this.main.type = type;
	}

	public void setDamage(int dmg){
		this.main.damage = dmg;
	}

	public void setNonLethal(){
		this.main.nonLethal = true;
	}

	public void setMultiplier(float m){
		this.main.multiplier = m;
	}

	@Override
	public void onUpdate(){
		this.main.onUpdate();
		if (this.main.particleAge >= this.main.particleMaxAge){
			setExpired();
		}
	}

	private static AMVector3 getRelativeViewVector(AMVector3 pos){
		EntityPlayer renderentity = ArsMagica2.proxy.getLocalPlayer();
		return new AMVector3((float)renderentity.posX - pos.x, (float)renderentity.posY - pos.y, (float)renderentity.posZ - pos.z);
	}

	private void renderBolt(BufferBuilder buffer, float partialframe, float cosyaw, float cospitch, float sinyaw, float cossinpitch, int pass){
		AMVector3 playervec = new AMVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
		float boltage = this.main.particleAge >= 0 ? this.main.particleAge / this.main.particleMaxAge : 0.0F;
		float mainalpha = 1.0F;
		if (pass == 0)
			mainalpha = (1.0F - boltage) * 0.9F;
		else if (pass == 1)
			mainalpha = 1.0F - boltage * 0.6F;
		else
			mainalpha = 1.0F - boltage * 0.3F;
		int renderlength = (int)((this.main.particleAge + partialframe + (int)(this.main.length * 3.0F)) / (int)(this.main.length * 3.0F) * this.main.numsegments0);
		for (Iterator<Segment> iterator = this.main.segments.iterator(); iterator.hasNext(); ){
			LightningBoltCommon.Segment rendersegment = iterator.next();
			if (rendersegment.segmentno <= renderlength){
				float width = 0.03F * (getRelativeViewVector(rendersegment.startpoint.point).length() / 10.0F + 1.0F) * (1.0F + rendersegment.light) * 0.5F;
				if (width > 0.05F) width = 0.05F;
				if (pass == 1) width += 0.025f;
				else if (pass == 1) width += 0.05f;
				AMVector3 diff1 = AMVector3.crossProduct(playervec, rendersegment.prevdiff).scale(width / rendersegment.sinprev);
				AMVector3 diff2 = AMVector3.crossProduct(playervec, rendersegment.nextdiff).scale(width / rendersegment.sinnext);
				AMVector3 startvec = rendersegment.startpoint.point;
				AMVector3 endvec = rendersegment.endpoint.point;
				float rx1 = (float)(startvec.x - interpPosX);
				float ry1 = (float)(startvec.y - interpPosY);
				float rz1 = (float)(startvec.z - interpPosZ);
				float rx2 = (float)(endvec.x - interpPosX);
				float ry2 = (float)(endvec.y - interpPosY);
				float rz2 = (float)(endvec.z - interpPosZ);
//				tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light);
				buffer.pos(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).tex(0.5D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				buffer.pos(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).tex(0.5D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				buffer.pos(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).tex(0.5D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				buffer.pos(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).tex(0.5D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				if (rendersegment.next == null){
					AMVector3 roundend = rendersegment.endpoint.point.copy().add(rendersegment.diff.copy().normalize().scale(width));
					float rx3 = (float)(roundend.x - interpPosX);
					float ry3 = (float)(roundend.y - interpPosY);
					float rz3 = (float)(roundend.z - interpPosZ);
					buffer.pos(rx3 - diff2.x, ry3 - diff2.y, rz3 - diff2.z).tex(0.0D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).tex(0.5D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).tex(0.5D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx3 + diff2.x, ry3 + diff2.y, rz3 + diff2.z).tex(0.0D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				}
				if (rendersegment.prev == null){
					AMVector3 roundend = rendersegment.startpoint.point.copy().sub(rendersegment.diff.copy().normalize().scale(width));
					float rx3 = (float)(roundend.x - interpPosX);
					float ry3 = (float)(roundend.y - interpPosY);
					float rz3 = (float)(roundend.z - interpPosZ);
					buffer.pos(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).tex(0.5D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx3 - diff1.x, ry3 - diff1.y, rz3 - diff1.z).tex(0.0D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx3 + diff1.x, ry3 + diff1.y, rz3 + diff1.z).tex(0.0D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
					buffer.pos(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).tex(0.5D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light).endVertex();
				}
			}
		}
	}
	
	@Override
	public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch) {
		Tessellator tessellator = Tessellator.getInstance();
		EntityPlayer renderentity = ArsMagica2.proxy.getLocalPlayer();
		int visibleDistance = 100;
		if ((!Minecraft.getMinecraft().gameSettings.fancyGraphics)) visibleDistance = 50;
		if (renderentity.getDistance(this.posX, this.posY, this.posZ) > visibleDistance) return;

		GlStateManager.pushMatrix();
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT | GL11.GL_COLOR_BUFFER_BIT);

		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("arsmagica2", "textures/items/particles/smoke.png"));

		this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);

		switch (this.type){
		case 0:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 1);
			break;
		case 1:
			this.particleRed = 0.6F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 1);
			break;
		case 2:
			this.particleRed = 0.6F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 1);
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 1);
			break;
		case 4:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 5:
			this.particleRed = 0.6F;
			this.particleGreen = 0.3F;
			this.particleBlue = 0.6F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.1f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.1f;
			GL11.glBlendFunc(770, 1);
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}
		try{
			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		}catch (Throwable t){
			tessellator.draw();
			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		}
		renderBolt(worldRendererIn, partialTicks, cosyaw, cospitch, sinyaw, cossinpitch, 0);
//		try{
//			tessellator.draw();
//		}catch (Throwable t){
//		}

		switch (this.type){
		case 0:
			this.particleRed = 1.0F;
			this.particleGreen = 0.6F;
			this.particleBlue = 1.0F;
			break;
		case 1:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 1.0F;
			break;
		case 2:
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			break;
		case 4:
			this.particleRed = 0.6F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 5:
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.6f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.6f;
			GL11.glBlendFunc(770, 1);
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}

//		try{
//			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//		}catch (Throwable t){
//			tessellator.draw();
//			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//		}
		renderBolt(worldRendererIn, partialTicks, cosyaw, cospitch, sinyaw, cossinpitch, 1);
//		try{
//			tessellator.draw();
//		}catch (Throwable t){
//		}

		switch (this.type){
		case 0:
			this.particleRed = 1.0F;
			this.particleGreen = 0.6F;
			this.particleBlue = 1.0F;
			break;
		case 1:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 1.0F;
			break;
		case 2:
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
			break;
		case 3:
			this.particleRed = 0.1F;
			this.particleGreen = 0.6F;
			this.particleBlue = 0.1F;
			break;
		case 4:
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 5:
			this.particleRed = 1.0F;
			this.particleGreen = 1.0F;
			this.particleBlue = 0.1F;
			GL11.glBlendFunc(770, 771);
			break;
		case 6:
			this.particleRed = 0.6f;
			this.particleGreen = 0.1f;
			this.particleBlue = 0.6f;
			GL11.glBlendFunc(770, 1);
			break;
		}

		if (overrideColor != -1){
			this.particleRed = ((overrideColor >> 16) & 0xFF) / 255.0f;
			this.particleGreen = ((overrideColor >> 8) & 0xFF) / 255.0f;
			this.particleBlue = (overrideColor & 0xFF) / 255.0f;
		}

//		try{
//			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//		}catch (Throwable t){
//			tessellator.draw();
//			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//		}
		renderBolt(worldRendererIn, partialTicks, cosyaw, cospitch, sinyaw, cossinpitch, 2);
		try{
			tessellator.draw();
		}catch (Throwable t){
		}

		try{
			worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX);
		}catch (Throwable t){
		}

		GlStateManager.disableBlend();
		GL11.glDepthMask(true);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	
	public int getRenderPass(){
		return 2;
	}
}
