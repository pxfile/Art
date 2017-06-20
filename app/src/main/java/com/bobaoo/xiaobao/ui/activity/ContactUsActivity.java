package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by you on 2015/6/16.
 */
public class ContactUsActivity extends BaseActivity {
    TextView mContactUsTelView;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("联系我们");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mContactUsTelView = (TextView) findViewById(R.id.tv_contact_us_tel);
        setOnClickListener(mContactUsTelView);
        onScroll();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_contact_us_tel:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mContactUsTelView.getText().toString().trim().substring(3, mContactUsTelView.getText().toString().trim().length())));
                jump(intent);
                break;
            default:
                break;
        }
    }

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
