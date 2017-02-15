package com.atguigu.fx.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.fragment.ChatFragment;
import com.atguigu.fx.controller.fragment.ContactListFragment;
import com.atguigu.fx.controller.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        initData();

        initListener();

    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Fragment fragment = null;

                switch (checkedId){
                    //会话列表界面
                    case R.id.rb_main_chat:
                        fragment = chatFragment;
                        break;

                    //联系人列表界面
                    case R.id.rb_main_contact:
                        fragment = contactListFragment;
                        break;

                    //设置界面
                    case R.id.rb_main_setting:
                        fragment = settingFragment;
                        break;
                }
                if (fragment == null){
                    return;
                }
                //实现Fragment切换的方法
                switchFragment (fragment);
            }
        });
        //默认选择会话列表界面
        rg_main.check(R.id.rb_main_chat);
    }

    //实现Fragment切换的方法
    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData() {

        //创建三个Fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }


}
