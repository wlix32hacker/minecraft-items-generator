package minecraft.mod;

import com.google.inject.Guice;
import com.mageddo.coc.DaggerCocFactory;
import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class MinecraftMod {

  private final MinecraftProcessFinder minecraftProcessFinder;

  @Inject
  public MinecraftMod(MinecraftProcessFinder minecraftProcessFinder) {
    this.minecraftProcessFinder = minecraftProcessFinder;
  }

  public MinecraftItemScanner attach(){
    final Window minecraft = this.minecraftProcessFinder.find();
    if(minecraft == null){
      throw new IllegalStateException("Minecraft wasn't found, is it running?");
    }
    log.info("attaching-to={}", minecraft);
    final MinecraftItemScanner itemScanner = JavaRamSpider.attach(minecraft.pid(), MinecraftItemScanner.class);
    log.info("status=attached!");
    return itemScanner;
  }

  public static MinecraftMod create(){
    return Guice
        .createInjector(new MinecraftDiModule())
        .getInstance(MinecraftMod.class)
    ;
  }
}
