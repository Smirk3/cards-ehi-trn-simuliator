### About application

WEB Application is used to simulate SOAP card authorization messages of GPS provider (https://globalprocessing.net/) like GPS External Host Interface (EHI) do.

External Host Interface (EHI) is a real-time transactional data feed that enables you to implement bespoke decision-making logic and your own authorization rules.

Global Processing Services (GPS) is an award-winning issuer processor enabling many of today’s most high-proﬁle ﬁntech innovators and disruptors. They are certiﬁed by Mastercard and Visa to process and manage any credit, debit or pre-paid card transaction globally.  
They enable the emerging payments industry to deliver breakthrough innovations through a unique combination of proprietary technology, their people and partners.

### Startup for Windows

You should have java 1.8+ installed on your machine.  
download executable *cards-ehi-trn-simulator* JAR (https://github.com/Smirk3/cards-ehi-trn-simuliator/tree/master/target)

**Start from command line:**  
`java -Dserver.port=8080 -jar card-ehi-trn-simuliator-1.0.0-def.jar`

OR

**Create BAT file:**  
- download cards-ehi-trn-simulator artifact (jar)  
- at the same directory create "startup.bat" file  
- copy/paste provided "startup.bat" file content:

>`set JAVA_HOME=c:\Program Files\Java\jre1.8.0_74`  
>`set PATH=%JAVA_HOME%\bin;%PATH%`  
>`java -Dserver.port=8080 -jar card-ehi-trn-simuliator-1.0.0-def.jar`  
>`pause`

- change "startup.bat" file content JAVA_HOME property value to Java 1.8 installed on your's machine path
- in "startup.bat" file content check card-ehi-trn-simuliator artifact version you are trying to execute: "card-ehi-trn-simuliator-1.0.0-def.jar"
- execute "startup.bat" file.  
- open in browser URL "http://localhost:8080/ehi"  
- you can start application server on any different port number by changing port number **-Dserver.port=8080**

### Main functionality
- Save/upload your's own configuration to/from file.
![settings!](/src/main/resources/static/images/readme_settings.png "settings upload/save")
- Manage cards list for testing.
![cards!](/src/main/resources/static/images/readme_cards.png "cards list")
- Set card transaction notification parameters form
![notification form!](/src/main/resources/static/images/readme_notification_form.png "notification form")
- View/edit generated by application XML request before submit.
![notification preview!](/src/main/resources/static/images/readme_notifacation_preview.png "notification preview")
- View sent card notifications. Send more card notifications based on recently sent.
![notification list!](/src/main/resources/static/images/readme_notification_list.png "notification list")
- View sent card notifications XML request and response
![XML request/response!](/src/main/resources/static/images/readme_notification_xml_request_response.png "XML request/response")

