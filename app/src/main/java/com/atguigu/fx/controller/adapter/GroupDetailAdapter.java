package com.atguigu.fx.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.fx.R;
import com.atguigu.fx.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 情v枫 on 2017/2/21.
 */

public class GroupDetailAdapter extends BaseAdapter {

    private Context context;
    private boolean isModify;
    private List<UserInfo> userInfos;
    private boolean isDeleteModle = false;

    public GroupDetailAdapter(Context context, boolean isModify) {
        this.context = context;
        this.isModify = isModify;
        userInfos = new ArrayList<>();
    }

    public void refresh(List<UserInfo> userinfos) {

        if (userinfos == null || userinfos.size() == 0) {
            return;
        }
        //清除原有数据
        this.userInfos.clear();
        //添加加减号
        initUser();
        //添加群成员
        this.userInfos.addAll(0, userinfos);

    }

    private void initUser() {
        this.userInfos.add(new UserInfo("remove"));
        this.userInfos.add(0, new UserInfo("add"));
    }

    @Override
    public int getCount() {
        return userInfos == null ? 0 : userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_group_members, null);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isModify) {
            //群主
            /**
             * 处理视图
             */
            if (position == userInfos.size() - 1) {//减号
                if (isDeleteModle) {
                    //删除模式下的减号
                    convertView.setVisibility(View.GONE);//隐藏整个减号
                } else {
                    convertView.setVisibility(View.VISIBLE);//展示整个减号
                    viewHolder.ivMemberDetele.setVisibility(View.GONE);//隐藏小减号
                    viewHolder.tvMemberName.setVisibility(View.GONE);//隐藏名字
                    viewHolder.ivMemberPhoto.setImageResource(R.mipmap.em_smiley_add_btn_pressed);//设置图片

                }
            } else {//群成员
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tvMemberName.setVisibility(View.VISIBLE);
                //根据删除模式决定是否展示小减号
                if (isDeleteModle) {
                    viewHolder.ivMemberDetele.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.ivMemberDetele.setVisibility(View.GONE);
                }
                viewHolder.ivMemberPhoto.setImageResource(R.mipmap.em_default_avatar);
            }

            /*
            监听事件
             */
            if (position == userInfos.size() - 1) {//减号
                viewHolder.ivMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isDeleteModle) {
                            isDeleteModle = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            } else if (position == userInfos.size() - 2) {//加号
                viewHolder.ivMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {//群成员
                viewHolder.ivMemberDetele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            //群成员
            if (position == userInfos.size() - 1) {
                convertView.setVisibility(View.GONE);
            } else if (position == userInfos.size() - 2) {
                convertView.setVisibility(View.GONE);
            } else {
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tvMemberName.setText(userInfos.get(position).getUsername());
                viewHolder.ivMemberDetele.setVisibility(View.GONE);
            }
        }

        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.iv_member_photo)
        ImageView ivMemberPhoto;
        @Bind(R.id.tv_member_name)
        TextView tvMemberName;
        @Bind(R.id.iv_member_detele)
        ImageView ivMemberDetele;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
