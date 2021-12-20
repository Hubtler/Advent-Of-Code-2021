fun main() {

    fun sparseEnhancement(lights: List<Pair<Int,Int>>, algorithm: List<Boolean>): List<Pair<Int,Int>>{
        if (algorithm[0]){
            println("ERROR, aus reiner Dunkelheit entsteht Licht")
        }
        val lightMapCounter = ArrayList<Triple<Int,Int,Int>>() //x,y,Wert der draufzuaddieren ist
        for (light in lights){
            var b = 1
            for (i in -1..1){
                for (j in -1..1){
                    lightMapCounter.add( Triple(light.first+i,light.second+j, b) )
                    b *= 2 // (i,j -> 2^(4-3i-j))
                }
            }
        }

        val g = lightMapCounter.groupBy { Pair(it.first,it.second) }
        return g.keys.map { k -> Pair(k,g[k]!!.sumOf { it.third }) }.filter { v -> algorithm[v.second] }.map { it.first }
    }

    fun sparseDoubleEnhancement(lights: List<Pair<Int,Int>>, algorithm: List<Boolean>): List<Pair<Int,Int>>{
        if (algorithm[0]){
            println("ERROR, aus reiner Dunkelheit entsteht Licht")
        }
        val lightMapCounter = ArrayList<Triple<Int,Int,Int>>() //x,y,Wert der draufzuaddieren ist
        for (light in lights){
            var b = 1
            for (i in -2..2){
                for (j in -2..2){
                    lightMapCounter.add( Triple(light.first+i,light.second+j, b) )
                    b *= 2
                }
            }
        }

        val g = lightMapCounter.groupBy { Pair(it.first,it.second) }
        return g.keys.map { k -> Pair(k,g[k]!!.sumOf { it.third }) }.filter { v -> algorithm[v.second] }.map { it.first }
    }

    fun doubleAlgorithmus(algorithm: List<Boolean>): List<Boolean>{
        val newAlgorithm = ArrayList<Boolean>() //für ein 11x11 Grid
        //erzeuge beliebiges 11x11 Feld,
        //erzeuge für Mittelpunkt und alle direkt umliegenden Felder das, was der Algorithmus liefert
        //wende nun nur auf den Mittelpunkt den Algorithmus
        for (f in 0..33554431){
            //Felder seien nummeriert von -2 bis 2 mit regel (i,j) -> 2^(5*(2-i)+(2-j)), so dass (-2,-2) = oben links den Höchsten Wert hat
            val fieldstring = (33554432+f).toString(2).drop(1).map { if (it=='0') '.' else '#' }
            var field = ""
            for (i in 0..fieldstring.size-1){
                if (i%5==0){
                    field += "\n"
                }
                field += fieldstring[i]
            }
            val map = field.trim().split("\n").map { it.map { if (it=='.') 0 else 1 }}
            val smallfield = ArrayList<ArrayList<Boolean>>()
            for (i0 in 1..3){
                val row = ArrayList<Boolean>()
                for (j0 in 1..3){
                    var b = 256
                    var algind = 0
                    for (i in -1..1){
                        for (j in -1..1){
                            if (map[i0+i][j0+j]==1){
                                algind += b
                            }
                            b/=2
                        }
                    }
                    //3x3 Feld ist nun an Stelle i0,j0 berechnet:
                    row.add(algorithm[algind])
                }
                smallfield.add(row)
            }
            //Nun noch Mittelpunkt berechnen (im zweiten Schritt)
            var b = 256
            var algind = 0
            for (i in -1..1){
                for (j in -1..1){
                    if (smallfield[1+i][1+j]){
                        algind += b
                    }
                    b/=2
                }
            }
            newAlgorithm.add(algorithm[algind])
        }
        return newAlgorithm
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

    fun lightsToMap(lights: List<Pair<Int,Int>>): String{
        val minX = lights.minOf { it.first }
        val maxX = lights.maxOf { it.first }
        val minY = lights.minOf { it.second }
        val maxY = lights.maxOf { it.second }
        var s = ""
        for (i in minX..maxX){
            for (j in minY..maxY){
                if (lights.contains(Pair(i,j))){
                    s += '#'
                }else{
                    s += '.'
                }

            }
            s += '\n'
        }
        return s
    }

    fun enhance(lights: List<Pair<Int,Int>>, algorithm: List<Boolean>, iterations: Int ): List<Pair<Int,Int>>{
        var newLights = lights
        if (algorithm[0]){
            val algorithm2 = doubleAlgorithmus(algorithm)
            if (iterations % 2 == 1){
                println("Unendlich viele Sterne leuchten")
            }else {
                for (i in 1..iterations / 2) {
                    newLights = sparseDoubleEnhancement(newLights, algorithm2)
                }
            }
        }else{
            for (i in 1..iterations){
                newLights = sparseEnhancement(newLights, algorithm)
            }
        }
        return newLights
    }

    fun part1(input: List<String>): Int {
        val lightAlg = parse(input)
        return enhance(lightAlg.first,lightAlg.second,2).size
    }

    fun part2(input: List<String>): Int {
        val lightAlg = parse(input)
        return enhance(lightAlg.first,lightAlg.second,50).size
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