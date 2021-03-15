#!/bin/bash
curl -d "username=admin&password=asdf" --cookie-jar ./cookiejar localhost:9080/loginPage
curl -H "Content-Type: application/json" -X POST --cookie ./cookiejar -d @body.json localhost:9080/characters --cookie-jar ./cookiejar
#curl --cookie ./cookiejar localhost:9080/characters/-2147483648 > out.json

