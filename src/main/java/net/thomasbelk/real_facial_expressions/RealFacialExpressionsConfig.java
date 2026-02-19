package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class RealFacialExpressionsConfig {
    public static final BuilderCodec<RealFacialExpressionsConfig> CODEC = BuilderCodec.builder(
                    RealFacialExpressionsConfig.class, RealFacialExpressionsConfig::new)
            .append(new KeyedCodec<>("FaceIdPort", Codec.INTEGER),
                    (config, value) -> config.port = value,
                    (config) -> config.port).add()
            .build();

    private int port = 25590;

    public RealFacialExpressionsConfig() {}

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
