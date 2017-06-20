package com.bobaoo.xiaobao.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.domain.DeleteResponse;
import com.bobaoo.xiaobao.domain.TimerStiker;
import com.bobaoo.xiaobao.domain.UserIdentifyDatas;
import com.bobaoo.xiaobao.listener.TimerCountCallback;
import com.bobaoo.xiaobao.manager.IdentifyModifyManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.IdentifyModifyActivity;
import com.bobaoo.xiaobao.ui.activity.MainActivity;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.ui.activity.OrderToPayActivity;
import com.bobaoo.xiaobao.ui.activity.PriceQueryContentActivity;
import com.bobaoo.xiaobao.ui.activity.UserLogInActivity;
import com.bobaoo.xiaobao.ui.activity.UserPayActivity;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by you on 2015/6/4.
 */
public class UserIdentifyFragment extends BasePagerLoadListViewFragment {
    private static final String IS_PUBLIC_ZERO = "0";
    private static final String IS_PUBLIC_ONE = "1";
    private static final String IDENTIFY_TYPE = "IDENTIFY_TYPE";
    //    type:0全部 1未支付 2已支付未鉴定 3已鉴定
    private int mType;
    private List<UserIdentifyDatas.DataEntity> mData;
    private Handler mTimerHandler;
    private List<TimerStiker> mTimerStickers = new ArrayList<>();
    private Map<String, String> mToggleStatus = new HashMap<>();

    private String strType;

    public static UserIdentifyFragment creatUserIdentifyFragment(int type) {
        UserIdentifyFragment fragment = new UserIdentifyFragment();
        Bundle arg = new Bundle();
        arg.putInt(IDENTIFY_TYPE, type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void loadData() {//用户鉴定刷新页面
        mCurrentPage = 1;
        startPageLoadRequest(mCurrentPage);
    }

    private boolean mTypeFlags = false;

    @Override
    protected BaseAdapter getAdapter() {
        return new UserIdentifyAdapter();
    }

    @Override
    protected RequestParams configNetRequestParams() {
        mType = getArguments().getInt(IDENTIFY_TYPE, 0);
        return NetConstant.getUserIdentifyParams(mContext, String.valueOf(mType));
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
        if (pageIndex == 1) {
            mLoadFailedUI.setVisibility(View.GONE);
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, mParams, new UserIdentifyListener(pageIndex));
    }

    private class UserIdentifyAdapter extends BaseAdapter {
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.user_identify_item, null);
                holder.tv_item_identify_type = (TextView) convertView.findViewById(R.id.tv_item_identify_type);
                holder.iv_identify_type_img = (SimpleDraweeView) convertView.findViewById(R.id.iv_identify_type_img);
                holder.bt_delete_identify = convertView.findViewById(R.id.bt_delete_identify);
                holder.rl_identify_contaner = convertView.findViewById(R.id.rl_identify_container);
                holder.sdv_item_identify_img = (SimpleDraweeView) convertView.findViewById(R.id.sdv_item_identify_img);
                holder.tv_item_identify_number = (TextView) convertView.findViewById(R.id.tv_item_identify_number);
                holder.tv_item_identify_note = (TextView) convertView.findViewById(R.id.tv_item_identify_note);
                holder.ll_no_pay_identify = convertView.findViewById(R.id.ll_no_pay_identify);
                holder.ll_switch_identified = convertView.findViewById(R.id.ll_switch_identified);
                holder.btn_edit_identify = (TextView) convertView.findViewById(R.id.btn_edit_identify);
                holder.bt_pay_identify = convertView.findViewById(R.id.bt_pay_identify);
                holder.showIdentifiedView = (ToggleButton) convertView.findViewById(R.id.bt_switch_identified);
                holder.mEvaluateBtn = (Button) convertView.findViewById(R.id.evaluate_btn);
                holder.mImageTimeout = convertView.findViewById(R.id.iv_timeout);
                holder.mTimeOutTv = (TextView) convertView.findViewById(R.id.timeout);
                holder.btn_refund_identified = (TextView) convertView.findViewById(R.id.btn_refund_identified);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final UserIdentifyDatas.DataEntity dataEntity = mData.get(i);
            if (dataEntity.getSpecify_expert_from() == 1) {
                strType = getString(R.string.identify_type_registration);
            } else {
                strType = dataEntity.getType();
            }
            holder.mEvaluateBtn.setText(dataEntity.getIscomment() != 1 ? R.string.evaluate_to_expert : R.string.already_evaluate_to_expert);
            holder.mEvaluateBtn.setTextColor(dataEntity.getIscomment() != 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black3));
            holder.mEvaluateBtn.setBackgroundResource(dataEntity.getIscomment() != 1 ? R.drawable.bg_evaluate : R.drawable.bg_already_evaluate);

            holder.mEvaluateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  2015/9/29 对专家进行评价
                    if (dataEntity.getIscomment() != 1) {
                        Intent intent;
//                    IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, dataEntity.getState());
//                    IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, dataEntity.getCharge_price());
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, dataEntity.getReport());
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, dataEntity.getPhoto());
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, dataEntity.getId());
                        //  2015/9/29 获取专家id，并传入评价页
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_EXPERT_ID, dataEntity.getAdmin_id());
                        IdentifyApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, true);
                        if (UserInfoUtils.checkUserLogin(mContext)) {
                            //  2015/10/9 刷新UI
                            notifyDataSetChanged();
                            jump(mContext, PriceQueryContentActivity.class);
                        } else {
                            intent = new Intent(mContext, UserLogInActivity.class);
                            jump(intent);
                        }
                    }
                }
            });
            holder.tv_item_identify_type.setText(strType);//鉴定类型
            holder.iv_identify_type_img.setImageURI(Uri.parse(dataEntity.getType_img()));
            holder.bt_delete_identify.setTag(i);//删除按钮
            holder.bt_delete_identify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.confirm_delete_collection)).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startDeleteIdentifyTreasureRequest((Integer) view.getTag());
                        }
                    }).setNegativeButton(getString(R.string.cancle), null).show();
                }
            });
            holder.rl_identify_contaner.setTag(i);//主体内容,用于Item点击
            holder.rl_identify_contaner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    UserIdentifyDatas.DataEntity entity = mData.get(pos);
                    String id = entity.getId();
                    if (mType == 1) {
                        mTypeFlags = false;
                        Intent intent = new Intent(getActivity(), OrderToPayActivity.class);
                        intent.putExtra(IntentConstant.CHARGED_STATE, entity.getCharged());
                        intent.putExtra(IntentConstant.ORDER_ID, id);
                        if (entity.getSpecify_expert_from() == 1) {
                            intent.putExtra(IntentConstant.IdentifyType, getString(R.string.identify_type_registration));
                        } else {
                            intent.putExtra(IntentConstant.IdentifyType, entity.getType());
                        }
                        startActivity(intent);
                    } else if (mType == 2) {
                        mTypeFlags = true;
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra(IntentConstant.CHARGED_STATE, entity.getCharged());
                        intent.putExtra(IntentConstant.ORDER_ID, id);
                        intent.putExtra(IntentConstant.IDENTIFY_TYPE_FLAG, mTypeFlags);
                        startActivity(intent);
                    } else {
                        mTypeFlags = false;
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra(IntentConstant.CHARGED_STATE, entity.getCharged());
                        intent.putExtra(IntentConstant.ORDER_ID, id);
                        startActivity(intent);
                    }
                }
            });
            holder.sdv_item_identify_img.setImageURI(Uri.parse(dataEntity.getPhoto()));
            //鉴定需求
            String note = dataEntity.getNote().trim();
            note = TextUtils.isEmpty(note) ? getString(R.string.identify_no_tips) : note;

            if (mType == 1) {//未支付
                holder.ll_switch_identified.setVisibility(View.GONE);
                holder.ll_no_pay_identify.setVisibility(View.VISIBLE);
                holder.bt_delete_identify.setVisibility(View.VISIBLE);
                holder.btn_edit_identify.setVisibility(View.VISIBLE);
                holder.btn_edit_identify.setText(R.string.indentify_modify);
                holder.tv_item_identify_number.setText(StringUtils.getString(getString(R.string.identify_number), dataEntity.getId()));
                holder.tv_item_identify_note.setText(note);
                holder.mTimeOutTv.setVisibility(View.GONE);
                holder.mImageTimeout.setVisibility(View.GONE);
            } else if (mType == 2) {//已支付未鉴定
                holder.mTimeOutTv.setVisibility(View.VISIBLE);
                holder.mImageTimeout.setVisibility(View.VISIBLE);
                //预约鉴定不计时
                boolean isShowTimeOut = !TextUtils.equals(strType, getString(R.string.identify_type_order));
                if(isShowTimeOut){
                    holder.mTimeOutTv.setVisibility(View.VISIBLE);
                    holder.mImageTimeout.setVisibility(View.VISIBLE);
                    //倒计时回调处理
                    if (mTimerStickers != null && mTimerStickers.size() > 0) {
                        TimerStiker stiker = mTimerStickers.get(i);
                        if (stiker.getTime() == 0) {
                            holder.mTimeOutTv.setText(R.string.identify_time_out);
                        } else {
                            holder.mTimeOutTv.setText(getDateFormatStr(stiker.getTime()));
                        }

                        TimerCallback callback = new TimerCallback(holder.mTimeOutTv, i);
                        stiker.setCallBack(callback);
                    }
                } else {
                    holder.mTimeOutTv.setVisibility(View.GONE);
                    holder.mImageTimeout.setVisibility(View.GONE);
                }

                holder.ll_switch_identified.setVisibility(View.GONE);
                holder.ll_no_pay_identify.setVisibility(View.GONE);
                holder.bt_delete_identify.setVisibility(View.GONE);
                holder.tv_item_identify_number.setText(StringUtils.getString(getString(R.string.identify_number), dataEntity.getId()));
                holder.tv_item_identify_note.setText(StringUtils.getString(note, "\n", getString(R.string.user_charge_time), dataEntity.getCharge_time()));
            } else if (mType == 3) {//已经鉴定
                holder.mTimeOutTv.setVisibility(View.GONE);
                holder.mImageTimeout.setVisibility(View.GONE);
                holder.ll_switch_identified.setVisibility(View.VISIBLE);
                holder.ll_no_pay_identify.setVisibility(View.GONE);
                holder.bt_delete_identify.setVisibility(View.VISIBLE);
                holder.tv_item_identify_number.setText(StringUtils.getString(getString(R.string.identify_number), dataEntity.getId()));
                holder.tv_item_identify_note.setText(note);
            }
            String expertId = dataEntity.getSpecify_expert_id();
            if (TextUtils.equals("0", expertId) || TextUtils.isEmpty(expertId)) {
                holder.btn_edit_identify.setVisibility(View.VISIBLE);
            } else if (dataEntity.getSpecify_expert_from() == 1) {
                holder.btn_edit_identify.setVisibility(View.VISIBLE);
            } else {
                //预约鉴定
                holder.btn_edit_identify.setVisibility(View.GONE);
            }
            holder.btn_edit_identify.setTag(dataEntity.getId());
            holder.btn_edit_identify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IdentifyModifyManager.getInstance().clearCache();
                    Intent intent = new Intent(mContext, IdentifyModifyActivity.class);
                    String orderId = (String) view.getTag();
                    intent.putExtra(IntentConstant.ORDER_ID, orderId);
                    mContext.startActivity(intent);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(UmengConstants.KEY_PAY_GOODS_ID, orderId);
                    UmengUtils.onEvent(getActivity(), EventEnum.Identify_Item_Modify, map);
                }
            });
            holder.bt_pay_identify.setTag(i);
            holder.bt_pay_identify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//支付页面
                    int pos = (int) view.getTag();
                    UserIdentifyDatas.DataEntity entity = mData.get(pos);
                    String goodId = entity.getId();
                    Intent intent = new Intent(getActivity(), UserPayActivity.class);
                    intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, goodId);
                    if (entity.getSpecify_expert_from() == 1) {
                        intent.putExtra(IntentConstant.IdentifyType, getString(R.string.identify_type_registration));
                    } else {
                        intent.putExtra(IntentConstant.IdentifyType, entity.getType());
                    }
                    getActivity().startActivity(intent);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(UmengConstants.KEY_PAY_GOODS_ID, goodId);
                    UmengUtils.onEvent(getActivity(), EventEnum.Identify_Item_Pay, map);
                }
            });
            String isPublic = mToggleStatus.get(dataEntity.getId());
            if (!TextUtils.isEmpty(isPublic)) {
                if (TextUtils.equals(IS_PUBLIC_ZERO, isPublic) || TextUtils.equals(IS_PUBLIC_ONE, isPublic)) {
                    holder.showIdentifiedView.setToggleOn(true);
                } else {
                    holder.showIdentifiedView.setToggleOff(true);
                }
            }
            final int openId = i;
            final ToggleButton showIdentifiedView = holder.showIdentifiedView;
            holder.showIdentifiedView.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    startOpenIdentifyTreasureRequest(openId, on, showIdentifiedView);
                }
            });

            //有退款
            holder.btn_refund_identified.setVisibility((dataEntity.getRefund() != null && dataEntity.getRefund().size() > 0) ? View.VISIBLE : View.GONE);
            holder.btn_refund_identified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (dataEntity.getRefund() != null && dataEntity.getRefund().size() > 0) {
                        for (int i = 0; i < dataEntity.getRefund().size(); i++) {
                            stringBuilder.append(dataEntity.getRefund().get(i).getName()).append(",").append(dataEntity.getRefund().get(i).getTime()).append(";");
                        }
                        if (stringBuilder.length() > 0) {
                            String content = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                            DialogUtils.showRefundDetailDialog(mContext, content);
                        }
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tv_item_identify_type;
            View bt_delete_identify;
            View rl_identify_contaner;
            SimpleDraweeView sdv_item_identify_img;
            TextView tv_item_identify_number;
            TextView tv_item_identify_note;
            View ll_no_pay_identify;//未支付需要显示的控件
            View ll_switch_identified;//已完成设置开关
            TextView btn_edit_identify;
            View bt_pay_identify;
            SimpleDraweeView iv_identify_type_img;
            ToggleButton showIdentifiedView;
            Button mEvaluateBtn;
            View mImageTimeout;
            View mTimeOutContainer;
            TextView mTimeOutTv;
            TextView btn_refund_identified;

        }

        private void startDeleteIdentifyTreasureRequest(final int pos) {
            if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                return;
            }
            UserIdentifyDatas.DataEntity dataEntity = mData.get(pos);
            new HttpUtils().configCurrentHttpCacheExpiry(0)
                    .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getDeleteIdentifyTreasureParams(mContext, dataEntity.getId()),
                            new DeleteIdentifyTreasureListener(pos));
        }

        private void startOpenIdentifyTreasureRequest(final int pos, boolean on, ToggleButton showIdentified) {
            if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                return;
            }
            UserIdentifyDatas.DataEntity dataEntity = mData.get(pos);
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getOpenIdentifiedParams(mContext, dataEntity.getId()),
                    new OpenIdentifiedListener(dataEntity.getId(), on, showIdentified));
        }
    }

    private class UserIdentifyListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserIdentifyDatas> {

        private int pageIndex;

        public UserIdentifyListener(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        public void onConvertSuccess(UserIdentifyDatas data) {
            if (pageIndex == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (data == null || data.isError() || data.getData() == null || data.getData().size() == 0) {
                    handleNoDataUI();
                    return;
                }
                mLoadFailedUI.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
            List<UserIdentifyDatas.DataEntity> tempData = data.getData();
            for (UserIdentifyDatas.DataEntity dataEntity : tempData) {
                mToggleStatus.put(dataEntity.getId(), dataEntity.getIs_public());
            }
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
            if (mType == 2) {
                if (mCurrentPage == 1) {
                    stopTimerTask();
                    mTimerStickers.clear();
                }
                if (tempData.size() > 0) {
                    for (int i = 0; i < tempData.size(); i++) {
                        UserIdentifyDatas.DataEntity dataEntity = tempData.get(i);
                        TimerStiker stiker = new TimerStiker();
                        stiker.setTime(dataEntity.getTimeout());
                        mTimerStickers.add(stiker);
                    }
                }
            }

            if (mType == 2) {
                startTimerTask();
            }
            updateContentUI();
            mSwipyRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onConvertFailed() {
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleNoDataUI();
                stopTimerTask();
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserIdentifyDatas> task = new StringToBeanTask<>(UserIdentifyDatas.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleLoadFailedUI();
                stopTimerTask();
            }
        }
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
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
                }
                return;
            }
            if (!data.isError()) {
                mData.remove(pos);
                updateContentUI();
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_success);
                }
            } else {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
                }

            }
        }

        @Override
        public void onConvertFailed() {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.remove_identification_failure);
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
                DialogUtils.showLongPromptToast(mContext, R.string.remove_identification_failure);
            }
        }
    }

    private class OpenIdentifiedListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {
        private ToggleButton showIdentifiedView;
        private String id;
        private boolean on;
        private int responseSuccess;
        private int responseFailed;

        public OpenIdentifiedListener(String id, boolean on, ToggleButton showIdentifiedView) {
            this.id = id;
            this.on = on;
            this.showIdentifiedView = showIdentifiedView;
            if (on) {
                responseSuccess = R.string.identification_open_success;
                responseFailed = R.string.identification_open_failure;
            } else {
                responseSuccess = R.string.identification_close_success;
                responseFailed = R.string.identification_close_failure;
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);

        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, responseFailed);
                setToggleStatus();
            }
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse data) {
            if (data == null) {
                DialogUtils.showLongPromptToast(mContext, responseFailed);
                setToggleStatus();
                return;
            }
            if (data.isError()) {
                DialogUtils.showLongPromptToast(mContext, responseFailed);
                setToggleStatus();
            } else {
                if (on) {
                    mToggleStatus.put(id, "1");
                } else {
                    mToggleStatus.put(id, "2");
                }
                DialogUtils.showLongPromptToast(mContext, responseSuccess);
            }
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showLongPromptToast(mContext, responseFailed);
            setToggleStatus();
        }

        /**
         * 当请求失败，Toggle开关状态不变
         */
        private void setToggleStatus() {
            if (on) {
                showIdentifiedView.setToggleOff(true);
            } else {
                showIdentifiedView.setToggleOn(true);
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
        mLoadFailedBtn.setText(R.string.our_indicated);
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.iv_identify);
                jump(intent);
                UmengUtils.onEvent(mContext,EventEnum.User_Identify_Nodate_Want_Identify);
            }
        });
    }
}
