all:	clean fdl

fdl:	clean validate_tb_fdl_hu tb_fdl.pdf

tb_fdl.pdf: tb-fdl.xml tb.xls
	dblatex tb-fdl.xml -p tb.xls

.PHONY: validate_tb_fdl_hu
validate_tb_fdl_hu:
	xmllint --xinclude tb-fdl.xml --output output.xml
	xmllint --relaxng docbookxi.rng output.xml --noout
	rm -f output.xml

.PHONY: clean
clean:
	rm -f tb-fdl.pdf
