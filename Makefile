all:	clean fdl tutoraltak

fdl:	clean validate_tb_fdl_hu tb_fdl.pdf

.PHONY: tutoraltak
tutoraltak:
	@./tutoraltak.sh

turing.gv: pictures/turing.gv
	@dot -Tpng pictures/turing.gv -o pictures/Turing.png

tb_fdl.pdf: tb-fdl.xml tb.xls turing.gv
	@dblatex tb-fdl.xml -p tb.xls

.PHONY: validate_tb_fdl_hu
validate_tb_fdl_hu:
	@xmllint --xinclude tb-fdl.xml --output output.xml
	@xmllint --relaxng tb/docbookxi.rng output.xml --noout
	@rm -f output.xml

.PHONY: clean
clean:
	@rm -f tb-fdl.pdf
	@rm -f pictures/Turing.png
