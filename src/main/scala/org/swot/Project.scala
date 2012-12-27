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
  
    def transform (data: DataRow) : Property = {
      new Property(data.name, data.weight)
    }
    
    try { 
      val lines = source.mkString
      source.close ()
      val fileData = Serialization.read[ProjectFileFormat](lines)
      strengthsModel.data = fileData.strengths.map(transform)
      weaknessesModel.data = fileData.weaknesses.map(transform)
      oportunitiesModel.data = fileData.oportunities.map(transform)
      threatsModel.data = fileData.threats.map(transform)

      for (strengthIndex <- List.range(0, fileData.so.length)) {
        for (oportunityIndex <- List.range(0, fileData.so(strengthIndex).length)) {
          strengthsModel.data(strengthIndex).connections =
            strengthsModel.data(strengthIndex).connections ++ 
            Array((
              fileData.so(strengthIndex)(oportunityIndex),
              oportunitiesModel.data(oportunityIndex)
            ))
        }
      }
      
      for (strengthIndex <- List.range(0, fileData.so.length)) {
        for (threatIndex <- List.range(0, fileData.st(strengthIndex).length)) {
          strengthsModel.data(strengthIndex).connections =
            strengthsModel.data(strengthIndex).connections ++ 
            Array((
              fileData.st(strengthIndex)(threatIndex),
              threatsModel.data(threatIndex)
            ))
        }
      }
      
      for (weaknessIndex <- List.range(0, fileData.wo.length)) {
        for (oportunityIndex <- List.range(0, fileData.so(weaknessIndex).length)) {
          weaknessesModel.data(weaknessIndex).connections =
            weaknessesModel.data(weaknessIndex).connections ++ 
            Array((
              fileData.wo(weaknessIndex)(oportunityIndex),
              oportunitiesModel.data(oportunityIndex)
            ))
        }
      }
 
      for (weaknessIndex <- List.range(0, fileData.wt.length)) {
        for (threatIndex <- List.range(0, fileData.wt(weaknessIndex).length)) {
          weaknessesModel.data(weaknessIndex).connections =
            weaknessesModel.data(weaknessIndex).connections ++ 
            Array((
              fileData.wt(weaknessIndex)(threatIndex),
              weaknessesModel.data(threatIndex)
            ))
        }
      }
      
    } catch {
      case _ => throw new ProjectException("Wrong file format!")
    }
   
//    this.connectionModel.so = fileData.so
//    this.connectionModel.wo = fileData.wo
//    this.connectionModel.st = fileData.st
//    this.connectionModel.wt = fileData.wt
  }

  def save() {
    if (filename != null) {
      val output = new java.io.FileWriter(filename)
      
      def transform (data: Property) : DataRow = {
        new DataRow(data.name, data.weight)
      }
   
      try {
        var so : Array[Array[Double]] = Array()
        var st : Array[Array[Double]] = Array()
        var wo : Array[Array[Double]] = Array()
        var wt : Array[Array[Double]] = Array()
        
        for (strengthIndex <- List.range(0, strengthsModel.data.length)) {
          var current = strengthsModel.data(strengthIndex)
          for (connectionIndex <- List.range(0, current.connections.length)) {
            for(oportunityIndex <- List.range(0, oportunitiesModel.data.length)) {
              if (current.connections(connectionIndex)._2 == oportunitiesModel.data(oportunityIndex)) {
                so(strengthIndex)(oportunityIndex) = current.connections(connectionIndex)._1
              }
            }
            for(threatIndex <- List.range(0, threatsModel.data.length)) {
              if (current.connections(connectionIndex)._2 == threatsModel.data(threatIndex)) {
                st(strengthIndex)(threatIndex) = current.connections(connectionIndex)._1
              }
            }
          }
        }

        for (weaknessIndex <- List.range(0, weaknessesModel.data.length)) {
          var current = weaknessesModel.data(weaknessIndex)
          for (connectionIndex <- List.range(0, current.connections.length)) {
            for(oportunityIndex <- List.range(0, oportunitiesModel.data.length)) {
              if (current.connections(connectionIndex)._2 == oportunitiesModel.data(oportunityIndex)) {
                wo(weaknessIndex)(oportunityIndex) = current.connections(connectionIndex)._1
              }
            }
            for(threatIndex <- List.range(0, threatsModel.data.length)) {
              if (current.connections(connectionIndex)._2 == threatsModel.data(threatIndex)) {
                wt(weaknessIndex)(threatIndex) = current.connections(connectionIndex)._1
              }
            }
          }
        }


        val fileData = new ProjectFileFormat (
          strengthsModel.data.map(transform),
          weaknessesModel.data.map(transform),
          oportunitiesModel.data.map(transform),
          threatsModel.data.map(transform),
          so,
          st,
          wo,
          wt
        )

        output.write(Serialization.write[ProjectFileFormat](fileData))
        output.close()
      } catch {
        case _ => throw new ProjectException("Wrong file format!")
      }

    } else {
      throw new ProjectException("No filename");
    }
  }

  def saveAs(filename: String) {
    this.filename = filename
    save
  }
}
