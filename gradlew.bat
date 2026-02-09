@echo off
setlocal enabledelayedexpansion
set APP_NAME=Gradle
set APP_BASE_NAME=%~n0
java -jar gradle/wrapper/gradle-wrapper.jar %*
