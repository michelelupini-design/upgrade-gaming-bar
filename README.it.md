# UpGrade Gaming Bar

**Un menu arcade di Q¹¹ Space Otherfold Games.**

[English](README.md) · [Contribuire](CONTRIBUTING.md) · [Verifiche e limiti](docs/VALIDATION.md)

Un menu per scegliere e avviare giochi attraverso un'installazione separata di RetroArch, usando joystick e pulsanti. Include ricerca con tastiera a schermo, suggerimenti dopo tre caratteri, filtro per piattaforma, elenco alfabetico, preferiti e guida illustrata ai comandi.

**Stato: prima pubblicazione sperimentale dei sorgenti, 23 novembre 2026. Codice [MIT](LICENSE); grafica, font e marchi hanno [condizioni distinte](ASSET-LICENSES.md).** Non è un firmware universale Pandora e non contiene una raccolta di giochi.

## Cosa funziona sulla console verificata

Il menu multilingua è installato sulla console di riferimento n200/meson8, Android 4.4.2 API 19 ARMv7. Avvio dalla microSD senza chiavetta USB, avvio/uscita di giochi campione e comandi fisici su titoli selezionati sono stati verificati. Il catalogo privato ha 484 voci principali; non viene distribuito con i sorgenti.

Le tre aperture originali UpGrade/Q11 si alternano senza ripetizione immediata e mostrano un indicatore animato di attesa, senza percentuali simulate. È stata corretta la permanenza dell'ultimo fotogramma sopra al menu; il proprietario ha confermato menu visibile e joystick funzionante. Resta una transizione nera di circa due secondi. Questa integrazione di avvio è specifica della scheda e separata dal launcher pubblico.

Il Wi-Fi ha superato trasferimento, inattività breve, riconnessione e accensione senza LAN. Le precedenti cadute di autenticazione impediscono di dichiararlo definitivamente risolto; rete del locale, PC di manutenzione e TV da 32 pollici restano da provare sul posto.

## Lingue

Italiano e inglese hanno 114 testi iniziali ciascuno. Spagnolo, portoghese, tedesco, francese, cinese semplificato e giapponese hanno 12 testi iniziali ciascuno e ripiego sull'inglese. Non sono traduzioni complete o revisionate da madrelingua. La tastiera a joystick inserisce lettere latine e numeri; sono predisposti resa CJK e ricerca, non un metodo di composizione cinese/giapponese.

## Cosa comprende il pacchetto

Sorgenti, grafica del progetto, istruzioni di compilazione, controlli automatici e modelli per contributi. Il catalogo è vuoto. RetroArch, core, giochi, BIOS, firmware, loghi terzi, credenziali e chiavi private sono esclusi. Sul dispositivo, Android e applicazioni stanno nella memoria interna; la microSD contiene giochi e catalogo.

La compilazione di anteprima usa un nome pacchetto separato, non diventa HOME e non richiede la chiave privata di produzione. Requisiti e comandi sono nel [README principale](README.md#build-a-development-preview). I percorsi e il collegamento all'emulatore sono specifici dell'installazione verificata; leggere [INSTALLATION.md](docs/INSTALLATION.md).

## Contributi

Sono utili traduzioni, verifiche su altre schede, mappature dei comandi, leggibilità e segnalazioni delle pubblicazioni originali degli autori dei giochi. Non inviare ROM o BIOS. Usare i modelli di segnalazione e distinguere avvio automatico da una partita realmente provata.

Q¹¹ Space Otherfold Games è il marchio completo della software house; UpGrade Gaming Bar è il nome del programma e del locale. Il codice è MIT, anche per uso commerciale. Tre asset grafici non di marchio sono CC BY 4.0; il font mantiene OFL. Marchi e loghi seguono la [politica dedicata](BRAND-POLICY.md): i fork devono distinguere la propria identità.
