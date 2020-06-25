package minecraft.parser.antlr.classmap;

import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testing.TestUtils.getResourceAsStream;

public class MinecraftClassMapTest {

//  void mustParseClassAndFields(){

  @SneakyThrows
  @Test
  void mustParseClasses(){
    final MinecraftClassMapParser parser = this.createParser("/example02.txt");
    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener(){
      public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
        System.out.printf("classDef: %s%n", ctx.getText() + ":");
      }
    };
    ParseTreeWalker.DEFAULT.walk(listener, parser.parse());
  }

  @SneakyThrows
  @Test
  void mustParseComments(){
    // arrange

    // act
    final MinecraftClassMapParser parser = this.createParser("/example01.txt");

    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener(){
      public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
        assertEquals("# xpto abc", ctx.getText());
      }
    };
    ParseTreeWalker.DEFAULT.walk(listener, parser.parse());


    // assert
  }

  @SneakyThrows
  MinecraftClassMapParser createParser(String path) {
    final InputStream source = getResourceAsStream(path);
    final MinecraftClassMapLexer lexer = new MinecraftClassMapLexer(CharStreams.fromStream(source));
    return new MinecraftClassMapParser(new CommonTokenStream(lexer));
  }

}
