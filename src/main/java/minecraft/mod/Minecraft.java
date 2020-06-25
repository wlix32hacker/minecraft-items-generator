package minecraft.mod;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.ramspiderjava.ResourceService;

import dagger.Component;
import lombok.SneakyThrows;

@Singleton
@Component
public class Minecraft {

  private final ResourceService resourceService;
  private final ObjectMapper objectMapper;
  private final MinecraftItemScanner minecraftItemScanner;

  public void findAndChange(){
    final GameVersion version = this.getVersion();
    version.getBuildTime()
  }


  @SneakyThrows
  public GameVersion getVersion(){
    return this.objectMapper.readValue(
        this.resourceService.getResourceString("/version.json"),
        GameVersion.class
    );
  }
}
