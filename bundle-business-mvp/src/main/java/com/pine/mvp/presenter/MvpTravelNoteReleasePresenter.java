package com.pine.mvp.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.pine.base.BaseConstants;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.BaseInputParam;
import com.pine.base.component.editor.bean.TextImageEditorItemData;
import com.pine.base.component.editor.bean.TextImageItemEntity;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.contract.IMvpTravelNoteReleaseContract;
import com.pine.mvp.model.IMvpTravelNoteModel;
import com.pine.mvp.model.MvpModelFactory;
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
    private IMvpTravelNoteModel mTravelNoteModel;
    private ArrayList<MvpShopItemEntity> mBelongShopList;

    public MvpTravelNoteReleasePresenter() {
        mTravelNoteModel = MvpModelFactory.getMvpTravelNoteModel();
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
        intent.putParcelableArrayListExtra(MvpShopSearchCheckPresenter.REQUEST_CHECKED_LIST_KEY, mBelongShopList);
        getActivity().startActivityForResult(intent, REQUEST_CODE_SELECT_BELONG_SHOP);
    }

    @Override
    public void onBelongShopSelected(Intent data) {
        mBelongShopList = data.getParcelableArrayListExtra(MvpShopSearchCheckPresenter.RESULT_CHECKED_LIST_KEY);
        String ids = "";
        String names = "";
        if (mBelongShopList != null) {
            for (MvpShopItemEntity entity : mBelongShopList) {
                ids += entity.getId() + ",";
                names += entity.getName() + ",";
            }
        }
        if (!TextUtils.isEmpty(ids) && ids.lastIndexOf(",") == ids.length() - 1) {
            ids = ids.substring(0, ids.length() - 1);
            names = names.substring(0, names.length() - 1);
        }
        getUi().setBelongShop(ids, names);
    }

    @Override
    public void addNote() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();

        BaseInputParam<String> title = getUi().getNoteTitleParam("title");
        if (title.checkIsEmpty(R.string.mvp_note_release_title_need)) {
            return;
        } else {
            params.put(title.getKey(), title.getValue());
        }

        BaseInputParam<String> setOutDate = getUi().getNoteSetOutDateParam("setOutDate");
        if (setOutDate.checkIsEmpty(R.string.mvp_note_release_set_out_date_need)) {
            return;
        } else {
            params.put(setOutDate.getKey(), setOutDate.getValue());
        }

        BaseInputParam<String> dayCount = getUi().getNoteTravelDayCountParam("dayCount");
        if (dayCount.checkIsEmpty(R.string.mvp_note_release_day_count_need) ||
                !dayCount.checkNumberRange(R.string.mvp_note_release_day_count_incorrect,
                        1, Integer.MAX_VALUE)) {
            return;
        } else {
            params.put(dayCount.getKey(), dayCount.getValue());
        }

        BaseInputParam<MvpShopItemEntity> belongShops = getUi().getNoteBelongShopsParam("belongShops", mBelongShopList);
        if (belongShops.checkIsEmpty(R.string.mvp_note_release_belong_shops_need)) {
            return;
        } else {
            params.put(belongShops.getKey(), new Gson().toJson(belongShops.getValue()));
        }

        BaseInputParam<String> preface = getUi().getNotePrefaceParam("preface");
        if (preface.checkIsEmpty(R.string.mvp_note_release_preface_need)) {
            return;
        } else {
            params.put(preface.getKey(), preface.getValue());
        }

        BaseInputParam<List<List<TextImageEditorItemData>>> contentBean = getUi().getNoteContentParam("content");
        if (contentBean.checkIsEmpty(R.string.mvp_note_release_note_content_need)) {
            return;
        } else {
            JSONArray contentArr = new JSONArray();
            try {
                for (int i = 0; i < contentBean.getValue().size(); i++) {
                    List<TextImageEditorItemData> dayContentList = contentBean.getValue().get(i);
                    if (dayContentList == null || dayContentList.size() < 1) {
                        contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_need);
                        return;
                    }
                    JSONArray dayContentArr = new JSONArray();
                    for (int j = 0; j < dayContentList.size(); j++) {
                        TextImageEditorItemData itemData = dayContentList.get(j);
                        switch (itemData.getType()) {
                            case TextImageItemEntity.TYPE_TEXT:
                                if (TextUtils.isEmpty(itemData.getText())) {
                                    contentBean.toastAndTryScrollTo(R.string.mvp_note_release_day_note_text_need);
                                    return;
                                }
                                break;
                            case TextImageItemEntity.TYPE_IMAGE:
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
        setUiLoading(true);
        mTravelNoteModel.requestAddTravelNote(params, new IModelAsyncResponse<MvpTravelNoteDetailEntity>() {
            @Override
            public void onResponse(MvpTravelNoteDetailEntity entity) {
                setUiLoading(false);
                showShortToast(R.string.mvp_note_release_success);
                finishUi();
            }

            @Override
            public boolean onFail(Exception e) {
                setUiLoading(false);
                return false;
            }

            @Override
            public void onCancel() {
                setUiLoading(false);
            }
        });
    }
}
