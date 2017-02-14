package com.atguigu.fx.modle;

import android.accounts.AbstractAccountAuthenticator;
import android.content.Context;

import com.atguigu.fx.modle.dao.AccountDao;
import com.atguigu.fx.modle.db.AccountDb;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 情v枫 on 2017/2/14.
 */

public class Modle {

    /**
     * 单例
     *
     * 第一步： 私有化构造器
     * 第二步： 创建一个静态变量
     * 第三步： 创建一个静态的公共方法返回实例
     */

    private static Modle modle = new Modle();
    private AccountDao accountDao;

    private Modle(){};

    public static Modle getInstance(){
        return modle;
    }

    private Context context;
    public void init(Context context){


        this.context = context;

        //创建AccountDB数据库
        accountDao = new AccountDao(context);
    }

    /*
    * 线程池分为四种
    * 第一种 缓存线程池 有多少可以开启多少
    * 第二种 定长线程池  固定大小
    * 第三种 调度线程池  可以延时周期执行
    * 第四种  单例线程池  单个
    *
    * */
    private ExecutorService  thread = Executors.newCachedThreadPool();

    public ExecutorService getGlobalThread(){
        return thread;
    }

    public AccountDao getAccountDao(){
        return accountDao;
    }

    public void loginSuccess(String currentUser) {
    }


}
