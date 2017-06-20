package com.bobaoo.xiaobao.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.ui.fragment.IdentifySkillFragment;
import com.bobaoo.xiaobao.ui.fragment.MeetFragment;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
/**
 * Created by Ameng on 2016/3/24.
 */
public class FindActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        int fragmentType = getIntent().getIntExtra(AppConstant.INTENT_FRAGMENT_TYPE, 0);
        if (AppConstant.INTENT_FRAGMENT_TYPE_1 == fragmentType){
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fl_container, new InfoActivity());
//            fragmentTransaction.commit();
//            startActivity(new Intent(this, InfoActivity.class));
        }else if (AppConstant.INTENT_FRAGMENT_TYPE_2 == fragmentType){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, new MeetFragment(this));
            fragmentTransaction.commit();
        }else if (AppConstant.INTENT_FRAGMENT_TYPE_3 == fragmentType){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, new IdentifySkillFragment(this));
            fragmentTransaction.commit();
        }
    }
}
