%{
// C kod, amit meghagyunk 1-az-1-ben
#include <stdio.h>
%}

%option noyywrap

%%
[-+]?([0-9]*\.[0-9]+|[0-9]+) {
			printf("Valost talaltam: %s\n", yytext);
		}
.|\n	{}
%%

int main(void)
{
	yylex();
	return 0;
}
