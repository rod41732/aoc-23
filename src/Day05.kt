/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    data class MapRule(val destStart: Long, val srcStart: Long, val length: Long) {
        fun mapOrNull(number: Long): Long? {
            return if (srcStart <= number && number <= srcStart + length - 1) {
                number + destStart - srcStart
            } else {
                null
            }
        }

        fun inverse(): MapRule {
            return MapRule(srcStart, destStart, length)
        }
    }

    fun List<MapRule>.map(value: Long): Long {
        return this.firstNotNullOfOrNull { it.mapOrNull(value) } ?: value
    }

    fun List<List<MapRule>>.map(value: Long): Long {
        return this.fold(value) { acc, layer -> layer.map(acc) }
    }


    fun parseInput(input: List<String>): Pair<List<Long>, List<List<MapRule>>> {
        val chunks = input.windowWhen { it == "" }
        val seedLine = chunks.first().first()
        val seeds = seedLine.substringAfter(": ").split(" ").map { it.toLong() }
        // layer of maps, i.e. listOf(seed-to-soil, soil-to-fertilizer, ...)
        val mapLayers = chunks.drop(1).map {
            val mapLines = it.drop(1)
            val mapData = mapLines.map { it.split(" ").map { it.toLong() }.let { (a, b, c) -> MapRule(a, b, c) } }
            mapData
        }

        return Pair(seeds, mapLayers)
    }

    fun part1(input: List<String>): Long {
        val (seeds, mapLayers) = parseInput(input)
        return seeds.minOf { mapLayers.map(it) }
    }


    fun part2(input: List<String>): Long {
        val (seeds, mapLayers) = parseInput(input)

        val boundaryPoints = buildSet {
            val inverseMapLayers = mapLayers.reversed().map { layer -> layer.map { it.inverse() } }
            // calculate boundary point for all layers
            inverseMapLayers.forEachIndexed { index, currentLayer ->
                val restLayers = inverseMapLayers.drop(index + 1)
                // calculate boundary point for each layer
                currentLayer.forEach {
                    val src = it.destStart
                    val origToAchieve = restLayers.map(src)
                    add(origToAchieve)
                }
            }

        }

        val seedsToCheck = seeds.chunked(2).flatMap { (start, length) ->
            val seedRange = start..(start + length - 1)
            boundaryPoints.filter { seedRange.contains(it) } + seedRange.start
        }

        return seedsToCheck.minOf { mapLayers.map(it) }
    }

    assertEqual(part1(testInput), 35)
    assertEqual(part2(testInput), 46)


    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}



