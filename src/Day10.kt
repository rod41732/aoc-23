fun main() {
    fun part1(input: List<String>): Int {
        val (startPos, graph) = parseGraph(input)
        val distance = bfs(startPos, graph)
        return distance.values.max()
    }

    fun part2(input: List<String>): Int {
        val (startPos, graph) = parseGraph(input)

        // prune graph so it contains only point in main loop (necessary for correct hit testing)
        val distance = bfs(startPos, graph)
        val mainLoopPoints = distance.keys.toSet()
        val prunedGraph: Graph = graph.filterKeys { mainLoopPoints.contains(it) }
            .mapValues { (_, v) -> v.filter { mainLoopPoints.contains(it) }.toSet() }


        return input.withIndex().sumOf { (r, row) ->
            row.withIndex().count { (c, _) ->
                // draw intersecting line upwards at col = c + 0.5, point is inside loop if it intersects a *main loop* pipe there
                when {
                    r to c in mainLoopPoints -> false
                    else -> {
                        (0..r - 1).count { testRow ->
                            prunedGraph.isConnected(testRow to c, testRow to c + 1)
                        } % 2 == 1
                    }
                }
            }
        }
    }

    val testInput = readInput("Day10_test").formatInput()
    assertEqual(part1(testInput), 4)
    val testInput2 = readInput("Day10_test2").formatInput()
    // Example at: Here's the more complex loop again: ...
    assertEqual(part1(testInput2), 8)

    assertEqual(part2(testInput), 1)
    // Example at: To determine whether it's even worth taking the time ...
    val testInput3 = readInput("Day10_test3").formatInput()
    // Example at: In fact, there doesn't even need to be a full tile path ...
    assertEqual(part2(testInput3), 4)
    // Example at: Here's a larger example: ...
    val testInput4 = readInput("Day10_test4").formatInput()
    assertEqual(part2(testInput4), 4)
    // Example at: Any tile that isn't part of the main loop can count as being enclosed by the loop...
    val testInput5 = readInput("Day10_test5").formatInput()
    assertEqual(part2(testInput5), 10)

    val input = readInput("Day10").formatInput()
    println("Part1: ${part1(input)}") // 6828
    println("Part2: ${part2(input)}") // 459
}

private typealias Point = Pair<Int, Int>
private typealias Graph = Map<Point, Set<Point>>
private typealias MutableGraph = MutableMap<Point, MutableSet<Point>>

// replace chars with box drawing to ease debugging
private val PIPE_SE = '┌'
private val PIPE_SW = '┐'
private val PIPE_NE = '└'
private val PIPE_NW = '┘'
private val PIPE_NS = '│'
private val PIPE_EW = '─'
private val replaceMap = "F7LJ|-".zip("┌┐└┘│─").toMap()

private fun String.replaceWithBoxDrawing(): String = this.map {
    replaceMap.getOrDefault(it, it)
}.joinToString("")

private fun List<String>.formatInput() = map { it.replaceWithBoxDrawing() }

private fun Pair<Int, Int>.south() = Pair(first + 1, second)
private fun Pair<Int, Int>.north() = Pair(first - 1, second)
private fun Pair<Int, Int>.east() = Pair(first, second + 1)
private fun Pair<Int, Int>.west() = Pair(first, second - 1)

private fun Graph.isConnected(p1: Point, p2: Point): Boolean {
    return (get(p1)?.contains(p2) ?: false) && (get(p2)?.contains(p1) ?: false)
}

private fun MutableGraph.getDefault(p: Point) = getOrPut(p) { mutableSetOf() }


private fun parseGraph(input: List<String>): Pair<Point, Graph> {
    val graph = mutableMapOf<Point, MutableSet<Point>>()
    lateinit var startPos: Point
    input.forEachIndexed { r, row ->
        row.forEachIndexed { c, char ->
            val p = r to c
            with(graph.getDefault(p)) {
                when (char) {
                    PIPE_EW -> { add(p.east()); add(p.west()) }
                    PIPE_NS -> { add(p.north()); add(p.south()) }
                    PIPE_NW -> { add(p.north()); add(p.west()) }
                    PIPE_NE -> { add(p.north()); add(p.east()) }
                    PIPE_SW -> { add(p.south()); add(p.west()) }
                    PIPE_SE -> { add(p.south()); add(p.east()) }
                    'S' -> {
                        startPos = p
                        add(p.south()); add(p.west()); add(p.north()); add(p.east())
                    }
                }

            }

        }
    }
    return startPos to graph
}

private fun bfs(startPos: Point, graph: Graph): Map<Point, Int> {
    val q = ArrayDeque<Point>()
    q.addFirst(startPos)
    val distance = mutableMapOf(startPos to 0)
    while (!q.isEmpty()) {
        val cur = q.removeLast()
        val curDist = distance.get(cur)!!
        graph.get(cur)?.forEach {
            if (it in distance) return@forEach
            if (graph[it]?.contains(cur) == true) {
                distance[it] = curDist + 1
                q.addFirst(it)
            }
        }
    }
    return distance
}
