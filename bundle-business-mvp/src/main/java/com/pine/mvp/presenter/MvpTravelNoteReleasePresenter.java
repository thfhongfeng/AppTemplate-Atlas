package com.pine.mvp.presenter;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.contract.IMvpTravelNoteReleaseContract;
import com.pine.mvp.model.MvpTravelNoteModel;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpTravelNoteReleasePresenter extends BasePresenter<IMvpTravelNoteReleaseContract.Ui>
        implements IMvpTravelNoteReleaseContract.Presenter {
    private MvpTravelNoteModel mModel;
    private boolean mIsLoadProcessing;


    public MvpTravelNoteReleasePresenter() {
        mModel = new MvpTravelNoteModel();
    }

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @NonNull
    @Override
    public HashMap<String, String> makeUploadDefaultParams() {
        HashMap<String, String> params = new HashMap<>();
        // Test code begin
        params.put("bizType", "10");
        params.put("orderNum", "100");
        params.put("orderNum", "100");
        params.put("descr", "");
        params.put("fileType", "1");
        // Test code end
        return params;
    }

    @Override
    public void addNote() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();

        InputParamBean title = getUi().getNoteTitleParam("title");
        if (title.checkIsEmpty(R.string.mvp_note_release_title_need)) {
            return;
        } else {
            params.put("title", title.getValue());
        }

        InputParamBean setOutDate = getUi().getNoteSetOutDateParam("setOutDate");
        if (setOutDate.checkIsEmpty(R.string.mvp_note_release_set_out_date_need)) {
            return;
        } else {
            params.put("setOutDay", setOutDate.getValue());
        }

        InputParamBean dayCount = getUi().getNoteTravelDayCountParam("dayCount");
        if (dayCount.checkIsEmpty(R.string.mvp_note_release_day_count_need) ||
                !dayCount.checkNumberRange(R.string.mvp_note_release_day_count_incorrect,
                        1, Integer.MAX_VALUE)) {
            return;
        } else {
            params.put("dayCount", dayCount.getValue());
        }

        InputParamBean belongShops = getUi().getNoteBelongShopsParam("belongShops");
        if (belongShops.checkIsEmpty(R.string.mvp_note_release_belong_shops_need)) {
            return;
        } else {
            params.put("belongShops", belongShops.getValue());
        }

        InputParamBean preface = getUi().getNotePrefaceParam("preface");
        if (preface.checkIsEmpty(R.string.mvp_note_release_preface_need)) {
            return;
        } else {
            params.put("preface", preface.getValue());
        }

        params.put("content", getUi().getNoteContentParam("content").getValue());

        startDataLoadUi();
        if (!mModel.requestAddTravelNote(params, new IModelAsyncResponse<MvpTravelNoteDetailEntity>() {
            @Override
            public void onResponse(MvpTravelNoteDetailEntity entity) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    Toast.makeText(getContext(), R.string.mvp_note_release_success, Toast.LENGTH_SHORT).show();
                    finishUi();
                    return;
                }
            }

            @Override
            public boolean onFail(Exception e) {
                finishDataLoadUi();
                return false;
            }
        })) {
            finishDataLoadUi();
        }
    }

    private void startDataLoadUi() {
        mIsLoadProcessing = true;
        if (isUiAlive()) {
            getUi().setSwipeRefreshLayoutRefresh(true);
        }
    }

    private void finishDataLoadUi() {
        mIsLoadProcessing = false;
        if (isUiAlive()) {
            getUi().setSwipeRefreshLayoutRefresh(false);
        }
    }
}
