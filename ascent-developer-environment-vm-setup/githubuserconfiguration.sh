#!/usr/bin/env bash
read -p "Enter email associated with github account: "  email
read -p "Enter your github username:" username
git config --global user.email "$email"
git config --global user.name "$username"
echo "Github user identity has been configured successfully"

