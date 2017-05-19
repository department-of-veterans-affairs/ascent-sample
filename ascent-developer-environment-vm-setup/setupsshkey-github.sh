#!/usr/bin/env bash
echo '######## Setting up ssh  key ########'
cd
ssh-keygen -t rsa -b 4096 -C "My GitHub Account"
echo '######## ssh key generated ########'
eval 'ssh-agent -s'
eval $(ssh-agent -s)
ssh-add -k ~/.ssh/id_rsa
echo '######## ssh key added to ssh-agent ########'