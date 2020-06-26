package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.coc.CocModule;
import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;
import com.mageddo.ramspiderjava.client.di.HttpClientModule;

import dagger.Component;
import minecraft.mod.Minecraft.PidModule;

@Singleton
@Component(modules = CocModule.class)
public interface MinecraftAttache {

  MinecraftProcessFinder minecraftProcessFinder();

  default Minecraft findAndAttachToRunning(){
    final Window minecraftWindow = minecraftProcessFinder().find();
    if(minecraftWindow == null){
      throw new IllegalStateException("Minecraft wasn't found, is it running?");
    }
    JavaRamSpider.attach(minecraftWindow.pid());
    return DaggerMinecraft
        .builder()
        .httpClientModule(new HttpClientModule(minecraftWindow.pid()))
        .pidModule(new PidModule(minecraftWindow.pid()))
        .build()
        ;
  }

  static MinecraftAttache create(){
    return DaggerMinecraftAttache.create();
  }
}
