@echo off
REM Set title
title Running Selenium TestNG Tests

REM Move to the project root (if not already)
cd /d %~dp0

REM Clear screen
cls

echo -----------------------------------------
echo Running Maven Clean Test with testng.xml
echo -----------------------------------------

REM Run the tests using testng.xml from root
mvn clean test -DsuiteXmlFile=testng.xml

echo -----------------------------------------
echo Test Execution Completed
pause
