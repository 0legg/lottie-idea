package net.olegg.bodylookin

import com.intellij.json.JsonFileType
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.vfs.VirtualFile

/**
 * Created by olegg on 2/15/17.
 */
class BodylookinPlugin : ApplicationComponent {

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