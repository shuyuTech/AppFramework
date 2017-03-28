package common;

import android.app.Application;

import com.ftyl.demo.di.module.CacheModule;
import com.ftyl.demo.di.module.ServiceModule;
import com.ftyl.demo.mvp.model.api.cache.CacheManager;
import com.ftyl.demo.mvp.model.api.service.ServiceManager;
import com.google.gson.Gson;

import javax.inject.Singleton;

import core.base.AppManager;
import core.di.module.AppModule;
import core.di.module.ClientModule;
import core.di.module.GlobeConfigModule;
import core.di.module.ImageModule;
import core.widget.imageloader.ImageLoader;
import dagger.Component;
import me.ftyl.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = {AppModule.class, ClientModule.class, ServiceModule.class, ImageModule.class,
        CacheModule.class, GlobeConfigModule.class})
public interface AppComponent {
    Application Application();

    //服务管理器,retrofitApi
    ServiceManager serviceManager();

    //缓存管理器
    CacheManager cacheManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();


    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

    //gson
    Gson gson();

    //用于管理所有activity
    AppManager appManager();
}
