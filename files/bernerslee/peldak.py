#!/usr/bin/env ptyhon3

# Listak
lista = ['boci', 3, 'kecske', 3.1415]
szamok = [1, 2, 3, 4, 5]
allatok = ['kutya', 'macska']

## Kiiratas
print(lista)
print(szamok)
print(allatok)

## Listahoz fuzes
allatok.append('eger')
szamok.append(6)

## Elem torlese
szamok.remove(2)

## Lista letrehozasa listabol

## Fuggveny
def vowel(ch): # Maganhangzo-e
    return ch.lower() in ['a', 'e', 'i', 'o', 'u']

## Vegiglepkedes a lista elemein
for allat in allatok:
    z = 'z' if vowel(allat[0]) else '' ## Ternary operator
    print('Az en kedvenc allatom a' + z, allat)

## Inline for ciklus
szamok_duplaja = [2 * szam for szam in szamok]
print('A szamok duplaja', szamok_duplaja)

## Lista, sztring, stb hossza
print('Tarolt allatok szama:', len(allatok))
