package minecraft.parser.antlr.classmap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    // arrange
    List<String> classes = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    final MinecraftClassMapParser parser = this.createParser("/example02.txt");

    // act
    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener(){
      public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
        comments.add(ctx.getText());
      }

      public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
        final String classDef = String.format(
            "%s:%s",
            ctx.classDefOringinalName()
                .getText(),
            ctx.classDefObfuscatedName()
                .getText()
        );
        System.out.println(classDef);
        classes.add(classDef);
      }
    };
    ParseTreeWalker.DEFAULT.walk(listener, parser.parse());

    // assert
    assertEquals(
        "[net.minecraft.world.item.ItemPropertyFunction:bem, net.minecraft.world.item.ItemStack:ben]",
        classes.toString()
    );
    assertEquals("[# some comment]", comments.toString());
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
