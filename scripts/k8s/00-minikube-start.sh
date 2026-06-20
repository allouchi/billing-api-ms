#!/bin/bash

set -e


echo "Delete Minikube..."
# Supprime le cluster défaillant et ses caches
minikube delete --all --purge

echo "Starting Minikube..."

minikube start --driver=docker --container-runtime=containerd --memory=6144 --cpus=4

echo "Minikube started"

kubectl get nodes