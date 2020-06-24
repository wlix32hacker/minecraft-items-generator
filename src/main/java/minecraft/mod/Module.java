package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.coc.DaggerCocFactory;
import com.mageddo.coc.WindowDAO;

import dagger.Provides;

@dagger.Module
class Module {

  @Provides
  @Singleton
  WindowDAO windowService() {
    return DaggerCocFactory
        .create()
        .windowDao()
    ;
  }
}
