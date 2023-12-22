import java.util.LinkedList
import kotlin.math.sign

fun main() {
    class Switch(var type: Char, val name: String){
        var outs: List<String> = listOf()
        var inputs: MutableMap<String, Boolean> = mutableMapOf()
        var isOn: Boolean = false
        var lastOutput = "low"
    }

    fun createMap(splitInput: List<List<String>>): MutableMap<String, Switch> {
        val mapSet: MutableMap<String, Switch> = mutableMapOf()
        splitInput.map { (input, outputs) ->
            val type = input[0]
            val name = input.drop(1).dropLast(1)
            val newSwitch = Switch(type, name)
            newSwitch.outs = outputs.split(",").map { it.strip() }
            mapSet[name] = newSwitch
        }
        val inverters = mapSet.keys.filter {mapSet.getOrDefault(it, Switch('x',"x")).type == '&' }
        inverters.println()
        mapSet.keys.forEach {
            val outs: List<String> = mapSet.getOrDefault(it, Switch('x',"x")).outs
            for (out in outs){
                if (out in inverters) mapSet.getOrDefault(out, Switch('x',"x")).inputs[it] = false
            }
        }

        return mapSet
    }

    fun generateOutputs(volume: String, switch: Switch, sigList: LinkedList<Triple<String, String, String>>, origin: String): Pair<Int,Int>{
        var low = 0
        var high = 0
        when(switch.type){
            'x' -> return Pair(0,0)
            'b' -> {
                switch.outs.forEach {
                    low++
                    sigList.add(Triple("low", it, switch.name))
                }
            }
            '%' -> {
                if (volume == "high") return Pair(0,0)
                switch.isOn = !switch.isOn
                var newVolume = "low"
                if (switch.isOn) newVolume = "high"
                switch.outs.forEach {
                    if (newVolume == "high") high++
                    else low++
                    sigList.add(Triple(newVolume, it, switch.name))
                }
                switch.lastOutput = newVolume
            }
            '&' -> {
                switch.inputs[origin] = (volume == "high")
                var outVol = "high"
                if (switch.inputs.all { it.value }) {
                    outVol = "low"
                }
                switch.outs.forEach {
                    if (outVol == "high") high++
                    else low++
                    sigList.add(Triple(outVol, it, switch.name))
                }
                switch.lastOutput = outVol
            }
        }
        return Pair(low,high)
    }

    fun pressButton(switchList: MutableMap<String, Switch>, times: Int ): Pair<Long,Long> {
        var lows:Long = 0
        var highs:Long = 0
        var signalList: LinkedList<Triple<String, String, String>> = LinkedList()
        for (i in (1..times)) {
            signalList.add(Triple("low", "roadcaster", "button"))
            lows++

            while (signalList.isNotEmpty()) {
                val sig = signalList.pop()
                val newSigs = generateOutputs(sig.first, switchList.getOrDefault(sig.second, Switch('x', "x")), signalList, sig.third)
                lows += newSigs.first
                highs += newSigs.second
//                signalList.println()
            }
        }
//        (lows + highs).println()
        return Pair(lows,highs)
    }

    fun press4feeders(switchList: MutableMap<String, Switch>, targets: List<String>): List<String> {
        var targetsFound: MutableList<String> = mutableListOf()
        var signalList: LinkedList<Triple<String, String, String>> = LinkedList()
        signalList.add(Triple("low", "roadcaster", "button"))

        while (signalList.isNotEmpty()) {
            val sig = signalList.pop()
            if (sig.first == "high" && sig.third in targets) targetsFound.add(sig.third)
            generateOutputs(sig.first, switchList.getOrDefault(sig.second, Switch('x', "x")), signalList, sig.third)
        }

        return targetsFound
    }


    fun part1(input: List<String>): Long{
        val splitted = input.map { it.split("->")}

        val switchMap = createMap(splitted)
        switchMap.keys.forEach {
            val switch = switchMap.getOrDefault(it, Switch('x',"x"))
            (it + ": " + switch.type + "  " + switch.outs.toString() + switch.inputs.toString() + switch.isOn).println()

        }
        val sigCounts = pressButton(switchMap, 1000)

        return sigCounts.first * sigCounts.second
    }

    fun part2(input: List<String>): Long {
        val splitted = input.map { it.split("->")}

        val switchMap = createMap(splitted)
        switchMap["rx"] = Switch('%', "rx")
        switchMap.keys.forEach {
            val switch = switchMap.getOrDefault(it, Switch('x',"x"))
            (it + ": " + switch.type + "  " + switch.outs.toString() + switch.inputs.toString() + switch.isOn).println()

        }

        val mainSwitch = switchMap.filter { it.value.outs.contains("rx") }.keys.first()
        mainSwitch.println()
        var feeders = switchMap.getOrDefault(mainSwitch, Switch('x',"x")).inputs.keys
        feeders.println()
        val seen : MutableMap<String, Long> = mutableMapOf()
        feeders.forEach {seen[it] = 0 }

        //Rethink this something is clearly wrong...
        var pressCount = 1
        while(seen.values.contains(0)){
            val sigs = press4feeders(switchMap, feeders.toList())
            if (sigs.isNotEmpty()){
                sigs.forEach {
                    if(feeders.contains(it)) {
                        feeders.remove(it)
                        seen[it] = pressCount.toLong()
                        (pressCount.toString() + it).println()
                    }
                }
            }
            pressCount++
        }
        seen.println()

        return seen.values.reduce {acc, i -> acc * i}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
//    part1(testInput).println()

    val test2Input = readInput("Day20_test2")
//    part1(test2Input).println()

    val input = readInput("Day20")
//    part1(input).println()
    part2(input).println()
}

