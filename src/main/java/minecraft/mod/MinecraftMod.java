package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;
import com.mageddo.ramspiderjava.client.RamSpiderAgent;

import org.apache.commons.lang3.tuple.Pair;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MinecraftMod {

  private final MinecraftProcessFinder minecraftProcessFinder;

  @Inject
  public MinecraftMod(MinecraftProcessFinder minecraftProcessFinder) {
    this.minecraftProcessFinder = minecraftProcessFinder;
  }

  public Pair<Integer, MinecraftItemScanner> attach(){
    final Window minecraft = this.minecraftProcessFinder.find();
    if(minecraft == null){
      throw new IllegalStateException("Minecraft wasn't found, is it running?");
    }
    log.info("attaching-to={}", minecraft);
    final JavaRamSpider javaRamSpider = JavaRamSpider.attach(minecraft.pid());
    log.info("status=attached!");
    return Pair.of(
        minecraft.pid(),
        MinecraftItemScanner_Factory.newInstance(javaRamSpider.classInstanceService())
    );
  }

  public static MinecraftMod create(){
    return DaggerMinecraftModFactory
        .create()
        .minecraftMod()
        ;
  }
}
