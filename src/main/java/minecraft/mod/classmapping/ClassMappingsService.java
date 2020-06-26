package minecraft.mod.classmapping;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import lombok.SneakyThrows;
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

  public ClassMappingsListener findFieldMappings() {
    return this.findFieldMappings(this.clientInfoService.findClientClassMappingsText());
  }

  public static ClassMappingsListener findFieldMappings(String clientClassMappingsText){
    final MinecraftClassMapLexer lexer = new MinecraftClassMapLexer(CharStreams.fromString(
        clientClassMappingsText
    ));
    final MinecraftClassMapParser parser = new MinecraftClassMapParser(new CommonTokenStream(lexer));
    final ClassMappingsListener mappingsListener = new ClassMappingsListener();
    ParseTreeWalker.DEFAULT.walk(mappingsListener, parser.parse());
    return mappingsListener;
  }

  @SneakyThrows
  public static void main(String[] args) {

    final String clientText = new String(Files.readAllBytes(Paths.get(
        "H:\\jogos-com-backup\\client-1.15.2.txt"
    )));
    final ClassMappingsListener fieldMappings = findFieldMappings(clientText);

    System.out.println(VersionDefs.of(null, fieldMappings));
  }

  public VersionDefs findVersionDefs(){
    return VersionDefs.of(this.minecraftVersionService.getGameVersion(), this.findFieldMappings());
  }
}
