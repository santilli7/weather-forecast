package org.weather.model.forecast

import org.joda.time.{ DateTime, DateTimeComparator, DateTimeZone }
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.weather.model.data.{ CommonData, FileOperations }
import com.cloudera.sparkts.models.ARIMA
import breeze.linalg.Vector
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * @author Paolo Santilli
 * @written 25 Marzo, 2020
 * @description 
 * 			Organizza le funzioni utilizzate nell'esecuzione delle funzioni di previsione 
 * 
 */

object PredictFunctions {

  /*
   * Per il vettore in input, per eseguire Arima per fare il forecast del prossimo valore.
   */
  def executeARIMAForecast(values: Array[Double]) = {
    ARIMA.fitModel(1, 1, 0, Vector(values))
      .forecast(Vector(values), values.size + 1)
      .toArray
      .last

  }

  /*
   * Funzione per predirre il meteo. L'applicazione classifica, il meteo come Sunny, Rain, Cloudy, 
   * Cold, Partly Cloudy. 
   * 
   */
  def predictOutlook(predictedRH: Int, predicteCloudCover: Int, predictedSunShine: Double, predictedAverageTemp: Double) = {
    //Rain. Quando c'è alta umidità, alta copertura nuvolosa e minor luce solare.
    if (predictedRH > 80 && predicteCloudCover > 6 && predictedSunShine < 3) "Rain" 
    else if (predictedSunShine > 5) "Sunny" //Quando la luce solare c'è per più di 5 ore .
    else if (predicteCloudCover > 6) "Cloudy" //Quando c'è un'alta copertura nuvolosa.
    else if (predictedAverageTemp < 15) "Cold" //Se la temperatura media è al di sotto dei 15°.
    else "Partly Cloudy" 
  }

  /*
   * Core function to execute predictions for various measurements for each of the city.
   * 1. Legge i dati in input ed estrae i dati che servono
   * 2. Deterimina Temperatura, Pressione, Umidità relativa, Copertura nuvolosa e luce solare.
   * 3. Dopo si deterimina il meteo.
   * 4. Una volta calcolato il tutto il risultato viene ritornato. 
   */
  
  def performPrediction(cityToCodeMapping: Map[String, CommonData.codes],sc: SparkContext) =
    for {
      city <- cityToCodeMapping.keys.toArray
      val bomObservations = FileOperations.getBomObservations(cityToCodeMapping(city).bomCode,sc)
      val tPlus1Date = "2020-04-13T10:00:00Z"  

      val temp = bomObservations.map(x => (x.max_temp + x.min_temp) / 2).collect.toArray
      val press = bomObservations.map(x => x.pressure).collect.toArray
      val hum = bomObservations.map(x => x.relative_humidity.toDouble).collect.toArray

      val predictedAverageTemp = PredictFunctions.executeARIMAForecast(temp)
      val predictedPressure = PredictFunctions.executeARIMAForecast(press)
      val predictedRH = PredictFunctions.executeARIMAForecast(hum).round.toInt
      val predictedCloudCover = bomObservations.map(x => x.cloud_amount).max
      val predictedSunShine = ((bomObservations.map(x => x.sunshine).sum) / bomObservations.count()).round.toInt

      val outlook = PredictFunctions.predictOutlook(predictedRH, predictedCloudCover, predictedSunShine, predictedAverageTemp)

      val geoCordinates = FileOperations.getLatLong(city)
    } yield Array(cityToCodeMapping(city).IATACode, geoCordinates, tPlus1Date, outlook, "%.2f".format(predictedAverageTemp), "%.2f".format(predictedPressure), predictedRH).mkString(CommonData.defaultWriteSep)
}