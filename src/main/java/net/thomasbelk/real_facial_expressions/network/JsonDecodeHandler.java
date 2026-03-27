package net.thomasbelk.real_facial_expressions.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import net.thomasbelk.real_facial_expressions.FacePacket;

public class JsonDecodeHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final Gson gson = new Gson();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf buf = packet.content();

        if (buf.getByte(buf.readerIndex()) != '{') {
            return;
        }

        String json = buf.toString(CharsetUtil.UTF_8);

        try {
            FacePacket facePacket = gson.fromJson(json, FacePacket.class);
            if (facePacket != null) {
                ctx.fireChannelRead(facePacket);
            }
        } catch (JsonSyntaxException ignored) {
            // drop malformed
        }
    }
}