fun main() {
    fun part1(input: List<String>): Int {
        val commands: Array<String> = arrayOf("forward","down","up")
        val moves = IntArray(commands.size){ m -> 0 }
        for (str in input){
            val bf = str.split(" ")
            moves[ commands.indexOf( bf[0] ) ] += bf[1].toInt()
        }
        return moves[ commands.indexOf("forward") ]*(moves[ commands.indexOf("down") ]-moves[ commands.indexOf("up") ])
        //moves.fold( 1, {acc , e -> acc*e} ) //Initializiert mit 1, da Elemente multipliziert werden
    }

    fun part2(input: List<String>): Int {
        var aim = 0
        var depth = 0
        var xPos = 0
        for (str in input){
            val bf = str.split(" ")
            val i = bf[1].toInt()
            when(bf[0]){
                    "forward" -> {xPos += i;  depth += aim*i}
                    "down" -> aim += i
                    "up" -> aim -= i
            }
        }
        return xPos*depth
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}