import java.math.BigInteger

fun main() {
    data class Packet(val s: String){
        val version: Int
        val typeId: Int
        var ownData: String
        val subpackets: ArrayList<Packet>
        var length: Int

        init{
            version = s.substring(0,3).toInt(2)
            typeId = s.substring(3,6).toInt(2)
            subpackets = ArrayList<Packet>()
            ownData = ""
            var index = 6
            when (typeId){
                4   ->  {//literal value
                    while (s[index] == '1'){
                        ownData += s.substring(index+1,index+1+4)
                        index += 5
                    }
                    ownData += s.substring(index+1,index+1+4) //die letzten 4 Stellen
                    index += 5
                }
                else ->  { //operator packet
                    val lengthBits: Int
                    if (s[index] == '0'){ //15 bits
                        lengthBits = 15
                        index++
                        val length = s.substring(index,index+lengthBits).toInt(2)
                        index += lengthBits
                        var used = 0
                        while (used < length){ //falls ich noch mindestens 6 Zeichen hab
                            val p = Packet(s.subSequence(index+used,index+length).toString())
                            subpackets.add(p)
                            used += p.length
                        }
                        index += used
                    }else{ //11 bits
                        lengthBits = 11
                        index++
                        val length = s.substring(index,index+lengthBits).toInt(2)
                        index += lengthBits
                        var used = 0
                        while (subpackets.size < length){ //falls mir noch ein Paket fehlt
                            val p = Packet(s.substring(index+used))
                            subpackets.add(p)
                            used += p.length
                        }
                        index += used
                    }
                }
            }
            length = index
        }
        fun sumVersions(): Int{
            var s = version
            for (p in subpackets){
                s += p.sumVersions()
            }
            return s
        }
        fun getValue(): BigInteger{
            when(typeId){
                4 -> return ownData.toBigInteger(2)
                0 -> return subpackets.map { it.getValue() }.sumOf { it }
                1 -> return subpackets.map { it.getValue() }.reduce { acc, it -> acc*it }
                2 -> return subpackets.map { it.getValue() }.reduce { acc, it -> minOf(acc,it) }
                3 -> return subpackets.map { it.getValue() }.reduce { acc, it -> maxOf(acc,it) }
                5 -> {if (subpackets[0].getValue() > subpackets[1].getValue()){ return 1.toBigInteger()}else{ return 0.toBigInteger()}}
                6 -> {if (subpackets[0].getValue() < subpackets[1].getValue()){ return 1.toBigInteger()}else{ return 0.toBigInteger()}}
                7 -> {if (subpackets[0].getValue() == subpackets[1].getValue()){ return 1.toBigInteger()}else{ return 0.toBigInteger()}}
                else -> return 0.toBigInteger()
            }
        }
    }

    fun parseHexToBin(hex: String): String{
        return ("F"+hex).toBigInteger(16).toString(2).drop(4)
    }

    fun part1(input: List<String>): Int {
        val binRep = parseHexToBin(input[0])
        return Packet(binRep).sumVersions()
    }

    fun part2(input: List<String>): BigInteger {
        val binRep = parseHexToBin(input[0])
        return Packet(binRep).getValue()
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day16"
    var testInput: List<String>

    testInput = readInput(dayname+"_test")
    check(part1(testInput) == 16)
    check(part2(listOf("CE00C43D881120"))==9.toBigInteger())

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}
