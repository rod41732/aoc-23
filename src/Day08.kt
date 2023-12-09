import java.lang.Exception

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    data class Node(val name: String, val left: String, val right: String);

    fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return a / gcd(a, b) * b
    }

    fun calculateCount(
        graph: Map<String, Node>,
        instruction: String,
        startNode: String,
        isEnd: (node: Node) -> Boolean
    ): Int {
        var curNode = graph.get(startNode)!!
        instruction.asSequence().repeat().forEachIndexed { idx, it ->
            if (isEnd(curNode)) return idx
            curNode = graph.get(
                when (it) {
                    'L' -> curNode.left
                    'R' -> curNode.right
                    else -> throw Exception("Invalid instruction: '$it'")
                }
            )!!
        }
        throw Exception("Should be unreachable")
    }

    fun parseInput(input: List<String>): Pair<String, Map<String, Node>> {
        val instruction = input.first()
        val graph = buildMap {
            input.drop(2).forEach {
                val (a, b, c) = Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)").find(it)!!.groupValues.drop(1)
                this[a] = Node(a, b, c)
            }
        }
        return Pair(instruction, graph)
    }

    fun part1(input: List<String>): Int {
        val (instruction, graph) = parseInput(input)
        return calculateCount(graph, instruction, "AAA", { it.name == "ZZZ" })

    }

    fun part2(input: List<String>): Long {
        val (instruction, graph) = parseInput(input)

        val startNodes = graph.values.filter { it.name.last() == 'A' }
        // from observation, each start node will end at some node ending Z at N, 2N, 3N, ...
        val cycleSizes = startNodes.map {
            calculateCount(graph, instruction, it.name, { it.name.last() == 'Z' })
        }
        return cycleSizes.fold(1L) { acc, it -> lcm(acc, it.toLong()) }
    }

    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    assertEqual(part1(testInput), 2)
    assertEqual(part1(testInput2), 6)

    val input = readInput("Day08")
    println("Part1: ${part1(input)}") // 19199
    println("Part2: ${part2(input)}") // 13663968099527
}

