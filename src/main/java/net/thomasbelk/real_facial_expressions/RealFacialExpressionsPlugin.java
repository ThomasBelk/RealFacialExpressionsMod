package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.thomasbelk.real_facial_expressions.look.LookCommand;

import java.util.concurrent.ConcurrentHashMap;

public class RealFacialExpressionsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private FacePacketStore facePacketStore;
    private int facePacketPort = 25590;

    public RealFacialExpressionsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }


    @Override
    protected void setup() {
        this.facePacketStore = new FacePacketStore();
        // register components
        var entityStoreRegistry = this.getEntityStoreRegistry();
        var faceAnimComponentType = entityStoreRegistry.registerComponent(
                PlayerFaceAnimationComponent.class,
                "RealFacialExpression_PlayerFaceAnimationData",
                PlayerFaceAnimationComponent.CODEC
        );
        PlayerFaceAnimationComponent.setComponentType(faceAnimComponentType);

        // register systems
        entityStoreRegistry.registerSystem(new PlayerJoinSystem(this.facePacketStore));

        // register commands
        this.getCommandRegistry().registerCommand(new LookFrontCommand());
        this.getCommandRegistry().registerCommand(new LookCommand());

        // setup to receive face packets
        FacePacketReceiver facePacketReceiver = new FacePacketReceiver(this.facePacketPort, this.facePacketStore);
        Thread reciverThread = new Thread(facePacketReceiver, "face-udp-receiver");
        reciverThread.start();
    }
}
