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
  : CLASSDEF
  ;

CLASSDEF
  : (~[ \r\n])+ ' -> ' (~[ \r\n])+ ':'
//  : ~ [ ]
//  net.minecraft.world.item.ItemStack -> ben:
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
