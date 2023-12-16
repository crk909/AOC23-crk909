import kotlin.math.max

fun main() {

    fun rotateGrid(rocksGrid: List<String>): List<String>{
        val horizDimension = rocksGrid[0].length
        val vertDimension = rocksGrid.size
        var newArray : MutableList<MutableList<Char>> = MutableList(horizDimension) {MutableList(vertDimension){'E'} }
        rocksGrid.mapIndexed {rowNum , row ->
            row.mapIndexed{ colNum , sym ->
                newArray[colNum][rowNum] = sym
            }
        }
        return newArray.map { it.joinToString().replace(", ","") }
    }

    fun findOccurences(chars: CharArray, target: Char): MutableList<Int>{
        return chars.mapIndexed { index, char ->
            if (char == target) index
            else null
        }.filterNotNull().toMutableList()
    }

    fun rockFall(row: String): String{
        val charRow = row.toCharArray()
        // Find index of all #
        var blocks : MutableList<Int> = findOccurences(charRow, '#')
        var rocks : MutableList<Int> = findOccurences(charRow, 'O')
//        blocks.println()
//        rocks.println()

        var fallenString = ".".repeat(charRow.size).toCharArray()
        var nextAvailable = 0
        while (blocks.isNotEmpty() && rocks.isNotEmpty()){
            if (blocks[0] < rocks[0]){
                fallenString[blocks[0]] = '#'
                nextAvailable = blocks[0] + 1
                blocks.removeFirst()
            }
            else {
                fallenString[nextAvailable] = 'O'
                nextAvailable++
                rocks.removeFirst()
            }
        }
        while (rocks.isNotEmpty()){
            fallenString[nextAvailable] = 'O'
            nextAvailable++
            rocks.removeFirst()
        }
        while (blocks.isNotEmpty()){
            fallenString[blocks[0]] = '#'
            blocks.removeFirst()
        }
        return fallenString.joinToString().replace(", ","")
    }

    fun moveAllNorth(input: List<String>): List<String>{
        val rotated = rotateGrid(input)
        val fallenSide = rotated.map {rockFall(it)}
        return rotateGrid(fallenSide)
    }

    fun moveAllWest(input: List<String>): List<String>{
        return input.map { rockFall(it) }
    }

    fun moveAllEast(input: List<String>): List<String>{
        val fallenRocks = input.map { rockFall(it.reversed()) }
        return fallenRocks.map {it.reversed()}
    }

    fun moveAllSouth(input: List<String>): List<String>{
        val rotated = rotateGrid(input)
        val fallenSide = rotated.map { rockFall(it.reversed()) }
        val flippedBack = fallenSide.map { it.reversed() }
        return rotateGrid(flippedBack)
    }

    fun calcLoadRow(input: String, index: Int): Int{
        val rocksInRow = input.replace("[^O]".toRegex(), "").length
        return rocksInRow * (index+1)
    }

    fun calcLoad(input: List<String>): Int{
        return input.reversed().mapIndexed{ index, row ->
            calcLoadRow(row, index)
        }.sum()
    }


    fun part1(input: List<String>): Int{
        val afterFall = moveAllNorth(input)
        afterFall.map { it.println() }
        "XXXXXXXXX".println()
        return calcLoad(afterFall)
    }

    fun doCycle(input: List<String>): List<String>{
        return moveAllEast(moveAllSouth(moveAllWest(moveAllNorth(input))))
    }

    fun getLoopFactor(list: List<Int>, ind: Int):Int{
        val findRep = list[ind]

        return max(list.subList(ind+1,list.size).indexOf(findRep) + 1, 1)
    }

    fun verifyLoopFactor(chunks: List<List<Int>>):  Boolean{
        val verifyVal = chunks[chunks.size-1][0]
        return chunks.map { it[0] == verifyVal }.all { it }
    }

    fun part2(input: List<String>): Int {
        input.map { it.println() }
        var currentRocks = input
        "XXXXXXXXX".println()
        "XXXXXXXXX".println()

        val cycleWeights: MutableList<Int> = mutableListOf()
        for (i in (1..200)){
            currentRocks = doCycle(currentRocks)
//            currentRocks.map { it.println() }
//            "XXXXXXXXX".println()
//            "XXXXXXXXX".println()
            cycleWeights.addLast(calcLoad(currentRocks))
        }


        var loopCheck = 50
        var loopFactor: Int = 1
        var verifyLoop = false
        while (!verifyLoop) {
            loopFactor = getLoopFactor(cycleWeights, loopCheck)
            verifyLoop = verifyLoopFactor(cycleWeights.drop(loopCheck).chunked(loopFactor))
            loopCheck++
        }

        val loopingWeights = cycleWeights.chunked(loopFactor)
        loopingWeights.map { it.println() }
        val lastComplete = loopingWeights[loopingWeights.size-2]
        lastComplete.println()

        val indexofBillion = (1000000000 % loopFactor) -1
        return lastComplete[indexofBillion]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    part1(testInput).println()
//    "----------".println()
//    "----------".println()
    part2(testInput).println()
//    "----------".println()
//    "----------".println()

    val input = readInput("Day14")
//    part1(input).println()
//    "----------".println()
//    "----------".println()
    part2(input).println()
}

