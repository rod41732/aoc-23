/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    fun replaceWordWithNumber(input: String): String {
        val numbers = listOf("zero", "one", "two", "three",
            "four", "five", "six", "seven",
            "eight", "nine")

        var out = input
        numbers.forEachIndexed { idx, it ->
            out = out.replace(it, it + idx.toString() + it)
        }

        return out
    }

    fun part1(input: List<String>): Int {
        return input
            .map {
                val fs = it.first { it.isDigit() } - '0'
                val ls = it.last { it.isDigit()} - '0'
                return@map fs*10+ls
            }.sum()
    }

    fun part2(input: List<String>): Int {
        return part1(
            input.map { replaceWordWithNumber(it) }
        )
    }

    val testInput = readInput("Day01_test")
    assertEqual(part1(testInput), 142)

    val testInput2 = readInput("Day01_test2")
    assertEqual(part2(testInput2), 281)

    val input = readInput("Day01")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

