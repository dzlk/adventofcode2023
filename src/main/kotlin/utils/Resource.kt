package utils

import day1.Day1
import java.io.File

class Resource {
    companion object {
        fun getFile(name: String): File? {
            val resource = Day1::class.java.classLoader.getResource(name) ?: return null

            return File(resource.toURI())
        }
    }
}

