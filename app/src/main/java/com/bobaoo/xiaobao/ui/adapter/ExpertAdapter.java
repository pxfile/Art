package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.ExpertData;
import com.bobaoo.xiaobao.ui.activity.ExpertDetailActivity;
import com.bobaoo.xiaobao.ui.activity.SubmitOrderActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/6/8.
 */
public class ExpertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ArrayList<ExpertData.DataEntity> mList;

    private OrganizationAdapter mAdapter;

    private ToggleButton.OnToggleChanged mExpertListener;

    private boolean isShowFooter;

//    private boolean mIsOnlineExpert;

    private String mIdentifyMethodSwitchInfos;
    private String mIdentifyMethodPrices;

    private final String CAN_NOT_APPOINTMENT = "0";
    public ExpertAdapter() {
        mList = new ArrayList<>();
        isShowFooter = false;
    }

    public ExpertAdapter(OrganizationAdapter adapter, ToggleButton.OnToggleChanged listener) {
        mExpertListener = listener;
        mList = new ArrayList<>();
        mAdapter = adapter;
        isShowFooter = false;
    }

//    public void setIsOnLineExpert(boolean isOnlineExpert) {
//        mIsOnlineExpert = isOnlineExpert;
//    }

    public void addData(List<ExpertData.DataEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void resetData(List<ExpertData.DataEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setShowFooter(boolean showFooter) {
        this.isShowFooter = showFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mAdapter != null) {
            return Types.header;
        } else if (mAdapter != null ? position - 1 == mList.size() : position == mList.size()) {
            return Types.footer;
        } else {
            return Types.normal;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Types.header:
                view = View.inflate(parent.getContext(), R.layout.header_expert, null);
                return new HeaderHolder(view);
            case Types.normal:
                view = View.inflate(parent.getContext(), R.layout.list_item_expert, null);
                return new ContentHolder(view);
            case Types.footer:
                view = View.inflate(parent.getContext(), R.layout.list_item_footer_refresh, null);
                return new FooterHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.organizationView.setAdapter(mAdapter);
            headerHolder.organizationView.setOnItemClickListener(mAdapter);
            headerHolder.showOrganizationView.setOnToggleChanged(mExpertListener);
        }
        if (holder instanceof ContentHolder) {
            int index;
            if (mAdapter == null) {
                index = position;
            } else {
                // 减掉header的位置
                index = position - 1;
            }
            ContentHolder contentHolder = (ContentHolder) holder;
            contentHolder.portraitView.setImageURI(Uri.parse(mList.get(index).getHead_img()));
            if ("1".equals(mList.get(index).getTui())) {
                contentHolder.recommend.setVisibility(View.VISIBLE);
                contentHolder.mRbStars.setVisibility(View.GONE);
                contentHolder.labelView.setVisibility(View.VISIBLE);
            } else if (mList.get(index).getStar_level_num() == 6) {
                contentHolder.recommend.setVisibility(View.GONE);
                contentHolder.labelView.setVisibility(View.VISIBLE);
                contentHolder.mRbStars.setVisibility(View.GONE);
            } else if (mList.get(index).getStar_level_num() == 0) {
                contentHolder.recommend.setVisibility(View.GONE);
                contentHolder.labelView.setVisibility(View.VISIBLE);
                contentHolder.mRbStars.setVisibility(View.GONE);
            } else {
                contentHolder.recommend.setVisibility(View.GONE);
                contentHolder.labelView.setVisibility(View.GONE);
                contentHolder.mRbStars.setVisibility(View.VISIBLE);
            }
            contentHolder.mAppointmentBtn.setVisibility(CAN_NOT_APPOINTMENT.equals(mList.get(index).getJbapp_yy()) ? View.GONE : View.VISIBLE);
            contentHolder.nameView.setText(mList.get(index).getName());
            contentHolder.descView.setText(mList.get(index).getHonors());
            contentHolder.labelView.setText(mList.get(index).getStar_level());
            contentHolder.mRbStars.setRating(mList.get(index).getStar_level_num());
//            contentHolder.containerView.setTag(mList.get(index).getId());
            contentHolder.containerView.setTag(index);
            contentHolder.containerView.setTag(R.id.tag_expert_state,mList.get(index).getState());
            contentHolder.containerView.setOnClickListener(this);
            contentHolder.mAppointmentBtn.setTag(index);
            contentHolder.mAppointmentBtn.setTag(R.id.tag_expert_name, mList.get(index).getName());
            contentHolder.mAppointmentBtn.setTag(R.id.tag_expert_id,mList.get(index).getId());
            contentHolder.mAppointmentBtn.setTag(R.id.tag_expert_state,mList.get(index).getState());
            contentHolder.mAppointmentBtn.setOnClickListener(this);
        }
        if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.progressBar.getProgress();
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (mAdapter == null) {
            count = mList.size();
        } else {
            // header的位置
            count = mList.size() + 1;
        }
        if (isShowFooter) {
            count++;
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        String state = (String) v.getTag(R.id.tag_expert_state);
        if(TextUtils.equals(state, "1")){
            DialogUtils.showShortPromptToast(v.getContext(), R.string.no_expert_exist);
            return;
        }
        if (v.getId() == R.id.expert_list_appointment_btn) {
            int index = (int) v.getTag();
            getExpertData(mList, index);//设置鉴定type列表和对应的价格列表
            Intent intent = new Intent(v.getContext(), SubmitOrderActivity.class);
            intent.putExtra(IntentConstant.EXPERT_ID,(String) v.getTag(R.id.tag_expert_id));
            intent.putExtra(IntentConstant.EXPERT_NAME,(String) v.getTag(R.id.tag_expert_name));
            intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
            intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
            ActivityUtils.jump(v.getContext(), intent);
        } else {
            Intent intent = new Intent(v.getContext(), ExpertDetailActivity.class);
            int pos = (int) v.getTag();
            ExpertData.DataEntity entity = mList.get(pos);
            String org = entity.getOrg();
            boolean isOnlineExpert = (org == null) ||TextUtils.equals("0", org);
            intent.putExtra(IntentConstant.EXPERT_ID, entity.getId());
            intent.putExtra(IntentConstant.IS_ONLINE_EXPERT, isOnlineExpert);
            ActivityUtils.jump(v.getContext(), intent);
            HashMap<String, String> map = new HashMap<>();
            map.put(UmengConstants.KEY_EXPERT_PAGE_ITEM_ID, entity.getId());
            UmengUtils.onEvent(v.getContext(), EventEnum.ExpertPageListItemClick, map);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private GridView organizationView;
        private ToggleButton showOrganizationView;

        public HeaderHolder(View itemView) {
            super(itemView);
            organizationView = (GridView) itemView.findViewById(R.id.fgv_organization);
            showOrganizationView = (ToggleButton) itemView.findViewById(R.id.tb_switch);
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private View containerView;
        private SimpleDraweeView portraitView;
        private View recommend;
        private TextView nameView;
        private TextView descView;
        private TextView labelView;
        private View attentionView;
        private View privateView;
        private View shareView;
        private View mAppointmentBtn;

        private RatingBar mRbStars;

        public ContentHolder(View itemView) {
            super(itemView);
            containerView = itemView;
            portraitView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_portrait);
            recommend = itemView.findViewById(R.id.iv_recommend);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
            descView = (TextView) itemView.findViewById(R.id.tv_desc);
            labelView = (TextView) itemView.findViewById(R.id.tv_label);
            attentionView = itemView.findViewById(R.id.tv_attention);
            privateView = itemView.findViewById(R.id.tv_private);
            shareView = itemView.findViewById(R.id.tv_commit);
            mRbStars = (RatingBar) itemView.findViewById(R.id.rb_label);
            mAppointmentBtn = itemView.findViewById(R.id.expert_list_appointment_btn);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb);
        }
    }

    private static class Types {
        private static final int header = 1;
        private static final int normal = 2;
        private static final int footer = 3;
    }

    /**
     * 抽取专家支持的鉴定方式及价格
     * @param mList
     * @param index
     */
    private void getExpertData(ArrayList<ExpertData.DataEntity> mList,int index){
        ExpertData.DataEntity data = mList.get(index);
        mIdentifyMethodSwitchInfos = StringUtils.getString(data.getJbapp_pt(), ",", data.getJbapp_js(), ",", data.getJbapp_sp(), ",", data.getJbapp_yy());
        mIdentifyMethodPrices = StringUtils.getString(data.getPt_price(), ",", data.getJs_price(), ",", data.getSp_price(), ",", data.getYy_price());
    }
}
