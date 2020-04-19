package org.weather.model.data

import java.io.{File, FileNotFoundException, PrintWriter}
import org.apache.spark.SparkContext
import scala.io.Source
import org.apache.spark.rdd.RDD
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FSDataOutputStream, Path}

object FileOperations {

  println("File operations")

  var path = ""
  var outPath = ""
  def setPath(typePath: String) = {
      typePath match {
        case "local" => {
          outPath = "C:/Users/paolo.santilli2/Desktop/Unibo/Scalable and Cloud Programming/weather_forecast/target/scala-2.10/output/"
          path = "C:/Users/paolo.santilli2/Desktop/Unibo/Scalable and Cloud Programming/weather_forecast/target/scala-2.10/data/"
        }
        case "cloud" => {
          path = "s3://aws-logs-045095359648-us-east-1/data/"
          outPath = "s3://aws-logs-045095359648-us-east-1/output/"
          
        }
        case _ : String => {}
      }
  }

  private val dataPattern = "^,[0-9]{4}-[0-9]{2}-[0-9]{1,2},".r

  def getBomObservations(implicit bomCode: String,sc:SparkContext): RDD[CommonData.fields] = {
    try{
      val new_record = sc.textFile(path + bomCode + ".csv")
      
      new_record.filter(r => dataPattern.findFirstIn(r).isDefined).map(r => {
      
      val fields = r.split(CommonData.bomFileSep)
    
      (new CommonData.fields(
          fields(1),
          if (fields(2) == "") 0.0D else fields(2).toDouble,
          if (fields(3) == "") 0.0D else fields(3).toDouble,
          if (fields(4) == "") 0.0D else fields(4).toDouble,
          if (fields(6) == "") 0.0D else fields(6).toDouble,
          if (fields(11) == "") 0 else fields(11).toInt,
          if (fields(12) == "") 4 else fields(12).toInt + 1,
          if (fields(15) == "") 0.0D else fields(15).toDouble))
      })
    } catch {
      case ex: FileNotFoundException => throw new FileNotFoundException(ex.getMessage 
          + ". Data source is not bom and files not found in the current folder")
    }
  }

def getMappingData(): Map[String, CommonData.codes] = {
    val mapping = for (
      line <- CommonData.cities.map(x => x.split(CommonData.defaultFieldSep))
    ) yield (line(0), CommonData.codes(line(1), line(2)))
    mapping.toMap
  }

  /*
   * Funzione che ritorna Latitudine, Longitudine e elevazione per ogni città.
   */
  def getLatLong(city: String) = {
    city match{
      case "SYDNEY" => Array(-33.8, 151.2, 92).mkString(",")
      case "WOLLONGONG" => Array(-34.4, 150.8, 463).mkString(",")
      case "CANBERRA" => Array(-35.2,149.1, 591).mkString(",")
      case "GOLD COAST" => Array(-28.0,153.4, 4).mkString(",")
      case "MELBOURNE" => Array(-37.8,144.9, 27).mkString(",")
      case "BRISBANE" => Array(-27.4,153.0, 49).mkString(",")
      case "CAIRNS" => Array(-16.9,145.7, 14).mkString(",")
      case "HOBART" => Array(-42.8,147.3, 215).mkString(",")
      case "DARWIN" => Array(-12.4,130.8, 31).mkString(",")
      case "ADELAIDE" => Array(-34.9,138.5, 65).mkString(",")
      case "PERTH" => Array(-31.9,115.8, 16).mkString(",")
    }
  } 

}