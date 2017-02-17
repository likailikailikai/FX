package com.atguigu.fx.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.activity.InviteActivity;
import com.atguigu.fx.utils.Contacts;
import com.atguigu.fx.utils.ShowToast;
import com.atguigu.fx.utils.SpUtils;
import com.hyphenate.easeui.ui.EaseContactListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 情v枫 on 2017/2/14.
 */

public class ContactListFragment extends EaseContactListFragment {

    @Bind(R.id.contanct_iv_invite)
    ImageView contanctIvInvite;
    @Bind(R.id.ll_new_friends)
    LinearLayout llNewFriends;
    @Bind(R.id.ll_groups)
    LinearLayout llGroups;
    private BroadcastReceiver recevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShow();
        }
    };

    @Override
    protected void initView() {
        super.initView();

        //初始化头布局
        View view = View.inflate(getActivity(), R.layout.fragment_contact_list_head, null);
        ButterKnife.bind(this, view);
        //添加头布局
        listView.addHeaderView(view);
        //添加actionbar右侧的加号
        titleBar.setRightImageResource(R.mipmap.em_add);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到邀请界面
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });
        //初始化小红点
        isShow();

        //注册广播
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());

        manager.registerReceiver(recevier, new IntentFilter(Contacts.NEW_INVITE_CHANGE));
    }

    @Override
    protected void setUpView() {
        super.setUpView();

    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                //隐藏小红点
                SpUtils.getInstace().save(SpUtils.NEW_INVITE, false);
                isShow();
                //跳转
                Intent intent = new Intent(getActivity(), InviteMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_groups:
                ShowToast.show(getActivity(), "bbb");
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void isShow() {
        boolean isShow = SpUtils.getInstace().getBoolean(SpUtils.NEW_INVITE, false);

        Log.i("aaaaaaaaaaaaaaaaaaa", "isShow: " + isShow);
        Log.i("aaaaaaaaaaaaaaaaaaa", "contanctIvInvite: " + contanctIvInvite);

        contanctIvInvite.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
