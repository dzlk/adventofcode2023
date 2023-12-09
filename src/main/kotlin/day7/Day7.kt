package day7

import utils.Resource


val jIsJoker = true
fun main() {
    val file = Resource.getFile("day7/input") ?: return

    val hands = mutableListOf<Hand>()
    file.forEachLine {
        println(it)

        hands.add(Hand.from(it))
    }

    hands.sortBy { it.rank }
    var sum = 0;
    for ((i, hand) in hands.withIndex()) {
        sum += (i + 1) * hand.bid

        println("$hand, ${i + 1} * ${hand.bid}")
    }

    println("Sum of hands: ${sum}")
}

enum class Combination {
    HIGH,
    PAIR,
    TWO_PAIRS,
    THREE,
    FULL_HOUSE,
    FOUR,
    FIVE,
}

var ranks = mapOf(
    '2' to "b",
    '3' to "c",
    '4' to "d",
    '5' to "e",
    '6' to "f",
    '7' to "g",
    '8' to "h",
    '9' to "i",
    'T' to "j",
    'J' to if (!jIsJoker) "k" else "a",
    'Q' to "l",
    'K' to "m",
    'A' to "n"
)

data class Hand(
    val value: String, val bid: Int,
    val rank: String
) {
    companion object {
        fun from(str: String): Hand {
            val parts = str.split(" ")
            require(parts.size == 2)

            val hand = mutableMapOf<Char, Int>()
            var handToRank = ""
            for (c in parts[0]) {
                hand[c] = hand.getOrDefault(c, 0) + 1

                handToRank += ranks[c]
            }

            val combination = combination(hand)

            val rank = "${combination.ordinal}-${handToRank}"
            println("Hand: $hand; Combination: $combination; Rank: $rank")

            return Hand(
                value = parts[0],
                bid = parts[1].toInt(),
                rank = rank
            )
        }

        private fun combination(hand: MutableMap<Char, Int>): Combination {
            val maxCount = hand.values.max()

            val combination = when (true) {
                (hand.size == 1) -> Combination.FIVE
                (hand.size == 2 && maxCount == 4) -> Combination.FOUR
                (hand.size == 2 && maxCount == 3) -> Combination.FULL_HOUSE
                (hand.size == 3 && maxCount == 3) -> Combination.THREE
                (hand.size == 3 && maxCount == 2) -> Combination.TWO_PAIRS
                (hand.size == 4) -> Combination.PAIR

                else -> Combination.HIGH
            }

            val countOfJ = hand.getOrDefault('J', 0)
            if (!jIsJoker || countOfJ == 0) {
                return combination
            }

            println("Count of Jokers: $countOfJ")

            return when(combination) {
                Combination.FIVE -> Combination.FIVE
                Combination.FOUR -> Combination.FIVE
                Combination.FULL_HOUSE -> Combination.FIVE
                Combination.THREE -> Combination.FOUR
                Combination.TWO_PAIRS -> if (countOfJ == 1) Combination.FULL_HOUSE else Combination.FOUR
                Combination.PAIR -> Combination.THREE
                Combination.HIGH -> Combination.PAIR
            }
        }
    }
}

