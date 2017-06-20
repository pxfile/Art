package com.bobaoo.xiaobao.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.IdentifySkillData;
import com.bobaoo.xiaobao.domain.InfoListData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.FindActivity;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Ameng on 2016/3/30.
 */
public class IdentifySkillFragment extends BaseFragment{
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.ll_add_parent)
    private LinearLayout llAddParent;
    private FindActivity findActivity;
    private IdentifySkillData mResponse;
    private String mIdentifyType;
    private int mPageCount = 1;
    public IdentifySkillFragment() {

    }

    public IdentifySkillFragment(FindActivity findActivity) {
        this.findActivity = findActivity;
    }

    @Override
    protected void getArgumentsData() {

    }

    @Override
    protected void initData() {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getIdentifySkillParams(mContext), new SkillListener());
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_identify_skill;
    }

    @Override
    protected void initContent() {
        ViewUtils.inject(this,mRootView);
        mTvTitle.setText(R.string.identify_title);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View view) {
        String id = (String) view.getTag();
        if (TextUtils.isEmpty(id)){
            findActivity.finish();
            return;
        }
        mIdentifyType = id;
        requestInterface(id,mPageCount);
    }

    private void requestInterface(String id,int page){
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getIdentifySkillDetailParams(mContext, id,page),
                new IdentifySkillListener());
    }

    public void addLayout(){
        for (int i = 0; i < mResponse.data.size(); i++) {
            LinearLayout llAddItem = (LinearLayout) View.inflate(mContext, R.layout.item_identify_skill, null);
            TextView tvName = (TextView) llAddItem.findViewById(R.id.tv_name);
            TextView tvCount = (TextView) llAddItem.findViewById(R.id.tv_count);
            IdentifySkillData.DataEntity dataEntity = mResponse.data.get(i);
            tvName.setText(dataEntity.name);
            tvCount.setText(dataEntity.count+"篇");
            llAddItem.setTag(dataEntity.id);
            setOnClickListener(llAddItem);
            llAddParent.addView(llAddItem);
        }
    }

    private class SkillListener extends com.lidroid.xutils.http.callback.RequestCallBack<String> implements StringToBeanTask.ConvertListener<IdentifySkillData>{

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifySkillData> task = new StringToBeanTask<>(IdentifySkillData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            //数据请求错误
            DialogUtils.showShortPromptToast(findActivity, R.string.request_error);
        }
        @Override
        public void onConvertSuccess(IdentifySkillData response) {
             if (response != null){
                 if (!response.error){
                     mResponse = response;
                     addLayout();
                 }else{
                     DialogUtils.showShortPromptToast(findActivity, response.message);
                 }
             }
        }

        @Override
        public void onConvertFailed() {

        }


    }

    private class IdentifySkillListener extends com.lidroid.xutils.http.callback.RequestCallBack<String> implements StringToBeanTask.ConvertListener<InfoListData>{
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<InfoListData> task = new StringToBeanTask<>(InfoListData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
        @Override
        public void onConvertSuccess(InfoListData response) {
            Intent intent = new Intent(getActivity(),InfoActivity.class);
            intent.putExtra(IntentConstant.IDENTIFY_SKILL_DATA, response);
            intent.putExtra(IntentConstant.IDENTIFY_SKILL_TYPE,mIdentifyType);
            intent.putExtra(IntentConstant.FIND_CHILD_TITLE,mResponse.data.get(Integer.parseInt(mIdentifyType) - 1).name);
            jump(intent);
        }

        @Override
        public void onConvertFailed() {

        }
    }
}
