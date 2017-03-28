package com.ftyl.demo.di.component;

import com.ftyl.demo.di.module.UserModule;
import com.ftyl.demo.mvp.ui.activity.UserActivity;

import core.di.scope.ActivityScope;

import common.AppComponent;
import dagger.Component;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
