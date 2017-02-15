package com.atguigu.fx.controller.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.activity.InviteActivity;
import com.atguigu.fx.utils.ShowToast;
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

    @Override
    protected void initView() {
        super.initView();

        //初始化头布局
        View view = View.inflate(getActivity(), R.layout.fragment_contact_list_head, null);
        ButterKnife.bind(this,view);
        //添加头布局
        listView.addHeaderView(view);
        //添加actionbar右侧的加号
        titleBar.setRightImageResource(R.mipmap.em_add);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
//        return rootView;
//    }


    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                ShowToast.show(getActivity(), "aaa");
                break;
            case R.id.ll_groups:
                ShowToast.show(getActivity(), "lll");
                break;
        }
    }
}
