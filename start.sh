#!/bin/bash

# Define variables
JAR_PATH="/usr/share/java/mysql-connector-j-9.4.0.jar"
SRC_FILE="Main.java"
CLASS_NAME="Main"

# Compile the Java program
echo "Compiling $SRC_FILE..."
javac -cp .:$JAR_PATH $SRC_FILE
javac -cp .:$JAR_PATH FkValidator.java 
javac -cp .:$JAR_PATH InputHelper.java 
javac -cp .:$JAR_PATH InsertHandler.java 
echo ""

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "                       Welcome back sir!"
    java -cp .:$JAR_PATH $CLASS_NAME
else
    echo "Compilation failed."
fi
