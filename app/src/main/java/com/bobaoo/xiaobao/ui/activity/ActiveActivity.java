package com.bobaoo.xiaobao.ui.activity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.domain.ActiveData;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class ActiveActivity extends Activity implements View.OnClickListener {
    private SimpleDraweeView mImageView;
    private ImageView mImageViewClose;
    private ActiveData activeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_dialog);

        UmengUtils.onEvent(this, EventEnum.ActiveCreate);
        mImageView = (SimpleDraweeView) findViewById(R.id.fresco_view);
        mImageViewClose = (ImageView) findViewById(R.id.iv_close);

        activeInfo = (ActiveData) getIntent().getSerializableExtra(IntentConstant.ACTIVE_INFO);
        Uri imageUri = Uri.parse(activeInfo.data.image);
        mImageView.setImageURI(imageUri);
        mImageView.setAspectRatio((float)activeInfo.data.width/activeInfo.data.height);
        mImageViewClose.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fresco_view){
            UmengUtils.onEvent(this, EventEnum.ActiveEnter);
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra(IntentConstant.WEB_URL,activeInfo.data.href);
            intent.putExtra(IntentConstant.WEB_TITLE,activeInfo.data.title);
            startActivity(intent);
        }else if (v.getId() == R.id.iv_close){
            UmengUtils.onEvent(this, EventEnum.ActiveClose);
        }
        finish();
    }


}
