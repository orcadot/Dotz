package com.orca.dot.services;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.KeyValue;

import java.util.List;

/**
 * Created by amit on 26/10/16.
 */

public interface TabDataContracts {

    interface View extends BaseView<Presenter> {
        void populateTabData(List<KeyValue> tabData);
    }

    interface Presenter extends BasePresenter {

    }

}
