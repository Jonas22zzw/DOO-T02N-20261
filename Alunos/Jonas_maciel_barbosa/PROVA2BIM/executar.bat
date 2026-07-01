@echo off
cd /d "%~dp0"

set JDK_BIN=C:\Program Files\Java\jdk-17\bin

"%JDK_BIN%\java.exe" -cp bin fag.Main
pause
