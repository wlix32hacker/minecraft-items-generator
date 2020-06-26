package minecraft.mod;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;

import minecraft.mod.classmapping.ClassMappingsService;

@Singleton
public class MinecraftItemManagerFactory {

  private final ClassInstanceService classInstanceService;
  private final ClassMappingsService classMappingsService;

  @Inject
  public MinecraftItemManagerFactory(
      ClassInstanceService classInstanceService,
      ClassMappingsService classMappingsService
  ) {
    this.classInstanceService = classInstanceService;
    this.classMappingsService = classMappingsService;
  }

  public MinecraftItemManager getInstance(VersionDefs version){
    return new MinecraftItemManager1_14Plus(this.classInstanceService, version);
  }

  public MinecraftItemManager getInstance(){
    return this.getInstance(this.classMappingsService.findVersionDefs());
  }
}
