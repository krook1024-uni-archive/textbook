all:	clean travis

fdl:	clean validate_tb_fdl_hu tb_fdl.pdf

travis:	clean validate_tb_fdl_hu tb_fdl.pdf_travis

tb_fdl.pdf: tb-fdl.xml tb.xls
	@dblatex -s tb/krook1024.sty -b xetex tb-fdl.xml -p tb.xls

.PHONY: tb_fdl.pdf_travis
tb_fdl.pdf_travis: tb-fdl.xml tb.xls
	dblatex tb-fdl.xml -p tb.xls

.PHONY: validate_tb_fdl_hu
validate_tb_fdl_hu:
	xmllint --xinclude tb-fdl.xml --output output.xml
	xmllint --relaxng tb/docbookxi.rng output.xml --noout
	@rm -f output.xml

.PHONY: clean
clean:
	@rm -f tb-fdl.pdf
