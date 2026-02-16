package net.thomasbelk.real_facial_expressions.look;

public enum LookDir {
//    Up("up", "LookUp"),
//    Down("down", "LookDown"),
//    Left("left", "LookLeft"),
//    Right("right", "LookRight"),
//    UpLeft("upleft", "LookUpLeft"),
//    UpRight("upright", "LookUpRight"),
//    DownLeft("downleft", "LookDownLeft"),
//    DownRight("downright", "LookDownRight"),
//    Center("center", "LookCenter");
    Up("up", "LookCU"),
    Down("down", "LookCD"),
    Left("left", "LookLC"),
    Right("right", "LookRC"),
    UpLeft("upleft", "LookLU"),
    UpRight("upright", "LookRU"),
    DownLeft("downleft", "LookLD"),
    DownRight("downright", "LookRD"),
    Center("center", "LookCC");

    private final String name;
    private final String animName;

    LookDir(String name, String animName) {
        this.name = name;
        this.animName = animName;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAnimationId() {
        return animName;
    }

    public static String getAnimationId(String dir) throws InvalidLookDirNameException {
        for (LookDir lookDir : values()) {
            if (lookDir.name.equalsIgnoreCase(dir)) {
                return lookDir.animName;
            }
        }
        throw new InvalidLookDirNameException(dir);
    }

    public static String getAllNamesAsOneString() {
        StringBuilder s = new StringBuilder("[ ");
        boolean first = true;
        for (LookDir lookDir : values()) {
            if (!first) {
                s.append(" | ");
            } else {
                first = false;
            }
            s.append(lookDir.toString());
        }
        s.append(" ]");
        return s.toString();
    }
}


