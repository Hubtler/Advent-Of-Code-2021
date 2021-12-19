import kotlin.math.abs

fun main() {
    fun rotate(rotation: Int, xyz: Triple<Int,Int,Int>): Triple<Int,Int,Int> {
        when (rotation){
            0 -> return Triple(xyz.first,xyz.second,xyz.third)
            1 -> return Triple(xyz.first,xyz.third,-xyz.second)
            2 -> return Triple(xyz.first,-xyz.second,-xyz.third)
            3 -> return Triple(xyz.first,-xyz.third,xyz.second)
            4 -> return Triple(-xyz.second,xyz.first,xyz.third)
            5 -> return Triple(-xyz.second,xyz.third,-xyz.first)
            6 -> return Triple(-xyz.second,-xyz.first,-xyz.third)
            7 -> return Triple(-xyz.second,-xyz.third,xyz.first)
            8 -> return Triple(-xyz.first,-xyz.second,xyz.third)
            9 -> return Triple(-xyz.first,xyz.third,xyz.second)
            10 -> return Triple(-xyz.first,xyz.second,-xyz.third)
            11 -> return Triple(-xyz.first,-xyz.third,-xyz.second)
            12 -> return Triple(xyz.second,-xyz.first,xyz.third)
            13 -> return Triple(xyz.second,xyz.third,xyz.first)
            14 -> return Triple(xyz.second,xyz.first,-xyz.third)
            15 -> return Triple(xyz.second,-xyz.third,-xyz.first)
            16 -> return Triple(-xyz.third,xyz.second,xyz.first)
            17 -> return Triple(-xyz.third,xyz.first,-xyz.second)
            18 -> return Triple(-xyz.third,-xyz.second,-xyz.first)
            19 -> return Triple(-xyz.third,-xyz.first,xyz.second)
            20 -> return Triple(xyz.third,-xyz.second,xyz.first)
            21 -> return Triple(xyz.third,xyz.first,xyz.second)
            22 -> return Triple(xyz.third,xyz.second,-xyz.first)
            23 -> return Triple(xyz.third,-xyz.first,-xyz.second)
        }
        println("ERROR ungültige Rotation " + rotation.toString())
        return xyz
    }

    data class Scanner(var nr: Int, var beacons: ArrayList<Triple<Int,Int,Int>> = ArrayList<Triple<Int,Int,Int>>(),var rotation: Int = 0, var rotKnown: Boolean = false, var shift: Triple<Int,Int,Int> = Triple(0,0,0), var tested: Boolean = false){
    }

    fun minus(x: Triple<Int,Int,Int>, y: Triple<Int,Int,Int>): Triple<Int,Int,Int>{
        return Triple(x.first - y.first, x.second - y.second, x.third - y.third)
    }
    fun plus(x: Triple<Int,Int,Int>, y: Triple<Int,Int,Int>): Triple<Int,Int,Int>{
        return Triple(x.first + y.first, x.second + y.second, x.third + y.third)
    }

    fun parse(input: List<String>): ArrayList<Scanner>{
        val scanner = ArrayList<Scanner>()
        var c = 0
        for (inp in input){
            if (inp.isNotEmpty()){
                if (inp.contains("--- scanner ")){
                    scanner.add(Scanner(c++))
                }else{
                    val tr = inp.split(",").map { it.toInt() }
                    scanner.last().beacons.add(Triple(tr[0],tr[1],tr[2]))
                }
            }
        }
        scanner[0].rotKnown = true
        return scanner
    }

    fun getShiftsAndRotations(scanner: ArrayList<Scanner>): ArrayList<Scanner>{
        var knownC = 1
        while (knownC < scanner.size){
            for (s in scanner){
                if (s.rotKnown && !s.tested){
                    for (us in scanner){
                        if (!us.rotKnown){//für alle bekannten Scanner s , gehe alle unbekannten Scanner us durch
                            for (r in 0..23){ //gehe alle Rotationen durch
                                val rotList = ArrayList<Triple<Int,Int,Int>>()
                                for (b in us.beacons){
                                    rotList.add(rotate(r,b))
                                }
                                val shifts = ArrayList<Triple<Int,Int,Int>>()
                                for (sb0 in s.beacons){
                                    for (ub0 in rotList){
                                        val shift = minus(sb0,ub0) //angenommen sb0 = ub0, dann berechne Shift
                                        shifts.add(shift)
                                    }
                                }
                                val test = shifts.groupingBy { it }.eachCount().filter { it.value >= 12 }
                                if (test.size>0){
                                    us.rotKnown = true
                                    us.rotation = r
                                    us.shift = plus(test.keys.first(), s.shift)
                                    us.beacons = rotList
                                    us.tested = false
                                    knownC++
                                    break
                                }
                            }
                        }
                    }
                    s.tested = true
                }
            }
        }
        return scanner
    }


    fun part1(input: List<String>): Int {
        val scanner = parse(input)
        getShiftsAndRotations(scanner)
        val beacons = ArrayList<Triple<Int,Int,Int>>()
        for (s in scanner){
            for (b in s.beacons){
                beacons.add(plus(s.shift,b))
            }
        }
        return beacons.distinct().size
    }

    fun norm1(x: Triple<Int,Int,Int>): Int{
        return abs(x.first) + abs(x.second) + abs(x.third)
    }

    fun part2(input: List<String>): Int {
        val scanner = parse(input)
        getShiftsAndRotations(scanner)
        var maxNorm = 0
        for (s1 in scanner) {
            for (s2 in scanner) {
                if (s1 != s2){
                    maxNorm = maxOf(maxNorm, norm1(minus(s1.shift,s2.shift)) )
                }
            }
        }
        return maxNorm
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day19"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}
