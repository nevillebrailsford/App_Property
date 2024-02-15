#!/bin/zsh

echo "*=============================="
echo "* Run the property application."
echo "*=============================="

echo "*================================="
echo "* Set up the required directories."
echo "*================================="

ROOT_DIR=/Users/nevil/Projects/Property.app
JAR_NAME=PropertyApp.jar
DATA_DIR=/Users/nevil/OneDrive/Projects/data

echo "*============================================="
echo "* Invoke the program - may take a few moments."
echo "*============================================="

java -jar ${ROOT_DIR}/${JAR_NAME} --name=property.application --dir=${DATA_DIR}