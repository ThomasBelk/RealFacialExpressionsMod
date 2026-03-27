package net.thomasbelk.real_facial_expressions.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.thomasbelk.real_facial_expressions.RealFacialExpressionsPlugin.LOGGER;

public class NettyFacePacketReceiver {
    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private EventLoopGroup group;
    private Channel channel;
    private EventLoopGroup workerGroup;

    public NettyFacePacketReceiver(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        if (running.get()) return;

        running.set(true);

        group = new NioEventLoopGroup(1);
        workerGroup = new DefaultEventLoopGroup(1, new DefaultThreadFactory("Face-UDP-Parsing-Worker"));

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) {
                        ch.pipeline()
                                .addLast(new PacketSizeHandler(1024))
                                .addLast(new RateLimitHandler(30))
                                .addLast(workerGroup, new JsonDecodeHandler())
                                .addLast(new FacePacketHandler());
                    }
                });

        // bind without blocking forever
        channel = bootstrap.bind(port).sync().channel();
    }

    public void shutdown() {
        if (!running.getAndSet(false)) return;

        try {
            if (channel != null) {
                channel.close().syncUninterruptibly(); // ensure channel closes first
            }

            if (group != null) {
                group.shutdownGracefully().syncUninterruptibly(); // wait for threads
            }

            if (workerGroup != null) {
                workerGroup.shutdownGracefully().syncUninterruptibly(); // wait for workers
            }
            LOGGER.atInfo().log("Successfully shutdown Real Facial Expressions Netty Threads.");
        } catch (Exception e) {
            LOGGER.atWarning().log("Failed to shutdown Real Facial Expressions Netty Threads. Reason: " + e.getMessage());
        }
    }
}
