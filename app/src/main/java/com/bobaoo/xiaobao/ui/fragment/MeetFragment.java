package com.bobaoo.xiaobao.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.MeetData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.EnrollActivity;
import com.bobaoo.xiaobao.ui.activity.FindActivity;
import com.bobaoo.xiaobao.ui.activity.UserLogInActivity;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Date;

/**
 * Created by Ameng on 2016/3/24.
 */
public class MeetFragment extends BaseFragment {
    private FindActivity findActivity;
    private LinearLayout mLlEnroll, mLlLoading;
    private LinearLayout mLlNetError;
    private ImageView mIvLoading;
    private MeetData mResponse;
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.tv_identify_type)
    private TextView mTvMeetType;
    @ViewInject(R.id.tv_online_pay)
    private TextView mTvMeetOnlinePay;
    @ViewInject(R.id.tv_live_pay)
    private TextView mTvMeetLivePay;
    @ViewInject(R.id.tv_address)
    private TextView mTvMeetAddress;
    @ViewInject(R.id.ll_add_time_range)
    private LinearLayout mLlAddTimeRange;
    @ViewInject(R.id.tv_meet_date)
    private TextView mTvMeetDate;
    @ViewInject(R.id.ll_add_phone)
    private LinearLayout mLlMeetAddPhone;
    @ViewInject(R.id.iv_meet_banner)
    private ImageView mIvMeetBanner;
    @ViewInject(R.id.ll_instant_enroll)
    private LinearLayout mLlInstantEnroll;
    @ViewInject(R.id.ll_add_professor_item)
    private LinearLayout mLlAddProfessorItem;

    public MeetFragment() {
    }

    public MeetFragment(FindActivity findActivity) {
        this.findActivity = findActivity;
    }

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getMeetParams(mContext), new MeetListener());
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_meet;
    }

    @Override
    protected void initContent() {
        ViewUtils.inject(this,mRootView);
        mIvLoading = (ImageView) mRootView.findViewById(R.id.iv_loading);
        mLlLoading = (LinearLayout) mRootView.findViewById(R.id.ll_loading);
        mLlEnroll = (LinearLayout) mRootView.findViewById(R.id.ll_enroll);
        mLlNetError = (LinearLayout) mRootView.findViewById(R.id.ll_net_error);
        Button btnEnter = (Button) mRootView.findViewById(R.id.btn_enter);
        mTvTitle.setText("鉴定会");
        setOnClickListener(btnEnter, mTvBack, mLlNetError);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter:
                if (!UserInfoUtils.checkUserLogin(mContext)) {
                    Intent intent = new Intent(mContext, UserLogInActivity.class);
                    intent.putExtra(IntentConstant.EnrollId, IntentConstant.EnrollId);
                    getActivity().startActivity(intent);
                    DialogUtils.showShortPromptToast(mContext, R.string.not_login);
                    return;
                }
                if (getCurrentTime() > Long.parseLong(mResponse.data.time) * 1000){
                    DialogUtils.showPromtAlertDialog(mContext,null,-1, AppConstant.ACTIVE_OVER,false);
                    return;
                }
                UmengUtils.onEvent(mContext, EventEnum.RequestEnrollClick);
                Intent intent = new Intent(findActivity, EnrollActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_INFO,mResponse);
                startActivity(intent);
                break;
            case R.id.tv_back:
                findActivity.finish();
                break;
            case R.id.ll_net_error:
                initData();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    public long getCurrentTime(){
        return new Date().getTime();
    }
    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showLoadingDialog() {
        mLlEnroll.setVisibility(View.GONE);
        mLlNetError.setVisibility(View.GONE);
        mLlLoading.setVisibility(View.VISIBLE);
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setRepeatCount(int repeatCount);
        animation.setDuration(3000);//设置动画持续时间
        animation.setFillAfter(true);
        mIvLoading.setAnimation(animation);

    }

    private class MeetListener extends com.lidroid.xutils.http.callback.RequestCallBack<String> implements StringToBeanTask.ConvertListener<MeetData> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<MeetData> task = new StringToBeanTask<>(MeetData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mLlEnroll.setVisibility(View.GONE);
            mLlInstantEnroll.setVisibility(View.GONE);
            mLlNetError.setVisibility(View.VISIBLE);
        }

        /**
         * @param response
         */
        @Override
        public void onConvertSuccess(MeetData response) {
            if (response != null) {
                if (!response.error) {
                    mLlEnroll.setVisibility(View.VISIBLE);
                    mLlInstantEnroll.setVisibility(View.VISIBLE);
                    mLlNetError.setVisibility(View.GONE);

                    mResponse = response;
                    AppConstant.CALL_CENTER = response.data.contact;
                    mTvMeetType.setText(response.data.kind_name);
                    mTvMeetOnlinePay.setText("在线支付：" + response.data.price + "/件");
                    mTvMeetLivePay.setText("现场支付：" + response.data.scene + "/件");
                    mTvMeetAddress.setText(response.data.location);
                    mTvMeetDate.setText(response.data.start_time);
                    Uri bannerUri = Uri.parse(response.data.banner);
                    mIvMeetBanner.setImageURI(bannerUri);
                    mIvMeetBanner.setVisibility(View.VISIBLE);
                    for (int i = 0; i < response.data.expert.size(); i++) {
                        MeetData.Expert expert = response.data.expert.get(i);
                        LinearLayout mLlAddProfessor = new LinearLayout(mContext);
                        TextView textView1 = new TextView(mContext);
                        textView1.setTextSize(14);
                        textView1.setTextColor(Color.parseColor("#646464"));
                        textView1.setText(expert.name);
                        mLlAddProfessor.addView(textView1);

                        TextView textView2 = new TextView(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT );
                        params.leftMargin = 30;
                        textView2.setLayoutParams(params);
                        textView2.setTextSize(14);
                        textView2.setTextColor(Color.parseColor("#646464"));
                        textView2.setText(expert.honors);
                        mLlAddProfessor.addView(textView2);
                        mLlAddProfessorItem.addView(mLlAddProfessor);
                    }
                    for (int i = 0; i < response.data.process.size(); i++) {
                        String timeRange = response.data.process.get(i);
                        TextView textView = new TextView(mContext);
                        textView.setTextSize(12);
                        textView.setTextColor(Color.parseColor("#646464"));
                        textView.setText(timeRange);
                        mLlAddTimeRange.addView(textView);
                    }
                    for (int i = 0; i < response.data.mobile.size(); i++) {
                        String mobile = response.data.mobile.get(i);
                        TextView textView = new TextView(mContext);
                        textView.setTextSize(14);
                        textView.setTextColor(Color.parseColor("#646464"));
                        textView.setText(mobile);
                        mLlMeetAddPhone.addView(textView);
                    }
                }else{
                    DialogUtils.showShortPromptToast(mContext, response.message);
                }
            }
        }

        @Override
        public void onConvertFailed() {
            Toast.makeText(mContext,"数据解析错误！",Toast.LENGTH_SHORT).show();
        }
    }
}
