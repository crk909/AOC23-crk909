fun main() {
    fun isPossible(conditions:String, recordsIn: List<Int>): Boolean{
        var splits = conditions.split(".").filterNot { it == "" }.toMutableList()
        var records :MutableList<Int> = recordsIn.toMutableList()
        while(splits.isNotEmpty() && records.isNotEmpty()){
            val nextRecordLen = records[0]
            if (nextRecordLen+1 < splits[0].length){
                splits[0] = splits[0].substring(nextRecordLen+1 ,splits[0].length)
                records.removeFirst()
            }
            else if (nextRecordLen <= splits[0].length){
                splits.removeFirst()
                records.removeFirst()
            }
            else{
                splits.removeFirst()
            }
        }
//        (conditions + " " + recordsIn.toString() + " " + records.isEmpty().toString()).println()
        return records.isEmpty()
    }

    fun matchesRecord(conditions: String, records: List<Int>): Boolean {
        var splits = conditions.split(".").filterNot { it == "" }.map { it.length }
        return (splits.zip(records).map { (spl, rec) ->
            spl == rec
        }.all { it } && splits.size == records.size)
    }

    fun findPossibilities(conditions: String, records: List<Int>): HashMap<String,Boolean>{
        if (!isPossible(conditions, records)) {
            return hashMapOf()
        }
        if (!conditions.contains("?")){
            if (matchesRecord(conditions, records)) return hashMapOf(Pair(conditions, true))
            else return hashMapOf()
        }
        else{
            val replaceDot = findPossibilities(conditions.replaceFirst("?","."), records)
            val replaceHash : HashMap<String,Boolean> = findPossibilities(conditions.replaceFirst("?","#"), records)
//            val firstQ = conditions.indexOf('?')
//            if (firstQ == conditions.length-1 || conditions[firstQ+1] != '?') replaceHash = findPossibilities(conditions.replaceFirst("?","#"), records)
//            else replaceHash = findPossibilities(conditions.replaceFirst("??", "#."), records)
            val returnMap : HashMap<String,Boolean> = hashMapOf()
            returnMap.putAll(replaceHash)
            returnMap.putAll(replaceDot)
            return returnMap
        }

    }

    fun part1(input: List<String>): Int{
        val processed = input.map { it.split(" ") }
        val conditions = processed.map { it[0] }
        val thingy = processed.map { it[1].split(",").map { cond -> cond.toIntOrNull() }.filterNotNull() }
        processed.println()
        conditions.println()
        thingy.println()

        conditions.map { it.replace("[^?]".toRegex(), "").length }.max().println()

        val possibilities = conditions.zip(thingy).map { (cond, thing) -> findPossibilities(cond, thing) }
        possibilities.map {(it.toString() + " " + it.size).println()}

        return possibilities.map { it.size }.sum()
    }

    fun findPossibilitiesBinary(conditions: String, records :List<Int>): Long{
        if(records.isEmpty()){
            if(conditions.contains("#")) return 0
            return 1
        }
        if(conditions.length < 16) {
            val answers = findPossibilities(conditions, records)
            return answers.keys.size.toLong()
        }
        val splittingSpringLocation = records.size/2
        val splittingSpring = records[splittingSpringLocation]
        val leftRecords = records.subList(0, splittingSpringLocation)
        val rightRecords = records.subList(splittingSpringLocation+1, records.size)
        val fitArray = (0..conditions.length-splittingSpring).map { ind ->
            //Also must check that value on left and right is NOT # (because then it is too big)
//            (conditions.substring(ind, ind+biggestSpring)).println()
            if(conditions.substring(ind, ind+splittingSpring).contains(".")) -1
            else if (conditions.getOrNull(ind-1) == '#' || conditions.getOrNull(ind+splittingSpring) == '#') -1
            else ind
        }.filterNot { it ==  -1 }

//        leftRecords.println()
//        rightRecords.println()
//        fitArray.println()
//        conditions.println()

        val totals = fitArray.map { it ->
            val leftConds = if (it > 1) conditions.substring(0, it-1)
                            else ""
            val rightConds = if (it < conditions.length-splittingSpring ) conditions.substring(it+splittingSpring+1, conditions.length)
                            else ""

            if(isPossible(leftConds, leftRecords) && isPossible(rightConds,rightRecords)){
                val possLeft = findPossibilitiesBinary(leftConds, leftRecords)
                val possRight = findPossibilitiesBinary(rightConds, rightRecords)
//                (it.toString() + " " + leftConds + " " + rightConds).println()
//                (it.toString() + " " + leftRecords.toString() + " " + rightRecords.toString()).println()
//                (possLeft.toString() + "   " + possRight.toString()).println()

                possRight * possLeft
            }
            else 0

//            (it.toString() + " " + leftConds + " " + rightConds).println()
//
//            (possLeft.toString() + "   " + possRight.toString()).println()
        }
//        totals.println()
        return totals.sum()
    }

    fun part2(input: List<String>): Long {
        val processed = input.map { it.split(" ") }
        val conditionsPre = processed.map { it[0] }
        val thingyPre = processed.map { it[1].split(",").map { cond -> cond.toIntOrNull() }.filterNotNull() }

        val conditions = conditionsPre.map { it+"?"+it+"?"+it+"?"+it+"?"+it }
        val thingy = thingyPre.map { listOf(it,it,it,it,it).flatten() }

        processed.println()
        conditions.println()
        thingy.println()

        conditions.map { it.replace("[^?]".toRegex(), "").length }.max().println()

        val possibilities = conditions.zip(thingy).map { (cond, thing) ->
            (cond + "  " + thing).println()
            val many = findPossibilitiesBinary(cond, thing)
            many.println()
            many
        }
//        val possibilities = listOf(findPossibilitiesBinary(conditions[1],thingy[1]))
//        possibilities.map { (it.toString() + " " + it.size).println() }

        return possibilities.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")

    part1(testInput).println()
//    part1(testInput2).println()

    "----------".println()
    "----------".println()
    part2(testInput).println()
    "----------".println()
    "----------".println()
    findPossibilities("???.##", listOf(1,1)).println()

    val input = readInput("Day12")
//    part1(input).println()
//    "----------".println()
//    "----------".println()
    part2(input).println()
}

