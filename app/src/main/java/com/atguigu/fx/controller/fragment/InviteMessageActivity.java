package com.atguigu.fx.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.adapter.InviteMessageAdapter;
import com.atguigu.fx.modle.Modle;
import com.atguigu.fx.modle.bean.InvitationInfo;
import com.atguigu.fx.utils.Contacts;
import com.atguigu.fx.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InviteMessageActivity extends AppCompatActivity {

    @Bind(R.id.invite_msg_lv)
    ListView inviteMsgLv;
    private InviteMessageAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };
    private LocalBroadcastManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    private void initData() {
        manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(receiver,new IntentFilter(Contacts.NEW_INVITE_CHANGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        manager.unregisterReceiver(receiver);
    }

    private void initView() {

        adapter = new InviteMessageAdapter(this, new InviteMessageAdapter.OnInviteChangeListener() {
            @Override
            public void onAccept(final InvitationInfo info) {

                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //网络 通知环信服务器
                        try {
                            EMClient.getInstance().contactManager()
                                    .acceptInvitation(info.getUserInfo().getHxid());
                            //本地
                            Modle.getInstance().getDbManager()
                                    .getInvitationDao()
                                    .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT,
                                            info.getUserInfo().getHxid());

                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"接受失败"+e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onReject(final InvitationInfo info) {

                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //网络通知环信
                        try {
                            EMClient.getInstance().contactManager()
                                    .declineInvitation(info.getUserInfo().getHxid());
                            //本地
                            Modle.getInstance().getDbManager().getInvitationDao()
                                    .removeInvitation(info.getUserInfo().getHxid());
                            Modle.getInstance().getDbManager().getContactDao()
                                    .deleteContactByHxId(info.getUserInfo().getHxid());
                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"拒绝成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"拒绝失败"+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onInviteAccept(final InvitationInfo info) {
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //网络
                            EMClient.getInstance().groupManager()
                                    .acceptInvitation(info.getGroupInfo().getGroupid(),
                                            info.getGroupInfo().getInvitePerson());
                            //本地
                            info.setStatus(InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                            Modle.getInstance().getDbManager().getInvitationDao()
                                    .addInvitation(info);
                         /*   Modle.getInstance().getDbManager()
                                    .getInvitationDao()
                                    .updateInvitationStatus(
                                            InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE,
                                            info.getUserInfo().getHxid()
                                            );*/
                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"接受失败"+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onInviteReject(final InvitationInfo info) {

                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        //网络
                        try {
                            EMClient.getInstance().groupManager()
                                    .declineInvitation(
                                            info.getGroupInfo().getGroupid(),
                                            info.getGroupInfo().getInvitePerson(),"");

                            //本地
                            info.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
                            Modle.getInstance().getDbManager().getInvitationDao()
                                    .addInvitation(info);
                            //内存和页面
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"拒绝成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"拒绝失败"+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onApplicationAccept(final InvitationInfo info) {

                //网络
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //网络
                        try {
                            EMClient.getInstance().groupManager()
                                    .acceptApplication(info.getGroupInfo().getGroupName(),
                                            info.getGroupInfo().getInvitePerson());

                            //本地
                            info.setStatus(InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                            Modle.getInstance().getDbManager().getInvitationDao()
                                    .addInvitation(info);
                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"接受失败"+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onApplicationReject(final InvitationInfo info) {

                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        //网络
                        try {
                            EMClient.getInstance().groupManager()
                                    .declineApplication(info.getGroupInfo().getGroupName(),
                                            info.getGroupInfo().getInvitePerson(),"");

                            //本地
                            info.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
                            Modle.getInstance().getDbManager().getInvitationDao()
                                    .addInvitation(info);
                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"接受失败"+e.getMessage());
                        }
                    }
                });
            }
        });

        inviteMsgLv.setAdapter(adapter);

        refresh();
    }


    private void refresh() {

        //获取数据
        List<InvitationInfo> invitations = Modle.getInstance().getDbManager().getInvitationDao()
                .getInvitations();
        //刷新数据
        if (invitations == null){
            return;
        }
        adapter.refresh(invitations);
    }
}
