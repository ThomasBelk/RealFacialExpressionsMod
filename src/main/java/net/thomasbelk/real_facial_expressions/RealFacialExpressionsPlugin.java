package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.thomasbelk.real_facial_expressions.commands.DisplayFaceIdCommand;
import net.thomasbelk.real_facial_expressions.commands.LookCommand;

public class RealFacialExpressionsPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private FacePacketStore facePacketStore;
    private int facePacketPort = 25590;
    private FacePacketReceiver facePacketReceiver;
    private Thread reciverThread;

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
        entityStoreRegistry.registerSystem(new PlayerJoinLeaveSystem(this.facePacketStore));
        entityStoreRegistry.registerSystem(new FaceAnimationSystem(this.facePacketStore));

        // register commands
        this.getCommandRegistry().registerCommand(new LookFrontCommand());
        this.getCommandRegistry().registerCommand(new LookCommand());
        this.getCommandRegistry().registerCommand(new DisplayFaceIdCommand(this.facePacketStore));

        // setup to receive face packets
        facePacketReceiver = new FacePacketReceiver(this.facePacketPort, this.facePacketStore);
        reciverThread = new Thread(facePacketReceiver, "face-udp-receiver");
        reciverThread.start();
    }

    @Override
    protected void shutdown() {
        if (facePacketReceiver != null) {
            facePacketReceiver.shutdown();
        }
        if (reciverThread != null) {
            reciverThread.interrupt();
        }
        super.shutdown();
    }
}
