package minecraft.parser.antlr.classmap;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ClassAndFieldsListener extends MinecraftClassMapBaseListener {

  private String clazz;
  private final List<String> comments;
  private final List<String> fields;

  public void enterComment(MinecraftClassMapParser.CommentContext ctx) {
    comments.add(ctx.getText());
    System.out.println("comment: " + ctx.getText());
  }

  public void enterClassDef(MinecraftClassMapParser.ClassDefContext ctx) {
    this.clazz = String.format(
        "%s:%s",
        ctx.classSignature()
            .classDefOriginalName()
            .getText(),
        ctx.classSignature()
            .classDefObfuscatedName()
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
        ctx.variableOriginalName()
            .getText(),
        ctx.variableObfuscatedName()
            .getText()
    );
    fields.add(variable);
    System.out.println("    variable: " + variable);
  }
}
