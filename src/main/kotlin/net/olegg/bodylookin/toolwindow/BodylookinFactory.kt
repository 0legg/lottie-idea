package net.olegg.bodylookin.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

/**
 * Created by olegg on 2/12/17.
 */
class BodylookinFactory: ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val view = BodylookinView()
        val content = ContentFactory.SERVICE.getInstance().createContent(view, null, false)
        toolWindow.contentManager.addContent(content)
        //val json = javaClass.classLoader.getResource("/data.json").readText()
    }
}