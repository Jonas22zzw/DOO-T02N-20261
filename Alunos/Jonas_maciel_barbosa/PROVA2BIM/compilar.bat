@echo off
setlocal
cd /d "%~dp0"

set JDK_BIN=C:\Program Files\Java\jdk-17\bin

if not exist bin mkdir bin

dir /s /b src\*.java > sources.txt

echo Compilando com o JDK 17...
"%JDK_BIN%\javac.exe" -d bin -encoding UTF-8 @sources.txt
set BUILD_RESULT=%ERRORLEVEL%
del sources.txt

if %BUILD_RESULT% NEQ 0 (
    echo.
    echo ============================================
    echo   ERRO NA COMPILACAO. Veja as mensagens acima.
    echo ============================================
) else (
    echo.
    echo Compilacao concluida com sucesso.
    echo Use executar.bat para rodar o sistema.
)

pause
