package com.bobaoo.xiaobao.wxapi;

import com.bobaoo.xiaobao.utils.UmengUtils;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by chenming on 2015/5/25.
 */
public class WXEntryActivity extends WXCallbackActivity {
    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }
}

