package com.orca.dot.interfaces;

import com.orca.dot.model.User;

/**
 * Created by amit on 30/11/16.
 */

public interface OnQueryListener<T> {
        void onSuccess(T t);
}
