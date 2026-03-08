package net.thomasbelk.real_facial_expressions.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
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
    FaceSettings initialFaceSettings;

    public FaceSettingsUI(@NonNull PlayerRef playerRef, @NonNull FaceSettings faceSettings) {
        super(playerRef, CustomPageLifetime.CantClose, FaceSettingsEventData.CODEC);
        this.faceSettings = faceSettings;
        this.initialFaceSettings = new FaceSettings(faceSettings);
        this.playerRef = playerRef;
    }

    private void setDefaultTextFromTranslation(@NonNull UICommandBuilder cmd) {
        setTooltip(cmd, "#BrowUpLabel.TooltipText", "server.faceSettingsUI.tooltip.browUp", FaceSettings.DEFAULT_BROW_THRESHOLD);
        setTooltip(cmd, "#BrowDownLabel.TooltipText", "server.faceSettingsUI.tooltip.browDown", FaceSettings.DEFAULT_BROW_DOWN_THRESHOLD);
        setTooltip(cmd, "#EyeLidClosedLabel.TooltipText", "server.faceSettingsUI.tooltip.eyelidClosed", FaceSettings.DEFAULT_EYE_LID_CLOSED_THRESHOLD);
        setTooltip(cmd, "#EyeLidHalfOpenLabel.TooltipText", "server.faceSettingsUI.tooltip.eyelidHalfOpen", FaceSettings.DEFAULT_EYE_LID_HALF_OPEN_THRESHOLD);
        setTooltip(cmd, "#SmileLabel.TooltipText", "server.faceSettingsUI.tooltip.smileActive", FaceSettings.DEFAULT_SMILE_THRESHOLD);
        setTooltip(cmd, "#MouthOpenLabel.TooltipText", "server.faceSettingsUI.tooltip.mouthOpen", FaceSettings.DEFAULT_MOUTH_OPEN_THRESHOLD);
    }

    public void setTooltip(@NonNull UICommandBuilder cmd, String selector, String translationKey, float defaultVal) {
        var browUpTooltip = Message.translation(translationKey).getAnsiMessage() + " " + Float.toString(defaultVal);
        cmd.set(selector, browUpTooltip);
    }

    private void setSliderValues(@NonNull UICommandBuilder cmd) {
        cmd.set("#BrowThreshold.Value", faceSettings.getBrowThreshold());
        cmd.set("#BrowDownThreshold.Value", faceSettings.getBrowDownThreshold());
        cmd.set("#EyeLidClosedThreshold.Value", faceSettings.getEyeLidClosedThreshold());
        cmd.set("#EyeLidHalfOpenThreshold.Value", faceSettings.getEyeLidHalfOpenThreshold());
        cmd.set("#SmileThreshold.Value", faceSettings.getSmileThreshold());
        cmd.set("#MouthOpenThreshold.Value", faceSettings.getMouthOpenThreshold());
        cmd.set("#BrowThresholdLabel.Text", Float.toString(faceSettings.getBrowThreshold()));
        cmd.set("#BrowDownThresholdLabel.Text", Float.toString(faceSettings.getBrowDownThreshold()));
        cmd.set("#EyeLidClosedThresholdLabel.Text", Float.toString(faceSettings.getEyeLidClosedThreshold()));
        cmd.set("#EyeLidHalfOpenThresholdLabel.Text", Float.toString(faceSettings.getEyeLidHalfOpenThreshold()));
        cmd.set("#SmileThresholdLabel.Text", Float.toString(faceSettings.getSmileThreshold()));
        cmd.set("#MouthOpenThresholdLabel.Text", Float.toString(faceSettings.getMouthOpenThreshold()));
    }

    @Override
    public void build(@NonNull Ref<EntityStore> ref, @NonNull UICommandBuilder cmd, @NonNull UIEventBuilder evt, @NonNull Store<EntityStore> store) {
        cmd.append("Pages/FaceSettingsPage.ui");

        setDefaultTextFromTranslation(cmd);

        // set all the values
        setSliderValues(cmd);

        // event listeners
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#BrowThreshold", new EventData().append("@BrowThreshold", "#BrowThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#BrowDownThreshold", new EventData().append("@BrowDownThreshold", "#BrowDownThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#EyeLidClosedThreshold", new EventData().append("@EyeLidClosedThreshold", "#EyeLidClosedThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#EyeLidHalfOpenThreshold", new EventData().append("@EyeLidHalfOpenThreshold", "#EyeLidHalfOpenThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#SmileThreshold", new EventData().append("@SmileThreshold", "#SmileThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.MouseButtonReleased, "#MouthOpenThreshold", new EventData().append("@MouthOpenThreshold", "#MouthOpenThreshold.Value"));
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#ResetButton", new EventData().append("@Reset", "#ResetButton.Visible")); // this seems dumb, there has to be a better way
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#SaveAndExit", new EventData().append("@SaveAndExit", "#SaveAndExit.Visible")); // this seems dumb, there has to be a better way
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#DiscardChanges", new EventData().append("@DiscardChanges", "#DiscardChanges.Visible")); // this seems dumb, there has to be a better way
    }

    @Override
    public void handleDataEvent(@NonNull Ref<EntityStore> ref, @NonNull Store<EntityStore> store, FaceSettingsUI.@NonNull FaceSettingsEventData data) {
        UICommandBuilder cmd = new UICommandBuilder();
        if (data.BrowThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Eyebrow Event Triggered");
            faceSettings.setBrowThreshold(data.BrowThreshold);
            cmd.set("#BrowThresholdLabel.Text", Float.toString(faceSettings.getBrowThreshold()));
        }
        if (data.BrowDownThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Eyebrow Event Triggered");
            faceSettings.setBrowDownThreshold(data.BrowDownThreshold);
            cmd.set("#BrowDownThresholdLabel.Text", Float.toString(faceSettings.getBrowDownThreshold()));
        }
        if (data.EyeLidClosedThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("EyelidCLosed Event Triggered");
            faceSettings.setEyeLidClosedThreshold(data.EyeLidClosedThreshold);
            cmd.set("#EyeLidClosedThresholdLabel.Text", Float.toString(faceSettings.getEyeLidClosedThreshold()));
        }
        if (data.EyeLidHalfOpenThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("EyeLidHalfOpenThreshold Event Triggered");
            faceSettings.setEyeLidHalfOpenThreshold(data.EyeLidHalfOpenThreshold);
            cmd.set("#EyeLidHalfOpenThresholdLabel.Text", Float.toString(faceSettings.getEyeLidHalfOpenThreshold()));
        }
        if (data.SmileThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("SmileThreshold Event Triggered");
            faceSettings.setSmileThreshold(data.SmileThreshold);
            cmd.set("#SmileThresholdLabel.Text", Float.toString(faceSettings.getSmileThreshold()));
        }
        if (data.MouthOpenThreshold != null) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("MouthOpenThreshold Event Triggered");
            faceSettings.setMouthOpenThreshold(data.MouthOpenThreshold);
            cmd.set("#MouthOpenThresholdLabel.Text", Float.toString(faceSettings.getMouthOpenThreshold()));
        }
        if (data.Reset) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Reset Event Triggered");
            faceSettings.resetToDefaults();
            setSliderValues(cmd);
        }
        if (data.DiscardChanges) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Discard Changes Event Triggered");
            faceSettings.copySettings(initialFaceSettings);
            closePage(ref, store);
            return;
        }
        if (data.SaveAndExit) {
            RealFacialExpressionsPlugin.LOGGER.atInfo().log("Save and Exit Event Triggered");
            closePage(ref, store);
            return;
        }
        sendUpdate(cmd, false);
    }

    public void closePage(@NonNull Ref<EntityStore> ref, @NonNull Store<EntityStore> store) {
        Player p = store.getComponent(ref, Player.getComponentType());
        assert (p != null);
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera());
        p.getPageManager().setPage(ref, store, Page.None);
    }

    public static class FaceSettingsEventData {
        public Float BrowThreshold;
        public Float BrowDownThreshold;
        public Float EyeLidClosedThreshold;
        public Float EyeLidHalfOpenThreshold;
        public Float SmileThreshold;
        public Float MouthOpenThreshold;
        public Boolean Reset = false;
        public Boolean SaveAndExit = false;
        public Boolean DiscardChanges = false;
        public static final BuilderCodec<FaceSettingsUI.FaceSettingsEventData> CODEC =
                BuilderCodec.builder(FaceSettingsUI.FaceSettingsEventData.class, FaceSettingsUI.FaceSettingsEventData::new)
                        .append(new KeyedCodec<>("@BrowThreshold", Codec.FLOAT),
                                (data, value) -> data.BrowThreshold = value,
                                (data) -> data.BrowThreshold).add()
                        .append(new KeyedCodec<>("@BrowDownThreshold", Codec.FLOAT),
                                (data, value) -> data.BrowDownThreshold = value,
                                (data) -> data.BrowDownThreshold).add()
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
                        .append(new KeyedCodec<>("@SaveAndExit", Codec.BOOLEAN),
                                (data, value) -> data.SaveAndExit = value,
                                (data) -> data.SaveAndExit).add()
                        .append(new KeyedCodec<>("@DiscardChanges", Codec.BOOLEAN),
                                (data, value) -> data.DiscardChanges = value,
                                (data) -> data.DiscardChanges).add()
                        .build();
    }
}