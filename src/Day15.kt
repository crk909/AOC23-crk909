fun main() {

    fun doStep(char: Char, cur: Int): Int{
        val afterAdd = cur + char.code
        val afterMult = afterAdd * 17
        return afterMult % 256
    }

    fun convertCode(code: String): Int{
        var solution = 0
        code.forEach { solution = doStep(it,solution) }
        return solution
    }


    fun part1(input: List<String>): Int {
        val split = input[0].split(",")
//        split.map { it.println() }
        val converted = split.map { convertCode(it) }
        converted.println()
        return converted.sum()
    }

    fun part2(input: List<String>): Int {
        val split = input[0].split(",")
        val codes = split.map { it.filter { c -> c.isLetter() } }
        codes.println()
        val operations = split.map { it.filter { c -> c == '=' || c == '-' } }
        operations.println()
        val lensPowersUn = split.map { it.filter { c -> c.isDigit() } }
        val lensPowers = lensPowersUn.map {it.replace("[^0-9]".toRegex(), "").toIntOrNull()}
        lensPowers.println()

        var lensMap : LinkedHashMap<Int, LinkedHashMap<String, Int>> = linkedMapOf()

        for (num in (0..255)){
            lensMap[num] = linkedMapOf()
        }

        for (i in split.indices){
            val code = codes[i]
            val codeIdx = convertCode(code)
            val op = operations[i]
            var lens = lensPowers[i]
            var map = lensMap.getOrDefault(codeIdx, linkedMapOf())
            if (lens == null) {
                map.remove(code)
            }
            else {
                map.put(code, lens)
            }
            lensMap.println()
        }
        lensMap.filter { it.value.size != 0 }.println()

        var lensPower = 0
        for (i in (0..255)){
            val boxContents = lensMap.getOrDefault(i, linkedMapOf())
            if (boxContents.isNotEmpty()){
                boxContents.values.onEachIndexed{slot, focalLength ->
                    lensPower += (i+1) * (slot+1) * focalLength
                }
            }
        }
        return lensPower
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    part1(testInput).println()

    val input = readInput("Day15")
    part1(input).println()

    part2(testInput).println()

    part2(input).println()
}

