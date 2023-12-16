fun main() {
    fun getNextLocation(options: Pair<String, String>, side: Char): String {
        if (side == 'L') return options.first
        if (side == 'R') return options.second
        return "ERROR"
    }

    fun getNextStep(walkSequence: CharArray,stepsTaken: Int): Char {
        return walkSequence[stepsTaken%walkSequence.size]
    }

    fun walkToDest(startLocation: String, walkSequence: CharArray, sideMap: Map<String, Pair<String,String>>) :Int {
        var stepsTaken = 0
        var location = startLocation

        while (!location.endsWith("Z")){
            val whichStep = getNextStep(walkSequence, stepsTaken)
            val sideOptions = sideMap.getOrDefault(location, Pair<String,String>("A","A"))
//            ("$location: $sideOptions $whichStep").println()
            stepsTaken++
            location = getNextLocation(sideOptions, whichStep)
        }
//        location.println()
        return stepsTaken
    }

    fun walkToDestTest(startLocation: String, walkSequence: CharArray, sideMap: Map<String, Pair<String,String>>) :Int {
        var stepsTaken = 0
        var location = startLocation
        while (stepsTaken < 1000000) {
            if(location.endsWith("Z")){
//                ("$location $stepsTaken").println()
            }
            val whichStep = getNextStep(walkSequence, stepsTaken)
            val sideOptions = sideMap.getOrDefault(location, Pair<String,String>("A","A"))
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

        return walkToDest( "AAA", sideSequence, sideMap)
    }

    fun greatestCommon(a: Long, b:Long): Long {
        var at = a
        var bt = b
        while (bt != 0.toLong()){
            var newa = bt
            bt = at%bt
            at = newa
        }
        return at
    }

    fun lowestCommon(a:Long, b: Long): Long {
        return ((a*b)/greatestCommon(a,b))
    }

    fun part2(input: List<String>): Long {
        val sideSequence = input[0].strip().toCharArray()
        val mapInputs = input.slice(2..<input.size).map { it.replace("[()]".toRegex(), "") }
        val processedMapInput = mapInputs.map { it.split(" = ").map { inner -> inner.split(", ") } }
        //Convert to map, each location has a pair containing left=first right=second
        val sideMap = processedMapInput.map { it[0][0] to Pair<String,String>(it[1][0], it[1][1]) }.toMap()
        sideMap.println()

        val starts = sideMap.keys.filter { it.endsWith("A") }
        starts.println()

        val walkLengths = starts.map { walkToDest(it, sideSequence, sideMap).toLong() }
        walkLengths.println()

        return walkLengths.reduce{acc, i -> lowestCommon(acc, i)}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    part1(testInput).println()
//    check(part1(testInput) == 142)

    val test2Input = readInput("Day08_test2")
    part2(test2Input).println()


    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()

}


