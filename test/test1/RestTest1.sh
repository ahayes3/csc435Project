#!/bin/bash
curl -d "username=admin&password=asdf" --cookie-jar ./cookiejar localhost:9080/loginPage
curl -X POST --cookie ./cookiejar -d "name=Joe&bg=urchin&race=elf&languages=common&str=10&dex=11&con=12&int=13&wis=14&cha=15&ac=14&init=2&speed=30&maxHp=46&class0=Fighter&level0=1" localhost:9080/characters --cookie-jar ./cookiejar
curl --cookie ./cookiejar localhost:9080/characters/-2147483648 > out.json

