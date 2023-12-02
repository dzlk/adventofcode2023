package day2

import utils.Resource
import kotlin.math.max

class Day2

//data class Options ()

//val regex = """Game (?<game>\d+): (?<set>(<?cube>\d+ (?<color>red|blue|green)(, )?)+;?)+""".toRegex()
val gameRe = """Game (?<game>\d+)""".toRegex()
val cubeRe = """(<?id>\d+) (?<color>(red|blue|green))""".toRegex()

fun main() {
    val file =  Resource.getFile("day2/input")?: return

    var sumPossibleGames = 0
    var sumNeededCubes = 0

    file.forEachLine {
        println(it)

        val gameParts = it.split(": ");
        assert(gameParts.size == 2)

        val gameMatch = gameRe.find(gameParts[0])!!
        val game = Game(
            id = gameMatch.groups["game"]?.value?.toInt() ?: throw Exception("Invalid game")
        )

        val maxOfCubes = mutableMapOf(
            CubeColor.RED to 1,
            CubeColor.GREEN to 1,
            CubeColor.BLUE to 1,
        )

        val sets = gameParts[1].split("; ");
        for (set in sets) {
            val cubes = set.split(", ");

            for (cubeStr in cubes) {
                val cube = Cube.from(cubeStr)
//                println(cube)

                if (!cube.isValid()) {
                    println("Game ${game.id} is invalid ($cube)")
                    game.invalid()
                }

                maxOfCubes[cube.color] = max(maxOfCubes[cube.color] ?: 1, cube.id)
            }
        }

        if (game.isValid()) {
            sumPossibleGames += game.id
        }

        sumNeededCubes += maxOfCubes.values.reduce {acc, i -> acc * i }
        println(maxOfCubes)
    }

    println("-".repeat(50))
    println("Sum of possible game id: $sumPossibleGames")
    println("Sum of needed cubes: $sumNeededCubes")
}

enum class CubeColor {
    RED,
    GREEN,
    BLUE,
}

data class Cube(val id: Int, val color: CubeColor)  {
    fun isValid(): Boolean {
        return when (color) {
            CubeColor.RED -> id in 1..12
            CubeColor.GREEN -> id in 1..13
            CubeColor.BLUE -> id in 1..14
        }
    }

    companion object {
        fun from(str: String): Cube {
            val parts = str.split(" ")
            assert(parts.size == 2)

            return Cube(
                id = parts[0].toInt(),
                color = when (parts[1]) {
                    "red" -> CubeColor.RED
                    "green" -> CubeColor.GREEN
                    "blue" -> CubeColor.BLUE
                    else -> throw Exception("Unknown color ${parts[1]}")
                }
            )
        }
    }
}

data class Game(val id: Int, private var valid: Boolean = true) {
    fun isValid() = valid

    fun invalid() { valid = false }
}
