package org.crosswire.jsword.javautil

//TODO temporarily ignoring resources and translations
class ResourceBundle {
    fun getString(key: String): String {
        return key.replace(".Full", "")
    }
}