fun main() {
    fun parse(input: String): Pair<Pair<Int,Int>,Pair<Int,Int>>{
        var s1 = input.drop("target area: x=".length)
        var i2 = s1.indexOf(", y=")
        var intX = s1.take(i2).split("..").map { it.toInt() }
        var intY = s1.drop(i2+", y=".length).split("..").map { it.toInt() }
        return Pair(Pair(intX[0],intX[1]),Pair(intY[0],intY[1]))
    }



    fun getHits(rangeX: Pair<Int,Int>, rangeY: Pair<Int,Int>): List<Pair<Pair<Int,Int>,Int>>{
        //liefert Liste mit (vx,vy,N) zurück (wo getroffen wurde, wird nicht mitgeteilt)
        //Ausprobieren, da es nur 1 Testcase gibt... LANGWEILIG...
        val hits = ArrayList<Pair<Pair<Int,Int>,Int>>()
        for (vx in 0..rangeX.second){
            for (vy in rangeY.first..400){
                var x = 0
                var y = 0
                for (N in 0..400){
                    if (vx-N > 0){
                        x += (vx-N)
                    }
                    y += (vy-N)
                    if (rangeX.first <= x && x <= rangeX.second && rangeY.first <= y && y<=rangeY.second){
                        hits.add(Pair(Pair(vx,vy),N))
                        break
                    }
                }
            }
        }
        return hits
    }

    //könnte ich auch direkt oben anstatt N mit übergeben. Maxium zwischenzeitlich mit abfangen, fertig.
    fun getMax(vy: Int, N: Int): Int{
        if (vy > 0){
            if (N > vy){
                return (vy+1)*vy-(vy*(vy+1)/2)
            }else{
                return vy*(N+1)-(N*(N+1)/2)
            }
        }else{
            return 0
        }
    }

    fun part1(input: List<String>): Int {
        var targetArea = parse(input[0])
        val hits = getHits(targetArea.first,targetArea.second)
        return hits.maxOf { getMax(it.first.second, it.second) }
    }

    fun part2(input: List<String>): Int {
        var targetArea = parse(input[0])
        val hits = getHits(targetArea.first,targetArea.second)
        return hits.size
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day17"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
