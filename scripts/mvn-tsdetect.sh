#!/usr/bin/env bash

set -e

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

JAVA17_HOME="${JAVA17_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"

if [[ ! -x "$JAVA17_HOME/bin/java" ]]; then
    echo "Erro: JDK 17 não encontrado em $JAVA17_HOME"
    echo "Instale o JDK 17 ou configure JAVA17_HOME."
    exit 1
fi

export JAVA_HOME="$JAVA17_HOME"
export PATH="$JAVA_HOME/bin:$PATH"

exec "$ROOT/mvnw" "$@"