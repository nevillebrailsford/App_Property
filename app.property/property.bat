@echo off

rem this file is stored in the Property.app directory
rem and executes the property application.

echo *==============================
echo * Run the property application.
echo *==============================

SETLOCAL

echo *=================================
echo * Set up the required directories.
echo *=================================

set ROOT_DIR=C:\Users\nevil\Projects\Property.app
set JAR_NAME=PropertyApp.jar
set DATA_DIR=C:\Users\nevil\OneDrive\Projects\data

echo *=============================================
echo * Invoke the program - may take a few moments.
echo *=============================================

java.exe -jar %ROOT_DIR%\%JAR_NAME% --name=property.application --dir=%DATA_DIR%

ENDLOCAL