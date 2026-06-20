#!/bin/bash

set -e

docker system prune -a --volumes -f

skaffold dev --cleanup=true --no-prune=false --cache-artifacts=false