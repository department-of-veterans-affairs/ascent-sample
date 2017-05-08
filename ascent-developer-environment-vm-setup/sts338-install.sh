#!/usr/bin/env bash
  #Installation of STS 3.8.4	
  echo 'Installing Spring Tool Suite 3.8.4'
	sudo wget -O /opt/spring-tool-suite-3.8.4.RELEASE-e4.6.3-linux-gtk-x86_64.tar.gz http://download.springsource.com/release/STS/3.8.4.RELEASE/dist/e4.6/spring-tool-suite-3.8.4.RELEASE-e4.6.3-linux-gtk-x86_64.tar.gz
	cd /opt/ && sudo tar -zxvf spring-tool-suite-3.8.4.RELEASE-e4.6.3-linux-gtk-x86_64.tar.gz
  echo 'Installed Spring Tool Suite 3.8.4'