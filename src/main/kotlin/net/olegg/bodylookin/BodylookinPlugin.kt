package net.olegg.bodylookin

import com.intellij.openapi.components.ApplicationComponent

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