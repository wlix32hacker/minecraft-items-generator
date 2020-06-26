package minecraft.parser.antlr.classmap;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassListener extends MinecraftClassMapBaseListener {

  private final List<String> comments;
  private final List<String> classes;

  public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
    comments.add(ctx.getText());
  }

  public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
    final String classDef = String.format(
        "%s:%s",
        ctx.classSignature()
            .classDefOriginalName()
            .getText(),
        ctx.classSignature()
            .classDefObfuscatedName()
            .getText()
    );
    System.out.println("classDef: " + classDef);
    classes.add(classDef);
  }
}

