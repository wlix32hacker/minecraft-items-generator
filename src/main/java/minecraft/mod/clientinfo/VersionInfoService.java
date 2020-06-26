package minecraft.mod.clientinfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.ramspiderjava.ResourceService;

import lombok.SneakyThrows;

@Singleton
public class VersionInfoService {

  private final ObjectMapper objectMapper;
  private final ResourceService resourceService;

  @Inject
  public VersionInfoService(ObjectMapper objectMapper, ResourceService resourceService) {
    this.objectMapper = objectMapper;
    this.resourceService = resourceService;
  }

  @SneakyThrows
  public GameVersion getGameVersion(){
    return this.objectMapper.readValue(
        this.resourceService.getResourceString("/version.json"),
        GameVersion.class
    );
  }
}
