package day4

import utils.Resource

class Day4

fun main() {
    val file =  Resource.getFile("day4/input")?: return

    var cardCount = 0;
    var sumOfPoints = 0;
    val copies = mutableMapOf<Int, Int>()

    file.forEachLine {
        cardCount++
        val card = Card.from(it)
        println("Card ${card.id}")

        val winCount = (card.winners intersect card.numbers).size

        copies[card.id] = copies.getOrDefault(card.id, 0) + 1


        var points = 0;
        for (x in 1 .. winCount) {
            points = if (points == 0) 1 else 2 * points

            val copiesCount = copies.getOrDefault(card.id, 0)
            val copyId = card.id + x;
            copies[copyId] = copies.getOrDefault(copyId, 0) + copiesCount
        }

        println("Intersects: ${card.winners intersect card.numbers}, points: $points")
        println("Copies: $copies")

        sumOfPoints += points


    }

    println("Sum of points: $sumOfPoints")

    val sumOfCopies = copies.values.take(cardCount).sum()
    println("Sum of copies: $sumOfCopies")
}

data class Card(
    val id: Int,
    val winners: Set<Int>,
    val numbers: Set<Int>
) {
    companion object {
        fun from(str: String): Card {
            var parts = str.split(": ")
            require(parts.size == 2)


            val cardParts = parts[0].split(" ")

            parts = parts[1].split(" | ")
            require(parts.size == 2)

            return Card(
                id = cardParts.last().toInt(),
                winners = parseNumbers(parts[0]),
                numbers = parseNumbers(parts[1])
                )
        }

        private fun parseNumbers(str: String): Set<Int> {
            return str.split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .toSet()
        }
    }
}