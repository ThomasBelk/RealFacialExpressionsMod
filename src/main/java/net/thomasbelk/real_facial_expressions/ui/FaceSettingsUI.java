package net.thomasbelk.real_facial_expressions.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.FaceSettings;
import net.thomasbelk.real_facial_expressions.RealFacialExpressionsPlugin;
import org.jspecify.annotations.NonNull;

public class FaceSettingsUI extends InteractiveCustomUIPage<FaceSettingsUI.FaceSettingsEventData> {
    FaceSettings faceSettings;
    PlayerRef playerRef;

    public FaceSettingsUI(@NonNull PlayerRef playerRef, @NonNull FaceSettings faceSettings) {
        super(playerRef, CustomPageLifetime.CanDismiss, FaceSettingsEventData.CODEC);
        this.faceSettings = faceSettings;
        this.playerRef = playerRef;
    }

    private void setSliderValues(@NonNull UICommandBuilder cmd) {
        cmd.set("#BrowThreshold.Value", faceSettings.getBrowThreshold());
        cmd.set("#EyeLidClosedThreshold.Value", faceSettings.getEyeLidClosedThreshold());
        cmd.set("#EyeLidHalfOpenThreshold.Value", faceSettings.getEyeLidHalfOpenThreshold());
        cmd.set("#SmileThreshold.Value", faceSettings.getSmileThreshold());
        cmd.set("#MouthOpenThreshold.Value", faceSettings.getMouthOpenThreshold());
    }

    @Override
    public void build(@NonNull Ref<EntityStore> ref, @NonNull UICommandBuilder cmd, @NonNull UIEventBuilder evt, @NonNull Store<EntityStore> store) {
        cmd.append("Pages/FaceSettingsPage.ui");

        // set all the values
        setSliderValues(cmd);

        // event listeners
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#BrowThreshold", new EventData().append("@BrowThreshold", "#BrowThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#EyeLidClosedThreshold", new EventData().append("@EyeLidClosedThreshold", "#EyeLidClosedThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#EyeLidHalfOpenThreshold", new EventData().append("@EyeLidHalfOpenThreshold", "#EyeLidHalfOpenThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#SmileThreshold", new EventData().append("@SmileThreshold", "#SmileThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#MouthOpenThreshold", new EventData().append("@MouthOpenThreshold", "#MouthOpenThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#ResetButton", new EventData().append("@Reset", "#ResetButton.Visible")); // this seems dumb, there has to be a better way
    }

    @Override
    public void handleDataEvent(@NonNull Ref<EntityStore> ref, @NonNull Store<EntityStore> store, FaceSettingsUI.@NonNull FaceSettingsEventData data) {
        UICommandBuilder cmd = new UICommandBuilder();
        if (data.BrowThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Eyebrow Event Triggered");
            faceSettings.setBrowThreshold(data.BrowThreshold);
        }
        if (data.EyeLidClosedThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("EyelidCLosed Event Triggered");
            faceSettings.setEyeLidClosedThreshold(data.EyeLidClosedThreshold);
        }
        if (data.EyeLidHalfOpenThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("EyeLidHalfOpenThreshold Event Triggered");
            faceSettings.setEyeLidHalfOpenThreshold(data.EyeLidHalfOpenThreshold);
        }
        if (data.SmileThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("SmileThreshold Event Triggered");
            faceSettings.setSmileThreshold(data.SmileThreshold);
        }
        if (data.MouthOpenThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("MouthOpenThreshold Event Triggered");
            faceSettings.setMouthOpenThreshold(data.MouthOpenThreshold);
        }
        if (data.Reset) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Reset Event Triggered");
            faceSettings.resetToDefaults();
            setSliderValues(cmd);
        }
        sendUpdate(cmd, false);
    }

    public static class FaceSettingsEventData {
        public Float BrowThreshold;
        public Float EyeLidClosedThreshold;
        public Float EyeLidHalfOpenThreshold;
        public Float SmileThreshold;
        public Float MouthOpenThreshold;
        public Boolean Reset = false;
        public static final BuilderCodec<FaceSettingsUI.FaceSettingsEventData> CODEC =
                BuilderCodec.builder(FaceSettingsUI.FaceSettingsEventData.class, FaceSettingsUI.FaceSettingsEventData::new)
                        .append(new KeyedCodec<>("@BrowThreshold", Codec.FLOAT),
                                (data, value) -> data.BrowThreshold = value,
                                (data) -> data.BrowThreshold).add()
                        .append(new KeyedCodec<>("@EyeLidClosedThreshold", Codec.FLOAT),
                                (data, value) -> data.EyeLidClosedThreshold = value,
                                (data) -> data.EyeLidClosedThreshold).add()
                        .append(new KeyedCodec<>("@EyeLidHalfOpenThreshold", Codec.FLOAT),
                                (data, value) -> data.EyeLidHalfOpenThreshold = value,
                                (data) -> data.EyeLidHalfOpenThreshold).add()
                        .append(new KeyedCodec<>("@SmileThreshold", Codec.FLOAT),
                                (data, value) -> data.SmileThreshold = value,
                                (data) -> data.SmileThreshold).add()
                        .append(new KeyedCodec<>("@MouthOpenThreshold", Codec.FLOAT),
                                (data, value) -> data.MouthOpenThreshold = value,
                                (data) -> data.MouthOpenThreshold).add()
                        .append(new KeyedCodec<>("@Reset", Codec.BOOLEAN),
                                (data, value) -> data.Reset = value,
                                (data) -> data.Reset).add()
                        .build();
    }
}