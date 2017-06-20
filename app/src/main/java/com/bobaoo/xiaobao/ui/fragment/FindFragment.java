 package com.bobaoo.xiaobao.ui.fragment;
 import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.activity.FindActivity;
import com.bobaoo.xiaobao.utils.UmengUtils;

 /**
  * Created by Ameng on 2016/3/24.
  */
 public class FindFragment extends BaseFragment {

     public FindFragment() {
     }

     @Override
     protected void getArgumentsData() {
     }

     @Override
     protected void initData() {
     }

     @Override
     protected int setLayoutViewId() {
         return R.layout.fragment_find;
     }

     @Override
     protected void initContent() {
         LinearLayout llFindSkill = (LinearLayout) mRootView.findViewById(R.id.ll_find_skill);
         LinearLayout llFindDynamic = (LinearLayout) mRootView.findViewById(R.id.ll_find_dynamic);
         LinearLayout llFindMeet = (LinearLayout) mRootView.findViewById(R.id.ll_find_meet);
         setOnClickListener(llFindSkill,llFindDynamic,llFindMeet);
     }

     @Override
     protected void loadData() {
     }

     @Override
     protected void attachData() {
     }

     @Override
     public void onClick(View view) {
         switch (view.getId()) {
             case R.id.ll_find_skill:
                 Intent intent3 = new Intent(mContext,FindActivity.class);
                 intent3.putExtra(AppConstant.INTENT_FRAGMENT_TYPE,AppConstant.INTENT_FRAGMENT_TYPE_3);
                 startActivity(intent3);
                 break;
             case R.id.ll_find_dynamic:
                 UmengUtils.onEvent(mContext, EventEnum.InfoFragmentClick);
                 Intent intent = new Intent(mContext,InfoActivity.class);
//                 intent1.putExtra(AppConstant.INTENT_FRAGMENT_TYPE,AppConstant.INTENT_FRAGMENT_TYPE_1);
//                 startActivity(intent1);
                 intent.putExtra(IntentConstant.FIND_CHILD_TITLE,"市场动态");
                 startActivity(intent);
                 break;
             case R.id.ll_find_meet:
                 UmengUtils.onEvent(mContext, EventEnum.IdentifyMeetClick);
                 Intent intent2 = new Intent(mContext,FindActivity.class);
                 intent2.putExtra(AppConstant.INTENT_FRAGMENT_TYPE,AppConstant.INTENT_FRAGMENT_TYPE_2);
                 startActivity(intent2);
                 break;
             default:
                 super.onClick(view);
                 break;
         }
     }
 }
