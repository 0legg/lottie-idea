package net.olegg.bodylookin.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView

/**
 * Created by olegg on 2/12/17.
 */
class BodylookinFactory: ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JFXPanel()
        val content = ContentFactory.SERVICE.getInstance().createContent(panel, null, false)
        toolWindow.contentManager.addContent(content)
        Platform.runLater {
            val view = WebView()
            panel.scene = Scene(view)
            view.engine.load("https://google.com")
        }
    }
}