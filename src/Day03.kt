fun main() {
    fun part1(input: List<String>): Int {
        val r = input.size
        val c = input[0].length
        val isSymbols = input.map {
            it.map { char -> !char.isDigit() && char != '.' }.toMutableList()
        }
        val isAdjSymbols = isSymbols.mapIndexed { i, row ->
            row.mapIndexed groupSym@{ j, isSym ->
                listOf(-1, 0, 1).forEach { dx ->
                    listOf(-1, 0, 1).forEach check@{ dy ->
                        val ni = i + dx
                        val nj = j + dy
                        if (ni < 0 || ni >= r || nj < 0 || nj >= c) {
                            return@check
                        }
                        if (isSymbols[ni][nj]) {
                            return@groupSym true
                        }
                    }
                }
                return@groupSym false
            }
        }

        var total = 0
        var acc = 0
        var accIsTouch = false
        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char.isDigit()) {
                    acc = 10 * acc + (char - '0')
//                    print("c $i $j ${isAdjSymbols[i][j]}")
                    accIsTouch = accIsTouch || isAdjSymbols[i][j]
                } else {
//                    if (acc > 0) {
//                        println("non: $acc $accIsTouch")
//                    }

                    if (accIsTouch)
                        total += acc

                    acc = 0
                    accIsTouch = false
                }

            }
            // flush
//            if (acc > 0) {
//                println("EOL: $acc $accIsTouch")
//            }
            if (accIsTouch)
                total += acc

            acc = 0
            accIsTouch = false
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val r = input.size
        val c = input[0].length
        var total = 0

        fun findAdjNumbers(x: Int, y: Int): List<Int> {
            var chars = ""
            listOf(-1, 0, 1).forEach { dx ->
                val px = x + dx
                if (px < 0 || px >= r) {
                    return@forEach
                }

                var lb = 0
                var rb = 0

                var lbp = -1
                while (true) {
                    val py = y + lbp
                    if (py < 0 || py >= c) break
                    lb = lbp
                    if (input[px][py] == '.') break
                    lbp--
                }

                var rbp = 1
                while (true) {
                    val py = y + rbp
                    if (py < 0 || py >= c) break
                    rb = rbp
                    if (input[px][py] == '.') break
                    rbp++
                }

                chars += ".${input[px].substring((y + lb)..(y + rb))}"
            }
            return Regex("\\d+").findAll(chars).map { Integer.parseInt(it.value) }.toList()
        }

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char == '*') {
                    val numbers = findAdjNumbers(i, j)
                    if (numbers.size == 2) {
                        total += numbers[0] * numbers[1]
                    }
                }
            }
        }

        return total
    }

    val testInput = readInput("Day03_test")
    assertEqual(part1(testInput), 4361)
    assertEqual(part2(testInput), 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
