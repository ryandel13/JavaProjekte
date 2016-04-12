Beschreibung der Komponenten:

JCAStart-lib
	* Bibliothek mit allen für die Crypto-Implementierung notwendigen Methoden
	* Enthält JUnit Tests

JCAStart-WSClient-lib
	* Mit Swagger generierter Webserviceclient
	* Enthält Methoden für den Zugriff auf den Webserver

JCAStart-Server
	* Server der eine übertragene Nachricht entschlüsselt
	* Kann innerhalb eines Application Servers als war deployed werden

JCAStart-Client
	* Commandline-client zum senden von verschlüsseltem Text
	* Folgende Funktionen:
		* genkeys: Erzeugt eine publicKey.crt und eine privateKey.pem (Base64 codierte Schlüssel)
		* selftest: Teste die lib gegen sich selbst mit den zur Zeit abgelegten Schlüsseln
		* sendmessage: Sendet die abgelegte Nachricht und den Einmalschlüssel jeweils verschlüsselt an den Server und bekommt als Antwort den Entschlüsselten klartext
		* quit: beendet die Anwendung

Bauen mit maven.