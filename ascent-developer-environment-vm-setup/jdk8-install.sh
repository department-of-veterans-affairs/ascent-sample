#!/usr/bin/env bash
  #Intallation of JDK1.8 
echo 'Installing JDK 1.8'  
    sudo add-apt-repository -y ppa:webupd8team/java
	sudo apt-get update
    sudo apt-get -y upgrade
    echo debconf shared/accepted-oracle-license-v1-1 select true | sudo debconf-set-selections 
    echo debconf shared/accepted-oracle-license-v1-1 seen true | sudo debconf-set-selections
    sudo apt-get -y install oracle-java8-installer
echo 'Installed JDK 1.8'	