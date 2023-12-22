import java.util.LinkedList
import java.util.Queue

fun main() {
    fun continueFlows(flows: List<String>): Boolean{
//        return flows[0] == "in"
        return (!flows.all { it == "R" || it == "A" })
    }

    fun chooseXmas(char: Char): Int{
        when(char){
            'x' -> return 0
            'm' -> return 1
            'a' -> return 2
            's' -> return 3
        }
        return 0
    }

    fun advanceFlow(xmas: List<Int>, flow: List<List<String>>): String{
        val noMatch = flow.last()[0]
        val validFlows = flow.dropLast(1)
        validFlows.forEach {
            val compValue = xmas[chooseXmas(it[0][0])]
            val comparitor = it[0][1]
            val toBeat = it[0].replace("[^0-9]".toRegex(), "").toInt()
            if (comparitor == '>' && compValue > toBeat) return it[1]
            if (comparitor == '<' && compValue < toBeat) return it[1]
        }
        return noMatch
    }

    fun part1(input: List<String>): Int {
        val proc1 = input.joinToString("/").split("//")
        val workflows = proc1[0].split("/")
//        workflows.println()
        val items = proc1[1].split("/")
        val xmasses = items.map { it.split(",").map { word -> word.replace("[^0-9]".toRegex(), "").toInt() } }

        val flowNames = workflows.map { it.takeWhile { it != '{' } }
        val flowConds = workflows.map { it.dropWhile { it != '{' }.drop(1).dropLast(1).split(",") }
//        flowConds.println()
        val flowsProcessed = flowConds.map {
            it.map { cond ->
                cond.split(":")
            }
        }
        var nextFlows = xmasses.map { "in" }
        xmasses.println()
        flowNames.println()
        flowsProcessed.println()
        "   ".println()

        while(continueFlows(nextFlows)){
            nextFlows = nextFlows.mapIndexed{ i, _ ->
                val flow = nextFlows[i]
                if (flow == "A" || flow == "R") flow
                else{
                    val xmas = xmasses[i]
                    val whichFlow = flowsProcessed[flowNames.indexOf(flow)]
                    advanceFlow(xmas, whichFlow)
                }
            }
            nextFlows.println()
        }

        return nextFlows.zip(xmasses).filter { (f,x) -> f == "A" }.map { it.second.sum() }.sum()
    }

    fun splitRanges(array: Array<IntRange>, splitVal: Int, splitDex: Int, comp: Char): Pair<Array<IntRange>, Array<IntRange>>{
        val specific = array[splitDex]
        val high = specific.last
        val low = specific.first
        if (low > splitVal) return Pair(Array(4){(10..0)}, array)
        if (high < splitVal) return Pair(array, Array(4){(10..0)})

        var lowRange = (low..splitVal)
        var highRange = (splitVal..high)
        if (comp == '<') lowRange = (low..splitVal-1)
        if (comp == '>') highRange = (splitVal+1..high)


        var arrayL = array.clone()
        var arrayR = array.clone()
        arrayL[splitDex] = lowRange
        arrayR[splitDex] = highRange
        return Pair(arrayL, arrayR)
    }

    fun arrayProduct(array: Array<IntRange>): Long{
        val sizes: List<Long> = array.map { (it.last - it.first + 1 ).toLong() }
        if (sizes.min() < 1) return 0
        return sizes.reduce {acc, i -> acc * i}
    }

    fun handleFlowRange(ranges: Array<IntRange>, flow: List<List<String>>, xmasQ: LinkedList<Pair<Array<IntRange>,String>>){
        val noMatch = flow.last()[0]
        val validFlows = flow.dropLast(1)
        var toSort = ranges
        validFlows.forEach {
            val comparitor = it[0][1]
            val toBeat = it[0].replace("[^0-9]".toRegex(), "").toInt()
            val arrIndex = chooseXmas(it[0][0])
            val arrayPair = splitRanges(toSort, toBeat, arrIndex, comparitor)
//            arrayPair.println()
            if (comparitor == '<'){
                xmasQ.add(Pair(arrayPair.first, it[1]))
                toSort = arrayPair.second
            }
            if (comparitor == '>'){
                xmasQ.add(Pair(arrayPair.second, it[1]))
                toSort = arrayPair.first
            }
        }
        xmasQ.add(Pair(toSort, noMatch))
    }

    fun part2(input: List<String>): Long {
        val proc1 = input.joinToString("/").split("//")
        val workflows = proc1[0].split("/")
        val flowNames = workflows.map { it.takeWhile { it != '{' } }
        val flowConds = workflows.map { it.dropWhile { it != '{' }.drop(1).dropLast(1).split(",") }
        val flowsProcessed = flowConds.map {
            it.map { cond ->
                cond.split(":")
            }
        }
        flowNames.println()
        flowsProcessed.println()
        "   ".println()
        val xmasQ: LinkedList<Pair<Array<IntRange>,String>> = LinkedList<Pair<Array<IntRange>,String>>()
        xmasQ.add(Pair(Array(4){(1..4000)}, "in"))
        val finished: LinkedList<Pair<Array<IntRange>,String>> = LinkedList<Pair<Array<IntRange>,String>>()

        while (xmasQ.isNotEmpty()){
            val popped = xmasQ.pop()
            val ranges = popped.first
            val flowN = popped.second
//            (ranges.map { it.toString() }.toList().toString() + "   " + flowN ).println()
            if (arrayProduct(ranges) == 0.toLong()) continue
            if ( flowN == "A" || flowN == "R" ){
                finished.add(popped)
                continue
            }
            val useFlow = flowsProcessed[flowNames.indexOf(flowN)]
            handleFlowRange(ranges, useFlow, xmasQ)
        }

        finished.filter { it.second == "A" }.map { arrayProduct(it.first) }.println()
        return finished.filter { it.second == "A" }.map { arrayProduct(it.first) }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
//    part1(testInput).println()
//    part2(testInput).println()
//    val test2Input = readInput("Day03_test2")


    val input = readInput("Day19")
//    part1(input).println()
    part2(input).println()
}

