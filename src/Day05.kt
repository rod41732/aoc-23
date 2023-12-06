import kotlin.math.min

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    data class Mapping(val destStart: Long, val srcStart: Long, val length: Long) {
        fun mapOrNull(number: Long): Long? {
            if (srcStart <= number && number <= srcStart + length - 1) {
                return number + destStart - srcStart
            }
            return null
        }

        fun inverse(): Mapping {
            return Mapping(srcStart, destStart, length)
        }
    }

    // apply list of mapping rules (e.g. seed-to-soil map) in to a value
    fun applyMapping(value: Long, maps: List<Mapping>): Long {
        maps.forEach {
            val mapped = it.mapOrNull(value)
            if (mapped != null) {
                return mapped
            }
        }
        return (value)
    }

    fun part1(input: List<String>): Long {
        val chunks = input.windowWhen { it == "" }
        val seedLine = chunks.first().first()
        val seeds = seedLine.substringAfter(": ").split(" ").map { it.toLong() }
        val mapLayers = chunks.drop(1).map {
            val mapLines = it.drop(1)
            val mapData = mapLines.map { it.split(" ").map { it.toLong() }.let { (a, b, c) -> Mapping(a, b, c) } }
            mapData
        }


        val locations = seeds.map {
            mapLayers.fold(it, { acc, mapLayer -> applyMapping(acc, mapLayer) })
        }

        return locations.min()
    }

    fun applyMultiLayers(value: Long, layers: List<List<Mapping>>): Long {
        return layers.fold(value) { acc, it -> applyMapping(acc, it ) }
    }

    fun part2(input: List<String>): Long {
        val chunks = input.windowWhen { it == "" }
        val seedLine = chunks.first().first()
        val seedValues = seedLine.substringAfter(": ").split(" ").map { it.toLong() }
        val mapLayers = chunks.drop(1).map {
            val mapLines = it.drop(1)
            val mapData = mapLines.map { it.split(" ").map { it.toLong() }.let { (a, b, c) -> Mapping(a, b, c) } }
            mapData
        }

        val boundaryPoints = mutableSetOf<Long>()
        val reverseMapLayers = mapLayers.reversed().map { layer -> layer.map { it.inverse() }}
        // calculate boundary point for all layers
        reverseMapLayers.forEachIndexed { index, currentLayer ->
            val restLayers = reverseMapLayers.drop(index + 1)
            // calculate boundary point for each layer
            currentLayer.forEach {
                val src = it.destStart
                val origToAchieve = restLayers.fold(src, { acc, layer -> applyMapping(acc, layer)})
                boundaryPoints.add(origToAchieve)
            }
        }

        var minLoc = Long.MAX_VALUE
        seedValues.chunked(2).forEach { (start, length) ->
            val seedRange = start .. (start + length - 1)
            val locStart = min(minLoc, applyMultiLayers(start, mapLayers))
            if (locStart < minLoc) {
                minLoc = locStart
            }

            boundaryPoints.forEach {
                if (seedRange.contains(it)) {
                    val locInteresting = min(minLoc, applyMultiLayers(it, mapLayers))
                    if (locInteresting < minLoc) {
                        minLoc = locInteresting
                    }
                }
            }
        }


        return minLoc
    }

    assertEqual(part1(testInput), 35)
    assertEqual(part2(testInput), 46)


    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}



