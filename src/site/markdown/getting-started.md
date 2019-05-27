## Card Transaction EHI simulator Getting Started Guide

### Introduction

The purpose of this guide is to show how to startup application on local machine.

### Startup

You should have java 1.8 installed on your machine.

- download card-ehi-trn-simulator artifact (jar)
- at the same directory create "startup.bat" file
- copy/paste provided "startup.bat" file content

```
set JAVA_HOME=c:\Program Files\Java\jre1.8.0_74
set PATH=%JAVA_HOME%\bin;%PATH%
java -Dserver.port=9191 -jar card-ehi-trn-simuliator-1.0.0.jar
pause
```

- change "startup.bat" file content JAVA_HOME property value to Java 1.8 installed on your's machine path
- in "startup.bat" file content check card-ehi-trn-simulator artifact version you are trying to execute: "card-ehi-trn-simuliator-`1.0.0`.jar"
- execute "startup.bat" file.
- open in browser URL "http://localhost:9191"
- you can start application server on any different port number by changing port number `-Dserver.port=9191`
