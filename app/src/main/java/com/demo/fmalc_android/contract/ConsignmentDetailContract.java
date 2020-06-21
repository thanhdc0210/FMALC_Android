package com.demo.fmalc_android.contract;


import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.Place;

import java.util.List;

public interface ConsignmentDetailContract {
    interface View{
        void findByConsignmentIdSuccess(ConsignmentDetail consignmentDetail);
        void findByConsignmentIdFailure(String message);
    }

    interface Presenter{
        void findByConsignmentId(Integer id);
    }
}
