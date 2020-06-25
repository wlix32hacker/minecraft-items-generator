package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;

@Singleton
public class MinecraftItemManagerFactory {

  private final ClassInstanceService classInstanceService;

  @Inject
  public MinecraftItemManagerFactory(ClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
  }

  public MinecraftItemManager getInstance(MinecraftVersion version){
    return new MinecraftItemManager1_14Plus(this.classInstanceService, version);
  }
}
