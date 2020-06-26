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
  : classDefOringinalName ' '* '->' ' '* classDefObfuscatedName ':' ' '*
  ;

classDefObfuscatedName
  : NAME_SPACE
  ;

classDefOringinalName
  : NAME_SPACE
  ;

fragment WHITESPACE: ' '*;

NAME_SPACE
  : ([a-zA-Z0-9_\\.])+
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
