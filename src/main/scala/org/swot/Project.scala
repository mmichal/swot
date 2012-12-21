package org.swot

import net.liftweb.json._

//import net.liftweb.json.Serialization.{read, write}
//implicit val formats = Serialization.formats(NoTypeHints)
/*val ser = write(Child("Mary", 5, None))
scala> read[Child](ser)
res1: Child = Child(Mary,5,None)import scala.util.parsing.json.JSON */

case class ProjectFileFormat(
  strengths: Array[DataRow], 
  weaknesses: Array[DataRow],
  oportunities: Array[DataRow],
  threats: Array[DataRow],
  so: Array[Array[Double]],
  st: Array[Array[Double]],
  wo: Array[Array[Double]],
  wt: Array[Array[Double]]
)

case class DataRow(name: String, weight : Double)
case class ProjectException(message: String) extends Exception
object Project {
  implicit val formats = net.liftweb.json.DefaultFormats
  var strengthsModel = new PropertiesTableModel
  var weaknessesModel = new PropertiesTableModel
  var oportunitiesModel = new PropertiesTableModel
  var threatsModel = new PropertiesTableModel

/*  var connectionModel = new ConnectionModel(
    strengthsModel,
    weaknessesModel,
    oportunitiesModel,
    threatsModel
  )
*/
  var filename : String = null;

  def clear() {
    //connectionModel.clear

    strengthsModel.clear
    weaknessesModel.clear
    oportunitiesModel.clear
    threatsModel.clear

    filename = null
  }

  def open(path: String) { 
    val source = io.Source.fromFile(path)
    try { 
      val lines = source.mkString
      source.close ()
      val fileData = Serialization.read[ProjectFileFormat](lines)
      strengthsModel.data = fileData.strengths.map(transform)
      weaknessesModel.data = fileData.weaknesses.map(transform)
      oportunitiesModel.data = fileData.oportunities.map(transform)
      threatsModel.data = fileData.threats.map(transform)
     
    } catch {
      case _ => throw new ProjectException("Wrong file format!")
    }
    
    def transform (data: DataRow) : Property = {
      new Property(data.name, data.weight)
    }
    
 
//    this.connectionModel.so = fileData.so
//    this.connectionModel.wo = fileData.wo
//    this.connectionModel.st = fileData.st
//    this.connectionModel.wt = fileData.wt
  }

  def save() {
    if (filename != null) {
    } else {
      throw new ProjectException("No filename");
    }
  }

  def saveAs(filename: String) {
    this.filename = filename
    save
  }
}
