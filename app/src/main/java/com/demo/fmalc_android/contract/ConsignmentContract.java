package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.StatusRequest;

import java.util.List;

public interface ConsignmentContract {
    interface View{
        void findByConsignmentStatusAndUsernameForFleetManagerSuccess(List<Consignment> consignmentList);
        void findByConsignmentStatusAndUsernameForFleetManagerFailure(String message);
    }

    interface Presenter{
        void findByConsignmentStatusAndUsernameForFleetManager(StatusRequest statusRequest);
    }
}
