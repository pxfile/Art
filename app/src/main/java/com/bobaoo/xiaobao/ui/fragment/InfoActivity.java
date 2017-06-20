package com.bobaoo.xiaobao.ui.fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.InfoBannerResponse;
import com.bobaoo.xiaobao.domain.InfoListData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.BaseActivity;
import com.bobaoo.xiaobao.ui.activity.ExpertDetailActivity;
import com.bobaoo.xiaobao.ui.activity.InfoDetailActivity;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.ui.activity.WebViewActivity;
import com.bobaoo.xiaobao.ui.adapter.HeaderAdapter;
import com.bobaoo.xiaobao.ui.adapter.InfoAdapter;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/5/29.
 */
public class InfoActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
    public static String IS_CLICK_COMMENT="IS_CLICK_COMMENT";
    private RecyclerView mInfoView;
    private SwipyRefreshLayout mRefreshLayout;
    private InfoAdapter mInfoAdapter;
    private HeaderAdapter mHeaderAdapter;
    private InfoListListener mListener;
    private List<InfoListData.DataEntity.ListEntity> mList;
    private int mCurrentPage;
    private SimpleDraweeView mHeadView;
    private InfoListData data;
    private String mIdentifyType;
    private String mTitle;

    @Override
    protected void getIntentData() {
        data = (InfoListData) getIntent().getSerializableExtra(IntentConstant.IDENTIFY_SKILL_DATA);
        mIdentifyType = getIntent().getStringExtra(IntentConstant.IDENTIFY_SKILL_TYPE);
        mTitle = getIntent().getStringExtra(IntentConstant.FIND_CHILD_TITLE);
    }

    @Override
    protected void initData() {
        mCurrentPage = 1;
        mInfoAdapter = new InfoAdapter();
        mListener = new InfoListListener();
        loadData();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initTitle() {

    }

    private TextView mTvBack;
    private TextView mTvTitle;

    @Override
    protected void initContent() {
        mTvBack = (TextView) mRootView.findViewById(R.id.tv_back);
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);

        setOnClickListener(mTvBack);
        mTvTitle.setText(mTitle);
        // 下拉刷新，下拉加载更多
        mRefreshLayout = (SwipyRefreshLayout) mRootView.findViewById(R.id.srl_info);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        // 专家列表
        mInfoView = (RecyclerView) mRootView.findViewById(R.id.rv_info);
        // 设置纵向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mInfoView.setLayoutManager(manager);
        //如果数据不为空则是从鉴定技巧传过来的数据，不加载头部信息
        if (data != null){
            mInfoView.setAdapter(mInfoAdapter);
            return;
        }
        // 设置adapter
        mHeaderAdapter = new HeaderAdapter(mInfoAdapter);
        //构建HeadView
        mHeadView = new SimpleDraweeView(this);
        mHeadView.setPadding(0,0,0, (int) SizeUtils.dp2Px(getResources(), 10));
        mHeadView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        int bannerWidth = (int) SizeUtils.dp2Px(getResources(), 360);
        int bannerHeight = (int) SizeUtils.dp2Px(getResources(), 160);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(bannerWidth, bannerHeight);
        mHeadView.setLayoutParams(lp);
        mHeaderAdapter.bindHeadView(mHeadView);
        mInfoView.setAdapter(mHeaderAdapter);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    private void loadBanner() {
        // 请求咨询列表数据
        mHandlerList.add(
                new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getInfoBannerParams(mContext), new InfoBannerListener()));
    }

    @Override
    protected void refreshData() {
        if (mCurrentPage == 1) {
            if (mList != null) {
                mInfoAdapter.resetData(mList);
                if (data != null){
                    mInfoAdapter.notifyDataSetChanged();
                }else{
                    mHeaderAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (mList != null) {
                mInfoAdapter.addData(mList);
                if (data != null){
                    mInfoAdapter.notifyDataSetChanged();
                }else{
                    mHeaderAdapter.notifyDataSetChanged();
                }
            }
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if(swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP){
            //下拉刷新
            mCurrentPage = 1;
            loadData();
        }else{
            mCurrentPage++;
            loadData();
        }
    }

    private void loadData() {
        if (data == null){
            // 请求咨询列表数据
            mHandlerList.add(
                    new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getInfoListParams(mContext, mCurrentPage), mListener));
            loadBanner();
            return;
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getIdentifySkillDetailParams(mContext, mIdentifyType,mCurrentPage),
                new IdentifySkillListener());
    }

    private class InfoBannerListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<InfoBannerResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<InfoBannerResponse> task = new StringToBeanTask<>(InfoBannerResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }

        @Override
        public void onConvertSuccess(InfoBannerResponse response) {
            //刷新header
            if(response != null && !response.isError() && response.getData() != null) {
                String imgUrl = response.getData().getUrl();
                final InfoBannerResponse.DataEntity data = response.getData();
                mHeadView.setImageURI(Uri.parse(imgUrl));
                mHeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        switch (data.getType()) {
                            case "expert":
                                intent = new Intent(mContext, ExpertDetailActivity.class);
                                intent.putExtra(IntentConstant.EXPERT_ID, data.getId());
                                jump(intent);
                                hashMap.clear();
                                hashMap.put(UmengConstants.KEY_INFO_BANNER_TYPE_EXPERT,data.getId());
                                UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick,hashMap);
                                break;
                            case "order":
                                intent = new Intent(mContext, OrderDetailActivity.class);
                                intent.putExtra(IntentConstant.ORDER_ID, data.getId());
                                intent.putExtra(IntentConstant.CHARGED_STATE,"1");
                                jump(intent);
                                hashMap.clear();
                                hashMap.put(UmengConstants.KEY_INFO_BANNER_TYPE_IDENTIFY,data.getId());
                                UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick,hashMap);
                                break;
                            case "info":
                                intent = new Intent(mContext, InfoDetailActivity.class);
                                intent.putExtra(IntentConstant.INFO_ID, data.getId());
                                jump(intent);
                                hashMap.clear();
                                hashMap.put(UmengConstants.KEY_INFO_BANNER_TYPE_INFO,data.getId());
                                UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick,hashMap);
                                break;
                            case "url":
                                intent = new Intent(mContext, WebViewActivity.class);
                                intent.putExtra(IntentConstant.WEB_URL, data.getId());
                                intent.putExtra(IntentConstant.WEB_TITLE, data.getName());
                                jump(intent);
                                hashMap.clear();
                                hashMap.put(UmengConstants.KEY_INFO_BANNER_TYPE_URL,data.getId());
                                UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick,hashMap);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }

        @Override
        public void onConvertFailed() {

        }
    }

    /**
     * 请求信息列表数据
     */
    private class InfoListListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<InfoListData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<InfoListData> task = new StringToBeanTask<>(InfoListData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (InfoActivity.this != null) {
                if(mList != null){
                    mList.clear();
                }
                refreshData();
            }
        }

        @Override
        public void onConvertSuccess(InfoListData response) {
            if (InfoActivity.this != null) {
                mList = response.getData().getList();
                refreshData();
            }
        }

        @Override
        public void onConvertFailed() {
            if (InfoActivity.this != null) {
                if(mList != null){
                    mList.clear();
                }
                refreshData();
            }
        }
    }

    @Override
    public void onClick(View view) {
        finish();
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
            data = response;
            mList = response.getData().getList();
            refreshData();
        }

        @Override
        public void onConvertFailed() {

        }
    }

}
