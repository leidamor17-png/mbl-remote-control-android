#!/bin/sh
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
CLASSPATH=$(find gradle/wrapper -name "gradle-wrapper.jar")
exec java -jar "$CLASSPATH" "$@"
