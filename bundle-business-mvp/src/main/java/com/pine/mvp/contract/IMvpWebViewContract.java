package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.component.share.bean.ShareBean;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpWebViewContract {
    interface Ui extends IBaseContract.Ui {
        void loadUrl(String url);
    }

    interface Presenter extends IBaseContract.Presenter {
        String getH5Url();

        ArrayList<ShareBean> getShareBeanList();
    }
}
