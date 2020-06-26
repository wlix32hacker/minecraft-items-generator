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

  @Test
  void mustParseClassAndFieldsAndMethods() {
    // arrange
    final List<String> comments = new ArrayList<>();
    final List<String> fields = new ArrayList<>();
    final List<String> methods = new ArrayList<>();

    final MinecraftClassMapParser parser = this.createParser("/example04.txt");

    // act
    ParseTreeWalker.DEFAULT.walk(
        new ClassAndFieldsAndMethodsListener(comments, fields, methods),
        parser.parse()
    );

    // assert
    assertEquals("[# some comment]", comments.toString());
    assertEquals("net.minecraft.world.item.ItemStack:ben(tag:g)", fields.get(0));
  }

  @Test
  void mustParseClassAndFields() {
    // arrange
    final List<String> comments = new ArrayList<>();
    final List<String> fields = new ArrayList<>();

    final MinecraftClassMapParser parser = this.createParser("/example03.txt");

    // act
    ParseTreeWalker.DEFAULT.walk(new ClassAndFieldsListener(comments, fields), parser.parse());

    // assert
    assertEquals("[# some comment]", comments.toString());
    assertEquals("net.minecraft.world.item.ItemPropertyFunction:bem(name:a)", fields.get(0));
    assertEquals("net.minecraft.world.item.ItemStack:ben(count:d)", fields.get(1));
    assertEquals("net.minecraft.world.item.ItemStack:ben(item:f)", fields.get(2));
  }

  @SneakyThrows
  @Test
  void mustParseClasses() {
    // arrange
    final List<String> classes = new ArrayList<>();
    final List<String> comments = new ArrayList<>();
    final MinecraftClassMapParser parser = this.createParser("/example02.txt");

    // act
    ParseTreeWalker.DEFAULT.walk(new ClassListener(comments, classes), parser.parse());

    // assert
    assertEquals(
        "[net.minecraft.world.item.ItemPropertyFunction:bem, net.minecraft.world.item.ItemStack:ben]",
        classes.toString()
    );
    assertEquals("[# some comment]", comments.toString());
  }

  @SneakyThrows
  @Test
  void mustParseComments() {
    // arrange

    // act
    final MinecraftClassMapParser parser = this.createParser("/example01.txt");

    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener() {
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
