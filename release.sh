#!/bin/bash

java -jar ~/proguard4.10/lib/proguard.jar @proguard.pro

jarsigner -storepass password -keypass password -signedjar dist/NetAcquireClient.jar dist/proguard.jar mykey

cp dist/NetAcquireClient.jar ~/code/tlstyer.com/NetAcquireClient/

