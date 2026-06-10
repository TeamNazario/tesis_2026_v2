#!/bin/bash
echo "Checking for Ubigeo classes in app.jar..."
if java -cp app.jar -version 2>/dev/null; then
  echo "ok"
fi
# Use Java to list JAR contents
java -Xmx64m -cp /usr/lib/jvm/temurin-17-jre-amd64/lib/tools.jar:. \
  -Dfile.encoding=UTF-8 \
  -jar app.jar --list 2>/dev/null || true

# Alternative: use find in the extracted BOOT-INF
echo "Listing BOOT-INF classes with ubigeo..."
if [ -d /tmp/jar_extracted ]; then rm -rf /tmp/jar_extracted; fi
mkdir -p /tmp/jar_extracted
cd /tmp/jar_extracted
unzip -q /app/app.jar "BOOT-INF/classes/com/example/demo/controller/Ubigeo*" 2>/dev/null
unzip -q /app/app.jar "BOOT-INF/classes/com/example/demo/model/Ubigeo*" 2>/dev/null  
unzip -q /app/app.jar "BOOT-INF/classes/com/example/demo/service/Ubigeo*" 2>/dev/null
ls -la BOOT-INF/classes/com/example/demo/controller/ 2>/dev/null | grep -i ubigeo
ls -la BOOT-INF/classes/com/example/demo/model/ 2>/dev/null | grep -i ubigeo
ls -la BOOT-INF/classes/com/example/demo/service/ 2>/dev/null | grep -i ubigeo
echo "Done"
