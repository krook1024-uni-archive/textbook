#!/usr/bin/env sh

accents=('á' 'é' 'í' 'ó' 'ö' 'ő' 'ú' 'ü' 'ű')

cd ../files/

for i in "${accents[@]}"; do
    echo $i
    egrep -lRZ $i . | xargs -0 -l sed -i -e "s/$i//g"
done

