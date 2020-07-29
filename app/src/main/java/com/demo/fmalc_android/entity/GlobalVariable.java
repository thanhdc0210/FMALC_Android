package com.demo.fmalc_android.entity;

import android.app.Application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalVariable extends Application {
    private String token;
    private String username;
    private String role;
    private Integer id;
}
