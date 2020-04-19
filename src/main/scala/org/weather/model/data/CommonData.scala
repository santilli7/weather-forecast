package org.weather.model.data

import scala.io.Codec

/**
 * @author Paolo Santilli
 * @written 29 Marzo, 2020
 * @description
 * 		Oggetto che contiene alcune delle strutture di dati comuni e costanti utilizzate in vari punti dell'applicazione.
 */
object CommonData {

  val bomFileCodec = Codec("windows-1252") //Codifica dei datafile BOM.

  val defaultFieldSep = "\\|" 
  val defaultWriteSep = "|"  
  val bomFileSep = "," 

  
  case class codes(IATACode: String, bomCode: String)
  case class dateRange(min: Int, max: Int) //Max & Min date ranges.

  //Campi utilizzati per la previsione. 
  case class fields(date: String, //Campo 1
                    min_temp: Double, //Campo 2
                    max_temp: Double, //Campo 3
                    rainfall: Double, //Campo 4
                    sunshine: Double, //Campo 6
                    relative_humidity: Integer, //Campo 11
                    cloud_amount: Integer, //Campo 12
                    pressure: Double) //Campo 15

  //Array delle città con il corrispondente IATACodes e il nome del file BOM.
  //IATA Codes sono presi da http://www.iata.org/publications/Pages/code-search.aspx 
 
  val cities = Array(
    "CANBERRA|CBR|IDCJDW2801",
    "SYDNEY|SYD|IDCJDW2124",
    "WOLLONGONG|WOL|IDCJDW2146",
    "MELBOURNE|MEL|IDCJDW3033",
    "BRISBANE|BNE|IDCJDW4019",
    "CAIRNS|CNS|IDCJDW4024",
    "GOLD COAST|OOL|IDCJDW4050",
    "ADELAIDE|ADL|IDCJDW5002",
    "PERTH|PER|IDCJDW6111",
    "HOBART|HBA|IDCJDW7021",
    "DARWIN|DRW|IDCJDW8014")

}