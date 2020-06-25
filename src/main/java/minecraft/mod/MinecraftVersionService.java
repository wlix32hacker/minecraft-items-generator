package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mageddo.ramspiderjava.ResourceService;

import lombok.SneakyThrows;

@Singleton
public class MinecraftVersionService {

  private final ObjectMapper objectMapper;
  private final ResourceService resourceService;

  @Inject
  public MinecraftVersionService(ObjectMapper objectMapper, ResourceService resourceService) {
    this.objectMapper = objectMapper;
    this.resourceService = resourceService;
  }

  public MinecraftVersion findMinecraftVersion(){
    throw new UnsupportedOperationException();
  }


  @SneakyThrows
  public GameVersion getGameVersion(){
    return this.objectMapper.readValue(
        this.resourceService.getResourceString("/version.json"),
        GameVersion.class
    );
  }
}
