package com.atguigu.fx.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.activity.InviteActivity;
import com.atguigu.fx.modle.Modle;
import com.atguigu.fx.modle.bean.UserInfo;
import com.atguigu.fx.utils.Contacts;
import com.atguigu.fx.utils.ShowToast;
import com.atguigu.fx.utils.SpUtils;
import com.baidu.platform.comapi.map.E;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private LocalBroadcastManager manager;
    private List<UserInfo> contacts;
    private BroadcastReceiver contactReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContact();
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
        manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(recevier, new IntentFilter(Contacts.NEW_INVITE_CHANGE));
        manager.registerReceiver(contactReceiver, new IntentFilter(Contacts.CONTACT_CHANGE));
        initData();

        initListener();
    }

    private void initListener() {

        //联系人点击监听
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

                //跳转
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //传参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    return false;
                }

                showDialog(position);
                return true;
            }
        });
    }

    private void showDialog(final int position) {

        new AlertDialog.Builder(getActivity()).setMessage("确定要删除吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteContact(position);
            }
        }).create().show();
    }

    private void deleteContact(final int position) {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {

                //网络删除
                try {
                    //获取到这个用户的环信ID
                    final UserInfo userInfo = contacts.get(position - 1);
                    EMClient.getInstance().contactManager().deleteContact(userInfo.getHxid());
                    //本地删除 删除联系人
                    Modle.getInstance().getDbManager().getContactDao()
                            .deleteContactByHxId(userInfo.getHxid());
                    //删除邀请信息
                    Modle.getInstance().getDbManager().getInvitationDao()
                            .removeInvitation(userInfo.getHxid());
                    //刷新
                    refreshContact();
                    ShowToast.showUI(getActivity(), "删除成功");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(getActivity(), "删除失败" + e.getMessage());
                }
            }
        });
    }


    @Override
    protected void setUpView() {
        super.setUpView();

    }

    private void initData() {
        //获取联系人
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {

                //从服务器获取联系人
                try {
                    List<String> contacts =
                            EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //保存数据库
                    //转化数据
                    List<UserInfo> userInfos = new ArrayList<UserInfo>();

                    for (int i = 0; i < contacts.size(); i++) {
                        userInfos.add(new UserInfo(contacts.get(i)));
                    }

                    Modle.getInstance().getDbManager().getContactDao()
                            .saveContacts(userInfos, true);
                    //内存和网页
                    //判断activity是否为空
                    if(getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshContact();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshContact() {
        //从本地获取数据
        contacts = Modle.getInstance().getDbManager().getContactDao()
                .getContacts();
        //校验
        if (contacts == null) {
            return;
        }
        //转换数据
        Map<String, EaseUser> maps = new HashMap<>();
        for (UserInfo userInfo : contacts) {
            EaseUser user = new EaseUser(userInfo.getHxid());
            maps.put(userInfo.getHxid(), user);
        }
        setContactsMap(maps);
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContact();
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

                //跳转到群列表
                Intent groupIntent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(groupIntent);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        manager.unregisterReceiver(recevier);
    }

    public void isShow() {
//        boolean isShow = SpUtils.getInstace().getBoolean(SpUtils.NEW_INVITE, false);
//
//        Log.i("aaaaaaaaaaaaaaaaaaa", "isShow: " + isShow);
//        Log.i("aaaaaaaaaaaaaaaaaaa", "contanctIvInvite: " + contanctIvInvite);
        boolean isShow = SpUtils.getInstace().getBoolean(SpUtils.NEW_INVITE, false);


        contanctIvInvite.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
