import java.util.LinkedList
import java.util.Queue

fun main() {
    var toVisitQueue : Queue<Pair<Pair<Int,Int>,Int>> = LinkedList()
    var splitArray : List<List<String>> = listOf(listOf())

    fun connectsTo(pipe: Char, direction: Char): Boolean{
        if (pipe == 'S') return true
        when(direction){
            'A' -> return pipe in listOf('|', 'L','J')
            'B' -> return pipe in listOf('|','7','F')
            'L' -> return pipe in listOf('-','J','7')
            'R' -> return pipe in listOf('-','L','F')
        }
        return true
    }


    fun tryAdd(i: Int, j: Int, rowDim:Int, colDim: Int, cost: Int, approachDirection: Char){
        //Check connects from direction
        if(i in 0..rowDim && j in 0..colDim){
            if (connectsTo(splitArray[i][j][0], approachDirection)) toVisitQueue.add(Pair(Pair(i,j), cost))
        }
    }

    fun initializeQueue(row: Int, col:Int, rowDim: Int, colDim:Int, cost:Int){
        tryAdd(row-1, col, rowDim, colDim, cost, 'B')
        tryAdd(row, col+1, rowDim, colDim, cost, 'L')
        tryAdd(row+1, col, rowDim, colDim, cost, 'A')
        tryAdd(row, col-1, rowDim, colDim, cost, 'R')
    }

    fun visitNode(row: Int, col: Int, pipe: Char, rowDim:Int, colDim:Int, cost: Int){
        //Expansion
        when(pipe){
            'S' -> {
                initializeQueue(row, col,rowDim, colDim, cost+1)
            }
            '|' -> {
                tryAdd(row+1,col,rowDim,colDim,cost+1, 'A')
                tryAdd(row-1, col, rowDim,colDim, cost+1, 'B')
            }
            '-' -> {
                tryAdd(row,col+1,rowDim,colDim,cost+1, 'L')
                tryAdd(row,col-1,rowDim,colDim, cost+1, 'R')
            }
            'L' -> {
                tryAdd(row-1,col,rowDim,colDim, cost+1, 'B')
                tryAdd(row, col+1, rowDim,colDim, cost+1, 'L')
            }
            'J' -> {
                tryAdd(row-1,col,rowDim,colDim,cost+1, 'B')
                tryAdd(row,col-1, rowDim,colDim, cost+1, 'R')
            }
            '7' -> {
                tryAdd(row,col-1, rowDim,colDim, cost+1, 'R')
                tryAdd(row+1, col, rowDim,colDim, cost+1, 'A')
            }
            'F' -> {
                tryAdd(row+1, col, rowDim,colDim, cost+1, 'A')
                tryAdd(row, col+1, rowDim,colDim, cost+1, 'L')
            }
        }
    }

    fun part1Mod(input: List<String>):Set<Pair<Int,Int>>{
//    fun part1(input: List<String>): Int {
        var visitCostMap = hashMapOf<Pair<Int,Int>,Int>()
        splitArray = input.map { it.split("").filter { it.length > 0 } }

        val rowDimensionMax = splitArray.size-1
        val colDimensionMax = splitArray[0].size-1
        for(i in (0..rowDimensionMax)){
            for(j  in (0..colDimensionMax)){
                if(splitArray[i][j].equals("S")) {
                    tryAdd(i,j,rowDimensionMax,colDimensionMax, 0, 'X')
                }
            }
        }

        while(toVisitQueue.isNotEmpty()){
//            toVisitQueue.println()
            val nextVisit = toVisitQueue.remove()
//            ("visit: "+nextVisit.toString()).println()
            val row = nextVisit.first.first
            val col = nextVisit.first.second
            val visitCost = nextVisit.second
            val highCost = visitCostMap.getOrDefault(Pair(row,col),99999)
            val pipeChar = splitArray[row][col]
            if(visitCost < highCost){
                visitCostMap[Pair(row,col)] = visitCost
                visitNode(row,col,pipeChar[0],rowDimensionMax, colDimensionMax, visitCost)
            }
        }

//        splitArray.println()
//        visitCostMap.println()
//        return visitCostMap.map { it.value }.max()
        return visitCostMap.keys
    }

    fun convertToEdge(bigArray: Array<Array<String>>): Array<Array<String>>{
        var anyChanges = true
        while (anyChanges){
            anyChanges = false
            bigArray.mapIndexed {rowNum, row ->
                row.mapIndexed{colNum, char ->
                    if (char == "o" || char == "X"){
                        val neighbours = listOf(bigArray[rowNum-1][colNum],bigArray[rowNum+1][colNum],bigArray[rowNum][colNum+1],bigArray[rowNum][colNum-1])
                        if ("O" in neighbours) {
                            bigArray[rowNum][colNum] = "O"
                            anyChanges = true
                        }
                    }
                }
            }
        }
        return bigArray
    }

    fun part2(input: List<String>): Int {
        val inLoop = part1Mod(input)
        inLoop.println()
        val rowDimension = splitArray.size
        val colDimension = splitArray[0].size
        splitArray.map { it.println() }
        var wideArray = Array(rowDimension) {Array(colDimension*2+1){"o"} }
        "----------------".println()
        splitArray.mapIndexed {rowNum, row ->
            row.mapIndexed {colNum, char ->
                if(inLoop.contains(Pair(rowNum,colNum))) wideArray[rowNum][(colNum+1)*2-1] = char
                else wideArray[rowNum][(colNum+1)*2-1] = "X"
            }
        }

        wideArray.mapIndexed {rowNum,row ->
            row.mapIndexed{colNum,char ->
                if (char == "o" && colNum !=0 && colNum != colDimension *2) {
                    val leftConnect = connectsTo(wideArray[rowNum][colNum-1][0], 'R')
                    val rightConnect = connectsTo(wideArray[rowNum][colNum+1][0],'L')
                    if (leftConnect && rightConnect) wideArray[rowNum][colNum] = "-"
                }
            }
        }
        wideArray.map { it.joinToString(", ").println() }
        "----------------".println()
        var tallArray = Array(rowDimension*2+1) {Array(colDimension*2+1){"o"} }
        wideArray.mapIndexed {rowNum, row ->
            row.mapIndexed {colNum, char ->
                tallArray[(rowNum+1)*2-1][colNum] = char
            }
        }
        tallArray.mapIndexed {rowNum,row ->
            row.mapIndexed{colNum,char ->
                if (char == "o" && rowNum !=0 && rowNum != rowDimension*2) {
                    val downConnect = connectsTo(tallArray[rowNum-1][colNum][0], 'B')
                    val upConnect = connectsTo(tallArray[rowNum+1][colNum][0],'A')
                    if (downConnect && upConnect) tallArray[rowNum][colNum] = "|"
                }
                if (rowNum == 0 || rowNum == rowDimension*2 || colNum == 0 || colNum == colDimension*2){
                    tallArray[rowNum][colNum] = "O"
                }
            }
        }

        tallArray.map { it.joinToString(", ").println() }
        "----------------".println()
        tallArray = convertToEdge(tallArray)

        tallArray.map { it.joinToString(", ").println() }
        "--------------333--".println()

        tallArray.map { it.joinToString("") }.joinToString("").replace("[^X]".toRegex(),"").println()
        return tallArray.map { it.joinToString("") }.joinToString("").replace("[^X]".toRegex(),"").length
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
//    part1(testInput).println()
//    part1(testInput2).println()

//    part2(testInput4).println()


    val input = readInput("Day10")
//    part1(input).println()
    part2(input).println()
}

