package com.pine.mvp.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.pine.base.BaseConstants;
import com.pine.base.bean.InputParamBean;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.component.editor.bean.EditorItemData;
import com.pine.base.component.editor.ui.TextImageEditorView;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.contract.IMvpTravelNoteReleaseContract;
import com.pine.mvp.model.MvpTravelNoteModel;
import com.pine.mvp.ui.activity.MvpShopSearchCheckActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpTravelNoteReleasePresenter extends BasePresenter<IMvpTravelNoteReleaseContract.Ui>
        implements IMvpTravelNoteReleaseContract.Presenter {
    public final int REQUEST_CODE_SELECT_BELONG_SHOP = 1;

    private MvpTravelNoteModel mModel;
    private boolean mIsLoadProcessing;
    private ArrayList<String> mBelongShopIdList;
    private ArrayList<String> mBelongShopNameList;

    public MvpTravelNoteReleasePresenter() {
        mModel = new MvpTravelNoteModel();
    }

    @Override
    public boolean parseIntentData() {
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @NonNull
    public UploadFileLinearLayout.OneByOneUploadAdapter getUploadAdapter() {
        return new UploadFileLinearLayout.OneByOneUploadAdapter() {

            @Override
            public String getFileKey(FileUploadBean fileUploadBean) {
                // Test code begin
                return "file";
                // Test code end
            }

            @Override
            public Map<String, String> getUploadParam(FileUploadBean fileUploadBean) {
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
            public String getRemoteUrlFromResponse(FileUploadBean fileUploadBean, JSONObject response) {
                // Test code begin
                if (response == null) {
                    return null;
                }
                if (!response.optBoolean(BaseConstants.SUCCESS)) {
                    return null;
                }
                JSONObject data = response.optJSONObject(BaseConstants.DATA);
                if (data == null) {
                    return null;
                }
                return data.optString("fileUrl");
                // Test code end
            }
        };
    }

    @Override
    public void selectBelongShop() {
        Intent intent = new Intent(getContext(), MvpShopSearchCheckActivity.class);
        intent.putStringArrayListExtra(MvpShopSearchCheckPresenter.REQUEST_CHECKED_IDS_KEY, mBelongShopIdList);
        getActivity().startActivityForResult(intent, REQUEST_CODE_SELECT_BELONG_SHOP);
    }

    @Override
    public void onBelongShopSelected(Intent data) {
        mBelongShopIdList = data.getStringArrayListExtra(MvpShopSearchCheckPresenter.RESULT_CHECKED_IDS_KEY);
        mBelongShopNameList = data.getStringArrayListExtra(MvpShopSearchCheckPresenter.RESULT_CHECKED_NAMES_KEY);
        if (mBelongShopIdList != null && mBelongShopNameList != null &&
                mBelongShopIdList.size() == mBelongShopNameList.size() && mBelongShopIdList.size() > 0) {
            String ids = mBelongShopIdList.get(0);
            String names = mBelongShopNameList.get(0);
            for (int i = 1; i < mBelongShopIdList.size(); i++) {
                ids += "," + mBelongShopIdList.get(i);
                names += "," + mBelongShopNameList.get(i);
            }
            getUi().setBelongShop(ids, names);
        }
    }

    @Override
    public void addNote() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();

        InputParamBean<String> title = getUi().getNoteTitleParam("title");
        if (title.checkIsEmpty(R.string.mvp_note_release_title_need)) {
            return;
        } else {
            params.put(title.getKey(), title.getValue());
        }

        InputParamBean<String> setOutDate = getUi().getNoteSetOutDateParam("setOutDate");
        if (setOutDate.checkIsEmpty(R.string.mvp_note_release_set_out_date_need)) {
            return;
        } else {
            params.put(setOutDate.getKey(), setOutDate.getValue());
        }

        InputParamBean<String> dayCount = getUi().getNoteTravelDayCountParam("dayCount");
        if (dayCount.checkIsEmpty(R.string.mvp_note_release_day_count_need) ||
                !dayCount.checkNumberRange(R.string.mvp_note_release_day_count_incorrect,
                        1, Integer.MAX_VALUE)) {
            return;
        } else {
            params.put(dayCount.getKey(), dayCount.getValue());
        }

        InputParamBean<String> belongShops = getUi().getNoteBelongShopsParam("belongShops");
        if (belongShops.checkIsEmpty(R.string.mvp_note_release_belong_shops_need)) {
            return;
        } else {
            params.put(belongShops.getKey(), belongShops.getValue());
        }

        InputParamBean<String> belongShopNames = getUi().getNoteBelongShopNamesParam("belongShopNames");
        if (belongShops.checkIsEmpty(R.string.mvp_note_release_belong_shops_need)) {
            return;
        } else {
            params.put(belongShopNames.getKey(), belongShopNames.getValue());
        }

        InputParamBean<String> preface = getUi().getNotePrefaceParam("preface");
        if (preface.checkIsEmpty(R.string.mvp_note_release_preface_need)) {
            return;
        } else {
            params.put(preface.getKey(), preface.getValue());
        }

        InputParamBean<List<List<EditorItemData>>> contentBean = getUi().getNoteContentParam("content");
        if (contentBean.checkIsEmpty(R.string.mvp_note_release_note_content_need)) {
            return;
        } else {
            JSONArray contentArr = new JSONArray();
            try {
                for (int i = 0; i < contentBean.getValue().size(); i++) {
                    List<EditorItemData> dayContentList = contentBean.getValue().get(i);
                    if (dayContentList == null || dayContentList.size() < 1) {
                        contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_need);
                        return;
                    }
                    JSONArray dayContentArr = new JSONArray();
                    for (int j = 0; j < dayContentList.size(); j++) {
                        EditorItemData itemData = dayContentList.get(j);
                        switch (itemData.getType()) {
                            case TextImageEditorView.TYPE_TEXT:
                                if (TextUtils.isEmpty(itemData.getText())) {
                                    contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_text_need);
                                    return;
                                }
                                break;
                            case TextImageEditorView.TYPE_IMAGE:
                                if (TextUtils.isEmpty(itemData.getRemoteFilePath())) {
                                    contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_image_need);
                                    return;
                                }
                                break;
                            default:
                                contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_content_incorrect);
                                return;
                        }
                        JSONObject object = new JSONObject();
                        object.put("type", itemData.getType());
                        object.put("text", itemData.getText());
                        object.put("imgUrl", itemData.getRemoteFilePath());
                        dayContentArr.put(object);
                    }
                    contentArr.put(dayContentArr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put(contentBean.getKey(), contentArr.toString());
        }

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
