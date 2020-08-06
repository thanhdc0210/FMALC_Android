package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.enumType.SearchTypeForDriverEnum;

import java.util.List;

public interface SearchingContract {
    interface View {
        void searchConsignmentSuccessful(List<Schedule> scheduleList);
        void searchConsignmentForFailure(String message);
    }

    interface Presenter {
        void searchConsignment(SearchTypeForDriverEnum searchType, String searchValue, String auth);
    }
}
