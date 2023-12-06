/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {

    fun getNumbers(line: String): List<Int> {
        return Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
    }

    fun getWaysToBeat(time: Int, record: Long): Int {
        (1L..time.toLong()).forEach { holdDuration ->
            if (holdDuration * (time - holdDuration) > record) {
                return ((time - holdDuration) - holdDuration + 1).toInt()
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val times = getNumbers(input[0])
        val record = getNumbers(input[1])
        return times.zip(record).map { (time, record) ->
            getWaysToBeat(time, record.toLong())
        }.product()
    }

    fun parseNumber2(line: String): Long {
        return Regex("\\d+").findAll(line).map { it.value }.joinToString("").toLong()
    }

    fun part2(input: List<String>): Int {
        val time = parseNumber2(input[0])
        val record = parseNumber2(input[1])

        return getWaysToBeat(time.toInt(), record)
    }

    val testInput = readInput("Day06_test")
    assertEqual(part1(testInput), 288)
    assertEqual(part2(testInput), 71503)

    val input = readInput("Day06")
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

