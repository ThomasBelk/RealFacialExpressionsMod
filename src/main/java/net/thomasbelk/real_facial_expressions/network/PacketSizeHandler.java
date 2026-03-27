package net.thomasbelk.real_facial_expressions.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import static net.thomasbelk.real_facial_expressions.RealFacialExpressionsPlugin.LOGGER;

public class PacketSizeHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final int maxSize;

    public PacketSizeHandler(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        if (packet.content().readableBytes() > maxSize) {
            LOGGER.atInfo().log("Dropped a packet of size: " +  packet.content().readableBytes() + " bytes.");
            return;
        }

        ctx.fireChannelRead(packet.retain());
    }
}