package net.thomasbelk.real_facial_expressions.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.thomasbelk.real_facial_expressions.FacePacket;
import net.thomasbelk.real_facial_expressions.FacePacketStore;

public class FacePacketHandler extends SimpleChannelInboundHandler<FacePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FacePacket packet) {
        if (FacePacketStore.INSTANCE.hasValidFaceId(packet.faceId())) {
            FacePacketStore.INSTANCE.putPacket(packet.faceId(), packet);
        }
    }
}
