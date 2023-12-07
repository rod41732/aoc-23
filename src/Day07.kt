/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    fun List<Int>.handTypeStrength(): Int = when (this) {
        listOf(5) -> 6
        listOf(4, 1) -> 5
        listOf(3, 2) -> 4
        listOf(3, 1, 1) -> 3
        listOf(2, 2, 1) -> 2
        listOf(2, 1, 1, 1) -> 1
        listOf(1, 1, 1, 1, 1) -> 0
        else -> throw Exception("Unrecognized hand type")
    }

    // count card and sort descending which is suitable determining hand type
    fun String.cardCounts(): List<Int> {
        val counts = mutableMapOf<Char, Int>()
        this.forEach { counts[it] = (counts[it] ?: 0) + 1 }
        return counts.entries.map { (char, count) -> count }.sortedDescending().toList()
    }

    // count card and sort descending which is suitable determining hand type (with joker acting as best possible)
    fun String.cardCountsWithJoker(): List<Int> {
        val counts = mutableMapOf<Char, Int>()
        this.forEach { counts[it] = (counts[it] ?: 0) + 1 }

        val jokerCount = counts.remove('J') ?: 0
        val cardCounts = counts.entries.map { (char, count) -> count }.sortedDescending().toMutableList()
        // prevent index out of range from list being empty if hand is full of joker
        if (cardCounts.isEmpty()) cardCounts.add(0)

        // greedy: it's always best for joker to be the most frequent card
        cardCounts[0] += jokerCount
        return cardCounts
    }

    // convert card to character that correspond to its strength
    fun String.strengthWiseCards(): String {
        val replaceMapping = "AKQJT98765432".zip("abcdefghijklm".reversed())
        return replaceMapping.fold(this) { replacedSoFar, (from, to) -> replacedSoFar.replace(from, to) }
    }

    // convert card to character that correspond to its strength (joker case)
    fun String.strengthWiseCardsWithJoker(): String {
        val replaceMapping = "AKQT98765432J".zip("abcdefghijklm".reversed())
        return replaceMapping.fold(this) { replacedSoFar, (from, to) -> replacedSoFar.replace(from, to) }
    }

    data class Hand(val typeStrength: Int, val cardStrength: String);
    data class HandAndBid(val hand: Hand, val bid: Int)

    fun part1(input: List<String>): Int {
        val handSortedByRank = input
            .map {
                val (hand, bid) = it.split(" ")
                HandAndBid(
                    Hand(hand.cardCounts().handTypeStrength(), hand.strengthWiseCards()),
                    bid.toInt()
                )
            }.sortedWith { a, b ->
                a.hand.typeStrength.compareTo(b.hand.typeStrength).valueIfZero(
                    a.hand.cardStrength.compareTo(b.hand.cardStrength)
                )
            }

        return handSortedByRank.mapIndexed { index, handAndBid ->
//            println("${index + 1} * ${handAndBid.bid} (${handAndBid.hand})")
            (index + 1) * handAndBid.bid
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val handSortedByRank = input
            .map {
                val (hand, bid) = it.split(" ")
                HandAndBid(
                    Hand(hand.cardCountsWithJoker().handTypeStrength(), hand.strengthWiseCardsWithJoker()),
                    bid.toInt()
                )
            }.sortedWith { a, b ->
                a.hand.typeStrength.compareTo(b.hand.typeStrength).valueIfZero(
                    a.hand.cardStrength.compareTo(b.hand.cardStrength)
                )
            }

        return handSortedByRank.mapIndexed { index, handAndBid ->
//            println("${index + 1} * ${handAndBid.bid} (${handAndBid.hand})")
            (index + 1) * handAndBid.bid
        }.sum()

    }

    val testInput = readInput("Day07_test")
    assertEqual(part1(testInput), 6440)
    assertEqual(part2(testInput), 5905)

    val input = readInput("Day07")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

