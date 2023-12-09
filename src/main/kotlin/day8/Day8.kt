package day8

import utils.Resource

fun main() {
    val isGhost = true
    val file = Resource.getFile("day8/input") ?: return

    var instruction: Instruction? = null
    val nodes = mutableMapOf<String, Node>()

    file.forEachLine {
        if (instruction == null) {
            instruction = Instruction(it)
            return@forEachLine
        }

        if (it == "") {
            return@forEachLine
        }

        val node = Node.from(it)
        println("$it -> $node")

        nodes[node.value] = node
    }

    println("Starting!")

    if (!isGhost) {
        var nodeValue = "AAA"
        var steps = 0

        while (nodeValue != "ZZZ") {
            val node = nodes[nodeValue]!!

            nodeValue = when(instruction!!.get()) {
                Direction.LEFT -> node.left
                Direction.RIGHT -> node.right
            }
            steps++
        }

        println("Steps: $steps")

        return
    }

    val nodeValues = nodes.values.filter { it.value.last() == 'A' }.map { it.value }.toMutableList()
    println(nodeValues)

    val loops = mutableListOf<Int>()
    for (currentNodeValue in nodeValues) {
        println("--------------------------------")
        println("CurrentNode: $currentNodeValue")
        var pair = (currentNodeValue to 0)
        val mapStates = mutableMapOf<Pair<String, Int>, Int>()
        val okStates = mutableListOf<Int>()

        var steps = 0
        instruction!!.reset()
        while (true) {
            steps++
            val node = nodes[pair.first]!!
            val nodeValue = when (instruction!!.get()) {
                Direction.LEFT -> node.left
                Direction.RIGHT -> node.right
            }

            pair = (nodeValue to instruction!!.index())

            if (nodeValue.last() == 'Z') {
                okStates.add(steps)
            }

            if (pair in mapStates) {
                println("Matched pair $pair loop: ${mapStates[pair]} .. $steps")
                loops.add(steps - mapStates[pair]!!)
                break
            }

            mapStates[pair] = steps;
        }


        println("Ok states: $okStates")

        println("Steps: $steps")
    }

    // LCM = 24035773251517
    // так как loop равен первой ок позиции
    println(loops)

}

enum class Direction {
    LEFT,
    RIGHT
}

data class Instruction(val value: String, private var index: Int = 0) {
    fun reset() {
        index = 0
    }
    fun get(): Direction {
        val char = value[index]
        index++
        if (index == value.length) {
            index = 0
        }

        return if (char == 'L') Direction.LEFT else Direction.RIGHT
    }

    fun index() = index
}

data class Node(val value: String, val left: String, val right: String) {
    companion object {
        private val re = """(?<value>\w{3}) = \((?<left>\w{3}), (?<right>\w{3})\)""".toRegex()
        fun from(value: String): Node {
            println(value)
            val match = re.find(value)!!

            println()

            return Node(
                value = match.groups["value"]!!.value,
                left = match.groups["left"]!!.value,
                right = match.groups["right"]!!.value
            )
        }
    }
}