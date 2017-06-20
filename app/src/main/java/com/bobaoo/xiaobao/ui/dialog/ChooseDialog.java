package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.activity.TakePictureActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;

public class ChooseDialog extends BaseCustomerDialog {
    private Context mContext;

    public ChooseDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_choose;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("请选择您要鉴定的钱币");
    }

    @Override
    protected void initView() {
        View paperView = findViewById(R.id.iv_money_paper);
        View silverView = findViewById(R.id.iv_money_silver);
        View bronzeView = findViewById(R.id.iv_money_bronze);
        setCanceledOnTouchOutside(true);
        setOnClickListener(paperView, silverView, bronzeView);
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, TakePictureActivity.class);
        switch (view.getId()) {
            case R.id.iv_money_paper:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeMoneyPaper);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneyPaper);
                break;
            case R.id.iv_money_silver:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeMoneySilver);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneySilver);
                break;
            case R.id.iv_money_bronze:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeMoneyBronze);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneyBronze);
                break;
            default:
                break;
        }
        dismiss();
        ActivityUtils.jump(mContext, intent);
    }

}
