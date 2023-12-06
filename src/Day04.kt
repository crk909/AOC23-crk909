import kotlin.math.pow

fun main() {
    fun getMatches(cardNums: List<Int>, winningNums: List<Int>): Int {
        val numMatches = cardNums.map { num -> num in winningNums}.filter { it }.size
        return numMatches
    }

    fun getPoints(winningNums: List<Int>, cardNums:List<Int>): Int{
        val numMatches = getMatches(cardNums, winningNums)
        if (numMatches == 0) return 0
        return 2.0.pow(numMatches-1).toInt()

    }
    fun part1(input: List<String>): Int {
        val removeGame = input.map {it.split(":").drop(1)}.flatten()
        val matchesRaw = removeGame.map {it.split("|").take(1)}.flatten()
        val numbersRaw = removeGame.map {it.split("|").drop(1)}.flatten()
        val matches = matchesRaw.map{it.split(" ").map{ num -> num.toIntOrNull()}.filterNotNull()}
        val numbers = numbersRaw.map { it.split(" ").map{ num -> num.toIntOrNull()}.filterNotNull()}
        val together = matches.zip(numbers).map {(mats, nums) -> getPoints(mats,nums)}
//        matches.println()
//        numbers.println()
//        together.println()
        return together.sum()
    }

    fun incrementCards(cardNum: Int, numMatch: Int, cardsArray:MutableList<Int>) {
        var howMany = cardsArray[cardNum]
        var list = (cardNum .. cardNum+numMatch).toList().drop(1)

        list.forEach { index ->
            cardsArray[index] += howMany
        }
    }

    fun part2(input: List<String>): Int {
        var numCards = input.map {1}.toMutableList()
        val removeGame = input.map {it.split(":").drop(1)}.flatten()
        val matchesRaw = removeGame.map {it.split("|").take(1)}.flatten()
        val numbersRaw = removeGame.map {it.split("|").drop(1)}.flatten()
        val matches = matchesRaw.map{it.split(" ").map{ num -> num.toIntOrNull()}.filterNotNull()}
        val numbers = numbersRaw.map { it.split(" ").map{ num -> num.toIntOrNull()}.filterNotNull()}
        val together = matches.zip(numbers).map {(mats, nums) -> getMatches(mats,nums)}
        together.println()
        numCards.println()

        together.forEachIndexed {cardNum, numMatch ->
            incrementCards(cardNum, numMatch, numCards)
        }
        numCards.println()

        return numCards.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val test2Input = readInput("Day04_test")
    check(part2(test2Input) == 30)


    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

