package day1

import utils.Resource

class Day1

data class Options (val replaceLettersNums: Boolean)

fun main() {
    val file =  Resource.getFile("day1/input")?: return

    var sum = 0
    val options = Options(replaceLettersNums = true)

    file.forEachLine {
        val line = prepareLine(it, options)
        val num = extractCalibrationValue(line)

        println("$it --> $line --> $num")

        sum += num
    }

    println(sum)
}

val digitsMapping = mapOf(
    "one" to "o1e",
    "two" to "t2o",
    "three" to "t3e",
    "four" to "f4r",
    "five" to "f5e",
    "six" to "s6x",
    "seven" to "s7n",
    "eight" to "e8t",
    "nine" to "n9e",
)
fun prepareLine(line: String, options: Options): String {
    if (!options.replaceLettersNums) {
        return line
    }

    var newLine = line

    for ((key, value) in digitsMapping.entries) {
        newLine = newLine.replace(key, value)
    }

    return newLine
}

fun extractCalibrationValue(line: String): Int {
    val digits = mutableListOf<Int>()
    for (char in line) {
        if (char in '1'..'9') {
            digits.add(char.toString().toInt())
        }
    }

    return digits.first() * 10 + digits.last()
}
