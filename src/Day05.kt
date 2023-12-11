fun main() {
    fun convertToRange(map: List<Long>): List<Long> {
        val startRange = map[1]
        val endRange = startRange + map[2] - 1
        val change =  map[0] - startRange

        return listOf(startRange, endRange, change)
    }

    fun convertWithMap(whip: Long, map: List<List<Long>>): Long {
        whip.println()
        map.println()
        map.forEach {
            if (whip >= it[0] && whip <= it[1]) return whip + it[2]
        }
        return whip
    }

    fun findDestination(start: Long, maps: List<List<List<Long>>>): Long{
        var result = start
        maps.forEach { converter ->
            result = convertWithMap(result, converter)
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val combined = input.joinToString { "$it " }
        val sections = combined.split(":")

        //Section 0 -> Nothing
        //Section 1 -> Seeds
        val seeds = sections[1].split(" ").map { it.toLongOrNull() }.filterNotNull()
        seeds.println()

        //Section 2-8 -> Maps
        val maps = sections.slice(2..8).map { it.split(" ").map { num -> num.toLongOrNull() }.filterNotNull().chunked(3) }


        val convertedMaps = maps.map { it.map{ unit -> convertToRange(unit) } }
        convertedMaps.println()

//        seeds.map { seed -> convertWithMap(seed, convertedMaps[0]) }.println()
        //Same for next maps
        val locations = seeds.map { seed -> findDestination(seed, convertedMaps)}
        locations.println()

        return locations.min()
    }

    fun ifMapContainsAll(map: List<Long>,start: Long, end: Long): Boolean {
        return (map[0] <= start && map[1] >= end)
    }

    fun ifMapContainsStart(map: List<Long>, start: Long): Boolean {
        return (map[0] <= start && map[1] >= start)
    }

    fun ifMapContainsEnd(map: List<Long>, end:Long): Boolean {
        return (map[0] <= end && map[1] >= end)
    }

    fun ifMapInRange(map: List<Long>, start: Long, end:Long): Boolean {
        return (map[0] > start && map[1] < end)
    }


    fun rangeConvert(seedRange: LongRange, maps: List<List<List<Long>>>): Long{
        var remainingRanges = listOf<LongRange>(seedRange);

        maps.forEach {destMap ->
            destMap.println()
            var resultingRanges = listOf<LongRange>()
            while (remainingRanges.isNotEmpty()) {
                var curRange = remainingRanges[0]
                remainingRanges = remainingRanges.drop(1)
                var startVal = curRange.start
                var endVal = curRange.last
                var noMatch = true
                destMap.forEach { map ->
                    if (ifMapContainsAll(map, startVal, endVal) && noMatch) {
                        resultingRanges = resultingRanges + listOf<LongRange>((startVal + map[2]..endVal + map[2]))
                        noMatch = false
                    } else if (ifMapContainsStart(map, startVal) && noMatch) {
                        resultingRanges = resultingRanges + listOf<LongRange>((startVal + map[2]..map[1] + map[2]))
                        //Add unconverted part back, end LOOP
                        remainingRanges = remainingRanges + listOf<LongRange>((map[1] + 1..endVal))
                        noMatch = false
                    } else if (ifMapContainsEnd(map, endVal) && noMatch) {
                        resultingRanges = resultingRanges + listOf<LongRange>((map[0] + map[2]..endVal + map[2]))
                        //Add unconverted part back
                        remainingRanges = remainingRanges + listOf<LongRange>((startVal..map[0] - 1))
                        noMatch = false
                    } else if (ifMapInRange(map, startVal, endVal) && noMatch) {
                        resultingRanges = resultingRanges + listOf<LongRange>((map[0] + map[2]..map[1] + map[2]))
                        //Add unconverted on both sides
                        remainingRanges = remainingRanges + listOf<LongRange>((startVal..map[0] - 1), (map[1] + 1..endVal))
                        noMatch = false
                    }

                }
                if (noMatch) resultingRanges = resultingRanges + listOf<LongRange>(curRange)
            }
            remainingRanges = resultingRanges
            remainingRanges.println()
        }
        val startVals = remainingRanges.map {it.start}
        startVals.println()

        return startVals.min()
    }

    fun part2(input: List<String>): Long {
        val combined = input.joinToString { "$it " }
        val sections = combined.split(":")

        //Section 0 -> Nothing
        //Section 1 -> Seeds

        val initialSeeds = sections[1].split(" ").map { it.toLongOrNull() }.filterNotNull()
//        initialSeeds.println()
        val seeds = initialSeeds.chunked(2).map { (a,b) -> (a..a+b)}
//        seeds.println()

        //Section 2-8 -> Maps
        val maps = sections.slice(2..8).map { it.split(" ").map { num -> num.toLongOrNull() }.filterNotNull().chunked(3) }
//        maps.println()

        val convertedMaps = maps.map { it.map{ unit -> convertToRange(unit) } }
//        convertedMaps.println()

        val locationMins = seeds.map { rangeConvert(it, convertedMaps) }
//        locationMins.println()



        return locationMins.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    part1(testInput).println()
//    check(part1(testInput) == 35)

    "\n\n\n\n".println()

//    val test2Input = readInput("Day03_test2")
    part2(testInput).println()
//    check(part2(test2Input) == 281)

    "\n\n\n\n".println()

    val input = readInput("Day05")
//    part1(input).println()

    "\n\n\n\n".println()

    part2(input).println()
}

