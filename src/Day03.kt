import com.sun.source.doctree.UnknownBlockTagTree

fun main() {
    fun getSymbols(input: List<String>, nums: Regex ): List<List<Int>>{
        val symbols = input.map { it.replace(nums, ".") }
        val symbolLocations = symbols.mapIndexed() { rowNum, row ->
            row.mapIndexed() { colNum, symbol ->
                if (symbol == '.') null;
                else listOf<Int>(rowNum,colNum)
            }.filterNotNull()
        }.flatten()
        return symbolLocations
    }

    fun getIndexes(rowNum: Int, row: String, nums: List<String>): List<List<Int>>{
//        nums.println()
        var myrow = row
        var results = mutableListOf<List<Int>>()
        for (num in nums){
            val index = myrow.indexOf(num)
            results.add(listOf(rowNum, index, num.length))
            val replaceValue = num.replace("[0-9]".toRegex(), ".")
            myrow = myrow.replaceFirst(num, replaceValue)
//            myrow.println()
        }
        return results
    }

    fun isAdjacent(symbols: List<List<Int>>, spotLocation: List<Int>): Boolean {
//        spotLocation.println()
        val adjacentRows = symbols.filter { (r,_) -> (r == spotLocation.get(0) || r-1 == spotLocation.get(0) || r+1 == spotLocation.get(0)) }
        val touching = adjacentRows.filter { (_,c) -> (c >= spotLocation.get(1)-1 && c <= spotLocation.get(1)+spotLocation.get(2))}
//        adjacentRows.println()
//        touching.println()
        return touching.isNotEmpty()
    }

    fun part1(input: List<String>): Int {
        val numerics = "[0-9]".toRegex()
        val nonNumerics = "[^0-9]".toRegex()
//        input.println()
        val symbolLocations = getSymbols(input, numerics)
//        symbolLocations.println()

        val numsFound = input.map {it.replace(nonNumerics, ".").split(".").filter { num -> num.toIntOrNull() != null }}

        val numsIndex = input.mapIndexed { rowNum, row -> getIndexes(rowNum, row, numsFound.get(rowNum))}.flatten()
        val numsFoundFlat = numsFound.flatten().map { it.toInt() }
//        numsFound.println()
//        numsIndex.println()
//        numsFoundFlat.sum().println()
        val numsNextToEngine = numsIndex.mapIndexed { index, spotList ->
            if(isAdjacent(symbolLocations, spotList)) numsFoundFlat.get(index)
            else 0
        }
//        numsNextToEngine.println()
        return numsNextToEngine.sum()
    }

    fun adjacentNums(gear: List<Int>, possibleNums: List<List<Int>>, nums: List<List<String>>): Int {
        val row = gear.get(0)
        val col = gear.get(1)
        val rowNumbers = possibleNums.filter { (r,_,_) -> (r == row || r-1 == row || r+1 == row) }
//        rowNumbers.println()

        val colNumbers = rowNumbers.filter { (_,c,l) -> (col >= c-1 && col <= c+l) }
//        colNumbers.println()
        val adjacents = colNumbers.map { num -> possibleNums.indexOf(num)}
        if (adjacents.size != 2) return 0
        else {
            val parts = adjacents.map { index -> nums.flatten().get(index).toInt()}
            return parts.get(0) * parts.get(1)
        }
    }


    fun part2(input: List<String>): Int {
        val nonNumerics = "[^0-9]".toRegex()
        val nonGears = "[^*]".toRegex()
        val possibleGears = getSymbols(input, nonGears)
//        possibleGears.println()

        val numsFound = input.map {it.replace(nonNumerics, ".").split(".").filter { num -> num.toIntOrNull() != null }}
        val numsIndex = input.mapIndexed { rowNum, row -> getIndexes(rowNum, row, numsFound.get(rowNum))}.flatten()
//        numsIndex.println()

        val gearVals = possibleGears.map { gear -> adjacentNums(gear, numsIndex, numsFound)}
        gearVals.println()
        return gearVals.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    part1(testInput).println()
    check(part1(testInput) == 4361)

    val test2Input = readInput("Day03_test")
    check(part2(test2Input) == 467835)


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

//557560 too low
//"6" in row 5 (3 from end) not counted?
