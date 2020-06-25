package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;

import com.mageddo.ramspiderjava.client.di.HttpClientModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MinecraftAttache {

  private final MinecraftProcessFinder minecraftProcessFinder;

  @Inject
  public MinecraftAttache(MinecraftProcessFinder minecraftProcessFinder) {
    this.minecraftProcessFinder = minecraftProcessFinder;
  }

  public Minecraft findAndAttachToRunning(){
    final Window minecraftWindow = this.minecraftProcessFinder.find();
    if(minecraftWindow == null){
      throw new IllegalStateException("Minecraft wasn't found, is it running?");
    }
    log.info("attaching-to={}", minecraftWindow);
    JavaRamSpider.attach(minecraftWindow.pid());
    log.info("status=attached!");
    return DaggerMinecraft
        .builder()
        .httpClientModule(new HttpClientModule(minecraftWindow.pid()))
        .module(new Minecraft.Module(minecraftWindow.pid()))
        .build()
        ;
  }

  public static MinecraftAttache create(){
    return DaggerMinecraftModFactory
        .create()
        .minecraftMod()
        ;
  }
}
