package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;

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
    final Window minecraft = this.minecraftProcessFinder.find();
    if(minecraft == null){
      throw new IllegalStateException("Minecraft wasn't found, is it running?");
    }
    log.info("attaching-to={}", minecraft);
    JavaRamSpider.attach(minecraft.pid());
    log.info("status=attached!");
    return DaggerMinecraft
        .builder()
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
