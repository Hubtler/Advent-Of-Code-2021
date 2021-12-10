fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (inp in input){
            var k = ""
            val zeile = inp.toCharArray()
            for (z in zeile){
                when(z){
                    '(','{','[','<' -> k += z
                    ')' -> {    if (k.lastOrNull()=='(') {
                                    k = k.dropLast(1)
                                }else{
                                    sum+=3
                                    break
                                }
                           }
                    ']' -> {if (k.lastOrNull()=='[') {k = k.dropLast(1)}else{sum+=57; break }}
                    '}' -> {if (k.lastOrNull()=='{') {k = k.dropLast(1)}else{sum+=1197; break }}
                    '>' -> {if (k.lastOrNull()=='<') {k = k.dropLast(1)}else{sum+=25137; break }}
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): ULong {
        val scores = ArrayList<ULong>()
        for (inp in input){
            var k = ""
            val zeile = inp.toCharArray()
            for (z in zeile){
                when(z){
                    '(','{','[','<' -> k += z
                    ')' -> {    if (k.lastOrNull()=='(') {
                                    k = k.dropLast(1)
                                }else{
                                    k= ""
                                    break
                                }
                            }
                    ']' -> {if (k.lastOrNull()=='[') {k = k.dropLast(1)}else{ k= ""; break }}
                    '}' -> {if (k.lastOrNull()=='{') {k = k.dropLast(1)}else{ k= ""; break }}
                    '>' -> {if (k.lastOrNull()=='<') {k = k.dropLast(1)}else{ k= ""; break }}
                }
            }
            if (k.length > 0){
                var score = 0UL
                k = k.reversed()
                val kl = k.toCharArray()
                for (klammer in kl){
                    score *= 5UL
                    when (klammer){
                        '(' -> score += 1UL
                        '[' -> score += 2UL
                        '{' -> score += 3UL
                        '<' -> score += 4UL
                    }
                }
                scores.add(score)
            }
        }
        scores.sort()
        return scores[scores.size/2] //Abrunden liefert automatisch das richtige
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day10"
    "".lastOrNull()
    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957UL)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
