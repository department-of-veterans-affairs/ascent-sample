#!/usr/bin/env bash
#Intsllation of Docker-Compose
echo 'Installing Docker-Compose...'
sudo curl -o /usr/local/bin/docker-compose -L "https://github.com/docker/compose/releases/download/1.11.2/docker-compose-$(uname -s)-$(uname -m)"
sudo chmod +x /usr/local/bin/docker-compose
echo 'Installed Docker-Compose...'
