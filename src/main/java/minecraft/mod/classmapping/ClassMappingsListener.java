package minecraft.mod.classmapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecraft.parser.antlr.classmap.MinecraftClassMapBaseListener;
import minecraft.parser.antlr.classmap.MinecraftClassMapParser;

public class ClassMappingsListener extends MinecraftClassMapBaseListener {

  private final Map<String, String> classBindings;
  private final Map<String, List<FieldMapping>> classFieldsBindings;

  public ClassMappingsListener() {
    this.classBindings = new HashMap<>();
    this.classFieldsBindings = new HashMap<>();
  }

  @Override
  public void enterClassSignature(MinecraftClassMapParser.ClassSignatureContext ctx) {
    this.classBindings.put(ctx.classDefOriginalName()
        .getText(), ctx.classDefObfuscatedName()
        .getText());
  }

  @Override
  public void enterVariableDef(MinecraftClassMapParser.VariableDefContext ctx) {
    final String fieldClassName = ctx
        .nameSpace()
        .getText();
    if (!this.classFieldsBindings.containsKey(fieldClassName)) {
      this.classFieldsBindings.put(fieldClassName, new ArrayList<>());
    }
    this.classFieldsBindings
        .get(fieldClassName)
        .add(FieldMapping
            .builder()
            .name(ctx
                .variableOriginalName()
                .getText()
            )
            .obfuscatedName(ctx
                .variableObfuscatedName()
                .getText()
            )
            .typeName(fieldClassName)
            .obfuscatedType(this.classBindings.get(fieldClassName))
            .build()
        );
  }

  public List<FieldMapping> getClassFields(String clazzName){
    return this.classFieldsBindings.get(clazzName);
  }

  public FieldMapping getField(String className, String fieldName){
    return this
        .getClassFields(className)
        .stream()
        .filter(it -> it.getName().equals(fieldName))
        .findFirst()
        .get();
  }

  public String getObfuscatedClassName(String className) {
    return this.classBindings.get(className);
  }
}
