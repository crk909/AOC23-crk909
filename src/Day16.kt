import kotlin.math.max

fun main() {
    fun printGrid(grid: List<CharArray>){
        grid.map { it.joinToString("").println() }
    }

    fun handleEmpty(dir: Char, loc: Pair<Int,Int>, beams: MutableList<Triple<Int,Int,Char>>){
        when (dir){
            'L' -> beams.addLast(Triple(loc.first,loc.second-1, dir))
            'R' -> beams.addLast(Triple(loc.first,loc.second+1, dir))
            'U' -> beams.addLast(Triple(loc.first-1,loc.second, dir))
            'D' -> beams.addLast(Triple(loc.first+1,loc.second, dir))
        }
    }
    fun handleHorSplit(dir: Char, loc: Pair<Int,Int>, beams: MutableList<Triple<Int,Int,Char>>){
        if (dir == 'L' || dir == 'R') handleEmpty(dir, loc, beams)
        else{
            beams.addLast(Triple(loc.first,loc.second+1,'R'))
            beams.addLast(Triple(loc.first,loc.second-1,'L'))
        }
    }
    fun handleVerSplit(dir: Char, loc: Pair<Int,Int>, beams: MutableList<Triple<Int,Int,Char>>){
        if (dir == 'U' || dir == 'D') handleEmpty(dir, loc, beams)
        else{
            beams.addLast(Triple(loc.first-1, loc.second, 'U'))
            beams.addLast(Triple(loc.first+1, loc.second, 'D'))
        }
    }
    fun handleMirrorL(dir: Char, loc: Pair<Int,Int>, beams: MutableList<Triple<Int,Int,Char>>){
        when(dir){
            'R' -> beams.addLast(Triple(loc.first+1,loc.second,'D'))
            'L' -> beams.addLast(Triple(loc.first-1,loc.second,'U'))
            'U' -> beams.addLast(Triple(loc.first,loc.second-1,'L'))
            'D' -> beams.addLast(Triple(loc.first,loc.second+1,'R'))
        }
    }
    fun handleMirrorR(dir: Char, loc: Pair<Int,Int>, beams: MutableList<Triple<Int,Int,Char>>){
        when(dir){
            'L' -> beams.addLast(Triple(loc.first+1,loc.second,'D'))
            'R' -> beams.addLast(Triple(loc.first-1,loc.second,'U'))
            'D' -> beams.addLast(Triple(loc.first,loc.second-1,'L'))
            'U' -> beams.addLast(Triple(loc.first,loc.second+1,'R'))
        }
    }

    fun locationInvalid(loc: Pair<Int,Int>, h: Int, v:Int ): Boolean{
        return (!(loc.first in (0..v-1) && loc.second  in (0..h-1)) )
    }


    fun part1(input: List<String>, startPoint: Triple<Int,Int, Char>): Int {
        val charGrid :MutableList<CharArray> = input.map { it.toCharArray()}.toMutableList()
        val gridHori = charGrid[0].size
        val gridVert = charGrid.size

        var activeBeams : MutableList<Triple<Int,Int,Char>> = mutableListOf(startPoint)
        //hashmaps containing the locations that have been entered from each direction
        var history: HashMap<Char, MutableSet<Pair<Int,Int>>> = hashMapOf()
        history['D'] = mutableSetOf()
        history['U'] = mutableSetOf()
        history['L'] = mutableSetOf()
        history['R'] = mutableSetOf()

        while(activeBeams.isNotEmpty()){
            val exploreNext = activeBeams[0]
            activeBeams.removeFirst()

            val location : Pair<Int,Int> = Pair(exploreNext.first, exploreNext.second)
            if(locationInvalid(location, gridHori, gridVert)) continue
            val direction = exploreNext.third
            if (history.getOrDefault(direction, setOf()).contains(location)) continue
            history.getOrDefault(direction, mutableSetOf()).add(location)
            val charHere = charGrid[location.first][location.second]

            when(charHere) {
                '-' -> handleHorSplit(direction, location, activeBeams)
                '|' -> handleVerSplit(direction, location, activeBeams)
                '.' -> handleEmpty(direction, location, activeBeams)
                '/' -> handleMirrorR(direction, location, activeBeams)
                '\\' -> handleMirrorL(direction, location, activeBeams)
            }
        }
        var combineSet: MutableSet<Pair<Int,Int>> = mutableSetOf()
        for (key in history.keys) {
            combineSet.addAll(history.getOrDefault(key, mutableSetOf()))
        }
        return combineSet.size
    }

    fun part2(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length

        var bestResult = 0
        for(row in (0..rows-1)){
            bestResult = max(bestResult, part1(input, Triple(row,0,'R')))
            bestResult = max(bestResult, part1(input, Triple(row,cols-1,'L')))
        }
        for(col in (0..cols-1)){
            bestResult = max(bestResult, part1(input, Triple(0, col, 'D')))
            bestResult = max(bestResult, part1(input, Triple(rows-1, col, 'U')))
        }
        return bestResult
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    part1(testInput, Triple(0,0,'R')).println()
    part2(testInput).println()

//    val test2Input = readInput("Day03_test2")


    val input = readInput("Day16")
//    part1(input, Triple(0,0,'R')).println()
//    part2(input)
    part2(input).println()
}

