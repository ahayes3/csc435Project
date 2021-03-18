#!/bin/bash
curl --cookie-jar cookies -X PUT -H "Content-Type: application/json" -d @user.json localhost:9080/login

curl --cookie-jar cookies --cookie cookies -X PUT -H "Content-Type: application/json" -d @test2.json localhost:9080/characters/86688bf6-7a32-4336-bc30-4326b10ce93d > out.json
