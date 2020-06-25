package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.ramspiderjava.client.di.RamSpiderModule;

import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = {RamSpiderModule.class, Minecraft.Module.class})
public interface Minecraft {

  int pid();

  MinecraftItemScanner minecraftItemScanner();

  @dagger.Module
  class Module {

    private final int pid;

    Module(int pid) {
      this.pid = pid;
    }

    @Provides
    @Singleton
    int pid(){
      return this.pid;
    }
  }
}
