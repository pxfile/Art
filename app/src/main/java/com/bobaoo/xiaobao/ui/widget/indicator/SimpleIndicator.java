package com.bobaoo.xiaobao.ui.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

/**
 * Created by kakaxicm on 2015/11/30.
 */
public class SimpleIndicator extends LinearLayout {
    private Paint mPaint;//画指示符的paint
    //指示符的rect
    private int mTop;
    private int mLeft;
    private int mWidth;

    private int mChildCount;
    private final float INDICATOR_WIDTH_RATIO = 0.7f;
    private int mIndicateHeight = 5;//indicator高度
    private int mIndicateColor;//indicator颜色
    private int mNormalTextColor;//normal 文本颜色
    private int mSelectedTextColor;//选中文本颜色

    private ViewPager mViewPager;
    private int mTabWidth;
    private int mSelectedIndex;

    public SimpleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.TRANSPARENT);// 必须设置背景，否则onDraw不执行

        mIndicateColor = getResources().getColor(R.color.orange0);
        mPaint = new Paint();
        mPaint.setColor(mIndicateColor);
        mPaint.setAntiAlias(true);

        mNormalTextColor = getResources().getColor(R.color.black1);
        mSelectedTextColor = getResources().getColor(R.color.orange0);
    }

    //关联ViewPager
    public void setViewPager(ViewPager vp) {
        mViewPager = vp;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onScroll(position, positionOffset);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //设置文本颜色
                mSelectedIndex = position;
                highLightTextViewsColor(position);
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }

    private void highLightTextViewsColor(int pos) {
        for (int i = 0; i < mChildCount; i++) {
            TextView tv = (TextView) getChildAt(i);
            if (i == pos) {
                tv.setTextColor(mSelectedTextColor);
            } else {
                tv.setTextColor(mNormalTextColor);
            }
        }
    }

    public void setTabTitles(String[] titles) {
        if (titles != null && titles.length > 0) {
            removeAllViews();
            for (int i = 0; i < titles.length; i++) {
                String s = titles[i];
                TextView tv = new TextView(getContext());
                tv.setText(s);
                if (i == mSelectedIndex) {
                    tv.setTextColor(mSelectedTextColor);
                } else {
                    tv.setTextColor(mNormalTextColor);
                }

                tv.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                addView(tv, lp);
            }
            setItemClickEvent();
        }
    }

    //对外暴露pager监听
    public interface PagerChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private PagerChangeListener mOnPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PagerChangeListener pageChangeListener) {
        mOnPageChangeListener = pageChangeListener;
    }

    /**
     * 设置点击事件
     */
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChildCount = getChildCount();
        if (mChildCount == 0) return;
        mTabWidth = w / mChildCount;
        mTop = h - mIndicateHeight;
        mWidth = (int) (INDICATOR_WIDTH_RATIO * mTabWidth);
        mLeft = (int) ((1 - INDICATOR_WIDTH_RATIO) * mTabWidth / 2);
        highLightTextViewsColor(mSelectedIndex);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(mLeft, mTop, mLeft + mWidth, mTop + mIndicateHeight);
        canvas.drawRect(rect, mPaint);
        super.onDraw(canvas);
    }

    public void onScroll(int position, float offset) {
        mLeft = (int) ((position + offset) * mTabWidth + (1 - INDICATOR_WIDTH_RATIO) * mTabWidth / 2);
        invalidate();
    }
}
