import kotlin.math.max

fun main() {
    fun parseShow(show: String): List<Int> {
        val counts = mutableListOf(0, 0, 0)
        val colors = show.split(',').map { it.trim() }
        colors.forEach {
            val (num, name) = it.split(' ')
            val numInt = num.toInt()
            when (name) {
                "red" -> counts[0] = numInt
                "green" -> counts[1] = numInt
                "blue" -> counts[2] = numInt
                else -> throw Exception("Unreachable!")
            }
        }
        return counts
    }

    fun part1(input: List<String>): Int {
        var total = 0
        input.forEachIndexed { idx, it ->
            val shows = it.substringAfter(':').split(";")
            val possible = !shows.any {
                val counts = parseShow(it)
                counts[0] > 12 || counts[1] > 13 || counts[2] > 14
            }
            if (possible) {
                total += idx + 1
            }
        }

        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0
        input.forEachIndexed { idx, it ->
            val shows = it.substringAfter(':').split(";")
            val counts = mutableListOf(0, 0, 0)
            shows.forEach {
                val colors = it.split(',').map { it.trim() }
                colors.forEach {
                    val (num, name) = it.split(' ')
                    val numInt = num.toInt()
                    when (name) {
                        "red" -> counts[0] = max(counts[0], numInt)
                        "green" -> counts[1] = max(counts[1], numInt)
                        "blue" -> counts[2] = max(counts[2], numInt)
                        else -> throw Exception("Unreachable!")
                    }
                }
            }
            total += counts[0] * counts[1] * counts[2]
        }

        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    assertEqual(part1(testInput), 8)
    assertEqual(part2(testInput) , 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
