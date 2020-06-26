package minecraft.mod.classmapping;

import javax.inject.Inject;
import javax.inject.Singleton;

import minecraft.mod.VersionDefs;

@Singleton
public class ClassMappingsServiceCached implements ClassMappingsService {

  private final ClassMappingsServiceDefault delegate;
  private VersionDefs cachedVersionDefs;

  @Inject
  public ClassMappingsServiceCached(ClassMappingsServiceDefault classMappingsService) {
    this.delegate = classMappingsService;
  }

  @Override
  public VersionDefs findVersionDefs() {
    if(this.cachedVersionDefs == null){
      return this.cachedVersionDefs = this.delegate.findVersionDefs();
    }
    return this.cachedVersionDefs;
  }
}
