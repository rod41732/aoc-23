/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {

    fun getNumbers(line: String): List<Int> {
        return Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
    }



    fun part1(input: List<String>): Int {
        val times = getNumbers(input[0])
        val record = getNumbers(input[1])
        val ways = times.zip(record).map { (time, record) ->
            (1..time).forEach { holdDuration ->
                if (holdDuration * (time - holdDuration) > record) {
                    return@map (time - holdDuration) - holdDuration + 1
                }
            }
            return@map 0
        }
        println(ways)
        return ways.reduce { acc, it -> acc * it }
    }

    fun parseNumber2(line: String): Long {
        return Regex("\\d+").findAll(line).map { it.value }.joinToString("").toLong()
    }
    fun part2(input: List<String>): Long {
        val time = parseNumber2(input[0])
        val record = parseNumber2(input[1])

        (1..time).forEach { holdDuration ->
            if (holdDuration * (time - holdDuration) > record) {
                return (time - holdDuration) - holdDuration + 1
            }
        }
        return 0
    }

    val testInput = readInput("Day06_test")
    assertEqual(part1(testInput), 288)
    assertEqual(part2(testInput), 71503)

    val input = readInput("Day06")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

