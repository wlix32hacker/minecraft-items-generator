grammar MinecraftClassMap;

@header {
  package minecraft.parser.antlr.classmap;
}

parse
   : value (NL value)*
   ;

value
  : classDef
  | comment
  | EOF
  ;

comment
  : SINGLE_LINE_COMMENT
  ;

classDef
  : classSignature
  | classSignature classBodyDef
  ;

classDefObfuscatedName
  : nameSpace
  ;

classDefOriginalName
  : nameSpace
  ;

classSignature
  : classDefOriginalName ' -> ' classDefObfuscatedName ':'
  ;

classBodyDef
  : (NL '    ' classBodyStm)*
  ;

classBodyStm
  : variableDef
  | methodDef
  ;

variableDef
  : nameSpace ' ' variableOriginalName ' -> ' variableObfuscatedName
  ;

variableOriginalName
  : NAME
  ;

variableObfuscatedName
  : NAME
  ;

methodDef
  : (methodDefSourceLine)? nameSpace ' ' methodDefName '(' methodDefArgs ')' ' -> ' methodDefObfuscatedName
  ;

methodDefSourceLine
  : INTEGER ':' INTEGER ':'
  ;

methodDefObfuscatedName
  : methodDefName
  ;

methodDefName
  : NAME
  | '<' NAME '>'
  ;

methodDefArgs
  : methodArg* (',' methodArg)*
  ;

methodArg
  : nameSpace
  ;

nameSpace
  : NAME ('.' NAME)*
  ;

NAME
  : [A-Za-z_$][a-zA-Z0-9_$]*
  ;

INTEGER
  : [0-9]+
  ;

SINGLE_LINE_COMMENT
   : '#' (LINE_TEXT)+
   ;

NL
  : '\r'? '\n'
  ;

fragment LINE_TEXT
   : ~ [\r\n] // what which isn't a new line
   ;
