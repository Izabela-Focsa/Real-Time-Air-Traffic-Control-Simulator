[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/tCLXqlpe)
# Tema2POO-2024-schelet
#### Readme

1. Clasa Main
   Aceasta este clasa principala care coordoneaza intregul program. Ea citeste comenzile dintr-un fisier de intrare si apeleaza metodele corespunzatoare pentru gestionarea pistelor si a avioanelor.
Citirea fisierelor: Foloseste BufferedReader pentru a citi comenzi dintr-un fisier de input si BufferedWriter pentru a scrie rezultatele si exceptiile in fisiere separate.
Gestionarea comenzilor: Metoda Comanda interpreteaza comenzile si efectueaza actiuni.

Abordarea pentru fiecare comanda:
-> add_runway_in_use
Descriere
Aceasta comanda adauga o pista noua in sistem.

Pasi executati
Extrage parametrii:

id: identificatorul pistei.
aterizeazaSAUdecoleaza: verifica daca pista este pentru decolare (takeoff) sau aterizare.
wideSAUnarrow: verifica daca pista este pentru avioane de tip wide body sau narrow body.
Creeaza pista:
Initializeaza un obiect de tip Runway<Airplane> cu parametrii specificati.

Adauga pista in lista de piste:
Pista este adaugata in colectia de piste active.

-> allocate_plane
Descriere
Aceasta comanda aloca un avion pe o pista.

Pasi executati
Preia detaliile avionului: Tipul avionului (wide body sau narrow body), modelul, ID-ul zborului, locatia de plecare si destinatia, ora dorita si pista pe care va fi alocat.

Initializeaza avionul:
Creeaza un obiect de tip WideBodyAirplane sau NarrowBodyAirplane, in functie de tip.

Gaseste pista:
Verifica daca pista exista. Daca nu, arunca exceptia IncorrectRunwayException.

Seteaza statusul avionului:

Daca pleaca din Bucuresti, avionul asteapta decolare.
Altfel, asteapta aterizare.
Verifica compatibilitatea cu pista:

Nu poate decola de pe o pista de aterizare si invers.
Avioanele wide body si narrow body trebuie sa fie alocate pe pista corespunzatoare.
Aloca avionul:
Daca toate verificarile trec, avionul este adaugat in coada pistei.

-> permission_for_maneuver
Descriere
Aceasta comanda permite unui avion sa efectueze manevra (decolare/aterizare).

Pasi executati
Identifica pista: Cauta pista specificata.

Verifica disponibilitatea:
Daca pista este ocupata, scrie eroarea in fisierul de exceptii.

Permite manevra:
Daca pista este libera, permite avionului cu prioritate sa efectueze manevra.

-> runway_info
Descriere
Aceasta comanda genereaza un raport detaliat despre starea unei piste si lista avioanelor asociate.

Pasi executati
Identificare pista:
Cauta pista cu ID-ul specificat (ID_pista) in colectia de piste.

Generare fisier de output:
Daca pista exista, creeaza un fisier denumit runway_info_<ID_pista>_<ora>.out.

Scriere informatii despre pista:

Se scrie daca pista este OCCUPIED (ocupata) sau FREE (libera).
Se adauga detalii despre toate avioanele alocate pistei, ordonate astfel:
Avioanele urgente apar primele.
Daca prioritatea este egala, se sorteaza dupa ora dorita.

-> flight_info
Descriere
Aceasta comanda afiseaza informatii despre un anumit zbor.

Pasi executati
Identificare zbor:
Parcurge toate pistele si cauta avionul cu ID-ul de zbor specificat (ID).

Scriere informatii:
Daca avionul este gasit, scrie in fisierul flight_info.out detalii despre avion, incluzand:

Ora curenta
Informatiile complete despre avion (toString())
Verificare in avioanele permise:
Daca avionul nu a fost gasit in lista avioanelor in asteptare, se verifica si in lista avioanelor care au primit deja permisiunea de manevra.

Scriere in fisier:
Daca avionul a fost gasit, se face flush la scriere pentru a se asigura ca datele sunt salvate.

-> exit
Descriere
Aceasta comanda opreste executia programului.

Pasi executati
Iesire din program:
Comanda foloseste return pentru a opri procesarea comenzilor si a inchide programul.



2. Clasa Airplane
   Aceasta este o clasa abstracta care defineste caracteristicile de baza ale unui avion:

Atribute: modelul avionului, ID-ul zborului, locatiile de plecare si destinatie, timpul dorit de manevra, timpul concret cand s-a efectuat manevra si daca avionul este sau nu in regim de urgenta.

Enum Status: definirea starii curente a avionului (WAITING_FOR_TAKEOFF, DEPARTED, WAITING_FOR_LANDING, LANDED).

Metode:

-> isUrgent: Metoda statica pentru verificarea urgentei unui avion.
-> toString: Metoda pentru afisarea detaliilor avionului intr-un format clar.

3. Clasele WideBodyAirplane si NarrowBodyAirplane sunt clase derivate din Airplane, reprezentand doua tipuri specifice de aeronave:
   -> WideBodyAirplane
   -> NarrowBodyAirplane


4. Clasa Runway<T extends Airplane>
   Aceasta modeleaza o pista de aterizare sau decolare.

Atribute:
-> id: identificatorul pistei.
-> decoleazaSAUaterizeaza: specifica daca pista este pentru decolare (true) sau aterizare (false).
-> wideSAUnarrow: specifica daca pista este pentru avioane mari (true) sau mici (false).
-> disponibil: timpul pana la care pista este ocupata.
-> avioane: priorityQueue care gestioneaza avioanele in functie de prioritate.
-> avioanePermise: avioane care au primit permisiunea de manevra.

Exceptii:
->IncorrectRunwayException: aruncata cand avionul este alocat gresit unei piste.
->UnavailableRunwayException: aruncata cand pista este ocupata.

Metode:
->addAirplane: adauga un avion in coada de prioritati dupa verificarea conditiilor de compatibilitate.
->permissionForManeuver: permite manevra avionului daca pista este libera.
->checkAvailability: verifica daca pista este libera.
isOccupied: determina daca pista este ocupata.

5. Utilizarea PriorityQueue
   Motivul folosirii PriorityQueue este pentru a gestiona eficient ordinea avioanelor care trebuie sa decoleze sau sa aterizeze, in functie de prioritate.
-> Optimizeaza gestionarea prioritatilor (urgenta si timp dorit).
-> Ofera eficienta in inserare si extragere.
-> Simplifica implementarea.
-> Se scaleaza usor pentru un volum mare de date.
-> Asigura corectitudinea deciziilor in alocarea pistelor.

Pentru aterizare (decoleazaSAUaterizeaza == false), avioanele sunt ordonate astfel:
Avioanele urgente au prioritate mai mare.
Daca prioritatea este egala, se ordoneaza dupa timpul dorit (timp_dorit).
Pentru decolare (decoleazaSAUaterizeaza == true), avioanele sunt ordonate doar dupa timpul dorit de manevra.
