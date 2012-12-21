
package org.swot

import javax.swing.table._
import org.utils._

class PropertiesTableModel extends AbstractTableModel {

  private var _data : Array[Property]= Array()

  def data_=(data: Array[Property]) {
    _data = data
    fireTableDataChanged()
  }
  
  def data : Array[Property] = {
    this._data
  }

  def clear {
    _data = Array()
    fireTableDataChanged
  }

  override def getColumnName( column: Int) : String = 
    column match {
      case 0 => I18n("name")
      case 1 => I18n("weight")
    }
 
  def getRowCount() = _data.length

  def getColumnCount() = 2
  
  def getValueAt( row: Int, col: Int): AnyRef = {
    col match {
      case 0 => _data(row).name.asInstanceOf[AnyRef]
      case 1 => _data(row).weight.asInstanceOf[AnyRef]
    }
  } 

  override def isCellEditable( row: Int, column: Int) = true
  
  override def setValueAt( value: Any, row: Int, col: Int) {
    col match {
      case 0 => _data(row).name = value.asInstanceOf[String]
      case 1 => _data(row).weight =  
        try { 
          value.asInstanceOf[String].toDouble
        } catch {
          case _ => 1.0
        }
    }

    fireTableDataChanged()
  }   
  
  def removeRows(begin : Int, end : Int) {
    _data = _data diff _data.slice(begin, end + 1);
    fireTableRowsDeleted(begin, end)
  }

  def addRows( data: Array[Property]) {
   _data ++= data.asInstanceOf[Array[Property]]
   fireTableRowsInserted(_data.length - data.length, _data.length - 1)
  }
  
}
