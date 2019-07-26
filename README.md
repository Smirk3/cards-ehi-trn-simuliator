Application is used to simulate card authorization messages of GPS provider (https://globalprocessing.net/) like GPS External Host Interface (EHI) do.

External Host Interface (EHI) is a real-time transactional data feed that enables you to implement bespoke decision-making logic and your own authorization rules.


Startup for Windows

You should have java 1.8 installed on your machine.

download cards-ehi-trn-simulator artifact (jar)
at the same directory create "startup.bat" file
copy/paste provided "startup.bat" file content:

set JAVA_HOME=c:\Program Files\Java\jre1.8.0_74
set PATH=%JAVA_HOME%\bin;%PATH%
java -Dserver.port=8080 -jar card-ehi-trn-simuliator-1.0.0.jar
pause

change "startup.bat" file content JAVA_HOME property value to Java 1.8 installed on your's machine path
in "startup.bat" file content check card-ehi-trn-simuliator artifact version you are trying to execute: "card-ehi-trn-simuliator-1.0.0.jar"
execute "startup.bat" file.
open in browser URL "http://localhost:8080"
you can start application server on any different port number by changing port number -Dserver.port=8080
