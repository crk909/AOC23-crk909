import kotlin.math.absoluteValue
import kotlin.math.exp

fun main() {

    fun expandedValue(initial:Int, empties: List<Int>, expansionMult: Int): Int{
        return initial + (empties.takeWhile { it < initial }.size * (expansionMult-1))
    }

    fun singleDistance(pairI: Pair<Int,Int>, pairJ: Pair<Int,Int>): Int{
        val a = pairI.first
        val b = pairJ.first
        val horizontalDistance = (a - b).absoluteValue

        val c = pairI.second
        val d = pairJ.second
        val verticalDistance = (c - d).absoluteValue
        return horizontalDistance + verticalDistance
    }

    fun findDistances(listy: List<Pair<Int,Int>>): List<Int>{
        var listDistances: MutableList<Int> = mutableListOf()
        for (i in (0..listy.size-1)){
            for(j in (i+1..listy.size-1)){
//                (listy[i].toString() + listy[j].toString()).println()
                listDistances.addLast(singleDistance(listy[i], listy[j]))
            }
        }
        return listDistances
    }

    fun part1(input: List<String>): Long {
        val vertDimension = input.size
        val horizDimension = input[0].length
        vertDimension.println()
        horizDimension.println()

        val emptyRowArray = Array(vertDimension){1}
        val emptyColArray = Array(horizDimension){1}
        val lines = input.map { it.split("").filterNot { symbol -> symbol == "" } }
        val galaxyLocations = lines.mapIndexed { rowNum, row ->
            row.mapIndexed {colNum, char ->
                if (char == "#") {
                    emptyRowArray[rowNum] = 0
                    emptyColArray[colNum] = 0
                    Pair(rowNum,colNum)
                }
                else Pair(-1, -1)
            }.filter { it.first != -1 }
        }.flatten()

        lines.map {it.println()}
        emptyRowArray.joinToString(" ").println()
        emptyColArray.joinToString(" ").println()
        galaxyLocations.println()
        val emptyRows : List<Int> = emptyRowArray.mapIndexed{index, empty1 ->
            if(empty1 == 1) index
            else -1
        }.filterNot { it == -1 }
        val emptyCols : List<Int> = emptyColArray.mapIndexed{index, empty1 ->
            if(empty1 == 1) index
            else -1
        }.filterNot { it == -1 }
        emptyRows.println()
        emptyCols.println()
        val expandedLocations = galaxyLocations.map { (row,col) ->
            Pair(expandedValue(row, emptyRows, 2), expandedValue(col, emptyCols, 2))
        }
        expandedLocations.println()

        val distances = findDistances(expandedLocations)
        distances.println()
        distances.size.println()

        return distances.sum().toLong()
    }

    fun longSum(dists: List<Int>): Long{
        var sum: Long = 0
        dists.forEach {sum += it}
        return sum
    }

    fun part2(input: List<String>): Long {
        val vertDimension = input.size
        val horizDimension = input[0].length

        val emptyRowArray = Array(vertDimension){1}
        val emptyColArray = Array(horizDimension){1}
        val lines = input.map { it.split("").filterNot { symbol -> symbol == "" } }
        val galaxyLocations = lines.mapIndexed { rowNum, row ->
            row.mapIndexed {colNum, char ->
                if (char == "#") {
                    emptyRowArray[rowNum] = 0
                    emptyColArray[colNum] = 0
                    Pair(rowNum,colNum)
                }
                else Pair(-1, -1)
            }.filter { it.first != -1 }
        }.flatten()

        val emptyRows : List<Int> = emptyRowArray.mapIndexed{index, empty1 ->
            if(empty1 == 1) index
            else -1
        }.filterNot { it == -1 }
        val emptyCols : List<Int> = emptyColArray.mapIndexed{index, empty1 ->
            if(empty1 == 1) index
            else -1
        }.filterNot { it == -1 }
        val expandedLocations = galaxyLocations.map { (row,col) ->
            Pair(expandedValue(row, emptyRows, 1000000), expandedValue(col, emptyCols, 1000000))
        }
        expandedLocations.println()

        val distances = findDistances(expandedLocations)
        distances.println()
        distances.size.println()

        return longSum(distances)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")

    part1(testInput).println()
//    part1(testInput2).println()

    "----------".println()
    "----------".println()
    part2(testInput).println()
    "----------".println()
    "----------".println()


    val input = readInput("Day11")
//    part1(input).println()
    "----------".println()
    "----------".println()

    part2(input).println()
}

