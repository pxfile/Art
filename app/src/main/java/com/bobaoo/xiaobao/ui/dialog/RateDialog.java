package com.bobaoo.xiaobao.ui.dialog;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

public class RateDialog extends BaseCustomerDialog {

    private Context mContext;
    private Activity mActivity;

    private View cancelView;
    private View badView;
    private View goodView;

    public RateDialog(Context context, Activity activity) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mContext = context;
        mActivity = activity;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_rate;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.tit_feed);
    }

    @Override
    protected void initView() {
        cancelView = findViewById(R.id.ll_cancel);
        badView = findViewById(R.id.ll_bad);
        goodView = findViewById(R.id.ll_good);

        TextView cancelTextView = (TextView) findViewById(R.id.tv_cancel);
        cancelTextView.setText(R.string.no_comments);
        TextView badTextView = (TextView) findViewById(R.id.tv_bad);
        badTextView.setText(R.string.stupid_app);
        TextView goodTextView = (TextView) findViewById(R.id.tv_good);
        goodTextView.setText(R.string.quite_nice);

        setOnClickListener(cancelView, badView, goodView);
    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.ll_bad) {
//            jump(mActivity, FeedBackActivity.class);
//            Map<String, Object> map = new HashMap<String, Object>();
//            // 添加评论
//            map.put(SharedPreferencesConstant.HAVE_CLICK_RATE_DIALOG, true);
//            // 记录到sharedPreference
//            SharedPreferencesUtils.save(mContext, map);
//            // 添加统计
//            UmengUtils.onEvent(mContext, EventEnum.ClickSendBad);
//            dismiss();
//        } else if (view.getId() == R.id.ll_good) {
//            ActivityUtils.jumpToMarketByOrder(mActivity, mContext.getPackageName());
//            Map<String, Object> map = new HashMap<String, Object>();
//            // 添加评论
//            map.put(SharedPreferencesConstant.HAVE_CLICK_RATE_DIALOG, true);
//            // 记录到sharedPreference
//            SharedPreferencesUtils.save(mContext, map);
//            // 添加统计
//            UmengUtils.onEvent(mContext, EventEnum.ClickSendGood);
//            dismiss();
//        } else if (view.getId() == R.id.ll_cancel) {
//            dismiss();
//        } else {
//        }
    }

}
