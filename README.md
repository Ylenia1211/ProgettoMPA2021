## Licenza

Questo progetto è licenziato sotto la Licenza [Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)]
[![License: CC BY-NC 4.0](https://img.shields.io/badge/License-CC%20BY--NC%204.0-lightgrey.svg)]
(https://creativecommons.org/licenses/by-nc/4.0/).
Consulta il file [LICENSE](LICENSE) per ulteriori dettagli.


# ProgettoMPA2021
Progetto MPA 2020/2021
Il software esposto in questa relazione avrà lo scopo di riuscire a gestire una clinica veterinaria in cui lavorano uno o più dottori. 
Sarà infatti possibile registrare tutti i medici che vi lavorano, ma anche gli addetti alla segreteria, filtrando le operazioni effettuabili da ogni tipologia di utente per organizzare meglio il lavoro e “facilitarlo”, suddividendo i compiti all’interno della clinica.
Per potere registrare i dati, per il momento è stato utilizzato H2, un database che può essere utilizzato in locale e che, dunque, permette di funzionare senza bisogno di una connessione alla rete.
VetClinic Management, in dettaglio, prevede il seguente funzionamento.
## View Generale
All’avvio del software, la prima interfaccia ad essere presentata all’utente è una finestra di login in cui inserire “username”, “password” e il ruolo dell’utente che sta effettuando l’accesso.
Sono previste infatti tre tipologie specifiche di utenti:
1. Amministratore
2. Segreteria
3. Dottore/ssa.

   
Inizialmente l’utente dopo aver effettuato l’autenticazione, si troverà davanti a una schermata simile alla seguente: 
![View 1](https://user-images.githubusercontent.com/56923471/274726222-36fae07b-24d9-494d-ae0a-eb30378f3f71.png)
In generale le schermate sono suddivise in 3 zone: 
1. la zona che contiene informazioni sul ruolo dell’utente loggato (in alto);
2. la zona che contiene la sidebar dinamica (a sinistra) dei comandi;
3. la macro zona centrale che contiene tutte le view del gestionale.

Tutti gli utenti che accedono al sistema, avranno a disposizione nella sidebar il comando di “Logout”, il quale consentirà all’utente di abbandonare la sessione e ritornare alla schermata iniziale di “Login”. 

Di seguito una lista di View del software in esame: 
![View 2](https://user-images.githubusercontent.com/56923471/274727443-23ba8b38-913e-4c32-9eb4-ce60ff4e9600.png)
![View 3](https://user-images.githubusercontent.com/56923471/274727451-c94e7753-4807-4096-94c8-134ce5499185.png)
![View 4](https://user-images.githubusercontent.com/56923471/274727474-7139dc13-20bf-4b66-b025-be7bce71a529.png)
![View 5](https://user-images.githubusercontent.com/56923471/274727489-34d52ac8-d6ac-4b2f-a54a-ec0df36b545a.png)
![View 6](https://user-images.githubusercontent.com/56923471/274727503-c1435c9b-78d5-4401-86e9-73f33b108abf.png)
