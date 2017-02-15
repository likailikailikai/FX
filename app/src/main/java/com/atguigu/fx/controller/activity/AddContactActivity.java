package com.atguigu.fx.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.fx.R;
import com.atguigu.fx.modle.Modle;
import com.atguigu.fx.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity {


    @Bind(R.id.invite_btn_save)
    Button inviteBtnSave;
    @Bind(R.id.invite_et_search)
    EditText inviteEtSearch;
    @Bind(R.id.invite_tv_username)
    TextView inviteTvUsername;
    @Bind(R.id.invite_btn_add)
    Button inviteBtnAdd;
    @Bind(R.id.invite_ll_item)
    LinearLayout inviteLlItem;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.invite_btn_save, R.id.invite_btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_btn_save:

                if (verify()){
                    inviteLlItem.setVisibility(View.VISIBLE);
                    inviteTvUsername.setText(username);
                }else{
                    inviteLlItem.setVisibility(View.GONE);
                }
                break;
            case R.id.invite_btn_add:
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager()
                                    .addContact(username,"添加好友");
                            ShowToast.showUI(AddContactActivity.this,"添加成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(AddContactActivity.this,"添加失败"+e.getMessage());
                        }
                    }
                });
                break;
        }
    }

    private boolean verify() {
        username = inviteEtSearch.getText().toString().trim();
        if (ShowToast.isEmpty(username)){
            ShowToast.show(this,"输入用户名不能为空");
            return false;
        }
        return true;
    }
}
