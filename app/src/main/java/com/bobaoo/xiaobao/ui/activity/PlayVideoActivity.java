package com.bobaoo.xiaobao.ui.activity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;

/**
 * Created by Administrator on 2015/8/31.
 */
public class PlayVideoActivity extends BaseActivity {

    private VideoView mVideoView;
    private String mVideoURL;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void getIntentData() {
        mVideoURL = getIntent().getStringExtra(IntentConstant.ORDER_VIDEO_URL);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void initContent() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setVideoPath(mVideoURL);
        mVideoView.setMediaController(new MediaController(mContext));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
