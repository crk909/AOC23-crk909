fun main() {
    fun getNextLocation(options: Pair<String, String>, side: Char): String {
        if (side == 'L') return options.first
        if (side == 'R') return options.second
        return "ERROR"
    }

    fun getNextStep(walkSequence: CharArray,stepsTaken: Int): Char {
        return walkSequence[stepsTaken%walkSequence.size]
    }

    fun walkToDest(walkSequence: CharArray, sideMap: Map<String, Pair<String,String>>) :Int {
        var stepsTaken = 0
        var location = "AAA"
        val destination = "ZZZ"

        while (location != destination){
            val whichStep = getNextStep(walkSequence, stepsTaken)
            val sideOptions = sideMap.getOrDefault(location, Pair<String,String>("A","A"))
            ("$location: $sideOptions $whichStep").println()
            stepsTaken++
            location = getNextLocation(sideOptions, whichStep)
        }
        return stepsTaken
    }

    fun part1(input: List<String>): Int {
        val sideSequence = input[0].strip().toCharArray()
        val mapInputs = input.slice(2..input.size-1).map { it.replace("[()]".toRegex(), "") }
        val processedMapInput = mapInputs.map { it.split(" = ").map { inner -> inner.split(", ") } }
        //Convert to map, each location has a pair containing left=first right=second
        val sideMap = processedMapInput.map { it[0][0] to Pair<String,String>(it[1][0], it[1][1]) }.toMap()
        sideMap.println()


        return walkToDest( sideSequence, sideMap)
    }
    fun ghostWalking(locations: List<String>): Boolean {
        return !locations.all { it.endsWith("Z") }
    }

    fun walkToDestGhost(startLocations: List<String>, walkSequence: CharArray, sideMap: Map<String, Pair<String,String>>): Int {
        var stepsTaken = 0
        var curLocations = startLocations

        while (ghostWalking(curLocations)){
            val whichStep = getNextStep(walkSequence, stepsTaken)
            var sideOptions = curLocations.map { sideMap.getOrDefault(it, Pair<String,String>("A","A")) }
//            sideOptions.println()
            stepsTaken += 1
            curLocations = sideOptions.map { getNextLocation(it, whichStep) }

        }

        return stepsTaken
    }

    fun part2(input: List<String>): Int {
        val sideSequence = input[0].strip().toCharArray()
        val mapInputs = input.slice(2..<input.size).map { it.replace("[()]".toRegex(), "") }
        val processedMapInput = mapInputs.map { it.split(" = ").map { inner -> inner.split(", ") } }
        //Convert to map, each location has a pair containing left=first right=second
        val sideMap = processedMapInput.map { it[0][0] to Pair<String,String>(it[1][0], it[1][1]) }.toMap()
        sideMap.println()

        val starts = sideMap.keys.filter { it.endsWith("A") }
        starts.println()

        return walkToDestGhost(starts, sideSequence, sideMap)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    part1(testInput).println()
//    check(part1(testInput) == 142)

    val test2Input = readInput("Day08_test2")
    part2(test2Input).println()


    val input = readInput("Day08")
//    part1(input).println()
    part2(input).println()
}


