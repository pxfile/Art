package com.bobaoo.xiaobao.ui.dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.bobaoo.xiaobao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ChooseIdentifyTypeDialog extends BaseCustomerDialog {

    private String[] mList;
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<HashMap<String,String>> mHashMap = new ArrayList<>();
    public ChooseIdentifyTypeDialog(Context context,String[] mList,AdapterView.OnItemClickListener mListener) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        this.mList = mList;
        this.mListener = mListener;
    }


    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_choose_identify_type;
    }

    @Override
    protected void initView() {
        ListView mTypeList = (ListView) findViewById(R.id.lv_identify_data);
        for (int i = 0;i < mList.length; i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("tv",mList[i]);
            mHashMap.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext,mHashMap,R.layout.dialog_list_item_choose,new String[]{"tv"},new int[]{R.id.item_list_tv});
        mTypeList.setAdapter(simpleAdapter);
        mTypeList.setOnItemClickListener(mListener);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View v) {
    }
}
