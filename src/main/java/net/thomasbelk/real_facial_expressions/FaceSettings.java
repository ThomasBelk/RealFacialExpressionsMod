package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class FaceSettings {
    public static final float DEFAULT_BROW_THRESHOLD = 0.5f;
    public static final float DEFAULT_EYE_LID_CLOSED_THRESHOLD = 0.4f;
    public static final float DEFAULT_EYE_LID_HALF_OPEN_THRESHOLD = 0.25f;
    public static final float DEFAULT_SMILE_THRESHOLD = 0.3f;
    public static final float DEFAULT_MOUTH_OPEN_THRESHOLD = 0.05f;

    private float browThreshold;
    private float eyeLidClosedThreshold;
    private float eyeLidHalfOpenThreshold;
    private float smileThreshold;
    private float mouthOpenThreshold;

    public FaceSettings() {
        this.browThreshold = DEFAULT_BROW_THRESHOLD;
        this.eyeLidClosedThreshold = DEFAULT_EYE_LID_CLOSED_THRESHOLD;
        this.eyeLidHalfOpenThreshold = DEFAULT_EYE_LID_HALF_OPEN_THRESHOLD;
        this.smileThreshold = DEFAULT_SMILE_THRESHOLD;
        this.mouthOpenThreshold = DEFAULT_MOUTH_OPEN_THRESHOLD;
    }

    public FaceSettings(
            float browThreshold,
            float eyeLidClosedThreshold,
            float eyeLidHalfOpenThreshold,
            float smileThreshold,
            float mouthOpenThreshold)
    {
        this.browThreshold = browThreshold;
        this.eyeLidClosedThreshold = eyeLidClosedThreshold;
        this.eyeLidHalfOpenThreshold = eyeLidHalfOpenThreshold;
        this.smileThreshold = smileThreshold;
        this.mouthOpenThreshold = mouthOpenThreshold;
    }

    public static FaceSettings newDefault() {
        return new FaceSettings();
    }

    public void resetToDefaults() {
        this.browThreshold = DEFAULT_BROW_THRESHOLD;
        this.eyeLidClosedThreshold = DEFAULT_EYE_LID_CLOSED_THRESHOLD;
        this.eyeLidHalfOpenThreshold = DEFAULT_EYE_LID_HALF_OPEN_THRESHOLD;
        this.smileThreshold = DEFAULT_SMILE_THRESHOLD;
        this.mouthOpenThreshold = DEFAULT_MOUTH_OPEN_THRESHOLD;
    }

    // would it be better practice to use a float array codec here?
    public static final BuilderCodec<FaceSettings> CODEC = BuilderCodec
            .builder(FaceSettings.class, FaceSettings::new)
            .append(
                    new KeyedCodec<>("BrowThreshold", Codec.FLOAT),
                    (data, value) -> data.browThreshold = value,
                    (data) -> data.browThreshold
            ).add()
            .append(
                    new KeyedCodec<>("EyeLidClosedThreshold", Codec.FLOAT),
                    (data, value) -> data.eyeLidClosedThreshold = value,
                    (data) -> data.eyeLidClosedThreshold
            ).add()
            .append(
                    new KeyedCodec<>("EyeBrowHalfOpenThreshold", Codec.FLOAT),
                    (data, value) -> data.eyeLidHalfOpenThreshold = value,
                    (data) -> data.eyeLidHalfOpenThreshold
            ).add()
            .append(
                    new KeyedCodec<>("SmileThreshold", Codec.FLOAT),
                    (data, value) -> data.smileThreshold = value,
                    (data) -> data.smileThreshold
            ).add()
            .append(
                        new KeyedCodec<>("MouthOpenThreshold", Codec.FLOAT),
                    (data, value) -> data.mouthOpenThreshold = value,
                    (data) -> data.mouthOpenThreshold
            ).add()
            .build();

    public float getBrowThreshold() {
        return browThreshold;
    }

    public void setBrowThreshold(float browThreshold) {
        this.browThreshold = browThreshold;
    }

    public float getEyeLidClosedThreshold() {
        return eyeLidClosedThreshold;
    }

    public void setEyeLidClosedThreshold(float eyeLidClosedThreshold) {
        this.eyeLidClosedThreshold = eyeLidClosedThreshold;
    }

    public float getEyeLidHalfOpenThreshold() {
        return eyeLidHalfOpenThreshold;
    }

    public void setEyeLidHalfOpenThreshold(float eyeLidHalfOpenThreshold) {
        this.eyeLidHalfOpenThreshold = eyeLidHalfOpenThreshold;
    }

    public float getSmileThreshold() {
        return smileThreshold;
    }

    public void setSmileThreshold(float smileThreshold) {
        this.smileThreshold = smileThreshold;
    }

    public float getMouthOpenThreshold() {
        return mouthOpenThreshold;
    }

    public void setMouthOpenThreshold(float mouthOpenThreshold) {
        this.mouthOpenThreshold = mouthOpenThreshold;
    }
}

