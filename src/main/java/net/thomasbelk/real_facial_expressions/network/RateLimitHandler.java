package net.thomasbelk.real_facial_expressions.network;

import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.thomasbelk.real_facial_expressions.RealFacialExpressionsPlugin.LOGGER;

public class RateLimitHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final long TTL_MS = 60_000; // 1 min
    private static final long CLEANUP_INTERVAL_MS = 10_000; // should this be configureable in config? maybe...
    private static final int MAX_ENTRIES = 10_000;      // should probably be configureable in config

    private final int maxPerSecond;
    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    private volatile long lastCleanup = System.currentTimeMillis();

    public RateLimitHandler(int maxPerSecond) {
        this.maxPerSecond = maxPerSecond;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        long now = System.currentTimeMillis();

        if (now - lastCleanup > CLEANUP_INTERVAL_MS) {
            lastCleanup = now;
            limiters.entrySet().removeIf(entry ->
                    entry.getValue().isExpired(now)
            );
        }

        String key = packet.sender().getAddress().getHostAddress(); // this should be a cheaper key than InetAdress

        RateLimiter limiter = limiters.get(key);
        if (limiter == null) {
            if (limiters.size() >= MAX_ENTRIES) {
                return; // drop
            }
            limiter = new RateLimiter(maxPerSecond, now);
            limiters.put(key, limiter);
        }

        if (!limiter.allow(now)) {
            LOGGER.atInfo().log(key + " exceeded their max tokens");
            return;
        }

        ctx.fireChannelRead(packet.retain());
    }

    private static class RateLimiter {
        private final int maxTokens;

        private volatile int tokens;
        private volatile long lastRefill;
        private volatile long lastSeen;

        RateLimiter(int maxTokens, long now) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefill = now;
            this.lastSeen = now;
        }

        boolean allow(long now) {
            lastSeen = now;

            long elapsed = now - lastRefill;
            if (elapsed >= 1000) {
                tokens = maxTokens;
                lastRefill = now;
            }

            if (tokens > 0) {
                tokens--; // this is a race condition but idrc cause udp is lossy af anyways.
                return true;
            }

            return false;
        }

        boolean isExpired(long now) {
            return now - lastSeen > TTL_MS;
        }
    }
}