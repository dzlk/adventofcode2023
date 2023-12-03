package day0

import utils.Resource

class Day0

fun main() {
    val file =  Resource.getFile("day0/input")?: return

    file.forEachLine {
        println(it)
    }
}
