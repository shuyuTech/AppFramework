package com.ftyl.demo.mvp.ui.adapter;

import android.view.View;

import com.ftyl.demo.mvp.model.entity.User;
import com.ftyl.demo.mvp.ui.holder.UserItemHolder;

import core.base.BaseHolder;
import core.base.DefaultAdapter;

import java.util.List;

import me.ftyl.mvparms.demo.R;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class UserAdapter extends DefaultAdapter<User> {
    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
