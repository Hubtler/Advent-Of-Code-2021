import java.util.*
import kotlin.collections.ArrayList

class SevenSegment {
    var s: SortedSet<Char>

    constructor(initS: String) {
        s = initS.toSortedSet()
    }

    constructor(initS: Set<Char>) {
        s = initS.toSortedSet()
    }

    override fun equals(other: Any?): Boolean{
        return s.equals(other)
    }

    fun andW(w: SevenSegment):SevenSegment{
        //gemeinsam auftredente Buchstaben
        return SevenSegment(s.intersect(w.s))
    }

    fun lengthAndW(w: SevenSegment):Int{
        return andW(w).s.size
    }

    override fun toString(): String {
        return s.toString()
    }

    override fun hashCode(): Int {
        return s.hashCode()
    }
}

fun main() {

    fun getOutput(input: List<String>): ArrayList<List<String>>{
        val output = ArrayList<List<String>>()
        for (inp in input){
            val o = inp.split(" | ")
            output.add(o[1].split(" "))
        }
        return output
    }

    fun getAll(input: List<String>): ArrayList<List<String>>{
        val output = ArrayList<List<String>>()
        for (inp in input){
            val o = inp.split(" | ")
            output.add(o[0].split(" ") +  o[1].split(" "))
        }
        return output
    }

    fun part1(input: List<String>): Int {
        var c = 0
        val output = getOutput(input)
        for (o in output){
            for (v in o){
                when (v.length){
                    2,4,3,7 -> c++
                    // 2 Segmente entspricht einer 1
                    // 4 Segmente einer 4
                    // 3 Segmente einer 7
                    // 7 Segmente einer 8
                }
            }
        }
        return c
    }

    fun part2(input: List<String>): Int {
        val rows = getAll(input)
        val output = getOutput(input)
        val n = rows.size
        var sum = 0
        for (i in 0..n-1){
            val row = rows[i]
            var s1: SevenSegment = SevenSegment("1")
            var s4: SevenSegment = SevenSegment("1")

            //var b = MutableList(10) { false }
            //zuerst finde die Werte 1,4 //(7 und 8 könnte ich direkt mitnehmen)
            for (dig in row){
                when (dig.length){
                    2 -> s1 = SevenSegment(dig)
                    4 -> s4= SevenSegment(dig)
                }
            }
            //Nun übersetze den Output
            var s = ""
            for (dig in output[i]){
                when (dig.length){
                    2 -> s += "1"
                    4 -> s += "4"
                    3 -> s += "7"
                    7 -> s += "8"
                    5 -> {  val seg = SevenSegment(dig);
                            if (seg.lengthAndW(s1)==2){
                                s += "3"
                            }else{
                                if (seg.lengthAndW(s4)==2){
                                    s += "2"
                                }else{
                                    s += "5"
                                }
                            }
                         }
                    6 -> {  val seg = SevenSegment(dig);
                        if (seg.lengthAndW(s1)==1){
                            s += "6"
                        }else{
                            if (seg.lengthAndW(s4)==4){
                                s += "9"
                            }else{
                                s += "0"
                            }
                        }
                    }
                }
            } //nun ist output übersetzt und steht als String in s
            sum += s.toInt()

        }
        return sum
    }


    // test if implementation meets criteria from the description, like:
    val dayname = "Day08"
    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)
    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
