#!/bin/bash
# Compila o sistema usando o JDK 17.
set -e
cd "$(dirname "$0")"

JDK_BIN="/c/Program Files/Java/jdk-17/bin"

mkdir -p bin
echo "Compilando com o JDK 17..."
find src -name "*.java" > sources.txt
"$JDK_BIN/javac" -d bin -encoding UTF-8 @sources.txt
rm sources.txt
echo "Compilacao concluida. Use ./executar.sh para rodar o sistema."
