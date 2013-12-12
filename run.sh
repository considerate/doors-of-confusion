#!/bin/sh
echo $1
javac -cp .:json-simple-1.1.1.jar *.java && java -cp .:json-simple-1.1.1.jar Main $1 8880 9090 2