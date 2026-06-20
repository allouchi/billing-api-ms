#!/bin/bash

set -e

docker system prune -f

echo "🧹 Cleaning dangling images..."

docker image prune -f

docker compose down --volumes

echo "✔ Clean completed"