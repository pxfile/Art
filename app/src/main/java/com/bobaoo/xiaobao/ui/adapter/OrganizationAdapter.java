package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.ui.activity.ExpertListActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by star on 15/6/8.
 */
public class OrganizationAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private ArrayList<Data> mList;

    public OrganizationAdapter() {
        mList = new ArrayList<>();
        mList.add(new Data(R.drawable.icon_capital_museum, R.drawable.icon_capital_museum_press, R.string.capital_museum, 2));
        mList.add(new Data(R.drawable.icon_palace_museum, R.drawable.icon_palace_museum_press, R.string.palace_museum, 1));
        mList.add(new Data(R.drawable.icon_national_museum, R.drawable.icon_national_museum_press, R.string.national_museum, 3));
        mList.add(new Data(R.drawable.icon_cultural_relics_bureau, R.drawable.icon_cultural_relics_bureau_press,
                R.string.cultural_relics_bureau, 4));
        mList.add(new Data(R.drawable.icon_association_school, R.drawable.icon_association_school_press, R.string.association_school, 5));
        mList.add(new Data(R.drawable.icon_caoss, R.drawable.icon_caoss_press, R.string.chinese_academy_of_social_sciences, 6));
        mList.add(new Data(R.drawable.icon_deal, R.drawable.icon_deal_press, R.string.deal, 7));
        mList.add(new Data(R.drawable.icon_collect_world, R.drawable.icon_collect_world_press, R.string.collect, 8));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Data getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(parent.getContext(), R.layout.list_item_organization, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_category);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_category);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.imageView.setImageResource(getItem(position).imageRes);
        holder.textView.setText(getItem(position).descRes);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(parent.getContext(), ExpertListActivity.class);
        intent.putExtra(IntentConstant.ORGANIZATION_ID, getItem(position).id);
        intent.putExtra(IntentConstant.ORGANIZATION_NAME, getItem(position).descRes);
        ActivityUtils.jump(parent.getContext(), intent);
        HashMap<String,String> map = new HashMap<>();
        map.put(UmengConstants.KEY_ORGANAIZATION_ID,getItem(position).id+"");
        UmengUtils.onEvent(parent.getContext(), EventEnum.ExpertPageOrganizationItemClick,map);
    }

    private class Holder {
        private ImageView imageView;
        private TextView textView;
    }

    private class Data {
        private int imageRes;
        private int imagePressRes;
        private int descRes;
        private int id;

        private Data(int imageRes, int imagePressRes, int descRes, int id) {
            this.imageRes = imageRes;
            this.imagePressRes = imagePressRes;
            this.descRes = descRes;
            this.id = id;
        }

    }
}
