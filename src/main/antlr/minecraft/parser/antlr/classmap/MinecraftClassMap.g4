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
//  | classSignature classBodyDef
  ;

classSignature
  : classDefOriginalName ' -> ' classDefObfuscatedName ':'
  ;

//classBodyDef
//  : NL '    ' classBodyStm
//  : NL '    ' classBodyStm (NL '    ' classBodyStm)*
//  ;

//classBodyStm
//  : NAME_SPACE ' '+ VAR_NAME ' '+ '->' ' '+ VAR_NAME
//  ;

classDefObfuscatedName
  : nameSpace
  ;

classDefOriginalName
  : nameSpace
  ;

nameSpace
  : NAME ('.' NAME)*
  ;


NAME
  : [A-Za-z_$][a-zA-Z0-9_$]*
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
