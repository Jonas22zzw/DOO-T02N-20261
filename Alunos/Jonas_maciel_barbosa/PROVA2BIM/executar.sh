#!/bin/bash
cd "$(dirname "$0")"

JDK_BIN="/c/Program Files/Java/jdk-17/bin"

"$JDK_BIN/java" -cp bin fag.Main
