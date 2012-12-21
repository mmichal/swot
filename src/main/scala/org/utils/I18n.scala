
package org.utils
import scala.util.parsing.json.JSON

class I18nException extends Exception {}

object I18n {
  var dictionary : Map[String, String] = null

  def loadlanguage(path: String) = {
    val url = getClass.getResource(path).toString
    val file = scala.io.Source.fromURL(url)
    val lines = file.mkString
    file.close()
    
    val result = JSON.parseFull(lines)
 
    result match {
      case Some(e) => {
        dictionary = e.asInstanceOf[Map[String, String]]
      } 
      case None => throw  new I18nException
    }
  }

  def apply(key: String): String = {
    dictionary(key)
  }

}
