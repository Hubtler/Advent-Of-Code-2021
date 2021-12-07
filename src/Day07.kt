import java.lang.Math.abs
import kotlin.math.min

fun main() {
    fun calcCosts(pos: List<Int>, moveTo: Int): Int{
        var c = 0
        for (p in pos){
            c += abs(p - moveTo)
        }
        return c
    }

    fun part1(input: List<String>): Int {
        val nums = input[0].split(",").map { it.toInt() }
        var x = nums.sum()/nums.size //Mittelwert berechnen
        var minc = Int.MAX_VALUE
        for (d in -300..300){
            val c = calcCosts(nums, x+d)
            minc = min(c,minc)
        }
        return minc
    }

    fun calcCosts2(pos: List<Int>, moveTo: Int): Int{
        var c = 0
        for (p in pos){
            //Ist die Differenz N, so kostet sie ja 1+2+3+4+5+..+N = N(N+1)/2
            val d = abs(p - moveTo)
            c += d*(d+1)/2
        }
        return c
    }

    fun part2(input: List<String>): Int {
        val nums = input[0].split(",").map { it.toInt() }
        var x = nums.sum()/nums.size //Mittelwert berechnen
        var minc = Int.MAX_VALUE
        for (d in -300..300){
            val c = calcCosts2(nums, x+d)
            minc = min(c,minc)
        }
        return minc
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day07"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 37)
    //check(part2(testInput) == )

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
