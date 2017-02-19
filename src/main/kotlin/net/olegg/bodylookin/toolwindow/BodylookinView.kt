package net.olegg.bodylookin.toolwindow

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBPanel
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import net.olegg.bodylookin.Constants
import java.awt.BorderLayout

/**
 * Created by olegg on 2/12/17.
 */
class BodylookinView : SimpleToolWindowPanel(true) {
    val root: String = javaClass.classLoader.getResource("/bodylookin.html").toExternalForm()

    val panel = JFXPanel()
    lateinit var webview: WebView
    lateinit var engine: WebEngine

    init {
        val manager = ActionManager.getInstance()
        val group = manager.getAction(Constants.ACTION_GROUP) as? ActionGroup
        if (group != null) {
            setToolbar(manager.createActionToolbar(ActionPlaces.TOOLWINDOW_TITLE, group, true).component)
        }
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

    fun loadAnimation(source: String) {
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
                engine.loadWorker.stateProperty().addListener { value, oldState, newState ->
                    if (newState == Worker.State.SUCCEEDED) {
                        engine.executeScript(script)
                    }
                }
            } else {
                engine.executeScript(script)
            }
        }
    }
}