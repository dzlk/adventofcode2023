package day3

import utils.Resource

class Day3

fun main() {
    val file = Resource.getFile("day3/input") ?: return

    val numbers = mutableListOf<List<Pair<String, Int>>>()
    val symbols = mutableListOf<Set<Int>>()
    val gears = mutableListOf<Set<Int>>()

    numbers.add(emptyList())
    symbols.add(emptySet())
    gears.add(emptySet())

    file.forEachLine {
        val lineNumbers = mutableListOf<Pair<String, Int>>()
        val lineSymbols = mutableSetOf<Int>()
        val lineGears = mutableSetOf<Int>()

        var numParts: MutableList<Char> = mutableListOf()
        for ((i, c) in ("$it.").withIndex()) {
            if (c.isDigit()) {
                numParts.add(c)
                continue
            }

            if (numParts.size > 0) {
                val num = numParts.joinToString("")

                lineNumbers.add(num to i - numParts.size)
                numParts = mutableListOf()
            }

            if (c != '.') {
                lineSymbols.add(i)
            }

            if (c == '*') {
                lineGears.add(i)
            }
        }

        numbers.add(lineNumbers)
        symbols.add(lineSymbols)
        gears.add(lineGears)
    }

    numbers.add(emptyList())
    symbols.add(emptySet())
    gears.add(emptySet())

    println("Gears count: ${gears.sumOf { it.size }}")

    val engineParts = mutableListOf<String>()
    val gearsParts = mutableMapOf<String, MutableList<String>>()

    for ((currLine, lineNumbers) in numbers.withIndex()) {
        if (currLine == 0 || currLine == numbers.size) continue

        for ((num, index) in lineNumbers) {
//            println("$num, $index, ${index - 1..index + num.length}")

            var numOk = false
            var usedInGear = false
            for (i in index - 1..index + num.length) {
                if (
                    i in symbols[currLine - 1] ||
                    i in symbols[currLine] ||
                    i in symbols[currLine + 1]
                ) {
                    numOk = engineParts.add(num)
                }



                if (!usedInGear) {
                    for (diff in -1..1) {
                        val line = currLine + diff
                        if (i in gears[line]) {
                            val key = "$line:$i"
                            gearsParts.putIfAbsent(key, mutableListOf())
                            gearsParts[key]!!.add(num)

                            usedInGear = true
                        }
                    }
                }
            }
            if (!numOk) {
                println("$currLine: $num -- bad")
            }
        }
    }

    println(gearsParts)

    val sumOfParts = engineParts.sumOf { it.toInt() }
    println("Sum of normal parts: $sumOfParts")


    val sumOfGears = gearsParts.values
        .filter { it.size == 2 }
        .sumOf { it[0].toInt() * it[1].toInt() }
    println("Sum of gears parts: $sumOfGears")
}
