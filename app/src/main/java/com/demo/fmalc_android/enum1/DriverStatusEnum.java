package com.demo.fmalc_android.enum1;

public enum DriverStatusEnum {
    AVAILABLE("Đang rảnh"){@Override public int getValue(){return 0;}},
    RUNNING("Đang chạy"){@Override public int getValue(){return 1;}},
    UNAVAILABLE("Xin nghỉ phép"){@Override public int getValue(){return 2;}},
    SCHEDULED("Có lịch chạy"){@Override public int getValue(){return 3;}},
    MAINTAIN("Có lịch bảo trì"){@Override public int getValue(){return 4;}};


    String driverStatusEnum;

    DriverStatusEnum(String driver_status_enum) {
        this.driverStatusEnum = driver_status_enum;
    }

    public String getDriverStatusEnum(){
        return driverStatusEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return AVAILABLE.getDriverStatusEnum();
            case 1:
                return RUNNING.getDriverStatusEnum();
            case 2:
                return UNAVAILABLE.getDriverStatusEnum();
            case 3:
                return SCHEDULED.getDriverStatusEnum();
            case 4:
                return MAINTAIN.getDriverStatusEnum();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
