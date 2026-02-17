package net.thomasbelk.real_facial_expressions;

import java.util.Map;

public enum BrowPos {
    Neutral("Neutral", "BrowN"),
    LeftUp("Left Up", "BrowLU"),
    RightUp("Right Up", "BrowRU"),
    BothUp("Both Up", "BrowU"),
    BothDown("Both Down", "BrowD");

    private final static float DEFAULT_THRESHOLD = 0.5f;
    private final String name;
    private final String animName;

    BrowPos(String name, String animName) {
        this.name = name;
        this.animName = animName;
    }

    public static BrowPos getBrowPosFromBlendshapes(Map<String, Float> blendshapes, float threshold) {
        if (blendshapes == null) {
            return BrowPos.Neutral;
        }
        float browInnerUp = blendshapes.getOrDefault("browInnerUp", 0f);
        float browDownLeft = blendshapes.getOrDefault("browDownLeft", 0f);
        float browDownRight = blendshapes.getOrDefault("browDownRight", 0f);
        float browOuterUpLeft = blendshapes.getOrDefault("browOuterUpLeft", 0f);
        float browOuterUpRight = blendshapes.getOrDefault("browOuterUpRight", 0f);

        boolean leftUp = browOuterUpLeft > threshold || browInnerUp > threshold;
        boolean rightUp = browOuterUpRight > threshold || browInnerUp > threshold;
        boolean leftDown = browDownLeft > threshold;
        boolean rightDown = browDownRight > threshold;

        if (leftUp && rightUp) return BrowPos.BothUp;
        else if (leftDown && rightDown) return BrowPos.BothDown;
        else if (leftUp) return BrowPos.LeftUp;
        else if (rightUp) return BrowPos.RightUp;
        else return BrowPos.Neutral;
    }

    public static BrowPos getBrowPosFromBlendshapes(Map<String, Float> blendshapes) {
        return getBrowPosFromBlendshapes(blendshapes, DEFAULT_THRESHOLD);
    }

    public String getAnimId() {
        return this.animName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
