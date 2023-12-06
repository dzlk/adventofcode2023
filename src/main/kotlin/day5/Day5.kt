package day5

import utils.Resource

class Day5

enum class Mode {
    Wait,

    FindValues,

    FindHeader,
    FindMapping
}

typealias Values = MutableList<LongRange>
typealias Mappings = MutableMap<LongRange, Long>

fun main() {
    val file = Resource.getFile("day5/input") ?: return


    var values: Values = mutableListOf()
    var mappings: Mappings = mutableMapOf()

    var mode = Mode.FindValues

    file.forEachLine {
        if (mode == Mode.FindValues) {
            values = parseValues2(it).toMutableList()
            println("Values: $values")

            mode = Mode.Wait
            return@forEachLine
        }

        if (it == "") {
            if (mode == Mode.FindMapping) {
                values = mapValues(values, mappings)
            }

            println()

            mode = Mode.FindHeader
            return@forEachLine
        }

        if (mode == Mode.FindHeader) {
            println(it)

            mappings = mutableMapOf()

            mode = Mode.FindMapping
            return@forEachLine
        }

        if (mode != Mode.FindMapping) {
            println("Skipped: '$it'")
            return@forEachLine
        }

        // Mode Mappings

        val mapping = parseMapping(it)
        mappings[mapping.first] = mapping.second

        println("Mappings: $it, parsed: $mapping")
    }

    values = mapValues(values, mappings)
    println("Mapped values: $values")

    println()
    println("Minimum value: ${values.minOf { it.first }}")
}


// for first star
fun parseValues(str: String): List<LongRange> {
    return str
        .split(": ")[1].split(" ")
            .map { it.toLong()..1 }
}

// for second star
fun parseValues2(str: String): List<LongRange> {
    return str
        .split(": ")[1].split(" ").map { it.toLong() }
        .chunked(2).map { it[0] until it[0] + it[1] }
}

fun parseMapping(str: String): Pair<LongRange, Long> {
    val parts = str.split(" ").map { it.toLong() }
    require(parts.size == 3)

    return parts[1] until parts[1] + parts[2] to parts[0]
}

fun mapValues(values: Values, mappings: Mappings): Values {
    val sortedMappingRanges = mappings.toList().sortedBy { it.first.first }
    println("Sorted mapping ranges: $sortedMappingRanges")


    val newValues: Values = mutableListOf()
    for (value in values) {
        newValues.addAll(mapValue(value, sortedMappingRanges))
    }

    println("Mapped values: $values --> $newValues")
    return newValues
}

fun mapValue(value: LongRange, mappings: List<Pair<LongRange, Long>>): Values {
    val newValues: Values = mutableListOf()

    var start = value.first
    val end = value.last

    for ((range, newValue) in mappings) {
        // f+++l***s---e
        if (start > range.last) {
            continue
        }

        // [s---e]***f+++l
        if (end < range.first) {
            newValues.add(start .. end)
            start = end + 1
            break
        }

        // **[s--]f~~e++l**
        if (start < range.first) {
            newValues.add(start until range.first)
            start = range.first
        }

        // **s--f?[s~~e]++l**
        // f -> new
        if (end <= range.last) {
            val diffStart = start - range.first
            val diffEnd = end - range.first
            newValues.add(newValue + diffStart .. newValue + diffEnd)
            start = end + 1
            break
        }

        // **s--[f+++l]--e**
        val diffStart = start - range.first
        val diffEnd = range.last - range.first
        newValues.add(newValue + diffStart .. newValue + diffEnd)
        start = range.last + 1
    }

    if (start <= end) {
        newValues.add(start .. end)
    }

    return newValues
}