package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.coc.Window;
import com.mageddo.coc.WindowDAO;

@Singleton
public class MinecraftProcessFinder {

  private final WindowDAO windowDAO;

  @Inject
  public MinecraftProcessFinder(WindowDAO windowDAO) {
    this.windowDAO = windowDAO;
  }

  public Window find(){
    return this.windowDAO
        .findWindows()
        .stream()
        .filter(it -> it.name().matches("^Minecraft \\d+\\.\\d+(\\.\\d)*.*"))
        .findFirst()
        .orElse(null)
    ;
  }

  public String getVersion()
}
