#!/bin/bash

java -jar ~/proguard4.6/lib/proguard.jar @proguard.pro 

jarsigner -storepass password -keypass password -signedjar dist/NetAcquireClient.jar dist/proguard.jar mykey

cp dist/NetAcquireClient.jar ~/projects/tlstyer.com/NetAcquireClient/

