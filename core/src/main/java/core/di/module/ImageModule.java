package core.di.module;

import javax.inject.Singleton;

import core.widget.imageloader.BaseImageLoaderStrategy;
import core.widget.imageloader.glide.GlideImageLoaderStrategy;
import dagger.Module;
import dagger.Provides;


@Module
public class ImageModule {

    @Singleton
    @Provides
    public BaseImageLoaderStrategy provideImageLoaderStrategy(GlideImageLoaderStrategy glideImageLoaderStrategy) {
        return glideImageLoaderStrategy;
    }

}
