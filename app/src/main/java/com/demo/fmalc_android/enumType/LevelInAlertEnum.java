package com.demo.fmalc_android.enumType;

public enum LevelInAlertEnum {
    WEAK("5p-15p"){@Override public int getValue(){return 0;}},
    MEDIUM("15p-30p"){@Override public int getValue(){return 1;}},
    HIGH("Lớn hơn 30p"){@Override public int getValue(){return 2;}},
    SUPER_HIGH("Yêu cầu đổi xe"){@Override public int getValue(){return 3;}};

    String levelInAlertEnum;

    LevelInAlertEnum(String levelInAlertEnum){
        this.levelInAlertEnum = levelInAlertEnum;
    }

    public String getLevelInAlertEnum() {
        return levelInAlertEnum;
    }

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return WEAK.getLevelInAlertEnum();
            case 1:
                return MEDIUM.getLevelInAlertEnum();
            case 2:
                return HIGH.getLevelInAlertEnum();
            case 3:
                return SUPER_HIGH.getLevelInAlertEnum();
            default:
                throw new AssertionError("Unknown operations ");
        }
    }

    public abstract int getValue();
}
