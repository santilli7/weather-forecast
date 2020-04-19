### Progetto Scalable and Cloud programming AA 2018/2019 
### Implementazione di un'applicazione distribuita per la previsione del meteo in Scala e Spark

Il progetto è stato realizzato per implementare un'applicazione distribuita per la previsione in tempo reale del meteo.
Verranno usati i dati disponibili per l'addestramento di un modello di previsione basato sull'analisi delle timeseries con il modello ARIMA in Scala.I dati meteo vengono elaborati in tempo reale per la previsione a breve termine del meteo per un gruppo di città stabilite a priori. Le città Canberra, Sydney, Wollogong, Melbourne, Brisbane, Cairns, Gold Coast, Adelaide, Perth, Hobart, Darwin.

L'applicazione è progettata per prevedere il tempo di varie città importanti in Australia. Poiché le osservazioni meteorologiche sono protratte per un certo periodo di tempo, l'analisi delle serie temporali utilizzando [ARIMA] (https://en.wikipedia.org/wiki/Autoregressive_integrated_moving_average) viene utilizzata per prevedere i parametri meteorologici.

I dati richiesti per la previsione provengono da [BOM (Bureau of Meteorology, Australia)] (http://www.bom.gov.au/climate/dwo/).

L'implementazione è stata effettuata con il linguaggio Scala ed è stato utilizzato Spark per la parallelizzazione di alcune procedure.

I run sono stati effettuati prima in locale e poi sulla piattaforma AWS importando il file .jar.

#### Struttura del progetto

* **_data/_**: in questa cartella sono presenti i file .csv delle città per cui si svolge la previsione
* **_src/main/_**
    * **_data/_**: in questa cartella sono presenti classi di ausilio alla classe principale
      **_forecast/_**  
      * _**PredictFunction**_: classe che fornisce funzioni per la previsione del tempo come per esempio la funzione 
      - excecuteArima:  che inizializza ed esegue il modello per l'analisi delle timeseries
      - performPrediction: che si occupa di maneggiare i dati da passare ad Arima. 
* **_PresentazioneSCPSantilli.pdf_**: presentazione del progetto e dei risultati.