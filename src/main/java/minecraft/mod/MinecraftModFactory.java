package minecraft.mod;

import javax.inject.Singleton;

import com.mageddo.coc.CocModule;

import dagger.Component;

@Singleton
@Component(modules = CocModule.class)
public interface MinecraftModFactory {
  MinecraftMod minecraftMod();
}
