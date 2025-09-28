package com.xinji.testrus;

import android.app.Application;

import com.pseudos.game.IGameManage;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //todo 生产环境下请改成false
//        XJGame.setDebug(true);
        IGameManage.Builder.build().onAppCreate(this);
    }
}
