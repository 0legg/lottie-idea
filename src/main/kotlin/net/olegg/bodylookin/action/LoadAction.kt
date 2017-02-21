package net.olegg.bodylookin.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.wm.ToolWindowManager
import net.olegg.bodylookin.Constants
import net.olegg.bodylookin.toolwindow.BodylookinView

/**
 * Created by olegg on 2/21/17.
 */
class LoadAction: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow(Constants.TOOL_WINDOW_ID)
        val json = editor?.document?.text
        if (toolWindow.isVisible) {
            val content = toolWindow.contentManager.getContent(0)?.component as? BodylookinView ?: return
            if (json != null) {
                content.loadJson(json)
            }
        } else {
            toolWindow.show {
                val content = toolWindow.contentManager.getContent(0)?.component as? BodylookinView ?: return@show
                if (json != null) {
                    content.loadJson(json)
                }
            }
        }
    }
}
