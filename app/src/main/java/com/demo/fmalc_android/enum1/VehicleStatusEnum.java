package com.demo.fmalc_android.enum1;


public enum VehicleStatusEnum {
    AVAILABLE("Đang rảnh"){@Override public int getValue(){return 0;}}, // Xe chưa xếp lịch chạy
    MAINTAINING("Đang bảo trì"){@Override public int getValue(){return 1;}},
    RUNNING("Đang chạy"){@Override public int getValue(){return 2;}},
    SOLD("Đã bán"){@Override public int getValue(){return 3;}},
    SCHEDULED("Có lịch chạy"){@Override public  int getValue(){return 4;}}; // Xe đã xếp lịch nhưng chưa chạy

    String vehicleStatusEnum;

    VehicleStatusEnum(String vehicleStatusEnum){
        this.vehicleStatusEnum = vehicleStatusEnum;
    }

    public String getVehicleStatusEnum() {
        return vehicleStatusEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return AVAILABLE.getVehicleStatusEnum();
            case 1:
                return MAINTAINING.getVehicleStatusEnum();
            case 2:
                return RUNNING.getVehicleStatusEnum();
            case 3:
                return SOLD.getVehicleStatusEnum();
            case 4:
                return SCHEDULED.getVehicleStatusEnum();
            default:throw new AssertionError("Unknown operations " );
        }
    }

}
