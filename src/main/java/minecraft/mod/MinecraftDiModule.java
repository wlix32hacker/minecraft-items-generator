package minecraft.mod;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.mageddo.coc.CocFactory;
import com.mageddo.coc.DaggerCocFactory;
import com.mageddo.coc.ProcessDAO;
import com.mageddo.coc.ProcessesIconFinder;
import com.mageddo.coc.WindowDAO;
import com.mageddo.coc.WindowService;

import javax.inject.Singleton;

class MinecraftDiModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(MinecraftMod.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  static WindowDAO windowService() {
    return DaggerCocFactory
        .create()
        .windowDao()
    ;
  }
}
