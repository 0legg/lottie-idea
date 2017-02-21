package net.olegg.bodylookin

import com.intellij.openapi.util.IconLoader

/**
 * Created by olegg on 2/19/17.
 */
object Icons {
    fun load(path: String) = IconLoader.getIcon(path)

    val LOAD = load("/icons/wiggle.png")
    val OPEN = load("/icons/in.png")
    val PLAY = load("/icons/play.png")
    val PAUSE = load("/icons/pause.png")
    val LOOP = load("/icons/repeat.png")
}