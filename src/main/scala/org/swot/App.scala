
package org.swot

import swing._
import javax.swing.UIManager 
import org.utils._


object App extends SimpleSwingApplication {
  UIManager.setLookAndFeel(
  //  UIManager.getCrossPlatformLookAndFeelClassName()
    UIManager.getSystemLookAndFeelClassName()
  )

  I18n.loadlanguage("/en.json");

  val top = new MainWindow
//  MainLine.gui = new Gui(top);
//  top.gui = MainLine.gui;
//  MainLine.model = new Model
 // MainLine.systemInterface = new SystemInterface
 // MainLine.start()
//  MainLine.model.start()
 // MainLine.gui.start()
//  MainLine.systemInterface.start()
 // Settings.save();

}

