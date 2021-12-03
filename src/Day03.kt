fun main() {
    fun part1(input: List<String>): Int {
        val nums = input[0].length
        val countOnes = IntArray(nums){ m -> 0 }
        for (str in input){
            for (j in 0..nums-1){
                if (str[j].equals('1')) {
                    countOnes.set(j,countOnes.get(j)+1)
                }
            }
        }
        var gamma = 0
        var zp = 1
        for (jr in 0..nums-1){
            val j = nums-1-jr
            if (countOnes.get(j) > input.size / 2) {
                gamma += zp
            }
            zp *= 2
        }
        return gamma*((zp)-1-gamma) //gamma + epsilon rate = 2^(nums) -1
    }

    fun mostCommonBit(index: Int, input: List<String>): Char{
        var countOnes = 0
        for (str in input){
            if (str[index].equals('1')) {
                    countOnes++
            }
        }
        if (2*countOnes >= input.size){
            return '1'
        }else{
            return '0'
        }
    }

    fun leastCommonBit(index: Int, input: List<String>): Char{
        var countOnes = 0
        for (str in input){
            if (str[index].equals('1')) {
                countOnes++
            }
        }
        if (2*countOnes >= input.size){
            return '0'
        }else{
            return '1'
        }
    }
    fun check(index: Int, input: List<String>, mostCommonBit: Char ): List<String>{
        val output = ArrayList<String>()
        for (str in input){
            if (str[index].equals(mostCommonBit)) {
                output.add(str)
            }
        }
        return output.toList()
    }

    fun BinStrToDec(str: String): Int{
        val nums = str.length
        var output = 0
        var zp = 1
        for (jr in 0..nums-1){
            val j = nums-1-jr
            if (str[j].equals('1')) {
                output += zp
            }
            zp *= 2
        }
        return output
    }


    fun part2(input: List<String>): Int {
        var s = input
        var s2 = input
        var oxygen = 0
        var co2 = 0
        val nums = input.get(0).length
        for (i in 0..nums-1){
            if (s.size > 1) {
                val b = mostCommonBit(i, s)
                s = check(i, s, b)
            }
            if (s2.size > 1) {
                val b = leastCommonBit(i, s2)
                s2 = check(i, s2, b)
            }
        }
        if (s.size == 1){
            oxygen = BinStrToDec(  s[0] )
        }
        if (s2.size == 1){
            co2 = BinStrToDec(  s2[0] )
        }
        return oxygen*co2
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}