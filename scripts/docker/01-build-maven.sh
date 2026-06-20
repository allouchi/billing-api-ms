#!/bin/bash

set -e

echo "🛠 Building Maven project..."

mvn clean install -DskipTests

echo "✅ Maven build completed"