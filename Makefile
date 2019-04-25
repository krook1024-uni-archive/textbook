all:	clean fdl

fdl:	clean validate_tb_fdl_hu tb_fdl.pdf

tb_fdl.pdf: tb-fdl.xml tb.xls
	@dblatex -s tb/krook1024.sty -b xetex tb-fdl.xml -p tb.xls -d

.PHONY: validate_tb_fdl_hu
validate_tb_fdl_hu:
	@xmllint --xinclude tb-fdl.xml --output output.xml
	@xmllint --relaxng tb/docbookxi.rng output.xml --noout
	@rm -f output.xml

.PHONY: clean
clean:
	@rm -f tb-fdl.pdf
