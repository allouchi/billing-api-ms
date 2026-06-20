#!/bin/bash

set -e

./scripts/k8s/00-minikube-start.sh
./scripts/k8s/01-build-maven.sh
./scripts/k8s/02-delete-k8s.sh
./scripts/k8s/03-build-images-k8s.sh
./scripts/k8s/04-deploy-k8s.sh
./scripts/k8s/05-k8s-status.sh