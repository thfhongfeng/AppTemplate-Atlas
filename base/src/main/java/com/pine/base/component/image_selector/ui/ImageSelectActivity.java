package com.pine.base.component.image_selector.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.component.image_selector.ImageSelector;
import com.pine.base.component.image_selector.ImageViewer;
import com.pine.base.component.image_selector.OnBackPressedListener;
import com.pine.base.component.image_selector.bean.ImageFolderBean;
import com.pine.base.component.image_selector.bean.ImageItemBean;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.base.ui.BaseActionBarTextMenuActivity;
import com.pine.tool.util.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@PermissionsAnnotation(Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE})
public class ImageSelectActivity extends BaseActionBarTextMenuActivity {
    public static final int DEFAULT_MAX_IMAGE_COUNT = 5;
    private static final int TAKE_PICTURE = 520;
    private static final int PREVIEW_PICTURE = 521;
    private final String TAG = LogUtils.makeLogTag(ImageSelectActivity.class);
    private GridView grid_view;
    private Button folder_select_btn, preview_btn;
    private ListView list_view;
    private TextView mMenuBtnTv;
    private int mMaxImgCount = DEFAULT_MAX_IMAGE_COUNT;
    private ArrayList<String> mSelectedImageList = new ArrayList<>();
    private String mCameraPath = null;
    private FolderAdapter mFolderAdapter;
    private ImageFolderBean mAllImageFolder, mCurrentImageFolder;
    private ContentResolver mContentResolver;
    private ImageAdapter mImageAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> mTmpDir;
    private ArrayList<ImageFolderBean> mDirPaths;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.base_activity_image_select;
    }

    @Override
    protected boolean initDataOnCreate() {
        if (getIntent().hasExtra(ImageSelector.INTENT_MAX_SELECTED_COUNT)) {
            mMaxImgCount = getIntent().getIntExtra(ImageSelector.INTENT_MAX_SELECTED_COUNT, DEFAULT_MAX_IMAGE_COUNT);
        }
        if (getIntent().hasExtra(ImageSelector.INTENT_SELECTED_IMAGE_LIST)) {
            mSelectedImageList = getIntent().getStringArrayListExtra(ImageSelector.INTENT_SELECTED_IMAGE_LIST);
        }
        if (mTmpDir == null) {
            mTmpDir = new HashMap<>();
        }
        if (mDirPaths == null) {
            mDirPaths = new ArrayList<>();
        }
        return false;
    }

    @Override
    protected void initViewOnCreate() {
        grid_view = findViewById(R.id.grid_view);
        folder_select_btn = findViewById(R.id.folder_select_btn);
        preview_btn = findViewById(R.id.preview_btn);
        list_view = findViewById(R.id.list_view);
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mMenuBtnTv = menuBtnTv;
        titleTv.setText(R.string.base_image_select);
        menuBtnTv.setText(getString(R.string.base_done) + "0/" + mMaxImgCount);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    protected void onAllAccessRestrictionReleased() {
        mContentResolver = getContentResolver();
        mAllImageFolder = new ImageFolderBean();
        mAllImageFolder.setDir("/" + getString(R.string.base_all_image));
        mCurrentImageFolder = mAllImageFolder;
        mDirPaths.add(mAllImageFolder);

        mImageAdapter = new ImageAdapter();
        grid_view.setAdapter(mImageAdapter);
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    goCamera();
                }
            }
        });
        mFolderAdapter = new FolderAdapter();
        list_view.setAdapter(mFolderAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentImageFolder = mDirPaths.get(position);
                hideListAnimation();
                mImageAdapter.notifyDataSetChanged();
                folder_select_btn.setText(mCurrentImageFolder.getName());
            }
        });
        folder_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_view.getVisibility() == View.VISIBLE) {
                    hideListAnimation();
                } else {
                    list_view.setVisibility(View.VISIBLE);
                    showListAnimation();
                    mFolderAdapter.notifyDataSetChanged();
                }
            }
        });
        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewer.create()
                        .count(mMaxImgCount)
                        .canSelect(true)
                        .selected(mSelectedImageList)
                        .position(0)
                        .start(ImageSelectActivity.this, PREVIEW_PICTURE);
            }
        });
        getThumbnail();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE && mCameraPath != null) {
                mSelectedImageList.add(mCameraPath);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCameraPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                Intent intent = new Intent();
                intent.putExtra(ImageSelector.INTENT_SELECTED_IMAGE_LIST, mSelectedImageList);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (requestCode == PREVIEW_PICTURE) {
                mSelectedImageList = data.getStringArrayListExtra(ImageViewer.INTENT_SELECTED_IMAGE_LIST);
                mImageAdapter.notifyDataSetChanged();
                if (data.getIntExtra(ImageViewer.INTENT_RETURN_TYPE, 0) < 0) {
                    mMenuBtnTv.performClick();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        boolean isHandled = false;
        for (int i = list.size() - 1; i >= 0; i--) {
            Fragment f = list.get(i);
            if (f != null && f instanceof OnBackPressedListener && f.isVisible()) {
                ((OnBackPressedListener) f).onBackPressed();
                isHandled = true;
                break; // only check the first not null (TOP fragment) and jump out
            }
        }
        if (!isHandled) {
            super.onBackPressed();
        }
    }

    private void goBack() {
        Intent data = new Intent();
        data.putExtra(ImageSelector.INTENT_SELECTED_IMAGE_LIST, mSelectedImageList);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    protected void goCamera() {
        if (mSelectedImageList.size() + 1 > mMaxImgCount) {
            Toast.makeText(this, "最多选择" + mMaxImgCount + "张", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 用于拍照时获取输出的Uri
     *
     * @return
     * @version 1.0
     * @author zyh
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtils.d(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        mCameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    private void checkSelected(String path, ImageView iv, boolean reverse) {
        if (mSelectedImageList.contains(path)) {
            if (reverse) {
                mSelectedImageList.remove(path);
                iv.setColorFilter(null);
            } else
                iv.setColorFilter(Color.parseColor("#77000000"));
        } else {
            if (reverse) {
                mSelectedImageList.add(path);
                iv.setColorFilter(Color.parseColor("#77000000"));
            } else
                iv.setColorFilter(null);
        }
        if (mSelectedImageList.size() == 0) {
            mMenuBtnTv.setEnabled(false);
            mMenuBtnTv.setText(getString(R.string.base_done));
            preview_btn.setEnabled(false);
            preview_btn.setText(getString(R.string.base_preview));
            preview_btn.setTextColor(Color.parseColor("#ff909090"));
        } else {
            mMenuBtnTv.setEnabled(true);
            mMenuBtnTv.setText(getString(R.string.base_done) + mSelectedImageList.size() + "/" + mMaxImgCount);
            preview_btn.setEnabled(true);
            preview_btn.setText(String.format(getString(R.string.base_preview_format), mSelectedImageList.size()));
            preview_btn.setTextColor(Color.parseColor("#ffffffff"));
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                mAllImageFolder.images.add(new ImageItemBean(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFolderBean imageFolder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!mTmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFolder = new ImageFolderBean();
                    imageFolder.setDir(dirPath);
                    imageFolder.setFirstImagePath(path);
                    mDirPaths.add(imageFolder);
                    mTmpDir.put(dirPath, mDirPaths.indexOf(imageFolder));
                } else {
                    imageFolder = mDirPaths.get(mTmpDir.get(dirPath));
                }
                imageFolder.images.add(new ImageItemBean(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        for (int i = 0; i < mDirPaths.size(); i++) {
            ImageFolderBean f = mDirPaths.get(i);
            LogUtils.d(TAG, i + "-----" + f.getName() + "---" + f.images.size());
        }
        mTmpDir = null;
    }

    public void showListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1,
                0f, 1, 1f, 1, 0f);
        ta.setDuration(200);
        list_view.startAnimation(ta);
    }

    public void hideListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1,
                0f, 1, 0f, 1, 1f);
        ta.setDuration(200);
        list_view.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                list_view.setVisibility(View.GONE);
            }
        });
    }

    class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mCurrentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(ImageSelectActivity.this, R.layout.base_item_image_select, null);
                holder = new ViewHolder();
                holder.iv = convertView.findViewById(R.id.iv);
                holder.checkBox = convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.iv.setImageResource(R.mipmap.base_iv_camera);
                holder.checkBox.setVisibility(View.INVISIBLE);
            } else {
                final int currPos = position - 1;
                holder.checkBox.setVisibility(View.VISIBLE);
                final ImageItemBean item = mCurrentImageFolder.images.get(currPos);
                ImageLoaderManager.getInstance().loadImage(ImageSelectActivity.this, "file://" + item.path, holder.iv);
                boolean isSelected = mSelectedImageList.contains(item.path);
                final ViewHolder finalHolder = holder;
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isSelected() && mSelectedImageList.size() + 1 > mMaxImgCount) {
                            Toast.makeText(ImageSelectActivity.this, "最多选择" + mMaxImgCount + "张", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        checkSelected(item.path, finalHolder.iv, true);
                        v.setSelected(mSelectedImageList.contains(item.path));
                    }
                });
                checkSelected(item.path, holder.iv, false);
                holder.checkBox.setSelected(isSelected);
                final ArrayList<ImageItemBean> allImages = mCurrentImageFolder.images;
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageViewer.create()
                                .count(mMaxImgCount)
                                .canSelect(true)
                                .selected(mSelectedImageList)
                                .position(currPos)
                                .start(ImageSelectActivity.this, PREVIEW_PICTURE);
                    }
                });
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv;
        Button checkBox;
    }

    class FolderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(ImageSelectActivity.this, R.layout.base_item_folder, null);
                holder = new FolderViewHolder();
                holder.image_iv = (ImageView) convertView.findViewById(R.id.image_iv);
                holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
                holder.count_tv = (TextView) convertView.findViewById(R.id.count_tv);
                holder.choose_iv = (ImageView) convertView.findViewById(R.id.choose_iv);
                convertView.setTag(holder);
            } else {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFolderBean item = mDirPaths.get(position);
            ImageLoaderManager.getInstance().loadImage(ImageSelectActivity.this, "file://" + item.getFirstImagePath(), holder.image_iv);
            holder.count_tv.setText(item.images.size() + "张");
            holder.name_tv.setText(item.getName());
            holder.choose_iv.setVisibility(mCurrentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    class FolderViewHolder {
        ImageView image_iv;
        ImageView choose_iv;
        TextView name_tv;
        TextView count_tv;
    }
}
