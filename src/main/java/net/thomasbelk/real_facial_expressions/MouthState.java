package net.thomasbelk.real_facial_expressions;

import java.util.Map;

public enum MouthState {
    MouthOpen("Mouth Open", "MouthO"),
    MouthClosed("Mouth Closed", "MouthC"),
    Smile("Smile", "MouthSC"),
    SmileMouthOpen("Smile Mouth Open", "MouthSO"),
    MouthFrown("Mouth Frown", "MouthF");

    private final static float DEFAULT_THRESHOLD = 0.5f;
    private final String name;
    private final String animName;

    MouthState(String name, String animName) {
        this.name = name;
        this.animName = animName;
    }

    public static MouthState getMouthStateFromBlendshapes(Map<String, Float> blendshapes, float threshold) {
        if (blendshapes == null) {
            return MouthClosed;
        }

        float jawOpen = blendshapes.getOrDefault("jawOpen", 0f);
        float mouthLeft = blendshapes.getOrDefault("mouthLeft", 0f);
        float mouthRight = blendshapes.getOrDefault("mouthRight", 0f);
        float mouthFrownLeft = blendshapes.getOrDefault("mouthFrownLeft", 0f);
        float mouthFrownRight = blendshapes.getOrDefault("mouthFrownRight", 0f);

        boolean isJawOpen = jawOpen > threshold;
        boolean isSmile = (mouthLeft + mouthRight) / 2 > threshold;
        boolean isFrown = (mouthFrownLeft + mouthFrownRight) / 2 > threshold;

        if (isSmile && isJawOpen) return SmileMouthOpen;
        else if (isSmile) return Smile;
        else if (isFrown) return MouthFrown;
        else if (isJawOpen) return MouthOpen;
        else return MouthClosed;
    }

    public String getAnimId() {
        return this.animName;
    }

    public static MouthState getMouthStateFromBlendshapes(Map<String, Float> blendshapes) {
        return getMouthStateFromBlendshapes(blendshapes, DEFAULT_THRESHOLD);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
