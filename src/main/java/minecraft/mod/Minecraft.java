package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.ramspiderjava.client.di.RamSpiderModule;

import dagger.Component;

@Singleton
@Component(modules = {RamSpiderModule.class, MinecraftModule.class})
public interface Minecraft {

  int pid();

  MinecraftItemScanner minecraftItemScanner();

}
