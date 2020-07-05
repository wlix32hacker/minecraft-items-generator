package minecraft.mod.classmapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;
import com.mageddo.ramspiderjava.MethodId;

import lombok.extern.slf4j.Slf4j;
import minecraft.parser.antlr.classmap.MinecraftClassMapBaseListener;
import minecraft.parser.antlr.classmap.MinecraftClassMapParser;
import minecraft.parser.antlr.classmap.MinecraftClassMapParser.MethodArgContext;
import minecraft.parser.antlr.classmap.MinecraftClassMapParser.MethodDefArgsContext;

@Slf4j
public class ClassMappingsListener extends MinecraftClassMapBaseListener {

  private final Map<String, String> classBindings;
  private final Map<String, List<FieldMapping>> classFieldsBindings;
  private final Map<MethodId, MethodId> classMethodsBindings;
  private String currentClassName;

  public ClassMappingsListener() {
    this.classBindings = new HashMap<>();
    this.classFieldsBindings = new HashMap<>();
    this.classMethodsBindings = new HashMap<>();
  }

  @Override
  public void enterClassSignature(MinecraftClassMapParser.ClassSignatureContext ctx) {
    this.currentClassName = ctx
        .classDefOriginalName()
        .getText();
    this.classBindings.put(
        this.currentClassName,
        ctx.classDefObfuscatedName()
            .getText()
    );
  }

  @Override
  public void enterVariableDef(MinecraftClassMapParser.VariableDefContext ctx) {
    final String fieldClassName = ctx.nameSpace()
        .getText();
    if (!this.classFieldsBindings.containsKey(this.currentClassName)) {
      this.classFieldsBindings.put(this.currentClassName, new ArrayList<>());
    }
    this.classFieldsBindings
        .get(this.currentClassName)
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
            .obfuscatedType(this.toObfuscatedClassName(fieldClassName))
            .build()
        );
  }

  @Override
  public void enterMethodDef(MinecraftClassMapParser.MethodDefContext ctx) {
    final String methodName = ctx
        .methodDefName()
        .getText();

    final MethodId vanilla = MethodId.of(
        ClassId.of(this.currentClassName),
        ctx.methodDefName()
            .getText(),
        this.toMethodArgs(ctx.methodDefArgs())
    );

    final MethodId obfuscated = MethodId.of(
        this.getCurrentObfuscatedClassId(),
        ctx.methodDefObfuscatedName()
            .getText(),
        this.toObfuscatedMethodArgs(ctx.methodDefArgs())
    );

    if (this.classMethodsBindings.containsKey(vanilla)) {
      log.trace("Duplicated method: {}, probably an interface default method", vanilla);
    } else {
      this.classMethodsBindings.put(vanilla, obfuscated);
    }
  }

  ClassId getCurrentObfuscatedClassId() {
    return ClassId.of(this.toObfuscatedClassName(this.currentClassName));
  }

  ClassId[] toMethodArgs(MethodDefArgsContext methodDefArgs) {
    return toMethodArgs(methodDefArgs, ClassId::of);
  }

  ClassId[] toObfuscatedMethodArgs(MethodDefArgsContext methodDefArgs) {
    return toMethodArgs(methodDefArgs, className -> ClassId.of(this.toObfuscatedClassName(className)));
  }

  ClassId[] toMethodArgs(MethodDefArgsContext methodDefArgs, Function<String, ClassId> transformer) {
    final int argsSize = methodDefArgs
        .methodArg()
        .size();
    final ClassId[] args = new ClassId[argsSize];
    for (int i = 0; i < argsSize; i++) {
      final MethodArgContext methodArgContext = methodDefArgs.methodArg(i);
      args[i] = transformer.apply(methodArgContext.getText());
    }
    return args;
  }

  public List<FieldMapping> getClassFields(String clazzName) {
    return this.classFieldsBindings.get(clazzName);
  }

  public FieldMapping getField(String className, String fieldName) {
    return this
        .getClassFields(className)
        .stream()
        .filter(it -> it.getName()
            .equals(fieldName))
        .findFirst()
        .get();
  }

  public String toObfuscatedClassName(String className) {
    return this.classBindings.get(className);
  }

  public MethodId findObfuscatedMethod(MethodId methodId) {
    return Objects.requireNonNull(this.classMethodsBindings.get(methodId), "Method not found: " + methodId);
  }

  public ClassId findObfuscatedClassName(ClassId classId) {
    return ClassId.of(this.toObfuscatedClassName(classId.getClassName()));
  }

  public FieldId findObfuscatedField(ClassId classId, FieldId fieldId) {
    return this.getClassFields(classId.getClassName())
        .stream()
        .filter(it -> fieldId.getName().equals(it.getName()))
        .map(it -> FieldId.of(it.getObfuscatedName()))
        .findFirst()
        .get();
  }
}
