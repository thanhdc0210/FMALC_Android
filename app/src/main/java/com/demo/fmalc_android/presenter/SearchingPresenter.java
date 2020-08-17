package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.SearchingContract;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.enumType.SearchTypeForDriverEnum;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.SearchingService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingPresenter implements SearchingContract.Presenter {

    SearchingContract.View view;

    public void setView(SearchingContract.View view) {
        this.view = view;
    }

    SearchingService searchingService = NetworkingUtils.getSearchingService();



    @Override
    public void searchConsignment(SearchTypeForDriverEnum searchType, String searchValue,Integer driverId, String auth) {
        Call<List<Schedule>> call = searchingService.searchConsignment(searchType, searchValue,driverId, auth);
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if(!response.isSuccessful()){
                    view.searchConsignmentForFailure("Không tìm thấy lịch trình");
                }else if (response.code() == 200 || response.code() == 204){
                    List<Schedule> scheduleList = response.body();
                    view.searchConsignmentSuccessful(scheduleList);
                }
            }
            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.searchConsignmentForFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.searchConsignmentForFailure("Mất kết nối mạng");
                }else{
                    view.searchConsignmentForFailure("Xin thử lại sau ít phút");
                }
            }
        });
    }
}
