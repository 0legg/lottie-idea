package net.olegg.lottie.idea.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.wm.ToolWindowManager
import net.olegg.lottie.idea.Constants
import net.olegg.lottie.idea.isJson
import net.olegg.lottie.idea.toolwindow.LottieIdeaView

/**
 * Animation loading logic.
 */
class LoadAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        if (project == null || project.isDefault || project.isDisposed) {
            return
        }

        val doc = event.getData(CommonDataKeys.EDITOR)?.document
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val toolWindow = toolWindowManager.getToolWindow(Constants.TOOL_WINDOW_ID)
        if (toolWindow.isVisible) {
            val content = toolWindow.contentManager.getContent(0)?.component as? LottieIdeaView ?: return
            if (doc != null) {
                content.loadJson(doc.text)
            } else if (file != null) {
                content.loadFile(file)
            }
        } else {
            toolWindow.show {
                val content = toolWindow.contentManager.getContent(0)?.component as? LottieIdeaView ?: return@show
                if (doc != null) {
                    content.loadJson(doc.text)
                } else if (file != null) {
                    content.loadFile(file)
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        if (project == null || project.isDefault || project.isDisposed) {
            e.presentation.isEnabledAndVisible = false
            return
        }
        val doc = e.getData(CommonDataKeys.EDITOR)?.document
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        val docJson = doc != null && doc.textLength > 0 && FileDocumentManager.getInstance().getFile(doc).isJson
        e.presentation.isEnabledAndVisible = file.isJson || docJson
    }
}
