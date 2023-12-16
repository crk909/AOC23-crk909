fun main() {

    fun rotatePuzzle(puzzle: String): String{
        val rows = puzzle.split("/")
        val horizDimension = rows[0].length
        val vertDimension = rows.size
        var newArray : MutableList<MutableList<Char>> = MutableList(horizDimension) {MutableList(vertDimension){'E'} }
//        rows.map { it.println() }
        rows.mapIndexed {rowNum , row ->
            row.mapIndexed{ colNum , sym ->
                newArray[colNum][rowNum] = sym
            }
        }
        return newArray.map { it.joinToString().replace(", ","") }.joinToString("/")
    }


    fun solvePuzzleRow(puzzle: String): Int{
        val rows = puzzle.split("/")
//        rows.println()

        var potentialReflections : MutableList<Int> = (0..rows.size-2).toMutableList()
        for(offset in (0..rows.size/2)){
            potentialReflections = potentialReflections.filter { refl ->
                val upperRow = refl - offset
                val lowerRow = refl + 1 + offset
                if (upperRow in rows.indices && lowerRow in rows.indices){
                    rows[upperRow] == rows[lowerRow]
                }
                else true
            }.toMutableList()
        }
        potentialReflections.println()
        if (potentialReflections.isNotEmpty()) return (potentialReflections[0] + 1)
        return 0
    }

    fun countDiffs(left: String, right:String): Int{
        return left.toCharArray().zip(right.toCharArray()).filter { (l,r) -> l !=r }.size
    }

    fun solvePuzzleSmudge(puzzle: String): Int{
        val rows = puzzle.split("/")

        var imperfections : MutableList<Int> = MutableList(rows.size-1){0}

        for(offset in (0..rows.size/2)){
            for(refl in (0..rows.size-2)){
                val upperRow = refl - offset
                val lowerRow = refl + 1 + offset
                if (upperRow in rows.indices && lowerRow in rows.indices){
                    if(rows[upperRow] != rows[lowerRow]){
                        imperfections[refl] += countDiffs(rows[upperRow], rows[lowerRow])
                    }
                }
            }
        }
        imperfections.println()
        //Get index of reflection with just 1 smudge, add 1 and return
        return imperfections.indexOf(1) + 1
    }

    fun part1(input: List<String>): Int{
//        input.println()
        val puzzles = input.joinToString("/").split("//")
//        puzzles.map { it.println() }

//        puzzles.map { solvePuzzle(it) }
        "----------".println()
        "----------".println()
        val puzzleSolves = puzzles.map { solvePuzzleRow(it) }
        val puzzlesRotated = puzzles.map { solvePuzzleRow(rotatePuzzle(it)) }
        puzzlesRotated.println()
        puzzleSolves.println()

        return (puzzlesRotated.sum()) + (puzzleSolves.sum()*100)
    }

    fun part2(input: List<String>): Int {
        val puzzles = input.joinToString("/").split("//")

        "----------".println()
        "----------".println()
        val puzzleSolves = puzzles.map { solvePuzzleSmudge(it) }
        val puzzlesRotated = puzzles.map { solvePuzzleSmudge(rotatePuzzle(it)) }
        puzzlesRotated.println()
        puzzleSolves.println()

        return (puzzlesRotated.sum()) + (puzzleSolves.sum()*100)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
//    part1(testInput).println()
//    "----------".println()
//    "----------".println()
    part2(testInput).println()
//    "----------".println()
//    "----------".println()

    val input = readInput("Day13")
//    part1(input).println()
//    "----------".println()
//    "----------".println()
    part2(input).println()
}

