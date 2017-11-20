package net.olegg.bodylookin.toolwindow

import com.intellij.json.JsonFileType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.JBUI
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import net.olegg.bodylookin.Icons
import net.olegg.bodylookin.isJson
import netscape.javascript.JSObject

/**
 * UI logic and JS player controller.
 */
class BodylookinView : SimpleToolWindowPanel(true) {
    val root: String = javaClass.classLoader.getResource("/bodylookin.html").toExternalForm()

    val panel = JFXPanel()
    lateinit var webview: WebView
    lateinit var engine: WebEngine
    var js: JSObject? = null

    val lookAction: AnAction = object : AnAction("Load from editor", "", Icons.LOAD) {
        override fun actionPerformed(e: AnActionEvent?) {
            val project = e?.project ?: return
            val json = FileEditorManager.getInstance(project).selectedTextEditor?.document?.text
            if (json != null) {
                loadJson(json)
            }
        }

        override fun update(e: AnActionEvent?) {
            e?.presentation?.isEnabled = let {
                val project = e?.project ?: return@let false
                val file = FileEditorManager.getInstance(project).selectedFiles.getOrNull(0)
                return@let file.isJson
            }
        }
    }

    val openAction: AnAction = object : AnAction("Load file", "", Icons.OPEN) {
        override fun actionPerformed(e: AnActionEvent?) {
            val descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(JsonFileType.INSTANCE)
            val file = FileChooserFactory.getInstance().createFileChooser(descriptor, null, null).choose(null).getOrNull(0)
            if (file != null) {
                loadFile(file)
            }
        }
    }

    val playAction: AnAction = object : AnAction("Play", "", Icons.PLAY) {
        override fun actionPerformed(e: AnActionEvent?) {
            Platform.runLater {
                js?.eval("""if (!this.loop && this.currentFrame >= this.totalFrames - 1) {
                    this.goToAndPlay(0, true);
                } else {
                    this.play();
                }""")
            }
        }

        override fun update(e: AnActionEvent?) {
            Platform.runLater {
                e?.presentation?.isEnabled = js?.eval("this.isLoaded && this.isPaused") as? Boolean ?: false
            }
        }
    }

    val pauseAction: AnAction = object : AnAction("Pause", "", Icons.PAUSE) {
        override fun actionPerformed(e: AnActionEvent?) {
            Platform.runLater {
                js?.eval("this.pause()")
            }
        }

        override fun update(e: AnActionEvent?) {
            Platform.runLater {
                e?.presentation?.isEnabled = js?.eval("this.isLoaded && !this.isPaused") as? Boolean ?: false
            }
        }
    }

    val loopAction: AnAction = object : ToggleAction("Toggle loop", "", Icons.LOOP) {
        var loop = true

        override fun isSelected(e: AnActionEvent?): Boolean {
            return loop
        }

        override fun setSelected(e: AnActionEvent?, state: Boolean) {
            Platform.runLater {
                loop = js?.eval("this.loop = $state") as? Boolean ?: state
            }
        }
    }

    init {
        val group = DefaultActionGroup()
        group.addAll(listOf(
                lookAction,
                openAction,
                Separator(),
                playAction,
                pauseAction,
                loopAction
        ))

        val toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_TITLE, group, true)
        setToolbar(JBUI.Panels.simplePanel(toolbar.component))
        setContent(panel)

        Platform.setImplicitExit(false)
        Platform.runLater {
            webview = WebView()
            engine = webview.engine
            engine.isJavaScriptEnabled = true
            engine.load(root)

            panel.scene = Scene(webview)
        }
    }

    fun loadJson(source: String) {
        Platform.runLater {
            val script = """
            bodymovin.destroy();
            bodymovin.loadAnimation({
                wrapper: document.getElementById('bodymovin'),
                animType: 'svg',
                loop: true,
                prerender: true,
                autoplay: true,
                animationData: JSON.parse('$source')
            });"""
            if (engine.loadWorker.state != Worker.State.SUCCEEDED) {
                engine.loadWorker.stateProperty().addListener { _, _, newState ->
                    if (newState == Worker.State.SUCCEEDED) {
                        js = engine.executeScript(script) as? JSObject
                    }
                }
            } else {
                js = engine.executeScript(script) as? JSObject
            }
        }
    }

    fun loadFile(file: VirtualFile) {
        ApplicationManager.getApplication().runReadAction {
            val json = LoadTextUtil.loadText(file).toString()
            loadJson(json)
        }
    }
}