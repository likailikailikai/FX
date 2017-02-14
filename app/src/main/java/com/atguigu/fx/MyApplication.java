package com.atguigu.fx;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by 情v枫 on 2017/2/14.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
            super.onCreate();

        //初始化环信SDK
        initHXSdk();
    }

    private void initHXSdk() {

        EMOptions options = new EMOptions();
        //不总是一直接受所有邀请
        options.setAcceptInvitationAlways(false);
        //不自动接受群邀请信息
        options.setAutoAcceptGroupInvitation(false);

        // 初始化EaseUI
        EaseUI.getInstance().init(this,options);
    }
}
