#!/usr/bin/env sh

cd files
for f in $(find . -name "*" ! -path '*venv*' ! -path '*png' ! -path *'jpg' ! -path *'.tar.gz' -type f); do
    echo $f
    cat "$f" | iconv -c -f utf-8 -t ascii//TRANSLIT//IGNORE > "$f.new"
    mv "$f.new" "$f"
done
