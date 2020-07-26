package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.StatusRequest;

import java.util.List;

public interface ConsignmentContract {
    interface View{
        void findByConsignmentStatusAndUsernameForSuccess(List<Consignment> consignmentList);
        void findByConsignmentStatusAndUsernameForFailure(String message);
    }

    interface Presenter{
        void findByConsignmentStatusAndUsername(List<Integer> status, String username);
    }
}
