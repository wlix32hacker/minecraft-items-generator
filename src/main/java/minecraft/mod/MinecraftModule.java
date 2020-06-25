package minecraft.mod;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module
class MinecraftModule {

  private final int pid;

  MinecraftModule(int pid) {
    this.pid = pid;
  }

  @Provides
  @Singleton
  int pid(){
    return this.pid;
  }
}
