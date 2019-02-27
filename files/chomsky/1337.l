%{
// C kod, amit meghagyunk 1-az-1-ben
#include <stdio.h>
%}

%option noyywrap

%%
"a"		{ printf("4");				}
"c"		{ printf("k");				}
"e"		{ printf("3");				}
"o"		{ printf("0");				}
"i"		{ printf("1");				}
"q"		{ printf("kw");				}
"A"		{ printf("/-\\");			}
"B"		{ printf("13");				}
"C"		{ printf("K");				}
"E"		{ printf("3");				}
"I"		{ printf("1");				}
"V"		{ printf("\\/");			}
"bye" 	{ printf("bai");			}
"and" 	{ printf("nd");				}
"dude" 	{ printf("d00d");			}
"dog" 	{ printf("dog8");			}
"from" 	{ printf("form");			}
"girl" 	{ printf("grill");			}
"guys" 	{ printf("guise");			}
"hacks" { printf("h4x");			}
"hai"	{ printf("hi");				}
"you" 	{ printf("j00");			}
"cool" 	{ printf("kewl");			}
"like" 	{ printf("liek");			}
"lol" 	{ printf("lopl");			}
"mate" 	{ printf("m8");				}
"master" { printf("mastah");		}
"max0r"	{ printf("maximum");		}
"oh my god" { printf("OMG");		}
"own" 	{ printf("pwn"); 			}
"please" { printf("pl0x"); 			}
"power" { printf("powwah"); 		}
"porn" 	{ printf("pr0n"); 			}
"the" 	{ printf("t3h"); 			}
"when" 	{ printf("wen"); 			}
"what" 	{ printf("wut"); 			}
"winner" { printf("winnar"); 		}
"are" 	{ printf("r"); 				}
"why" 	{ printf("y"); 				}
"yes" 	{ printf("yass"); 			}
"yeah" 	{ printf("ya/yea"); 		}
"cya"	{ printf("see you");		}
"loveu" { printf("10/3u"); 			}
.|\n	{ printf("%s", yytext);		}
%%

int main(void)
{
	yylex();
	return 0;
}
