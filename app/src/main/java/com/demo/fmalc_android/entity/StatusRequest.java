package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest implements Serializable {

    @SerializedName("username")
    private String username;

    @SerializedName("status")
    private List<Integer> status;
}
