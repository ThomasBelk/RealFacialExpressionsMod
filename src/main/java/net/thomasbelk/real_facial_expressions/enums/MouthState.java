package net.thomasbelk.real_facial_expressions.enums;

import java.util.Map;

public enum MouthState {
    MouthOpen("Mouth Open", "MouthO"),
    MouthClosed("Mouth Closed", "MouthC"),
    Smile("Smile", "MouthSC"),
    SmileMouthOpen("Smile Mouth Open", "MouthSO"),
    MouthFrown("Mouth Frown", "MouthF");

    private final static float DEFAULT_SMILE_THRESHOLD = 0.3f;
    private final static float DEFAULT_JAW_OPEN_THRESHOLD = 0.05f;
    private final String name;
    private final String animName;

    MouthState(String name, String animName) {
        this.name = name;
        this.animName = animName;
    }

    public static MouthState getMouthStateFromBlendshapes(Map<String, Float> blendshapes, float smileThreshold, float jawOpenThreshold) {
        if (blendshapes == null) {
            return MouthClosed;
        }

        float jawOpen = blendshapes.getOrDefault("jawOpen", 0f);
        float mouthLeft = blendshapes.getOrDefault("mouthSmileLeft", 0f);
        float mouthRight = blendshapes.getOrDefault("mouthSmileRight", 0f);
        float mouthFrownLeft = blendshapes.getOrDefault("mouthFrownLeft", 0f);
        float mouthFrownRight = blendshapes.getOrDefault("mouthFrownRight", 0f);


        boolean isJawOpen = jawOpen > jawOpenThreshold;
        boolean isSmile = (mouthLeft + mouthRight) / 2 > smileThreshold;
        boolean isFrown = (mouthFrownLeft + mouthFrownRight) / 2 > smileThreshold; // frown is not working rn, apparently it is a more complicated expression

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
        return getMouthStateFromBlendshapes(blendshapes, DEFAULT_SMILE_THRESHOLD, DEFAULT_JAW_OPEN_THRESHOLD);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
