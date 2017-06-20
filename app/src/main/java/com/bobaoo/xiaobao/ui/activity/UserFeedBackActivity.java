package com.bobaoo.xiaobao.ui.activity;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.NetWorkRequestConstants;
import com.bobaoo.xiaobao.domain.UserFeedBackData;
import com.bobaoo.xiaobao.domain.UserFeedBackDatas;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.AppUtils;
import com.bobaoo.xiaobao.utils.DeviceUtil;
import com.bobaoo.xiaobao.utils.NetUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by you on 2015/6/3.
 */
public class UserFeedBackActivity extends BaseActivity {
    private ListView mFeedBackLv;
    private List<UserFeedBackDatas.DataEntity> mData = new LinkedList<>();
    private final int SUB_TYPE_TIME = 0;
    private final int SUB_TYPE_MSG = 1;
    private UserFeedBackAdapter mAdapter;
    private ImageView mSendBtn;
    private EditText mSendMsgEt;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_feedback_layout;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("意见反馈");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mFeedBackLv = (ListView) findViewById(R.id.lv_price_content);
        mSendBtn = (ImageView) findViewById(R.id.iv_sendmsg);
        mSendMsgEt = (EditText) findViewById(R.id.et_message);
        mAdapter = new UserFeedBackAdapter();
        mFeedBackLv.setAdapter(mAdapter);
        startUserFeedBackRequest();
        setOnClickListener(mSendBtn);
    }

    private void startSendUserMsg(final int pos, String content) {
        updateListViewPartly(pos, STATE.LOADING);
        mStatesMap.put(pos, STATE.LOADING);
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.POST, NetConstant.HOST, NetConstant.getSendUserMsgParams(mContext, content), new SendUserMsgListener(pos));
    }

    /**
     * 用户发送消息+失败重发测试
     */
    enum STATE {
        LOADING, FINISH, FAIL
    }

    private HashMap<Integer, STATE> mStatesMap = new HashMap<>();

    private void updateListViewPartly(int position, STATE state) {
        Log.e("UpdatePartly", "update" + position);
        int firstVisiblePosition = mFeedBackLv.getFirstVisiblePosition();
        int lastVisiblePosition = mFeedBackLv.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = mFeedBackLv.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof ViewHolder) {
                ViewHolder vh = (ViewHolder) view.getTag();
                TextView tv = vh.tv_send_state_test;
                if (state == STATE.LOADING) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("正在加载");
                } else if (state == STATE.FINISH) {
                    tv.setText("加载完成");
                    tv.setVisibility(View.GONE);
                } else if (state == STATE.FAIL) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("加载失败");
                }
            }
        }
    }


    private void startUserFeedBackRequest() {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserFeedBackParams(mContext), new UserFeedBackListener());
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
            case R.id.iv_sendmsg:
                sendMessage();
            default:
                super.onClick(view);
                break;
        }
    }

    private void sendMessage() {
        // 收起软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        String content = mSendMsgEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(UserFeedBackActivity.this, "发送内容不能为空,请重新发送", Toast.LENGTH_SHORT).show();
            return;
        }
        mSendMsgEt.setText("");
        UserFeedBackDatas.DataEntity dataEntity = new UserFeedBackDatas.DataEntity();
        dataEntity.setContent(content);
        dataEntity.setSend_type(1);
        dataEntity.setBelong("0");
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(dataEntity);
        mFeedBackLv.smoothScrollToPosition(mData.size() - 1);
        startSendUserMsg(mData.size() - 1, content);
        mAdapter.notifyDataSetChanged();
    }

    private class UserFeedBackAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData == null ? null : mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(viewGroup.getContext(), R.layout.item_user_feedback, null);
                holder.tv_user_feedback_time = (TextView) view.findViewById(R.id.tv_user_feedback_time);
                holder.ll_server_msg_container = (LinearLayout) view.findViewById(R.id.ll_server_msg_container);
                holder.tv_feedback_from_server = (TextView) view.findViewById(R.id.tv_feedback_from_server);
                holder.ll_user_msg_container = (LinearLayout) view.findViewById(R.id.ll_user_msg_container);
                holder.tv_feedback_from_user = (TextView) view.findViewById(R.id.tv_feedback_from_user);
                holder.portrait = (SimpleDraweeView) view.findViewById(R.id.sdv_portrait);
                //User消息发送状态提示
                holder.tv_send_state_test = (TextView) view.findViewById(R.id.tv_send_state_test);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            UserFeedBackDatas.DataEntity dataEntity = mData.get(i);
            if (SUB_TYPE_TIME == dataEntity.getSend_type()) {
                holder.tv_user_feedback_time.setVisibility(View.VISIBLE);
                holder.ll_server_msg_container.setVisibility(View.GONE);
                holder.ll_user_msg_container.setVisibility(View.GONE);
                holder.tv_user_feedback_time.setText(dataEntity.getAddtime());
            } else {
                holder.ll_user_msg_container.setTag(i);
                holder.tv_user_feedback_time.setVisibility(View.GONE);
                String sender = dataEntity.getBelong();
                int belong = Integer.parseInt(sender);
                if (belong > 0) {
                    holder.ll_server_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_user_msg_container.setVisibility(View.GONE);
                    holder.tv_feedback_from_server.setText(dataEntity.getContent());
                } else {
                    holder.ll_user_msg_container.setVisibility(View.VISIBLE);
                    holder.ll_server_msg_container.setVisibility(View.GONE);
                    holder.tv_feedback_from_user.setText(dataEntity.getContent());
                    holder.portrait.setImageURI(Uri.parse(UserInfoUtils.getCacheHeadImagePath(viewGroup.getContext())));
                }

                if (mStatesMap.get(i) == STATE.LOADING) {
                    holder.tv_send_state_test.setVisibility(View.VISIBLE);
                    holder.tv_send_state_test.setText("正在加载");
                } else if (mStatesMap.get(i) == STATE.FINISH) {
                    holder.tv_send_state_test.setVisibility(View.GONE);
                    holder.tv_send_state_test.setText("");
                } else if (mStatesMap.get(i) == STATE.FAIL) {
                    holder.tv_send_state_test.setVisibility(View.VISIBLE);
                    holder.tv_send_state_test.setText("加载失败");
                    holder.ll_user_msg_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO
                            int clickPoS = (int) view.getTag();
                            UserFeedBackDatas.DataEntity data = mData.get(clickPoS);
                            if (mStatesMap.get(clickPoS) == STATE.FAIL) {
                                //如果失败点击重新发送
                                startSendUserMsg(clickPoS, data.getContent());
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    private class ViewHolder {
        TextView tv_user_feedback_time;
        LinearLayout ll_server_msg_container;
        TextView tv_feedback_from_server;
        LinearLayout ll_user_msg_container;
        TextView tv_feedback_from_user;
        TextView tv_send_state_test;
        SimpleDraweeView portrait;
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

    private class UserFeedBackListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserFeedBackDatas> {

        @Override
        public void onConvertSuccess(UserFeedBackDatas data) {
            if (data == null) {
                Toast.makeText(UserFeedBackActivity.this, "获取鉴宝信息失败..", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.isError()) {
            } else {
                mData = data.getData();
                if (mData != null) {
                    mAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mData.size(); i++) {
                        mStatesMap.put(i, STATE.FINISH);
                    }
                    mFeedBackLv.setSelection(mData.size() - 1);
                }
            }
        }

        @Override
        public void onConvertFailed() {

        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserFeedBackDatas> task = new StringToBeanTask<>(UserFeedBackDatas.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    }

    private class SendUserMsgListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserFeedBackData> {
        private int pos;

        public SendUserMsgListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onConvertSuccess(UserFeedBackData data) {
            if (data == null) {
                Toast.makeText(UserFeedBackActivity.this, "发送返回信息为空", Toast.LENGTH_SHORT).show();
                return;
            }
            updateListViewPartly(pos, STATE.FINISH);
            mStatesMap.put(pos, STATE.FINISH);
            Toast.makeText(UserFeedBackActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConvertFailed() {
            updateListViewPartly(pos, STATE.FAIL);
            mStatesMap.put(pos, STATE.FAIL);
            Toast.makeText(UserFeedBackActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserFeedBackData> task = new StringToBeanTask<>(UserFeedBackData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            updateListViewPartly(pos, STATE.FAIL);
            mStatesMap.put(pos, STATE.FAIL);
            Toast.makeText(UserFeedBackActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
        }
    }
}
