package com.demo.fmalc_android.enum1;
public enum ConsignmentStatusEnum {
    WAITING("Đang chờ xử lý"){@Override public int getValue(){return 0;}},
    OBTAINING("Đang lấy hàng"){@Override public int getValue(){return 1;}},
    DELIVERING("Đang giao hàng"){@Override public int getValue(){return 2;}},
    COMPLETED("Hoàn thành"){@Override public int getValue(){return 3;}},
    CANCELED("Bị hủy"){@Override public int getValue(){return 4;}},
    MISSING_DOCUMENT("Thiếu chứng từ"){@Override public int getValue(){return 5;}},
    PENDING("Tạm hoãn"){@Override public int getValue(){return 6;}};

    String consignmentStatusEnum;

    ConsignmentStatusEnum(String consignmentStatusEnum){
        this.consignmentStatusEnum = consignmentStatusEnum;
    }

    public String getConsignmentStatusEnum() {
        return consignmentStatusEnum;
    }

    public static String getValueEnumToShow(int status){
        switch (status){
            case 0:
                return WAITING.getConsignmentStatusEnum();
            case 1:
                return OBTAINING.getConsignmentStatusEnum();
            case 2:
                return DELIVERING.getConsignmentStatusEnum();
            case 3:
                return COMPLETED.getConsignmentStatusEnum();
            case 4:
                return CANCELED.getConsignmentStatusEnum();
            case 5:
                return MISSING_DOCUMENT.getConsignmentStatusEnum();
            case 6:
                return PENDING.getConsignmentStatusEnum();
            default:
                throw new AssertionError("Unknown operations ");
        }
    }

    public abstract int getValue();
}
