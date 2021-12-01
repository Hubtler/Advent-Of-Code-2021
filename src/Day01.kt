fun main() {
    fun part1(input: List<String>): Int {
        var incs = 0
        if (input.size > 0){
            var last = input[0].toInt()
            for (str in input){
                val now = str.toInt()
                if (now > last){
                    incs++
                }
                last = now
            }
        }
        return incs
    }

    fun part2(input: List<String>): Int {
        val groups = input.size-3 // wir müssen den letzten, und vorletzen nicht mehr berücksichtigen.
        var incs = 0
        var last = Int.MAX_VALUE
        for (i in 0..groups){
            var groupval = 0
            for (j in 0..2){
                groupval += input[i+j].toInt()
            }
            if (groupval > last){
                incs++
            }
            last = groupval
        }
        return incs
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
