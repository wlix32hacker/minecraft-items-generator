package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.ramspiderjava.client.di.RamSpiderModule;

import dagger.Binds;
import dagger.Component;
import dagger.Provides;
import minecraft.mod.classmapping.ClassMappingsService;
import minecraft.mod.classmapping.ClassMappingsServiceCached;

@Singleton
@Component(modules = {RamSpiderModule.class, Minecraft.Module.class, Minecraft.PidModule.class})
public interface Minecraft {

  int pid();

  MinecraftItemScanner minecraftItemScanner();

  ClassMappingsService classMappingsService();

  @dagger.Module
  interface Module {
    @Binds
    ClassMappingsService classMappingsService(ClassMappingsServiceCached impl);
  }

  @dagger.Module
  class PidModule {

    private final int pid;

    PidModule(int pid) {
      this.pid = pid;
    }

    @Provides
    @Singleton
    int pid(){
      return this.pid;
    }
  }
}
