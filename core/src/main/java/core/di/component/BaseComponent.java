package core.di.component;

import javax.inject.Singleton;

import core.base.BaseApplication;
import core.di.module.AppModule;
import dagger.Component;

@Singleton
@Component(modules={AppModule.class})
public interface BaseComponent {
    void inject(BaseApplication application);
}
