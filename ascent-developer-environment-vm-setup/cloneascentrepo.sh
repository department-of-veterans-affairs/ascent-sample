#!/usr/bin/env bash
echo '######## Starting to clone the Ascent-Platform project repository ########'
mkdir -p projects/workspace
cd projects/workspace
git clone https://github.com/department-of-veterans-affairs/ascent-platform.git
echo '######## Ascent-Platform project repository cloned under /home/vagrant/projects/workspace ########' 
