fun main() {
    fun part1(input: List<String>): Int {

        return 5
    }

    fun part2(input: List<String>): Int {
        return 5
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    part1(testInput).println()
    check(part1(testInput) == 142)

//    val test2Input = readInput("Day03_test2")
//    check(part2(test2Input) == 281)


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

