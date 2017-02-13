package net.olegg.bodylookin.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.wm.ToolWindowManager
import net.olegg.bodylookin.Constants
import net.olegg.bodylookin.toolwindow.BodylookinView


/**
 * Created by olegg on 2/12/17.
 */
class LookAction: AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow(Constants.TOOL_WINDOW_ID)
        if (toolWindow.isVisible) {
            if (editor != null) {
                val content = toolWindow.contentManager.getContent(0)?.component as? BodylookinView ?: return
                content.loadAnimation(editor.document.text)
            }
        } else {
            toolWindow.show {
                if (editor != null) {
                    val content = toolWindow.contentManager.getContent(0)?.component as? BodylookinView ?: return@show
                    content.loadAnimation(editor.document.text)
                }
            }
        }
    }
}