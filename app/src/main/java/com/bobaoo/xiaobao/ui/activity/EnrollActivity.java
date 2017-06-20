package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.EnrollData;
import com.bobaoo.xiaobao.domain.MeetData;
import com.bobaoo.xiaobao.domain.TimeRangeData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.ui.dialog.TimeRangeAlertDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/**
 * Created by Ameng on 2016/3/24.
 */
public class EnrollActivity extends BaseActivity {
    private TextView mTvMeetName;
    private TextView mTvMeetTime;
    private TextView mTvMeetPrice;
    private TextView mTvTimeRange;
    private LinearLayout mLlTimeRange;
    private EditText mEtMobile;
    private EditText mEtName;
    private String mMobile;
    private String[] mArr;
    private Button btnEnter;
    private ArrayList<MeetData.Kind> kinds = new ArrayList<>();
    private HashMap<Integer, ViewHolder> mapHolder = new HashMap<>();
    private HashMap<Integer, String> cacheCount = new HashMap<>();
    private HashSet<Integer> keySets = new HashSet<>();
    private LinearLayout llAddParent;
    private MeetData meetData;
    private int middleCount;
    private ProgressDialog mEnrollProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvTimeRange.setText((String) msg.obj);
            if (AppConstant.TIME_RANGE_ERROR.equals(mTvTimeRange.getText().toString()) || isHasCount() || TextUtils.isEmpty(mEtMobile.getText().toString()) || TextUtils.isEmpty(mEtName.getText().toString())) {
                btnEnter.setBackgroundResource(R.drawable.enroll_enter_button_bg);
                return;
            }
            btnEnter.setBackgroundResource(R.drawable.enroll_enter_button_bg2);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_enroll;
    }

    @Override
    protected void initData() {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getTimeRangeParams(mContext), new TimeRangeListener());
    }

    @Override
    protected void initTitle() {
        LinearLayout ivBack = (LinearLayout) mRootView.findViewById(R.id.iv_back_enroll);
        setOnClickListener(ivBack);
    }

    @Override
    protected void initContent() {
        mTvMeetName = (TextView) mRootView.findViewById(R.id.tv_meet_name);
        mTvMeetTime = (TextView) mRootView.findViewById(R.id.tv_meet_time);
        mTvMeetPrice = (TextView) mRootView.findViewById(R.id.tv_meet_price);
        mTvTimeRange = (TextView) mRootView.findViewById(R.id.tv_time_range);
        mLlTimeRange = (LinearLayout) mRootView.findViewById(R.id.ll_time_range);
        mEtMobile = (EditText) mRootView.findViewById(R.id.et_mobile);
        mEtName = (EditText) mRootView.findViewById(R.id.et_name);
        llAddParent = (LinearLayout) mRootView.findViewById(R.id.ll_add_parent);
        btnEnter = (Button) mRootView.findViewById(R.id.btn_enter);


        if (meetData != null) {
            mTvMeetName.setText(meetData.data.stage_name);
            mTvMeetTime.setText(meetData.data.start_time);
            mTvMeetPrice.setText(meetData.data.price + "元/件");
            String mobile = UserInfoUtils.getPhone(mContext);
            if (!TextUtils.isEmpty(mobile)){
                mEtMobile.setText(mobile);
            }
            kinds = meetData.data.kind;
            llAddParent.removeAllViews();
            for (int i = 0; i < kinds.size(); i++) {
                addLayout(i);
            }
        }
        setOnClickListener(btnEnter, mLlTimeRange);
        mEtName.addTextChangedListener(mTextWatch);
        mEtMobile.addTextChangedListener(mTextWatch);
    }

    class ViewHolder {
        TextView tvName;
        ImageView ivReduce;
        ImageView ivAdd;
        TextView tvAntique;
    }

    public void addLayout(int position) {

        View convertView = View.inflate(EnrollActivity.this, R.layout.activity_enroll_item, null);//
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) convertView.getLayoutParams();
        if (params == null) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        params.height = 80;
        convertView.setLayoutParams(params);
        ViewHolder holder = new ViewHolder();
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_count_name);
        holder.ivAdd = (ImageView) convertView.findViewById(R.id.iv_add);
        holder.ivReduce = (ImageView) convertView.findViewById(R.id.iv_reduce);
        holder.tvAntique = (TextView) convertView.findViewById(R.id.tv_antique_count);
        convertView.setTag(R.id.tag_0, position);
        mapHolder.put(position, holder);
        holder.tvAntique.addTextChangedListener(mTextWatch);
        holder.tvName.setText(kinds.get(position).name);
        setOnClickListener(holder.ivAdd, holder.ivReduce);
        llAddParent.addView(convertView);
    }

    private TextWatcher mTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isHasCount() || AppConstant.TIME_RANGE_ERROR.equals(mTvTimeRange.getText().toString()) || TextUtils.isEmpty(mEtMobile.getText().toString()) || TextUtils.isEmpty(mEtName.getText().toString())) {
                btnEnter.setBackgroundResource(R.drawable.enroll_enter_button_bg);
                return;
            }
            btnEnter.setBackgroundResource(R.drawable.enroll_enter_button_bg2);
        }
    };

    @Override
    protected void initFooter() {

    }

    @Override
    protected void getIntentData() {
        meetData = (MeetData) getIntent().getSerializableExtra(IntentConstant.IDENTIFY_INFO);
    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_enroll:
                DialogUtils.showPromtAlertDialog(this, null, -1, AppConstant.MEET_CANCEL_ENROLL, false);
                break;
            case R.id.ll_time_range:
                showAlertDialog();
                break;
            case R.id.iv_add:
                LinearLayout llAddCount = (LinearLayout) view.getParent().getParent().getParent();
                int parentPositionAdd = (int) llAddCount.getTag(R.id.tag_0);
                ViewHolder holder = mapHolder.get(parentPositionAdd);
                int value = Integer.parseInt(holder.tvAntique.getText().toString());
                cacheCount.put(parentPositionAdd, (value + 1) + "");
                keySets.add(parentPositionAdd);
                holder.tvAntique.setText((value + 1) + "");
                break;
            case R.id.iv_reduce:
                LinearLayout llAddCount2 = (LinearLayout) view.getParent().getParent().getParent();
                int parentPositionAdd2 = (int) llAddCount2.getTag(R.id.tag_0);
                ViewHolder holder2 = mapHolder.get(parentPositionAdd2);
                int value2 = Integer.parseInt(holder2.tvAntique.getText().toString());
                if (value2 - 1 == 0) {
                    if (keySets.contains(parentPositionAdd2)) {
                        keySets.remove(parentPositionAdd2);
                        cacheCount.remove(parentPositionAdd2);
                        holder2.tvAntique.setText((value2 - 1) + "");
                    }
                    return;
                }
                if (value2 == 0) {
                    return;
                }
                cacheCount.put(parentPositionAdd2, (value2 - 1) + "");
                holder2.tvAntique.setText((value2 - 1) + "");
                break;
            case R.id.btn_enter:
                if (TextUtils.isEmpty(mEtMobile.getText().toString())) {
                    DialogUtils.showShortPromptToast(EnrollActivity.this, R.string.write_mobile);
                    return;
                }
                if (!StringUtils.checkPhoneNumber(mEtMobile.getText().toString())) {
                    DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
                    return;
                }
                if (TextUtils.isEmpty(mEtName.getText().toString())) {
                    DialogUtils.showShortPromptToast(EnrollActivity.this, R.string.write_name);
                    return;
                }
                if (AppConstant.TIME_RANGE_ERROR.equals(mTvTimeRange.getText().toString())) {
                    DialogUtils.showShortPromptToast(EnrollActivity.this, R.string.click_time_range);
                    return;
                }
                if (isHasCount()) {
                    DialogUtils.showShortPromptToast(EnrollActivity.this, R.string.write_count);
                    return;
                }

                if (!keySets.contains(0)) {
                    cacheCount.put(0, 0 + "");
                }

                middleCount = 0;
                Iterator<Integer> key = keySets.iterator();
                while (key.hasNext()) {
                    Integer keyValue = key.next();
                    if (keyValue != 0) {
                        middleCount += Integer.parseInt(cacheCount.get(keyValue));
                    }
                }
                if (mEnrollProgressDialog == null) {
                    mEnrollProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.commit_enroll_info_loading));
                }

                if (TextUtils.isEmpty(mMobile)) {
                    mMobile = mEtMobile.getText().toString().trim();
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                        NetConstant.getEnrollParams(mContext, meetData.data.stage, mTvTimeRange.getText().toString(), mMobile, mEtName.getText().toString(), cacheCount.get(0), middleCount + ""),
                        new EnrollListener());

                break;
        }
    }


    private void showAlertDialog() {
        new TimeRangeAlertDialog(this, mArr, mHandler);
    }

    private boolean isHasCount() {
        int allCount = 0;
        if (keySets.size() > 0) {
            allCount = 1;
        }
        return allCount == 0;
    }

    private class EnrollListener extends com.lidroid.xutils.http.callback.RequestCallBack<String> implements StringToBeanTask.ConvertListener<EnrollData> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<EnrollData> task = new StringToBeanTask<>(EnrollData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(EnrollActivity.this, R.string.enter_info_error);
            if (mEnrollProgressDialog != null) {
                if (mEnrollProgressDialog != null) {
                    mEnrollProgressDialog.dismiss();
                    mEnrollProgressDialog = null;
                }
            }
        }

        @Override
        public void onConvertSuccess(EnrollData response) {
            if (mEnrollProgressDialog != null) {
                mEnrollProgressDialog.dismiss();
                mEnrollProgressDialog = null;
            }
            if (response != null) {
                if (!response.error) {
                    String meetSignId = response.data;//报名id
                    UmengUtils.onEvent(mContext, EventEnum.EnrollSuccess);
                    Intent intent = new Intent(mContext, UserPayActivity.class);
                    intent.putExtra(IntentConstant.IdentifyMeetingId, meetSignId);
                    jump(intent);
                    finish();
                } else {
                    DialogUtils.showWarnDialog(mContext, getString(R.string.enroll_failed_title), response.message, null);
                }
            } else {
                DialogUtils.showWarnDialog(mContext, getString(R.string.enroll_failed_title), response.message, null);
            }
        }

        @Override
        public void onConvertFailed() {
            if (mEnrollProgressDialog != null) {
                mEnrollProgressDialog.dismiss();
                mEnrollProgressDialog = null;
            }
            DialogUtils.showWarnDialog(mContext, getString(R.string.enroll_failed_title), getString(R.string.dialog_please_check_network), null);
        }
    }

    private class TimeRangeListener extends com.lidroid.xutils.http.callback.RequestCallBack<String> implements StringToBeanTask.ConvertListener<TimeRangeData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<TimeRangeData> task = new StringToBeanTask<>(TimeRangeData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onConvertSuccess(TimeRangeData response) {
            if (response != null) {
                if (!response.error) {
                    mArr = new String[response.data.size()];
                    for (int i = 0; i < response.data.size(); i++) {
                        mArr[i] = response.data.get(i).time;
                    }
                } else {
                    DialogUtils.showShortPromptToast(EnrollActivity.this, response.message);
                }
            }
        }

        @Override
        public void onConvertFailed() {

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            DialogUtils.showPromtAlertDialog(this,null,-1, AppConstant.MEET_CANCEL_ENROLL,false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}