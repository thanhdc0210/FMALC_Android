package com.demo.fmalc_android.enumType;

public enum DayOffEnum {
    WAITING("Chờ xác nhận"){@Override public int getValue(){return -1;}},
    APPROVED("Được chấp nhận"){@Override public int getValue(){return 1;}},
    REJECTED("Không được chấp nhận"){@Override public int getValue(){return 0;}};

    String dayOffEnum;

    DayOffEnum(String dayOffEnum){
        this.dayOffEnum = dayOffEnum;
    }

    public String getDayOffEnum() {
        return dayOffEnum;
    }

    public static String getValueEnumToShow(int status) {
        switch (status) {
            case -1:
                return WAITING.getDayOffEnum();
            case 0:
                return REJECTED.getDayOffEnum();
            case 1:
                return APPROVED.getDayOffEnum();
            default:
                throw new AssertionError("Unknown operations ");
        }
    }

    public abstract int getValue();
}
