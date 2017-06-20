package com.bobaoo.xiaobao.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.domain.IdentifyMeetPayResponse;
import com.bobaoo.xiaobao.domain.IdentifyMeetWXPayStatusResponse;
import com.bobaoo.xiaobao.utils.UmengUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 16/3/28.
 */
public class IdentifyMeetPaySuccessDialog extends BaseCustomerDialog{
    private Button mConfirmBtn;
    private TextView mTvAppointmentTime;
    private TextView mTvPromptInfo;
    private View.OnClickListener mOnConfirmListener;
    private IdentifyMeetPayResponse mData;//鉴定会牌号数据
    private LinearLayout mOrderNumberContainer;
    private final LinearLayout.LayoutParams LP_WRAP_CONTENT =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);

    public IdentifyMeetPaySuccessDialog(Context context, IdentifyMeetPayResponse data, View.OnClickListener listener) {
        super(context, R.style.CustomDialog);
        mData = data;
        mOnConfirmListener = listener;
    }

    public IdentifyMeetPaySuccessDialog(Context context, IdentifyMeetWXPayStatusResponse data,View.OnClickListener listener){
        super(context, R.style.CustomDialog);
        mOnConfirmListener = listener;
        convertModel(data);
    }

    private void convertModel(IdentifyMeetWXPayStatusResponse data) {
        mData = new IdentifyMeetPayResponse();
        mData.setError(data.isError());
        mData.setMessage(data.getMessage());

        List<IdentifyMeetPayResponse.DataEntity> items = new ArrayList<>();
        List<IdentifyMeetWXPayStatusResponse.DataEntity.InfoEntity> infos = data.getData().getInfo();
        if(infos != null) {
            for(int i = 0; i < infos.size(); i++) {
                IdentifyMeetWXPayStatusResponse.DataEntity.InfoEntity info = infos.get(i);
                IdentifyMeetPayResponse.DataEntity entity = new IdentifyMeetPayResponse.DataEntity();

                entity.setId(info.getId());
                entity.setName(info.getName());
                entity.setPaihao(info.getPaihao());

                items.add(entity);
            }
            mData.setData(items);
        }



    }
    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_pay_identify_meet_sucess;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mTvAppointmentTime = (TextView) findViewById(R.id.tv_appointment_time);
        mOrderNumberContainer = (LinearLayout) findViewById(R.id.order_number_container);
        mTvPromptInfo = (TextView) findViewById(R.id.tv_prompt_info);
        Button btnCall = (Button) findViewById(R.id.btn_call);
        setOnClickListener(mConfirmBtn,btnCall);
    }

    @Override
    protected void attachData() {
        //加载牌号数据
        mOrderNumberContainer.removeAllViews();

        List<IdentifyMeetPayResponse.DataEntity> orders = mData.getData();
        if(orders != null && orders.size() > 0) {
            for(int i = 0; i < orders.size(); i++) {
                IdentifyMeetPayResponse.DataEntity order = orders.get(i);
                if (!TextUtils.isEmpty(order.getTime())){
                    mTvAppointmentTime.setText(order.getTime());
                }
                if ("0".equals(order.getIsup())){
                    mTvPromptInfo.setVisibility(View.INVISIBLE);
                }
                if ("1".equals(order.getIsup())){
                    mTvPromptInfo.setVisibility(View.VISIBLE);
                }
                if(!TextUtils.isEmpty(order.getPaihao())) {
                    LinearLayout line = new LinearLayout(getContext());

                    TextView nameTv = new TextView(getContext());
                    nameTv.setTextSize(16);
                    nameTv.setTextColor(Color.parseColor("#646464"));
                    nameTv.setText(order.getName()+"  x"+order.getCount() + "　");
                    line.addView(nameTv, LP_WRAP_CONTENT);

                    TextView orderNumberTv = new TextView(getContext());
                    orderNumberTv.setTextSize(16);
                    orderNumberTv.setTextColor(Color.parseColor("#FFDD2C00"));
                    orderNumberTv.setText(order.getPaihao());

                    line.addView(orderNumberTv);

                    mOrderNumberContainer.addView(line, LP_WRAP_CONTENT);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                if(mOnConfirmListener != null) {
                    mOnConfirmListener.onClick(v);
                    UmengUtils.onEvent(context, EventEnum.PayMeetSuccess);
                }
                dismiss();
                break;
            case R.id.btn_call:
                if (!"".equals(AppConstant.CALL_CENTER)){
                    Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + AppConstant.CALL_CENTER));
                    context.startActivity(phoneIntent);
                    AppConstant.CALL_CENTER = "";
                    dismiss();
                    ((Activity)context).finish();
                }
                break;
            default:break;
        }
    }
}
