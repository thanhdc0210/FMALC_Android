package com.demo.fmalc_android.enumType;

public enum NotificationTypeEnum {

    ODD_HOURS_ALERTS("Chạy xe ngoài giờ làm việc") {
        @Override
        public int getValue() {
            return 0;
        }
    },
    LONG_IDLE_TIMES("Dừng xe quá lâu") {
        @Override
        public int getValue() {
            return 1;
        }
    },
    MAINTAIN_SCHEDULE("Lịch bảo trì") {
        @Override
        public int getValue() {
            return 2;
        }
    },
    TASK_SCHEDULE("Lịch chạy") {
        @Override
        public int getValue() {
            return 3;
        }
    },
    DAY_OFF_BY_SCHEDULE("Xin nghỉ phép") {
        @Override
        public int getValue() {
            return 4;
        }
    },
    DAY_OFF_UNEXPECTED("Xin nghỉ đột xuất") {
        @Override
        public int getValue() {
            return 5;
        }
    },

    ALERT("Cảnh báo"){
        @Override
        public int getValue(){return 6;}
    },
    APPROVED("Được chấp nhận"){
        @Override
        public int getValue(){return 7;}
    },
    REJECTED("Bị từ chối"){
        @Override
        public int getValue(){return 8;}
    };

    String notificationTypeEnum;

    NotificationTypeEnum(String notificationTypeEnum) {
        this.notificationTypeEnum = notificationTypeEnum;
    }

    public String getNotificationTypeEnum() {
        return notificationTypeEnum;
    }

    public abstract int getValue();

    public static String getValueEnumToShow(Integer type) {
        switch (type) {
            case 0:
                return ODD_HOURS_ALERTS.getNotificationTypeEnum();
            case 1:
                return LONG_IDLE_TIMES.getNotificationTypeEnum();
            case 2:
                return MAINTAIN_SCHEDULE.getNotificationTypeEnum();
            case 3:
                return TASK_SCHEDULE.getNotificationTypeEnum();
            case 4:
                return DAY_OFF_BY_SCHEDULE.getNotificationTypeEnum();
            case 5:
                return DAY_OFF_UNEXPECTED.getNotificationTypeEnum();
            case 6:
                return ALERT.getNotificationTypeEnum();

            case 7:
                return APPROVED.getNotificationTypeEnum();
            case 8:
                return REJECTED.getNotificationTypeEnum();

            default:
                throw new AssertionError("Unknown operations");
        }
    }

    public static Integer getValueNumber(String type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getNotificationTypeEnum() == type) {
                return notificationTypeEnum.getValue();
            }
        }
        return -1;
    }

}
