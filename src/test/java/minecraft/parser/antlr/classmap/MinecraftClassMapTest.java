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
  void mustParseClassAndFields(){
    // arrange
    final List<String> comments = new ArrayList<>();
    final List<String> fields = new ArrayList<>();

    final MinecraftClassMapParser parser = this.createParser("/example03.txt");

    // act
    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener(){

      private String clazz;

      public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
        comments.add(ctx.getText());
        System.out.println("comment: " + ctx.getText());
      }

      public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
        this.clazz = String.format(
            "%s:%s",
            ctx.classSignature().classDefOriginalName()
                .getText(),
            ctx.classSignature().classDefObfuscatedName()
                .getText()
        );
        System.out.println("classDef: " + clazz);
      }

      public void enterClassBodyStm(MinecraftClassMapParser.ClassBodyStmContext ctx) {
        System.out.println("  classBodyStm: " + ctx.getText());
      }

      public void enterVariableDef(MinecraftClassMapParser.VariableDefContext ctx) {
        final String variable = String.format(
            "%s(%s:%s)",
            this.clazz,
            ctx.variableOriginalName().getText(),
            ctx.variableObfuscatedName().getText()
        );
        fields.add(variable);
        System.out.println("    variable: " + variable);
      }
    };
    ParseTreeWalker.DEFAULT.walk(listener, parser.parse());

    // assert
    assertEquals("[# some comment]", comments.toString());
    assertEquals("net.minecraft.world.item.ItemPropertyFunction:bem(name:a)", fields.get(0));
    assertEquals("net.minecraft.world.item.ItemStack:ben(count:d)", fields.get(1));
    assertEquals("net.minecraft.world.item.ItemStack:ben(item:f)", fields.get(2));
  }

  @SneakyThrows
  @Test
  void mustParseClasses(){
    // arrange
    final List<String> classes = new ArrayList<>();
    final List<String> comments = new ArrayList<>();
    final MinecraftClassMapParser parser = this.createParser("/example02.txt");

    // act
    final MinecraftClassMapBaseListener listener = new MinecraftClassMapBaseListener(){
      public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
        comments.add(ctx.getText());
      }

      public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
        final String classDef = String.format(
            "%s:%s",
            ctx.classSignature().classDefOriginalName()
                .getText(),
            ctx.classSignature().classDefObfuscatedName()
                .getText()
        );
        System.out.println("classDef: " + classDef);
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
