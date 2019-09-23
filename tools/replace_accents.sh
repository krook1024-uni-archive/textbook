#!/usr/bin/env sh

cd files
for f in $(find . -name "*" ! -path '*venv*' ! -path '*png' ! -path *'jpg' -type f); do
    echo $f
    cat "$f" | iconv -f utf8 -t ascii//TRANSLIT//IGNORE > "$f.new"
    mv "$f.new" "$f"
done
