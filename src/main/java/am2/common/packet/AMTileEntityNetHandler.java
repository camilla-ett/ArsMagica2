package am2.common.packet;

import am2.common.LogHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;

public class AMTileEntityNetHandler{

	private static final String ChannelLabel = "AMTileEntitySync";
	private static FMLEventChannel Channel;

	private boolean registeredChannels = false;

	private AMTileEntityNetHandler(){

	}

	public static final AMTileEntityNetHandler INSTANCE = new AMTileEntityNetHandler();

	public void init(){
		Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(ChannelLabel);
	}

	public void registerChannels(AMTileEntityPacketProcessorServer proc){
		if (!registeredChannels){
			registeredChannels = true;
			Channel.register(proc);
			MinecraftForge.EVENT_BUS.register(proc);
		}else{
			LogHelper.info("Redundant call to register channels.");
		}
	}

	public void sendPacketToClientPlayer(EntityPlayerMP player, byte packetID, BlockPos from, byte[] data){

		byte[] pkt_data = new byte[data.length + 13];
		pkt_data[0] = packetID;
		pkt_data[1] = (byte) (from.getX() >> 24 & 0xFF);
		pkt_data[2] = (byte) (from.getX() >> 16 & 0xFF);
		pkt_data[3] = (byte) (from.getX() >> 8 & 0xFF);
		pkt_data[4] = (byte) (from.getX() & 0xFF);
		pkt_data[5] = (byte) (from.getY() >> 24 & 0xFF);
		pkt_data[6] = (byte) (from.getY() >> 16 & 0xFF);
		pkt_data[7] = (byte) (from.getY() >> 8 & 0xFF);
		pkt_data[8] = (byte) (from.getY() & 0xFF);
		pkt_data[9] = (byte) (from.getZ() >> 24 & 0xFF);
		pkt_data[10] = (byte) (from.getZ() >> 16 & 0xFF);
		pkt_data[11] = (byte) (from.getZ() >> 8 & 0xFF);
		pkt_data[12] = (byte) (from.getZ() & 0xFF);
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}


		FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(Unpooled.copiedBuffer(pkt_data)), ChannelLabel);
		packet.setTarget(Side.CLIENT);
		Channel.sendTo(packet, player);
	}

	public void sendPacketToServer(byte packetID, BlockPos from, byte[] data){
		byte[] pkt_data = new byte[data.length + 13];
		pkt_data[0] = packetID;
		pkt_data[1] = (byte) (from.getX() >> 24 & 0xFF);
		pkt_data[2] = (byte) (from.getX() >> 16 & 0xFF);
		pkt_data[3] = (byte) (from.getX() >> 8 & 0xFF);
		pkt_data[4] = (byte) (from.getX() & 0xFF);
		pkt_data[5] = (byte) (from.getY() >> 24 & 0xFF);
		pkt_data[6] = (byte) (from.getY() >> 16 & 0xFF);
		pkt_data[7] = (byte) (from.getY() >> 8 & 0xFF);
		pkt_data[8] = (byte) (from.getY() & 0xFF);
		pkt_data[9] = (byte) (from.getZ() >> 24 & 0xFF);
		pkt_data[10] = (byte) (from.getZ() >> 16 & 0xFF);
		pkt_data[11] = (byte) (from.getZ() >> 8 & 0xFF);
		pkt_data[12] = (byte) (from.getZ() & 0xFF);
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}

		FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(Unpooled.copiedBuffer(pkt_data)), ChannelLabel);
		packet.setTarget(Side.SERVER);
		Channel.sendToServer(packet);
	}

	public void sendPacketToAllClientsNear(int dimension, double ox, double oy, double oz, BlockPos from, double radius, byte packetID, byte[] data){
		//first byte is ID, followed by data
		byte[] pkt_data = new byte[data.length + 13];
		pkt_data[0] = packetID;
		pkt_data[1] = (byte) (from.getX() >> 24 & 0xFF);
		pkt_data[2] = (byte) (from.getX() >> 16 & 0xFF);
		pkt_data[3] = (byte) (from.getX() >> 8 & 0xFF);
		pkt_data[4] = (byte) (from.getX() & 0xFF);
		pkt_data[5] = (byte) (from.getY() >> 24 & 0xFF);
		pkt_data[6] = (byte) (from.getY() >> 16 & 0xFF);
		pkt_data[7] = (byte) (from.getY() >> 8 & 0xFF);
		pkt_data[8] = (byte) (from.getY() & 0xFF);
		pkt_data[9] = (byte) (from.getZ() >> 24 & 0xFF);
		pkt_data[10] = (byte) (from.getZ() >> 16 & 0xFF);
		pkt_data[11] = (byte) (from.getZ() >> 8 & 0xFF);
		pkt_data[12] = (byte) (from.getZ() & 0xFF);
		for (int i = 0; i < data.length; ++i){
			pkt_data[i + 1] = data[i];
		}


		FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(Unpooled.copiedBuffer(pkt_data)), ChannelLabel);
		packet.setTarget(Side.CLIENT);
		Channel.sendToAllAround(packet, new TargetPoint(dimension, ox, oy, oz, radius));
	}
}
