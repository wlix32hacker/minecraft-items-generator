package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.coc.CocModule;

import dagger.Component;
import minecraft.mod.entrypoint.MinecraftMod;

@Singleton
@Component(modules = CocModule.class)
public interface MinecraftModFactory {

  void inject(MinecraftMod mod);

  MinecraftAttache minecraftMod();

}
