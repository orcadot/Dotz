package com.orca.dot.services.cart;

import com.orca.dot.BasePresenter;
import com.orca.dot.BaseView;
import com.orca.dot.model.BaseModel;
import com.orca.dot.model.Style;

import java.util.List;

/**
 * Created by amit on 31/10/16.
 */

public interface CartContract {

    interface View extends BaseView<Presenter>{
        void showCartItems(List<? extends BaseModel> data);
    }

    interface Presenter extends BasePresenter{

        void favOrAddClicked(final Style style);
    }
}
