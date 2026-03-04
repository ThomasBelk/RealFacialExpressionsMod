package net.thomasbelk.real_facial_expressions.enums;

import net.thomasbelk.real_facial_expressions.FaceSettings;

import java.util.Map;

public enum BrowPos {
    Neutral("Neutral", "BrowN"),
    LeftUp("Left Up", "BrowLU"),
    RightUp("Right Up", "BrowRU"),
    BothUp("Both Up", "BrowU"),
    BothDown("Both Down", "BrowD");

    private final String name;
    private final String animName;

    BrowPos(String name, String animName) {
        this.name = name;
        this.animName = animName;
    }

    public static BrowPos getBrowPosFromBlendshapes(Map<String, Float> blendshapes, float upThreshold, float downThreshold) {
        if (blendshapes == null) {
            return BrowPos.Neutral;
        }
        float browInnerUp = blendshapes.getOrDefault("browInnerUp", 0f);
        float browDownLeft = blendshapes.getOrDefault("browDownLeft", 0f);
        float browDownRight = blendshapes.getOrDefault("browDownRight", 0f);
        float browOuterUpLeft = blendshapes.getOrDefault("browOuterUpLeft", 0f);
        float browOuterUpRight = blendshapes.getOrDefault("browOuterUpRight", 0f);

        boolean leftUp = browOuterUpLeft > upThreshold || browInnerUp > upThreshold;
        boolean rightUp = browOuterUpRight > upThreshold || browInnerUp > upThreshold;
        boolean leftDown = browDownLeft > downThreshold;
        boolean rightDown = browDownRight > downThreshold;

        if (leftUp && rightUp) return BrowPos.BothUp;
        else if (leftDown && rightDown) return BrowPos.BothDown;
        else if (leftUp) return BrowPos.LeftUp;
        else if (rightUp) return BrowPos.RightUp;
        else return BrowPos.Neutral;
    }

    public static BrowPos getBrowPosFromBlendshapes(Map<String, Float> blendshapes) {
        return getBrowPosFromBlendshapes(blendshapes, FaceSettings.DEFAULT_BROW_THRESHOLD, FaceSettings.DEFAULT_BROW_DOWN_THRESHOLD);
    }

    public static BrowPos getBrowPosFromBlendshapes(Map<String, Float> blendshapes, FaceSettings faceSettings) {
        return getBrowPosFromBlendshapes(blendshapes, faceSettings.getBrowThreshold(), faceSettings.getBrowDownThreshold());
    }

    public String getAnimId() {
        return this.animName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
