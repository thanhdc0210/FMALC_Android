package com.demo.fmalc_android.directionhelpers;

/**
 * Created by Vishal on 10/20/2018.
 */

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void onDistanceDone(Integer... meter);
}
