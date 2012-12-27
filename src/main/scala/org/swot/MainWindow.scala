package org.swot

import org.utils._
import swing._
import java.awt.Dimension
import javax.swing.ListSelectionModel
import java.io.File

class MainWindow extends MainFrame {
  var changed = false
  
  minimumSize = new Dimension(800, 600)

  val newMenuItem = new MenuItem(I18n("new")) {
    action = new Action(I18n("new")) {
      def apply = { newProject }
    }
  }

  val openMenuItem = new MenuItem(I18n("open")) {
    action = new Action(I18n("open")) {
      def apply = { openProject }
    } 
  }

  val saveMenuItem = new MenuItem(I18n("save")) {
    action = new Action(I18n("save")) {
      def apply = { saveProject }
    }
  }

  val saveAsMenuItem = new MenuItem(I18n("save-as")) {
    action = new Action(I18n("save-as")) {
      def apply = { saveProjectAs }
    }
  }

  val quitMenuItem = new MenuItem(I18n("quit")) {
    action = new Action(I18n("quit")) {
      def apply = { 
        quit 
      }
    } 
  }

  val aboutMenuItem = new MenuItem(I18n("about")) {
    action = new Action(I18n("about")) {
      def apply = { about }
    }
  }

  val projectMenu = new Menu(I18n("project")) {
    contents += newMenuItem
    contents += new Separator()
    contents += openMenuItem
    contents += saveMenuItem
    contents += saveAsMenuItem
    contents += new Separator()
    contents += quitMenuItem
  }
  val helpMenu = new Menu(I18n("help")) {
    contents += aboutMenuItem
  }

  menuBar = new MenuBar() {
    contents += projectMenu
    contents += helpMenu
  }
  

  val connectionPane = new BorderPanel
  
  val summaryPane = new BorderPanel
  val mapPane = new BorderPanel


  val tabs = new TabbedPane() {
    pages += new TabbedPane.Page(
      I18n("strengths"), 
      makePropertyPane(Project.strengthsModel)
    )
    
    pages += new TabbedPane.Page(
      I18n("weaknesses"),
      makePropertyPane(Project.weaknessesModel)
    )
    
    pages += new TabbedPane.Page(
      I18n("oportunities"), 
      makePropertyPane(Project.oportunitiesModel)
    )

    pages += new TabbedPane.Page(
      I18n("threats"),
      makePropertyPane(Project.threatsModel)
    )
    pages += new TabbedPane.Page(I18n("connections"), connectionPane)
    pages += new TabbedPane.Page(I18n("map"), mapPane)
    pages += new TabbedPane.Page(I18n("summary"), summaryPane)

  }
  
  val panel = new BorderPanel() {
    add(tabs, BorderPanel.Position.Center)
  }

  contents = panel

  protected def makePropertyPane(tableModel: PropertiesTableModel) : Panel = {
    val table = new Table() {
      model = tableModel
      selection.intervalMode = Table.IntervalMode.SingleInterval
    }

    val pane = new BorderPanel {

      add(
        new BoxPanel(Orientation.Horizontal) {

          contents += new Button(
            new Action(I18n("add")) {
              def apply {
                tableModel.addRows(Array(new Property("", 1)))
              }
            }
          )

          contents +=new Button(
            new Action(I18n("delete")) {
              def apply {
                tableModel.removeRows(
                  table.selection.rows.head,
                  table.selection.rows.last
                )
      
              }
            }
          )

        },

        BorderPanel.Position.North
      )

      add(new ScrollPane(table), BorderPanel.Position.Center)
    }
    pane
  }

 /* def update = {
  }

  def createMap = {
  }

  def createReport = {
  } */

  def showError(title: String, message: String) {
    Dialog.showMessage(
      parent = null,
      title = title,
      message = message
    )
  }

  def newProject = {
    Project.clear
  }

  def openProject = {
    val chooser = new FileChooser(new File("."))
    chooser.title = I18n("open")
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve) {
      try {
        Project.open(chooser.selectedFile.getCanonicalPath)
      } catch {
        case e : ProjectException => showError(I18n("open"), I18n("file-format-error"))
      }

    }
  }

  def saveProject = {
    if (Project.filename != null) {
      Project.save
    } else { 
      saveProjectAs
    }
  }

  def saveProjectAs = {
    val chooser = new FileChooser(new File("."))
    chooser.title = I18n("save-as")
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve) {
      Project.saveAs(chooser.selectedFile.getCanonicalPath)
    }
 }

  def about = {
  }

  def quit = {
    close()
    App.quit
  }
}
