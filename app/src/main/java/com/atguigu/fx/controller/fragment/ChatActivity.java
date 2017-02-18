package com.atguigu.fx.controller.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.atguigu.fx.R;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseContactList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.chat_fl)
    FrameLayout chatFl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        //聊天的Fragment
        EaseChatFragment chatFragment = new EaseChatFragment();

        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_fl,chatFragment).commit();
    }
}
