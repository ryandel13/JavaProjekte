Beschreibung der Komponenten:

JCAStart-lib
	* Bibliothek mit allen f�r die Crypto-Implementierung notwendigen Methoden
	* Enth�lt JUnit Tests

JCAStart-WSClient-lib
	* Mit Swagger generierter Webserviceclient
	* Enth�lt Methoden f�r den Zugriff auf den Webserver

JCAStart-Server
	* Server der eine �bertragene Nachricht entschl�sselt
	* Kann innerhalb eines Application Servers als war deployed werden

JCAStart-Client
	* Commandline-client zum senden von verschl�sseltem Text
	* Folgende Funktionen:
		* genkeys: Erzeugt eine publicKey.crt und eine privateKey.pem (Base64 codierte Schl�ssel)
		* selftest: Teste die lib gegen sich selbst mit den zur Zeit abgelegten Schl�sseln
		* sendmessage: Sendet die abgelegte Nachricht und den Einmalschl�ssel jeweils verschl�sselt an den Server und bekommt als Antwort den Entschl�sselten klartext
		* quit: beendet die Anwendung

Bauen mit maven.