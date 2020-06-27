package minecraft.mod;

import java.util.Objects;

import javax.inject.Singleton;

import com.mageddo.coc.CocModule;
import com.mageddo.coc.JvmProcessService;
import com.mageddo.coc.ProcessDAO;
import com.mageddo.coc.Window;
import com.mageddo.ramspiderjava.client.JavaRamSpider;
import com.mageddo.ramspiderjava.client.di.HttpClientModule;

import org.apache.commons.lang3.arch.Processor.Arch;

import dagger.Component;
import lombok.SneakyThrows;
import minecraft.mod.Minecraft.PidModule;

@Singleton
@Component(modules = CocModule.class)
public interface MinecraftAttache {

  @SneakyThrows
  static void checkMinecraftProcess() {
    final Window minecraft = create()
        .minecraftProcessFinder()
        .find();
    if(minecraft == null){
      throw new IllegalStateException("Minecraft it's not running, start it first");
    }
    final boolean spawn = create()
        .jvmProcessService()
        .spawnOnTargetVmWhenNotCompatible(minecraft.pid());
    if(spawn){
      System.exit(0);
    }
  }

  MinecraftProcessFinder minecraftProcessFinder();

  ProcessDAO processDao();

  JvmProcessService jvmProcessService();

  default Minecraft findAndAttachToRunning() {

    final Window minecraftWindow = this.minecraftProcessFinder()
        .find();
    if (minecraftWindow == null) {
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

  static MinecraftAttache create() {
    return DaggerMinecraftAttache.create();
  }
}
