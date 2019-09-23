#!/usr/bin/env ptyhon3

# Listák
lista = ['boci', 3, 'kecske', 3.1415]
szamok = [1, 2, 3, 4, 5]
allatok = ['kutya', 'macska']

## Kiíratás
print(lista)
print(szamok)
print(allatok)

## Listához fűzés
allatok.append('egér')
szamok.append(6)

## Elem törlése
szamok.remove(2)

## Lista létrehozása listából

## Függvény
def vowel(ch): # Magánhangzó-é
    return ch.lower() in ['a', 'e', 'i', 'o', 'u']

## Végiglépkedés a lista elemein
for allat in allatok:
    z = 'z' if vowel(allat[0]) else '' ## Ternary operator
    print('Az én kedvenc állatom a' + z, allat)

## Inline for ciklus
szamok_duplaja = [2 * szam for szam in szamok]
print('A számok duplája', szamok_duplaja)

## Lista, sztring, stb hossza
print('Tárolt állatok száma:', len(allatok))
