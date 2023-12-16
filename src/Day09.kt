import kotlin.math.absoluteValue

fun main() {
    fun solvePuzzle(startNums: List<Int>): Int{
        " - - - - - ".println()

        val sizeArray = startNums.size
        val myArray = Array(sizeArray) {Array(sizeArray){0} }

        startNums.mapIndexed() {index, value ->
            myArray[0][index] = value
        }

        for (i in (1..sizeArray-1)){
            for (j in (0..sizeArray-i-1)){
//                (i.toString() + j.toString()).println()
                myArray[i][j] = myArray[i-1][j+1] - myArray[i-1][j]
            }
        }
        myArray.map { it.joinToString(" "){it.toString()} .println()  }
        " - - - - - ".println()
        var rowSolver = sizeArray-2
        while (rowSolver > 0){
            var colSolver = sizeArray-rowSolver
            myArray[rowSolver][colSolver] = myArray[rowSolver][colSolver-1] + myArray[rowSolver+1][colSolver-1]
            rowSolver--
        }

        myArray.map { it.joinToString(" "){it.toString()} .println()  }


        return myArray[0][sizeArray-1] + myArray[1][sizeArray-1]
    }


    fun part1(input: List<String>): Int {
        val games = input.map { it.split(" ").map { inner -> inner.toIntOrNull() }.filterNotNull() }
        val answers = games.map {solvePuzzle(it)}

        answers.println()

        return answers.sum()
    }

    fun part2(input: List<String>): Int {
        val games = input.map { it.split(" ").map { inner -> inner.toIntOrNull() }.filterNotNull() }
        val answers = games.map {solvePuzzle(it.reversed())}

        answers.println()

        return answers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    part1(testInput).println()

    part2(testInput).println()


    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

