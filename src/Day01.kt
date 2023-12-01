fun main() {
    fun part1(input: List<String>): Int {
        val numerics = "[^0-9]".toRegex()
        val noAlpha = input.map { it.replace(numerics, "")}
        val firstAndLast = noAlpha.map { it.first().toString().plus(it.last().toString()).toInt()}
        return firstAndLast.reduce { acc,next -> acc + next}
    }

    fun part2(input: List<String>): Int {
        val numerics = "[^0-9]".toRegex()
        //This replace is bad, should improve once I learn a better way
        //There must be some way to have a map where one -> 1 (well o1e) I guess, this isn't that efficient
        val replaced = input.map { it.replace("one","o1e").replace("two","t2o").replace("three","t3e").replace("four","f4r").replace("five","f5e").replace("six","s6x").replace("seven","s7n").replace("eight","e8t").replace("nine","n9e").replace(numerics,"")}
        val firstAndLast = replaced.map { it.first().toString().plus(it.last().toString()).toInt()}

            //output for visualization
//        input.println()
//        replaced.println()
//        firstAndLast.println()

        return firstAndLast.reduce { acc,next -> acc + next}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val test2Input = readInput("Day01_test2")
    check(part2(test2Input) == 281)


    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
