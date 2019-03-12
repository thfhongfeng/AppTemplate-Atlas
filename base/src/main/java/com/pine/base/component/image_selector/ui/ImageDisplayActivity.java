package com.pine.base.component.image_selector.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.component.image_selector.ImageViewer;
import com.pine.base.component.image_selector.bean.ImageItemBean;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.base.ui.BaseActionBarTextMenuActivity;
import com.pine.tool.widget.ZoomImageView;

import java.util.ArrayList;

@PermissionsAnnotation(Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE})
public class ImageDisplayActivity extends BaseActionBarTextMenuActivity {
    private ViewPager view_pager;
    private TextView choose_tv;
    private Button check_btn;
    private TextView mTitleTv, mMenuBtnTv;
    private ArrayList<ImageItemBean> mImageBeanList;
    private ViewPagerAdapter mAdapter;
    private ArrayList<String> mSelectedImageList = new ArrayList<>();
    private int mCurPosition;
    private int mMaxImgCount;
    private boolean mCanSelected;
    private boolean mNoImageList;
    private ContentResolver mContentResolver;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.base_activity_image_display;
    }

    @Override
    protected void findViewOnCreate() {
        view_pager = findViewById(R.id.view_pager);
        choose_tv = findViewById(R.id.choose_tv);
        check_btn = findViewById(R.id.check_btn);
    }

    @Override
    protected boolean parseIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ImageViewer.INTENT_IMAGE_BEAN_LIST)) {
            mImageBeanList = (ArrayList<ImageItemBean>) intent.getSerializableExtra(ImageViewer.INTENT_IMAGE_BEAN_LIST);
        } else if (ImageViewer.mBigOriginBeanData != null) {
            mImageBeanList = ImageViewer.mBigOriginBeanData;
            ImageViewer.mBigOriginBeanData = null;
        } else {
            mNoImageList = true;
        }
        mCurPosition = intent.getIntExtra(ImageViewer.INTENT_CUR_POSITION, 0);
        mSelectedImageList = intent.getStringArrayListExtra(ImageViewer.INTENT_SELECTED_IMAGE_LIST);
        if (mSelectedImageList == null) {
            mSelectedImageList = new ArrayList<>();
        }
        mMaxImgCount = intent.getIntExtra(ImageViewer.INTENT_MAX_SELECTED_COUNT,
                ImageSelectActivity.DEFAULT_MAX_IMAGE_COUNT);
        mCanSelected = intent.getBooleanExtra(ImageViewer.INTENT_CAN_SELECT, false);
        return false;
    }

    @Override
    protected void init() {
        mContentResolver = getContentResolver();
        if (mNoImageList) {
            getThumbnail();
        }
        if (mImageBeanList.size() < 1) {
            finish();
            return;
        }
        mCurPosition = mCurPosition < mImageBeanList.size() ? mCurPosition : 0;
        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        view_pager.setCurrentItem(mCurPosition);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTitleTv.setText(String.format("%1$s/%2$s", position + 1, mImageBeanList.size()));
                ImageItemBean item = mImageBeanList.get(view_pager.getCurrentItem());
                boolean isSelected = mSelectedImageList.contains(item.path);
                check_btn.setSelected(isSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mCanSelected) {
            check_btn.setSelected(mSelectedImageList.contains(mImageBeanList.get(mCurPosition).path));
            check_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageItemBean item = mImageBeanList.get(view_pager.getCurrentItem());
                    boolean isSelected = mSelectedImageList.contains(item.path);
                    if (isSelected) {
                        mSelectedImageList.remove(item.path);
                    } else {
                        if (mSelectedImageList.size() >= mMaxImgCount) {
                            Toast.makeText(ImageDisplayActivity.this,
                                    "最多选择" + mMaxImgCount + "张", Toast.LENGTH_SHORT).show();
                            check_btn.setSelected(false);
                            return;
                        }
                        mSelectedImageList.add(item.path);
                    }
                    check_btn.setSelected(!isSelected);
                    updateDoneButton();
                }
            });
            choose_tv.setVisibility(View.VISIBLE);
            check_btn.setVisibility(View.VISIBLE);
        } else {
            choose_tv.setVisibility(View.GONE);
            check_btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        mTitleTv = titleTv;
        mMenuBtnTv = menuBtnTv;
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(ImageViewer.INTENT_SELECTED_IMAGE_LIST, mSelectedImageList);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        titleTv.setText(String.format("%1$s/%2$s", mCurPosition + 1, mImageBeanList.size()));
        if (mCanSelected) {
            updateDoneButton();
            menuBtnTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goComplete();
                }
            });
            menuBtnTv.setVisibility(View.VISIBLE);
        } else {
            menuBtnTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        ImageViewer.mBigOriginBeanData = null;
        super.onDestroy();
    }

    private void goComplete() {
        Intent data = new Intent();
        data.putExtra(ImageViewer.INTENT_SELECTED_IMAGE_LIST, mSelectedImageList);
        data.putExtra(ImageViewer.INTENT_RETURN_TYPE, -1);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (mCursor.moveToFirst()) {
            mImageBeanList = new ArrayList<>();
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                mImageBeanList.add(new ImageItemBean(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }

    private void updateDoneButton() {
        if (mSelectedImageList.size() == 0) {
            mMenuBtnTv.setEnabled(false);
            mMenuBtnTv.setText(getString(R.string.base_done));
        } else {
            mMenuBtnTv.setEnabled(true);
            mMenuBtnTv.setText(getString(R.string.base_done) + mSelectedImageList.size() + "/" + mMaxImgCount);
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(ImageViewer.INTENT_SELECTED_IMAGE_LIST, mSelectedImageList);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    /**
     * ViewPager的适配器
     */
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZoomImageView zoomImageView = new ZoomImageView(ImageDisplayActivity.this);
            String path = mImageBeanList.get(position).path;
            if (!TextUtils.isEmpty(path) && path.startsWith("http")) {
                ImageLoaderManager.getInstance().loadImage(ImageDisplayActivity.this,
                        path, zoomImageView);
            } else {
                ImageLoaderManager.getInstance().loadImage(ImageDisplayActivity.this,
                        "file://" + path, zoomImageView);
            }
            zoomImageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            container.addView(zoomImageView);
            return zoomImageView;
        }

        @Override
        public int getCount() {
            return mImageBeanList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(Object object) {
            //return super.getItemPosition(object);
            return POSITION_NONE;
        }
    }
}
