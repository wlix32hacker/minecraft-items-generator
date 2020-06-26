package minecraft.mod.classmapping;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import minecraft.mod.ClientInfoService;
import minecraft.mod.MinecraftVersionService;
import minecraft.mod.VersionDefs;
import minecraft.parser.antlr.classmap.MinecraftClassMapLexer;
import minecraft.parser.antlr.classmap.MinecraftClassMapParser;

@Singleton
public class ClassMappingsService {

  private final ClientInfoService clientInfoService;
  private final MinecraftVersionService minecraftVersionService;

  @Inject
  public ClassMappingsService(
      ClientInfoService clientInfoService,
      MinecraftVersionService minecraftVersionService
  ) {
    this.clientInfoService = clientInfoService;
    this.minecraftVersionService = minecraftVersionService;
  }

  public ClassMappingsListener findFieldMappings(){
    final String clientClassMappingsText = this.clientInfoService.findClientClassMappingsText();
    final MinecraftClassMapLexer lexer = new MinecraftClassMapLexer(CharStreams.fromString(
        clientClassMappingsText
    ));
    final MinecraftClassMapParser parser = new MinecraftClassMapParser(new CommonTokenStream(lexer));
    final ClassMappingsListener mappingsListener = new ClassMappingsListener();
    ParseTreeWalker.DEFAULT.walk(mappingsListener, parser.parse());
    return mappingsListener;
  }

  public VersionDefs findVersionDefs(){
    return VersionDefs.of(this.minecraftVersionService.getGameVersion(), this.findFieldMappings());
  }
}
