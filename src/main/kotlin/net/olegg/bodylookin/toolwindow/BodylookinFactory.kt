package net.olegg.bodylookin.toolwindow

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.UIUtil
import net.olegg.bodylookin.BodylookinPlugin
import javax.swing.JComponent

/**
 * Tool window provider. Shows notification if user doesn't have JavaFX.
 */
class BodylookinFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val view: JComponent = if (ApplicationManager.getApplication().getComponent(BodylookinPlugin::class.java).hasJavafx) {
            BodylookinView()
        } else {
            JBLabel("""<html>Unable to instantiate JavaFX.
            <br/><br/>
            Please launch IDE using JDK with JavaFX bundled.</html>""", UIUtil.ComponentStyle.LARGE)
        }

        val content = ContentFactory.SERVICE.getInstance().createContent(view, null, false)
        toolWindow.contentManager.addContent(content)
    }
}