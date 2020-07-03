package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {
    @SerializedName("token")
    private String token;
    @SerializedName("username")
    private String username;
    @SerializedName("role")
    private String role;
}
