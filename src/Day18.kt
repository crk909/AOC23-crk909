import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {

    fun getVertices(sides: List<Long>, dirs: List<Char>): MutableList<Pair<Long,Long>>{
        var verts : MutableList<Pair<Long,Long>> = mutableListOf()
        var row: Long = 0;
        var col: Long = 0;
        sides.zip(dirs).map { (side, dir) ->
            when(dir){
                'L' -> col -= side
                'R' -> col += side
                'U' -> row -= side
                'D' -> row += side
            }
            verts.addLast(Pair(row,col))
        }
        return verts
    }

    fun shoelace(vertices: List<Pair<Long,Long>>): Long{
        val xVals : List<Long> = vertices.map { it.first + 50 }
        val yVals : List<Long> = vertices.map { it.second + 50 }
        val xShift : List<Long> = xVals.subList(1,xVals.size) + listOf(xVals[0])
        val yShift : List<Long> = yVals.subList(1,yVals.size) + listOf(yVals[0])
//        xVals.println()
//        yVals.println()
//        xShift.println()
//        yShift.println()
        val shoes1 = xVals.zip(yShift).map { (x, y) -> x * y}.sum()
        val shoes2 = yVals.zip(xShift).map { (x, y) -> x * y}.sum()

        return (shoes1 - shoes2).absoluteValue / 2
    }

    fun part1(input: List<String>): Long{
        val processed = input.map { it.split(" ") }
        val directions = processed.map { it[0][0] }
        val sideLengths = processed.map { it[1].toLong() }
//        sideLengths.println()
//        directions.println()

        val vertices = getVertices(sideLengths,directions)
//        vertices.println()

        val inside = shoelace(vertices)
        val perim = sideLengths.sum()
        (inside.toString() + " " + perim.toString()).println()
        return inside + (perim/2) + 1
    }

    fun convertToDir(char: Char): Char{
        when(char){
            '0' -> return 'R'
            '1' -> return 'D'
            '2' -> return 'L'
            '3' -> return 'U'
        }
        return 'X'
    }

    fun part2(input: List<String>): Long{
        val hexDecs = input.map {it.split(" ")[2]}
        val sideLengths = hexDecs.map { it.substring(2,7).toLong(radix = 16) }
        val directions = hexDecs.map { convertToDir(it[7]) }

        val vertices = getVertices(sideLengths,directions)
        vertices.println()

        val inside = shoelace(vertices)
        val perim = sideLengths.sum()
        return inside + (perim/2) + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    part1(testInput).println()
    part2(testInput).println()

    val test2Input = readInput("Day18_test2")
    part1(test2Input).println()

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

