import java.util.PriorityQueue

fun main() {

    fun getCost(row: Int, col: Int, grid: List<List<Int>>): Int{
        if (row in (0..grid.size-1) && col in (0..grid[0].size-1)){
            return grid[row][col]
        }
        return 99
    }

    fun addVerts(start: Int, end: Int, row: Int, col: Int, initCost: Int, prioQ: PriorityQueue<Pair<Triple<Int,Int,Char>,Int>>, grid: List<List<Int>>){
        var costUp = initCost
        var costDown = initCost

        for (i in (1..end)){
            costUp += getCost(row-i, col, grid)
            costDown += getCost(row+i, col, grid)
            if (i >= start){
                prioQ.add(Pair(Triple(row-i, col, 'U'), costUp))
                prioQ.add(Pair(Triple(row+i, col, 'D'), costDown))
            }
        }
    }
    fun addHoris(start: Int, end: Int, row: Int, col: Int, initCost: Int, prioQ: PriorityQueue<Pair<Triple<Int,Int,Char>,Int>>, grid: List<List<Int>>){
        var costLeft = initCost
        var costRight = initCost
        for (i in (1..end)){
            costLeft += getCost(row, col-i, grid)
            costRight += getCost(row, col+i, grid)
            if (i >= start) {
                prioQ.add(Pair(Triple(row, col-i, 'L'), costLeft))
                prioQ.add(Pair(Triple(row, col+i, 'R'), costRight))
            }
        }
    }

    fun part1(input: List<String>, minTurn: Int, maxTurn:Int): Int {
        val toVisit: PriorityQueue<Pair<Triple<Int,Int,Char>,Int>> = PriorityQueue(100, Comparator.comparing(Pair<Triple<Int,Int,Char>,Int>::second))
        val visited : MutableSet<Triple<Int,Int,Char>> = mutableSetOf()

        val intGrid = input.map { it.toCharArray().map { inner-> inner.digitToIntOrNull() }.filterNotNull() }
        val goalRow = intGrid.size-1
        val goalCol = intGrid[0].size-1
        toVisit.add(Pair(Triple(0,0,'X'),0))

        while(toVisit.isNotEmpty()){
            val currentNode = toVisit.remove()
            val row = currentNode.first.first
            val col = currentNode.first.second
            val cost = currentNode.second
            if (row == goalRow && col == goalCol) return cost
            if (visited.contains(currentNode.first)) continue
            visited.add(currentNode.first)
            //Add all possible neighbours
            val lastDir = currentNode.first.third
            if (lastDir != 'U' && lastDir != 'D') {
                addVerts(minTurn,maxTurn, row, col, cost, toVisit, intGrid)
            }
            if (lastDir != 'L' && lastDir != 'R'){
                addHoris(minTurn,maxTurn, row, col, cost, toVisit, intGrid)
            }
        }


        return 5
    }

    fun part2(input: List<String>): Int {
        val toVisit: PriorityQueue<Pair<Triple<Int,Int,Char>,Int>> = PriorityQueue(100, Comparator.comparing(Pair<Triple<Int,Int,Char>,Int>::second))
        val visited : MutableSet<Triple<Int,Int,Char>> = mutableSetOf()


        val intGrid = input.map { it.toCharArray().map { inner-> inner.digitToIntOrNull() }.filterNotNull() }
        val goalRow = intGrid.size-1
        val goalCol = intGrid[0].size-1
        toVisit.add(Pair(Triple(0,0,'X'),0))

        while(toVisit.isNotEmpty()){
            val currentNode = toVisit.remove()
            val row = currentNode.first.first
            val col = currentNode.first.second
            val cost = currentNode.second
            if (row == goalRow && col == goalCol) return cost
            if (visited.contains(currentNode.first)) continue
            visited.add(currentNode.first)
            //Add all possible neighbours
            val lastDir = currentNode.first.third
            if (lastDir != 'U' && lastDir != 'D') {
                var costUp = cost + getCost(row - 1, col, intGrid)
                toVisit.add(Pair(Triple(row - 1, col, 'U'), costUp))
                costUp += getCost(row - 2, col, intGrid)
                toVisit.add(Pair(Triple(row - 2, col, 'U'), costUp))
                costUp += getCost(row - 3, col, intGrid)
                toVisit.add(Pair(Triple(row - 3, col, 'U'), costUp))
                var costDown = cost + getCost(row + 1, col, intGrid)
                toVisit.add(Pair(Triple(row + 1, col, 'D'), costDown))
                costDown += getCost(row + 2, col, intGrid)
                toVisit.add(Pair(Triple(row + 2, col, 'D'), costDown))
                costDown += getCost(row + 3, col, intGrid)
                toVisit.add(Pair(Triple(row + 3, col, 'D'), costDown))
            }
            if (lastDir != 'L' && lastDir != 'R'){
                var costLeft = cost + getCost(row,col-1, intGrid)
                toVisit.add(Pair(Triple(row,col-1, 'L'),costLeft ))
                costLeft += getCost(row,col-2, intGrid)
                toVisit.add(Pair(Triple(row,col-2, 'L'),costLeft ))
                costLeft += getCost(row,col-3, intGrid)
                toVisit.add(Pair(Triple(row,col-3, 'L'),costLeft ))
                var costRight = cost + getCost(row,col+1, intGrid)
                toVisit.add(Pair(Triple(row,col+1, 'R'),costRight ))
                costRight += getCost(row,col+2, intGrid)
                toVisit.add(Pair(Triple(row,col+2, 'R'),costRight ))
                costRight += getCost(row,col+3, intGrid)
                toVisit.add(Pair(Triple(row,col+3, 'R'),costRight ))
            }
        }


        return 5
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    part1(testInput,1,3).println()
    part1(testInput,4,10).println()
    val test2Input = readInput("Day17_test2")
    part1(test2Input,4,10).println()


    val input = readInput("Day17")
    part1(input,1,3).println()
    part1(input,4,10).println()
}

