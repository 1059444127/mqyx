@echo off
cd %~d0
call mvn clean package -DskipTests
pause