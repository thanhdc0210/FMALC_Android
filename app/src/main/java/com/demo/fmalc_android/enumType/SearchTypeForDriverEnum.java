package com.demo.fmalc_android.enumType;

public enum SearchTypeForDriverEnum {
    CONSIGNMENT_ID(0),
    LICENSE_PLATE(1),
    OWNER_NAME(2);

    Integer searchType;

    SearchTypeForDriverEnum(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getSearchType() {
        return searchType;
    }
    public int getValue() {
        return searchType;
    }

    public static Integer getValueEnumToShow(Integer type) {
        switch (type) {
            case 0:
                return CONSIGNMENT_ID.getSearchType();
            case 1:
                return LICENSE_PLATE.getSearchType();
            case 2:
                return OWNER_NAME.getSearchType();
            default:
                throw new AssertionError("Unknown operations");
        }
    }
}
