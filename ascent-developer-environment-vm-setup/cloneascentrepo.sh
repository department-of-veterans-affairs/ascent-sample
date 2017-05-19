#!/usr/bin/env bash
echo '######## Starting to clone the Ascent project repository ########'
mkdir -p projects/workspace
cd projects/workspace
git clone git@github.com:department-of-veterans-affairs/ascent.git
echo '######## Ascent project repository cloned under /home/vagrant/projects/workspace ########' 