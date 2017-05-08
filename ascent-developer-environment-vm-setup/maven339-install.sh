#!/usr/bin/env bash
echo 'Installing Apache Maven 3.3.9'
sudo wget http://apache.mirrors.lucidnetworks.net/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
sudo mkdir -p /usr/local/apache-maven
sudo mv apache-maven-3.3.9-bin.tar.gz /usr/local/apache-maven
cd /usr/local/apache-maven/ && sudo tar -xzvf apache-maven-3.3.9-bin.tar.gz
echo 'Installed Apache Maven 3.3.9'
