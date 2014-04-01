#!/bin/bash

java -jar ~/proguard4.11/lib/proguard.jar @proguard.pro

cp dist/proguard.jar ~/code/tlstyer.com/NetAcquireClient/NetAcquireClient.jar
