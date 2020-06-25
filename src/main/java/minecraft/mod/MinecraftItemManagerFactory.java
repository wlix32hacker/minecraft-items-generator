package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;

@Singleton
public class MinecraftItemManagerFactory {

  private final ClassInstanceService classInstanceService;
  private final MinecraftVersionService minecraftVersionService;

  @Inject
  public MinecraftItemManagerFactory(
      ClassInstanceService classInstanceService,
      MinecraftVersionService minecraftVersionService
  ) {
    this.classInstanceService = classInstanceService;
    this.minecraftVersionService = minecraftVersionService;
  }

  public MinecraftItemManager getInstance(Version version){
    return new MinecraftItemManager1_14Plus(this.classInstanceService, version);
  }

  public MinecraftItemManager getInstance(){
    return this.getInstance(this.minecraftVersionService.findMinecraftVersion());
  }
}
