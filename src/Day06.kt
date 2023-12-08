import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    fun beatsRecord(time: Long, record: Long, charge: Long): Boolean {
        // 10 * (20) = 200
        return ((charge * (time-charge)) > record )
    }

    fun FindBoundaryTime(firstTime: Long, secondTime: Long, maxTime: Long, record: Long): Long{
        (firstTime.toString() + " " + secondTime.toString()).println()
        if (firstTime == secondTime) return firstTime;
        if ((firstTime - secondTime).absoluteValue == 1.toLong()) {
            if (beatsRecord(maxTime, record, firstTime)) return firstTime
            else return secondTime
        }
        val midTime = (firstTime + secondTime).floorDiv(2)
        if (beatsRecord(maxTime, record, midTime)) return FindBoundaryTime(firstTime, midTime, maxTime, record)
        return FindBoundaryTime(midTime,secondTime, maxTime, record)
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(" ").map { it.toLongOrNull() }.filterNotNull()
        times.println()
        val record = input[1].split(" ").map { it.toLongOrNull() }.filterNotNull()
        record.println()

        //Find lowest win
        val minBeat = times.zip(record).map { (time, rec) -> FindBoundaryTime(1, time, time, rec) }
        //Find highest win
        val maxBeat = times.zip(record).map { (time, rec) -> FindBoundaryTime(time, 1, time, rec) }
        minBeat.println()
        maxBeat.println()
        val numWins = minBeat.zip(maxBeat).map { (low,high) -> high-low+1 }
        numWins.println()

        return numWins.reduceRight { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val nonNumeric = "[^0-9]".toRegex()
        val time = input[0].replace(nonNumeric, "").toLong()
        val record = input[1].replace(nonNumeric, "").toLong()
        time.println()
        record.println()

        val min = FindBoundaryTime(1, time, time, record)
        val max = FindBoundaryTime(time, 1, time, record)
        min.println()
        max.println()

        return max - min + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    part1(testInput).println()
//    check(part1(testInput) == 142)

//    val test2Input = readInput("Day03_test2")
    part2(testInput).println()


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

