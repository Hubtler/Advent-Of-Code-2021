fun main() {
    fun sparseEnhancement(lights: List<Pair<Int,Int>>, algorithm: List<Boolean>, standard: Boolean): Pair<List<Pair<Int,Int>>,Boolean>{
        val lightMapCounter = ArrayList<Triple<Int,Int,Int>>()
        for (light in lights){
            var b = 1
            for (i in -1..1){
                for (j in -1..1){
                    lightMapCounter.add( Triple(light.first+i,light.second+j, b) )
                    b *= 2
                }
            }
        }
        val g = lightMapCounter.groupBy { Pair(it.first,it.second) }
        return Pair(g.keys.map { k -> Pair(k,g[k]!!.sumOf { it.third }) }.filter { v -> (standard && algorithm[511-v.second] xor algorithm[511])||(!standard && algorithm[v.second] xor algorithm[0]) }.map { it.first }, (standard && algorithm[511])||(!standard && algorithm[0]) )
    }

    fun parse(input: List<String>): Pair< List<Pair<Int,Int>>, List<Boolean> >{
        val lights = ArrayList<Pair<Int,Int>>()
        val algorithm = input[0].map { it == '#' } //true falls es ein # ist, sonst kommt es nicht vor
        val lightmap = input.drop(2)
        for (row in lightmap.indices){
            for (col in lightmap[row].indices){
                if (lightmap[row][col]=='#'){
                    lights.add(Pair(row,col))
                }
            }
        }
        return Pair(lights,algorithm)
    }

    fun enhance(lights: List<Pair<Int,Int>>, algorithm: List<Boolean>, iterations: Int): List<Pair<Int,Int>>{
        var lightMap = Pair(lights,false)
        for (i in 1..iterations){
            lightMap = sparseEnhancement(lightMap.first,algorithm,lightMap.second)
        }
        if (lightMap.second){
            println("es gibt unendlich Lichtpunkte, es werden die Punkte zurückgegeben, die keine Lichtpunkte sind")
        }
        return lightMap.first
    }

    fun part1(input: List<String>): Int {
        val lightAlg = parse(input)
        return enhance(lightAlg.first, lightAlg.second, 2).size
    }

    fun part2(input: List<String>): Int {
        val lightAlg = parse(input)
        return enhance(lightAlg.first, lightAlg.second, 50).size
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day20"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}