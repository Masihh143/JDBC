#!/bin/bash

# Define variables
JAR_PATH="./sqlite-jdbc-3.50.3.0.jar"
CLASS_NAME="Main_SQLite"

# Compile all source files
echo "Compiling Java files..."
javac -cp ".:$JAR_PATH" DBconnector_SQLite.java
javac -cp ".:$JAR_PATH" FkValidator.java
javac -cp ".:$JAR_PATH" InputHelper.java
javac -cp ".:$JAR_PATH" InsertHandler.java
javac -cp ".:$JAR_PATH" FQ.java
javac -cp ".:$JAR_PATH" $CLASS_NAME.java
echo ""

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "                       Welcome back sir!"
    java -cp ".:$JAR_PATH" $CLASS_NAME
else
    echo "Compilation failed."
fi
