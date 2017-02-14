package com.atguigu.fx.modle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.fx.modle.table.AccountTable;

/**
 * Created by 情v枫 on 2017/2/14.
 */

public class AccountDb extends SQLiteOpenHelper {
    public AccountDb(Context context) {
        super(context, "account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**
         * 创建数据库
         */
        db.execSQL(AccountTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
