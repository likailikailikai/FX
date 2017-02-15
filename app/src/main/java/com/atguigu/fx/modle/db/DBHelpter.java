package com.atguigu.fx.modle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.fx.modle.table.ContactTable;
import com.atguigu.fx.modle.table.InvitationTable;

/**
 * Created by 情v枫 on 2017/2/15.
 */

public class DBHelpter extends SQLiteOpenHelper {
    public DBHelpter(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.CREATE_TABLE);//创建联系人表
        db.execSQL(InvitationTable.CREATE_TABLE);//创建邀请信息表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
