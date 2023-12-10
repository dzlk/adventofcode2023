package day9

import utils.Resource

fun main() {
    val file =  Resource.getFile("day9/input")?: return

    var sum = 0

    file.forEachLine {
        println(it)

        val nums = it.split(" ").map { it.toInt() }
        val value = extrapolate(nums)

        println("--------------")
        println("$value for $it")
        println("--------------")

        sum += value
    }

    println("Sum of value: $sum")
}

fun extrapolate(nums: List<Int>): Int {
    val lasts = mutableListOf<Int>()
    val values = nums.reversed().toMutableList()

    var allZeros = false
    while (!allZeros) {
        allZeros = true

        for (i in 0 .. values.size - 2 ) {
            values[i] = values[i + 1] - values[i]
            if (values[i] != 0) {
                allZeros = false
            }
        }

        lasts.add(values.removeLast())

        println("values: $values")
    }

    println("lasts: $lasts")

    var value = 0
    for (v in lasts.reversed()) {
        value += v
    }

    return value
}