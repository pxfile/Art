 package com.bobaoo.xiaobao.ui.fragment;

 import android.content.Intent;
 import android.view.MotionEvent;
 import android.view.View;

 import com.bobaoo.xiaobao.R;
 import com.bobaoo.xiaobao.constant.AppConstant;
 import com.bobaoo.xiaobao.constant.EventEnum;
 import com.bobaoo.xiaobao.constant.IntentConstant;
 import com.bobaoo.xiaobao.ui.activity.TakePictureActivity;
 import com.bobaoo.xiaobao.utils.ActivityUtils;
 import com.bobaoo.xiaobao.utils.DialogUtils;
 import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
 import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by star on 15/5/29.
 */
public class IdentifyFragment extends BaseFragment {
    private static final String SP_KEY_INNER_GUIDER = "inner_guider";

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_identify;
    }

    @Override
    protected void initContent() {
        View iconView = mRootView.findViewById(R.id.iv_icon);
        View bronzeView = mRootView.findViewById(R.id.iv_take_picture_bronze);
        View chinaView = mRootView.findViewById(R.id.iv_take_picture_china);
        View woodenView = mRootView.findViewById(R.id.iv_take_picture_wooden);
        View jadeView = mRootView.findViewById(R.id.iv_take_picture_jade);
        View sundryView = mRootView.findViewById(R.id.iv_take_picture_sundry);
        View paintingView = mRootView.findViewById(R.id.iv_take_picture_painting);
        final View moneyView = mRootView.findViewById(R.id.iv_take_picture_money);
        setOnClickListener(iconView, bronzeView, chinaView, woodenView, jadeView, sundryView, paintingView, moneyView);
        final View innerGuiderView = mRootView.findViewById(R.id.img_inner_guider);
        boolean innerGuiderFlag = SharedPreferencesUtils.getSharedPreferencesBoolean(mContext, SP_KEY_INNER_GUIDER);
        innerGuiderView.setVisibility(innerGuiderFlag ? View.GONE : View.VISIBLE);
        innerGuiderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, SP_KEY_INNER_GUIDER, true);
                innerGuiderView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, TakePictureActivity.class);
        switch (view.getId()) {
            case R.id.iv_icon:
                showDebugInfo(mContext, "");
                break;
            case R.id.iv_take_picture_bronze:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeBronze);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageBronze);
                break;
            case R.id.iv_take_picture_china:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeChina);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageChina);
                break;
            case R.id.iv_take_picture_wooden:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeWooden);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageWooden);
                break;
            case R.id.iv_take_picture_jade:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeJade);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageJade);
                break;
            case R.id.iv_take_picture_sundry:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeSundry);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageSundry);
                break;
            case R.id.iv_take_picture_painting:
                intent.putExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypePainting);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPagePainting);
                break;
            case R.id.iv_take_picture_money:
                DialogUtils.showChooseDialog(mContext);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageMoney);
                break;
            default:
                super.onClick(view);
                break;
        }
    }
}
