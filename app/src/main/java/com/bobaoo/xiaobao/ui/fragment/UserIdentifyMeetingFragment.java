package com.bobaoo.xiaobao.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.DeleteResponse;
import com.bobaoo.xiaobao.domain.IdentifyMeetingSendMessageResponse;
import com.bobaoo.xiaobao.domain.TimerStiker;
import com.bobaoo.xiaobao.domain.UserIdentifyMeetingDatas;
import com.bobaoo.xiaobao.listener.TimerCountCallback;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.FindActivity;
import com.bobaoo.xiaobao.ui.activity.UserPayActivity;
import com.bobaoo.xiaobao.ui.dialog.PayAlertDialog;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by you on 2015/6/4.
 */
public class UserIdentifyMeetingFragment extends BasePagerLoadListViewFragment {
    private static final String IDENTIFY_TYPE = "IDENTIFY_TYPE";
    //    type:0全部 1未支付 2已支付未鉴定 3已鉴定
    private int mType;
    private List<UserIdentifyMeetingDatas.DataEntity> mData;
    private Handler mTimerHandler;
    private List<TimerStiker> mTimerStickers = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private PayAlertDialog mPayAlertDialog;

    public static UserIdentifyMeetingFragment creatUserIdentifyMeetingFragment(int type) {
        UserIdentifyMeetingFragment fragment = new UserIdentifyMeetingFragment();
        Bundle arg = new Bundle();
        arg.putInt(IDENTIFY_TYPE, type);
        fragment.setArguments(arg);
        return fragment;
    }
    @Override
    protected BaseAdapter getAdapter() {
        return new UserIdentifyAdapter();
    }

    @Override
    protected RequestParams configNetRequestParams() {
        mType = getArguments().getInt(IDENTIFY_TYPE, 1);
        return NetConstant.getUserIdentifyMeetingParams(mContext, String.valueOf(mType));
    }

    @Override
    protected String configUrl() {
        return NetConstant.HOST;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void startNetPagerDataRequest(final int pageIndex) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.data_download));
        }
        if (pageIndex == 1) {
            mLoadFailedUI.setVisibility(View.GONE);
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, mParams, new UserIdentifyListener(pageIndex));
    }

    private class UserIdentifyAdapter extends BaseAdapter implements View.OnClickListener {
        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if (mData != null) {
                return mData.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_identify_meeting_item, null);
                holder.rl_identify_container = convertView.findViewById(R.id.rl_identify_container);

                holder.tv_item_identify_type = (TextView) convertView.findViewById(R.id.tv_item_identify_type);
                holder.tv_item_identify_pay_num = (TextView) convertView.findViewById(R.id.tv_item_identify_pay_num);

                holder.tv_expert = (TextView) convertView.findViewById(R.id.tv_expert);
                holder.tv_classes = (TextView) convertView.findViewById(R.id.tv_classes);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);

                holder.tv_identify_china = (TextView) convertView.findViewById(R.id.tv_identify_china);
                holder.tv_identify_suntry = (TextView) convertView.findViewById(R.id.tv_identify_suntry);
                holder.tv_appointment_time = (TextView) convertView.findViewById(R.id.tv_appointment_time);

                holder.ll_appointment_num = (LinearLayout) convertView.findViewById(R.id.ll_appointment_num);
                holder.ll_china = (LinearLayout) convertView.findViewById(R.id.ll_china);
                holder.tv_china_num = (TextView) convertView.findViewById(R.id.tv_china_num);
                holder.ll_sundry = (LinearLayout) convertView.findViewById(R.id.ll_sundry);
                holder.tv_sundry_num = (TextView) convertView.findViewById(R.id.tv_sundry_num);
                holder.tv_no_appointment = (TextView) convertView.findViewById(R.id.tv_no_appointment);

                holder.fr_click_btn = (FrameLayout) convertView.findViewById(R.id.fr_click_btn);

                holder.rl_no_pay_identify = (RelativeLayout) convertView.findViewById(R.id.rl_no_pay_identify);
                holder.timeout = (TextView) convertView.findViewById(R.id.timeout);
                holder.bt_delete_identify = convertView.findViewById(R.id.bt_delete_identify);
                holder.bt_pay_identify = convertView.findViewById(R.id.bt_pay_identify);

                holder.rl_switch_identified = (RelativeLayout) convertView.findViewById(R.id.rl_switch_identified);
                holder.btn_send_to_phone = convertView.findViewById(R.id.btn_send_to_phone);

                holder.line = convertView.findViewById(R.id.line);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final UserIdentifyMeetingDatas.DataEntity dataEntity = mData.get(i);

            holder.fr_click_btn.setVisibility(mType == 3 ? View.GONE : View.VISIBLE);
            holder.ll_appointment_num.setVisibility(mType == 1 ? View.GONE : View.VISIBLE);
            holder.tv_no_appointment.setVisibility(mType == 1 ? View.VISIBLE : View.GONE);
            holder.rl_no_pay_identify.setVisibility(mType == 1 ? View.VISIBLE : View.GONE);

            holder.rl_switch_identified.setVisibility(mType == 2 ? View.VISIBLE : View.GONE);

            holder.line.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);

            holder.btn_send_to_phone.setEnabled((mType == 2 && dataEntity.getSend_count() < 3));
            holder.btn_send_to_phone.setBackgroundResource((mType == 2 && dataEntity.getSend_count() < 3) ? R.drawable.bg_button_submit : R.drawable.btn_gray_shape);

            holder.tv_item_identify_type.setText(dataEntity.getName());
            holder.tv_item_identify_pay_num
                    .setTextColor(mType == 3 ? getActivity().getResources().getColor(R.color.black3) : getActivity().getResources().getColor(R.color.red_64));
            String priceStr = getString(mType == 1 ? R.string.to_pay : R.string.had_pay);
            holder.tv_item_identify_pay_num.setText(StringUtils.getString(priceStr, getString(R.string.with_money, dataEntity.getPrice())));

            holder.tv_expert.setText(dataEntity.getExpert());
            holder.tv_classes.setText(dataEntity.getKind());
            holder.tv_time.setText(dataEntity.getTime());
            holder.tv_address.setText(dataEntity.getLocation());

            String chinaStr = (dataEntity.getTaoci() == null || dataEntity.getTaoci().getCount() == 0) ? "" :
                    StringUtils.getString(getString(R.string.china), dataEntity.getTaoci().getCount(), getString(R.string.item));
            String suntryStr = (dataEntity.getZaxiang() == null || dataEntity.getZaxiang().getCount() == 0) ? "" :
                    StringUtils.getString(getString(R.string.suntry), dataEntity.getZaxiang().getCount(), getString(R.string.item));
            holder.tv_identify_china.setText(chinaStr);
            holder.tv_identify_suntry.setText(suntryStr);
            holder.tv_identify_china.setVisibility((dataEntity.getTaoci() == null || dataEntity.getTaoci().getCount() == 0) ? View.GONE : View.VISIBLE);
            holder.tv_identify_suntry.setVisibility((dataEntity.getZaxiang() == null || dataEntity.getZaxiang().getCount() == 0) ? View.GONE : View.VISIBLE);
            holder.tv_appointment_time.setText(dataEntity.getApply());

            if (dataEntity.getTaoci() != null && !TextUtils.isEmpty(dataEntity.getTaoci().getNumber())) {
                holder.ll_china.setVisibility(View.VISIBLE);
                holder.tv_china_num.setText(dataEntity.getTaoci().getNumber());
            } else {
                holder.ll_china.setVisibility(View.GONE);
            }
            if (dataEntity.getZaxiang() != null && !TextUtils.isEmpty(dataEntity.getZaxiang().getNumber())) {
                holder.ll_sundry.setVisibility(View.VISIBLE);
                holder.tv_sundry_num.setText(dataEntity.getZaxiang().getNumber());
            } else {
                holder.ll_sundry.setVisibility(View.GONE);
            }

            if (mType == 1) {
                //倒计时回调处理
                if (mTimerStickers != null && mTimerStickers.size() > 0) {
                    TimerStiker stiker = mTimerStickers.get(i);
                    if (stiker.getTime() == 0) {
                        holder.timeout.setText(R.string.identify_meeting_time_out);
                        holder.bt_pay_identify.setBackgroundResource(R.drawable.btn_gray_shape);
                        holder.bt_pay_identify.setEnabled(false);
                    } else {
                        holder.timeout.setText(getDateFormatStr(stiker.getTime()));
                        holder.bt_pay_identify.setBackgroundResource(R.drawable.bg_button_submit);
                        holder.bt_pay_identify.setEnabled(true);
                    }
                    TimerCallback callback = new TimerCallback(holder.timeout, i);
                    stiker.setCallBack(callback);
                }
            }

            holder.bt_delete_identify.setTag(i);//删除按钮
            holder.bt_delete_identify.setOnClickListener(this);
            holder.bt_pay_identify.setTag(i);//支付按钮
            holder.bt_pay_identify.setOnClickListener(this);
            holder.btn_send_to_phone.setTag(i);//发短信按钮
            holder.btn_send_to_phone.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            final int pos;
            switch (v.getId()) {
                case R.id.bt_delete_identify:
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    DialogUtils.showPromtAlertDialog(mContext,UserIdentifyMeetingFragment.this,pos,AppConstant.MEET_ORDER_DELETE,false);
                    break;
                case R.id.bt_pay_identify://支付页面
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    UserIdentifyMeetingDatas.DataEntity entity = mData.get(pos);
                    if (TextUtils.isEmpty(entity.getTip())) {
                        toPay(entity.getId());
                    } else {
                        mPayAlertDialog = DialogUtils.showPayAlertDialog(mContext, getString(R.string.pay_alert), entity.getTip(), pos, this);
                    }
                    break;
                case R.id.tv_ok:
                    if (mPayAlertDialog != null) {
                        mPayAlertDialog.dismiss();
                    }
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    toPay(mData.get(pos).getId());
                    break;
                case R.id.btn_send_to_phone://发送短信
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    new HttpUtils().configCurrentHttpCacheExpiry(0)
                            .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getSendMessageParams(mContext, mData.get(pos).getId()), new SendMessageListener(pos));
                    break;
                default:
                    break;
            }
        }

        /**
         * 跳转支付页面
         */
        private void toPay(String id) {
            Intent intent = new Intent(getActivity(), UserPayActivity.class);
            intent.putExtra(IntentConstant.IdentifyMeetingId, id);
            ActivityUtils.jump(mContext, intent);
            HashMap<String, String> map = new HashMap<>();
            map.put(UmengConstants.KEY_PAY_IDENTIFY_MEETING_ID, id);
            UmengUtils.onEvent(getActivity(), EventEnum.Identify_Meeting_Item_Pay, map);
        }

        private class ViewHolder {
            View rl_identify_container;

            TextView tv_item_identify_type;
            TextView tv_item_identify_pay_num;

            TextView tv_expert;
            TextView tv_classes;
            TextView tv_time;
            TextView tv_address;

            TextView tv_identify_china;
            TextView tv_identify_suntry;
            TextView tv_appointment_time;

            LinearLayout ll_appointment_num;
            LinearLayout ll_china;
            TextView tv_china_num;
            LinearLayout ll_sundry;
            TextView tv_sundry_num;
            TextView tv_no_appointment;

            FrameLayout fr_click_btn;

            RelativeLayout rl_no_pay_identify;
            TextView timeout;
            View bt_delete_identify;
            View bt_pay_identify;

            RelativeLayout rl_switch_identified;
            View btn_send_to_phone;
            View line;
        }
    }

    private class UserIdentifyListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserIdentifyMeetingDatas> {

        private int pageIndex;

        public UserIdentifyListener(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        public void onConvertSuccess(UserIdentifyMeetingDatas data) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (pageIndex == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (data == null || data.isError() || data.getData() == null || data.getData().size() == 0) {
                    handleNoDataUI();
                    return;
                }
                mLoadFailedUI.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
            List<UserIdentifyMeetingDatas.DataEntity> tempData = data.getData();
            mCurrentPage = pageIndex;
            if (mData == null) {
                mData = new LinkedList<>();
            }
            if (mCurrentPage == 1) {
                mData.clear();
            }
            if (tempData.size() > 0) {
                mData.addAll(tempData);
            }
            //构造定时UI数据
            if (mType == 1) {
                if (mCurrentPage == 1) {
                    stopTimerTask();
                    mTimerStickers.clear();
                }
                if (tempData.size() > 0) {
                    for (int i = 0; i < tempData.size(); i++) {
                        UserIdentifyMeetingDatas.DataEntity dataEntity = tempData.get(i);
                        TimerStiker stiker = new TimerStiker();
                        stiker.setTime(dataEntity.getTimeout());
                        mTimerStickers.add(stiker);
                    }
                }
            }

            if (mType == 1) {
                startTimerTask();
            }
            updateContentUI();
            mSwipyRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onConvertFailed() {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleNoDataUI();
                stopTimerTask();
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserIdentifyMeetingDatas> task = new StringToBeanTask<>(UserIdentifyMeetingDatas.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleLoadFailedUI();
                stopTimerTask();
            }
        }
    }

    private void stopTimerTask() {
        if (mTimerHandler != null) {
            mTimerHandler.removeMessages(1);//刷新操作 重置倒计时
            mTimerHandler = null;
        }
    }

    private void startTimerTask() {
        if (mTimerHandler == null) {//这个判空条件保证定时任务只开启一次,停止定时会将它置空
            mTimerHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (!isNeedCountTimer()) {
                        stopTimerTask();
                        return;
                    }
                    for (int i = 0; i < mTimerStickers.size(); i++) {
                        TimerStiker task = mTimerStickers.get(i);//数据
                        TimerCountCallback callBack = task.getCallBack();//相应位置的CallBack
                        if (task.getTime() == 0) {//找到相应位置的回调
                            if (callBack != null) {
                                callBack.onTimerEnd();
                            }
                        } else {
                            task.setTime(task.getTime() - 1);
                            if (callBack != null) {
                                if (task.getTime() > 0) {
                                    callBack.onTimerSticker(task.getTime());
                                } else {
                                    callBack.onTimerEnd();
                                }
                            }
                        }
                    }
                    mTimerHandler.sendEmptyMessageDelayed(1, 1000);
                }
            };
            mTimerHandler.sendEmptyMessage(1);
        }
    }

    private String getDateFormatStr(long secs) {
        int sec = (int) (secs % 60);
        int min = (int) ((secs / 60) % 60);
        int hour = (int) ((secs / 3600) % 24);
        int day = (int) (secs / (3600 * 24));
        String s = StringUtils.getString(day, "天", hour, "小时", min, "分", sec, "秒");
        return s;
    }

    private boolean isNeedCountTimer() {
        if (mTimerStickers == null || mTimerStickers.size() == 0) {
            return false;
        }
        for (int i = 0; i < mTimerStickers.size(); i++) {
            TimerStiker stiker = mTimerStickers.get(i);
            if (stiker.getTime() > 0) {
                return true;
            }
        }
        return false;
    }

    class TimerCallback implements TimerCountCallback {
        public int mPos;
        private TextView mTextView;

        public TimerCallback(TextView tv, int pos) {
            mTextView = tv;
            mPos = pos;
            mTextView.setTag(pos);
        }

        @Override
        public void onTimerSticker(long sec) {
            if ((int) (mTextView.getTag()) == mPos) {
                mTextView.setText(getDateFormatStr(sec));
            }
        }

        @Override
        public void onTimerEnd() {
            if ((int) (mTextView.getTag()) == mPos) {
                mTextView.setText(R.string.identify_time_out);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
    }

    @Override
    protected void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mLoadFailedTipTv.setText(R.string.no_related_order);
        mLoadFailedBtn.setVisibility(View.VISIBLE);
        mLoadFailedBtn.setText(R.string.our_sign_up);
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FindActivity.class);
                intent.putExtra(AppConstant.INTENT_FRAGMENT_TYPE, 2);
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.User_Identify_Nodate_Want_Identify);
            }
        });
    }

    public void startDeleteIdentifyTreasureRequest(int pos) {
        if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
            return;
        }
        UserIdentifyMeetingDatas.DataEntity dataEntity = mData.get(pos);
        new HttpUtils().configCurrentHttpCacheExpiry(0)
                .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getDeleteIdentifyMeetingTreasureParams(mContext, dataEntity.getId()),
                        new DeleteIdentifyTreasureListener(pos));
    }

    private class DeleteIdentifyTreasureListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<DeleteResponse> {
        private int pos;

        public DeleteIdentifyTreasureListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onConvertSuccess(DeleteResponse data) {
            if (data == null) {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_fail);
                }
                return;
            }
            if (!data.isError()) {
                mData.remove(pos);
                updateContentUI();
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_success);
                }
            } else {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_fail);
                }
            }
        }

        @Override
        public void onConvertFailed() {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.delete_fail);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<DeleteResponse> task = new StringToBeanTask<>(DeleteResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.delete_fail);
            }
        }
    }

    private class SendMessageListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<IdentifyMeetingSendMessageResponse> {

        private int mPos;

        public SendMessageListener(int pos) {
            mPos = pos;
        }

        @Override
        public void onConvertSuccess(IdentifyMeetingSendMessageResponse data) {
            if (data == null || data.isError()) {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.send_message_fail);
                }
                return;
            }
            mData.get(mPos).setSend_count(data.getData().getCount());
            updateContentUI();
            if (getActivity() != null) {
                DialogUtils.showSendMessageSuccessDialog(mContext, UserInfoUtils.getPhone(mContext),
                        String.valueOf((IntentConstant.IdentifyMeetingSendMessageCount - data.getData().getCount())));
            }
        }

        @Override
        public void onConvertFailed() {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.send_message_fail);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifyMeetingSendMessageResponse> task = new StringToBeanTask<>(IdentifyMeetingSendMessageResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.send_message_fail);
            }
        }
    }
}
