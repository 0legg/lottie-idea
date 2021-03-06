package net.olegg.lottie.idea

import com.intellij.json.JsonFileType
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.vfs.VirtualFile

/**
 * Root plugin class.
 */
class LottieIdeaPlugin : ApplicationComponent {

    override fun getComponentName() = javaClass.name

    val hasJavafx: Boolean by lazy {
        try {
            Class.forName("javafx.application.Platform")
            return@lazy true
        } catch (e: Exception) {
            return@lazy false
        }
    }

    override fun initComponent() { }

    override fun disposeComponent() { }
}

val VirtualFile?.isJson: Boolean
    get() = (this?.fileType == JsonFileType.INSTANCE)