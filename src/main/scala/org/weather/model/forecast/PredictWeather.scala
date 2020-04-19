package org.weather.model.forecast

import org.weather.model.data.{ FileOperations }
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author Paolo Santilli
 * @written 26 Marzo 2020
 * @description:
 * 	 		Punto di ingresso per l'applicazione delle previsioni del tempo, raccoglie le città in cui deve essere effettuata la previsione,
 *      Quindi gli utenti dell'analisi delle serie storiche ARIMA, per prevedere le varie condizioni meteorologiche come la temperatura
 *      media (° C), umidità relativa (%) e pressione (hPa).
 *      La previsione è effettuata considerando la massima copertura nuvolosa nel periodo di previsione, le ore di sole medie e i 3 parametri predetti sopra menzionati.
 *
 */

object PredictWeather extends App {

  
  val typeComputation = args(0)
  

  typeComputation match{
    //Run in locale
    case "local" => {
      FileOperations.setPath("local")
      val session = SparkSession
            .builder
            .appName("PredictWeather")
            .config("spark.master", "local")
            .enableHiveSupport()
            .getOrCreate()
      import session.implicits._
      val sc = session.sparkContext

      val hadoopConfig: Configuration = sc.hadoopConfiguration 
      hadoopConfig.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName) 
      hadoopConfig.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
      val cityToCodeMapping = FileOperations.getMappingData
      val finalPredictions = PredictFunctions.performPrediction(cityToCodeMapping,sc)
      val resultRDD = sc.parallelize(finalPredictions)
      resultRDD.saveAsTextFile(FileOperations.outPath)
      sc.stop()
    }
    // Run sul cloud
    case "cloud" => {
      FileOperations.setPath("cloud")
      val session = SparkSession
          .builder
          .appName("PredictWeather")
          .config("spark.master", "local")
          .enableHiveSupport()
          .getOrCreate()
      import session.implicits._
      val sc = session.sparkContext

      val hadoopConfig: Configuration = sc.hadoopConfiguration 
      hadoopConfig.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName) 
      hadoopConfig.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
      val cityToCodeMapping = FileOperations.getMappingData
      val finalPredictions = PredictFunctions.performPrediction(cityToCodeMapping,sc)
      val resultRDD = sc.parallelize(finalPredictions);
      resultRDD.saveAsTextFile(FileOperations.outPath)
      sc.stop()
    }

    case _ : String => {}
  }


}