fun main() {

    fun isValidStep(r: Int, c: Int, maxRows: Int, maxCols: Int, blockedTiles: MutableSet<Pair<Int, Int>>): Boolean{
        if(r in (0..maxRows) && c in (0..maxCols)){
            return !blockedTiles.contains(Pair(r,c))
        }
        return false
    }

    fun expandSteps(starts: Set<Pair<Int, Int>>, blockedTiles: MutableSet<Pair<Int, Int>>, maxRows: Int, maxCols: Int, ): MutableSet<Pair<Int, Int>>{
        var destinations = mutableSetOf<Pair<Int,Int>>()
        starts.forEach{(r,c) ->
            if (isValidStep(r+1,c,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r+1,c))
            if (isValidStep(r-1,c,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r-1,c))
            if (isValidStep(r,c+1,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r,c+1))
            if (isValidStep(r,c-1,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r,c-1))
        }
        return destinations
    }

    fun part1(input: List<String>, maxSteps: Int): Int {
        val rows = input.map { it.toCharArray() }
        var blocks = mutableSetOf<Pair<Int,Int>>()
        var reachable = mutableSetOf<Pair<Int,Int>>()
        val maxRows = rows.size
        val maxCols = rows[0].size

        rows.forEachIndexed {rowNum, row ->
            row.forEachIndexed { colNum, char ->
                when(char){
                    '#' -> blocks.add(Pair(rowNum,colNum))
                    'S' -> reachable.add(Pair(rowNum,colNum))
                }
            }
        }

        for(i in (1..maxSteps)){
            reachable = expandSteps(reachable, blocks, maxRows, maxCols)
        }

        reachable.println()

        return reachable.size
    }

    fun isValidINF(r: Int, c: Int, maxRows: Int, maxCols: Int, blockedTiles: MutableSet<Pair<Int, Int>>): Boolean{
        var loopedR = r % maxRows
        if (r < 0) loopedR = maxRows-loopedR
        var loopedC = c % maxCols
        if (c < 0) loopedC = maxCols-loopedC
        return !blockedTiles.contains(Pair(loopedR, loopedC))
    }

    fun expandLooping(starts: Set<Pair<Int, Int>>, blockedTiles: MutableSet<Pair<Int, Int>>, maxRows: Int, maxCols: Int, ): MutableSet<Pair<Int, Int>>{
        var destinations = mutableSetOf<Pair<Int,Int>>()
        starts.forEach{(r,c) ->
            if (isValidINF(r+1,c,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r+1,c))
            if (isValidINF(r-1,c,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r-1,c))
            if (isValidINF(r,c+1,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r,c+1))
            if (isValidINF(r,c-1,maxRows,maxCols,blockedTiles)) destinations.add(Pair(r,c-1))
        }
        return destinations
    }

    fun part2(input: List<String>, numSteps: Int): Long {
        val rows = input.map { it.toCharArray() }
        var blocks = mutableSetOf<Pair<Int,Int>>()
        var reachable = mutableSetOf<Pair<Int,Int>>()
        val maxRows = rows.size
        val maxCols = rows[0].size

        rows.forEachIndexed {rowNum, row ->
            row.forEachIndexed { colNum, char ->
                when(char){
                    '#' -> blocks.add(Pair(rowNum,colNum))
                    'S' -> reachable.add(Pair(rowNum,colNum))
                }
            }
        }

        for(i in (1..numSteps)){
            reachable = expandLooping(reachable, blocks, maxRows, maxCols)
            (i.toString() + ": " + reachable.size).println()
        }

        reachable.println()

        return reachable.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
//    part1(testInput, 6).println()
    part2(testInput, 100)
//    val test2Input = readInput("Day21_test2")


    val input = readInput("Day21")
    part1(input,64).println()
//    part2(input).println()
}

