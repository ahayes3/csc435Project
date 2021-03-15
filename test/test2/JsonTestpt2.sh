#!/bin/bash
curl --cookie ./cookiejar localhost:9080/characters/$1 > out.json
echo $1
