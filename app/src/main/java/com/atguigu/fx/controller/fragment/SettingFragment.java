package com.atguigu.fx.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.atguigu.fx.R;
import com.atguigu.fx.controller.activity.LoginActivity;
import com.atguigu.fx.modle.Modle;
import com.atguigu.fx.utils.ShowToast;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 情v枫 on 2017/2/14.
 */

public class SettingFragment extends Fragment {


    @Bind(R.id.bt_setting_out)
    Button btSettingOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                String currentUser = EMClient.getInstance().getCurrentUser();

                btSettingOut.setText("退出登录("+currentUser+")");
            }
        });
    }

    @OnClick(R.id.bt_setting_out)
    public void onClick(){

        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器退出登录
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //数据库关闭

                        //跳转结束
                        if (getActivity() == null){
                            return;
                        }

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        Modle.getInstance().exitLogin();
                        //跳转到登陆界面
                        ShowToast.showUI(getActivity(),"退出登录成功");
                        getActivity().finish();

                    }

                    @Override
                    public void onError(int i, String s) {

                        ShowToast.showUI(getActivity(),"退出登录失败" + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
