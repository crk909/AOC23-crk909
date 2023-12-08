import java.util.Objects
import java.util.concurrent.CountDownLatch

fun main() {
    fun countTimes (char: Char, str: String): Int {
        return str.toCharArray().filter { it.equals(char) }.count()
    }

    fun convertToChars(hand: String): List<Char> {
        val uniques = hand.toCharArray().toSet().toList()
        return uniques
    }

    fun getCounts(hand: String, chars: List<Char>): List<Int>{
        return chars.map { countTimes(it, hand) }
    }

    fun getHandType(chars: List<Char>, counts: List<Int>): String {
        val handSize = chars.size;
        if (handSize == 5) return "" //High Card
        if (handSize == 4) return "1" //Pair
        if (counts.max() == 2) return "2" //2 Pair
        if (counts.min() == 2) return "4" //Full House
        if (counts.max() == 3) return "3" //Three of a Kind
        if (counts.max() == 4) return "5" //Four of a Kind
        return "6" //FIVE of a Kind
    }

    fun getHandTypeJoker(chars: List<Char>, counts: List<Int>, jokers: Int): String{
        if (jokers == 5) return "6"
        val bestCombo = jokers + counts.max()
        if (bestCombo == 5) return "6" //FIVE of a Kind
        if (bestCombo == 4) return "5" //Four of a Kind
        if (bestCombo == 3) {
            if (counts.min() == 2) return "4" //Full House
            return "3" //3 of a Kind
        }
        if (bestCombo == 1) return "" //High Card
        if (counts.max() == 2 && counts.size == 3 && jokers == 0) return "2" //Two Pair (If any Jokers 2 pair is impossible, 3 of a Kind is just better)
        return "1" //One Pair

    }


    val charToIntMap = mapOf<Char,String>('2' to "02", '3' to "03", '4' to "04", '5' to "05", '6' to "06", '7' to "07", '8' to "08", '9' to "09", 'T' to "10", 'J' to "11", 'Q' to "12", 'K' to "13", 'A' to "14")
    val charToIntMapJoker = mapOf<Char,String>('2' to "02", '3' to "03", '4' to "04", '5' to "05", '6' to "06", '7' to "07", '8' to "08", '9' to "09", 'T' to "10", 'J' to "01", 'Q' to "12", 'K' to "13", 'A' to "14")
    fun convertChar(char: Char): String {
        val attempt = charToIntMap.get(char)
        return attempt.toString() //Does nothing but Kotlin isn't convinced it would always be string otherwise
    }
    fun convertCharJoker(char: Char):String {
        val attempt = charToIntMapJoker.get(char)
        return attempt.toString()
    }

//    fun handSort(chars: List<Char>, counts: List<Int>): Long {
////        "HANDSORT".println()
////        chars.println()
////        counts.println()
//        val handType = getHandType(chars, counts)
//        val charInts = chars.zip(counts).map {(char, count) -> convertChar(char).repeat(count).toInt()}
////        charInts.println()
//        val sortedChars = charInts.sortedDescending().map { it.toString() }
//        val sortedCharsWithZero = sortedChars.map {
//            if (it.length%2 == 1) "0" + it
//            else it
//        }
////        sortedCharsWithZero.println()

//        return (handType.toString() + sortedCharsWithZero.joinToString("")).toLong()
//    }

    fun handConvert(hand: String, type: String): String{
        val handString = hand.toCharArray().map { convertChar(it) }.joinToString("")
        return type + handString
    }

    fun handConvertJoker(hand: String, type: String): String{
        val handString = hand.toCharArray().map { convertCharJoker(it) }.joinToString("")
        return type + handString
    }

    fun part1(input: List<String>): Int {
        val splitInput = input.map { it.split(" ") }
        val hands = splitInput.map { it[0] }
        val bets = splitInput.map { it[1] }
        val handChars = hands.map { convertToChars(it) }
        val handCounts = hands.zip(handChars).map { (hand, chars) -> getCounts(hand, chars) }

        val handTypes = handChars.zip(handCounts).map { (chars, counts) -> getHandType(chars, counts) }

        val handConverts = hands.zip(handTypes).map { (hand, type) -> handConvert(hand, type).toLong() }

        val betConvertPairs = bets.zip(handConverts).map { (bet, value) -> Pair<Int,Long>(bet.toInt(), value) }.sortedBy { it.second }
        val winnings = betConvertPairs.mapIndexed {index, betPair -> betPair.first * (index+1) }
        betConvertPairs.println()
        winnings.println()

        return winnings.sum()
    }

    fun part2(input: List<String>): Int {
        val splitInput = input.map { it.split(" ") }
        val hands = splitInput.map { it[0] }
        val bets = splitInput.map { it[1] }
        val handChars = hands.map { convertToChars(it.replace("J","")) }
        val jokers = hands.map { countTimes('J', it) }
        val handCounts = hands.zip(handChars).map { (hand, chars) -> getCounts(hand, chars) }
        val handTypes = handChars.mapIndexed {index, chars -> getHandTypeJoker(chars, handCounts[index], jokers[index] )}
        val handConverts = hands.zip(handTypes).map { (hand, type) -> handConvertJoker(hand, type).toLong() }

        hands.println()
        handChars.println()
        handCounts.println()
        jokers.println()
        handTypes.println()

        val betConvertPairs = bets.zip(handConverts).map { (bet, value) -> Pair<Int,Long>(bet.toInt(), value) }.sortedBy { it.second }
        val winnings = betConvertPairs.mapIndexed {index, betPair -> betPair.first * (index+1) }
        betConvertPairs.println()
        winnings.println()

        return winnings.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    part1(testInput).println()
//    check(part1(testInput) == 142)

//    val test2Input = readInput("Day03_test2")
//    check(part2(test2Input) == 281)
    part2(testInput).println()


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

