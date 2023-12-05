fun main() {

    /** Simply takes in each color pulled and returns True if possible*/
    fun checkOverLimit(x: String, y:String ):Boolean {
        val count = x.toIntOrNull()
        if (count == null) return true

        when(y) {
            "blue" -> return(count <= 14)
            "red" -> return(count <= 12)
            "green" -> return(count <= 13)
        }
        return true
    }

    fun part1(input: List<String>): Int {
        //Split by delimiters, creating sublists like [3, blue],[4, red] etc..
        val inputList = input.map { it.split(":",",",";").map { ir -> ir.removeRange(0,1).split(" ") }}
        
        //Converts sublists to True or False
        val gameAllowedList = inputList.map { game -> game.map { (num, color) -> checkOverLimit(num, color)} }
        
        //Converts each game to True if every value True returned true
        val gameAllowed = gameAllowedList.map { it -> it.all { wis -> wis } }
        
        //Replace True with game number and False with 0
        val vals = gameAllowed.mapIndexed() { index, element ->
            if(element) index + 1;
            else 0
        }
        return vals.sum()
    }

    /** Sorts into lists corresponding to colors, then gets max of each and returns product of all 3*/
    fun powerSum(game: List<List<String>>): Int {
        val reds = game.filter { (_, color) -> color == "red" }.map { (value, _) -> value.toInt() }
        val blues = game.filter { (_,color) -> color == "blue" }.map { (value, _) -> value.toInt() }
        val greens = game.filter { (_, color) -> color == "green" }.map { (value, _) -> value.toInt() }
        return reds.max() * greens.max() * blues.max()
    }

    fun part2(input: List<String>): Int {
        val inputList = input.map { it.split(":",",",";").map { ir -> ir.removeRange(0,1).split(" ") }}
        //Removes first sublist from each since it is [Game, X] and not needed
        val removeGame = inputList.map { it -> it.drop(1) }

        //
        val summies = removeGame.map { it -> powerSum(it) }
        return summies.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val test2Input = readInput("Day02_test2")
    check(part2(test2Input) == 2286)


    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
