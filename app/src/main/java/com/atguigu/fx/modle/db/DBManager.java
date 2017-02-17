package com.atguigu.fx.modle.db;

import android.content.Context;

import com.atguigu.fx.modle.dao.ContactDao;
import com.atguigu.fx.modle.dao.InvitationDao;

/**
 * Created by 情v枫 on 2017/2/15.
 */

public class DBManager {

    private final ContactDao contactDao;
    private final InvitationDao invitationDao;
    private final DBHelpter dbHelpter;

    public DBManager(Context context, String name){
        dbHelpter = new DBHelpter(context, name);

        //创建联系人操作类
        contactDao = new ContactDao(dbHelpter);
        //创建邀请信息操作类
        invitationDao = new InvitationDao(dbHelpter);
    }

    public ContactDao getContactDao(){
        return contactDao;
    }

    public InvitationDao getInvitationDao(){
        return invitationDao;
    }

    public void close(){
        dbHelpter.close();
    }

}
