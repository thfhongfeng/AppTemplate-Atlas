package com.pine.demo.novice_guide;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.pine.base.component.novice_guide.NoviceGuideItemView;
import com.pine.base.component.novice_guide.NoviceGuideView;
import com.pine.demo.R;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class DemoNoviceGuideActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_home_novice_guide);

        initNoviceGuide();
    }

    private void initNoviceGuide() {
        ArrayList<NoviceGuideView.GuideBean> list = new ArrayList<>();
        NoviceGuideView.GuideBean bean = new NoviceGuideView.GuideBean();
        bean.setName("Guide 1");
        bean.setDescImageViewResId(R.mipmap.demo_iv_guide_1);
        bean.setGuideViewLocation(NoviceGuideView.GuideBean.LOCATION_ON_BOTTOM_RIGHT);
        bean.setGuideViewOffsetMarginX(-getResources().getDimensionPixelOffset(R.dimen.dp_15));
        bean.setTargetView(findViewById(R.id.guide_1_tv));
        list.add(bean);
        bean = new NoviceGuideView.GuideBean();
        bean.setName("Guide 2");
        bean.setActionImageViewResId(R.mipmap.demo_iv_guide_2);
        bean.setGuideViewLocation(NoviceGuideView.GuideBean.LOCATION_ON_BOTTOM_CENTER);
        bean.setTargetView(findViewById(R.id.guide_2_tv));
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName("translationY");
        animator.setFloatValues(0, getResources().getDimensionPixelOffset(R.dimen.dp_15), 0);
        animator.setDuration(800);
        animator.setRepeatCount(3);
        bean.setAnimator(animator);
        bean.setHasHole(false);
        bean.setBgColor(Color.parseColor("#00FFFFFF"));
        list.add(bean);
        bean = new NoviceGuideView.GuideBean();
        bean.setName("Guide 3");
        bean.setDescImageViewResId(R.mipmap.demo_iv_guide_3);
        bean.setGuideViewLocation(NoviceGuideView.GuideBean.LOCATION_ON_BOTTOM_LEFT);
        bean.setTargetView(findViewById(R.id.guide_3_tv));
        list.add(bean);
        bean = new NoviceGuideView.GuideBean();
        bean.setName("Guide 4");
        bean.setDescImageViewResId(R.mipmap.demo_iv_guide_4);
        bean.setGuideViewLocation(NoviceGuideView.GuideBean.LOCATION_ON_TOP_RIGHT);
        bean.setTargetView(findViewById(R.id.guide_4_tv));
        list.add(bean);
        NoviceGuideView.GuideBean actionBean = new NoviceGuideView.GuideBean();
        actionBean.setName("Guide 5");
        actionBean.setDescImageViewResId(R.mipmap.demo_iv_guide_5_desc);
        actionBean.setActionImageViewResId(R.mipmap.demo_iv_guide_5_action);
        actionBean.setDescToActionViewGapPx(getResources().getDimensionPixelOffset(R.dimen.dp_30));
        actionBean.setListener(new NoviceGuideView.GuideBean.IDoActionListener() {
            @Override
            public void doAction(NoviceGuideItemView view, int position) {
                Toast.makeText(DemoNoviceGuideActivity.this, "点击了登陆", Toast.LENGTH_SHORT).show();
            }
        });
        list.add(actionBean);
        ((NoviceGuideView) findViewById(R.id.novice_guide_view)).init(list);
    }
}
