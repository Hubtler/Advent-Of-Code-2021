fun main() {
    fun nachUnten(map: List<String>, maxX: Int): List<String>{
        var newMap = ArrayList<String>()
        for (x in map.indices){ //x entspricht Zeilen
            var s = ""
            for (y in map[x].indices){ // y entspricht Spalten
                if (map[x][y] == '.'){ //feld jetzt leer, schaue ob was nachrückt
                    if (map[(x-1+maxX)%maxX][y] == 'v'){
                        s += 'v'
                    }else{
                        s += '.'
                    }
                }else{ //feld ist aktuell belegt
                    if (map[x][y] == 'v') { // schaue ob v abhaut
                        if (map[(x+1)%maxX][y] == '.'){ //nächste Feld ist leer, also haut er ab
                            s += '.'
                        }else{
                            s += 'v'
                        }
                    }else{ //was auch immer da war, soll da bleiben
                        s += map[x][y]
                    }

                }
            }
            newMap.add(s)
        }
        return newMap
    }
    fun nachRechts(map: List<String>, maxY:Int): List<String>{
        var newMap = ArrayList<String>()
        for (x in map.indices){ //x entspricht Zeilen
            var s = ""
            for (y in map[x].indices){ // y entspricht Spalten
                if (map[x][y] == '.'){ //feld jetzt leer, schaue ob was nachrückt
                    if (map[x][(y-1+maxY)%maxY] == '>'){
                        s += '>'
                    }else{
                        s += '.'
                    }
                }else{ //feld ist aktuell belegt
                    if (map[x][y] == '>') { // schaue ob > abhaut
                        if (map[x][(y+1)%maxY] == '.'){ //nächste Feld ist leer, also haut er ab
                            s += '.'
                        }else{
                            s += '>'
                        }
                    }else{ //was auch immer da war, soll da bleiben
                        s += map[x][y]
                    }
                }
            }
            newMap.add(s)
        }
        return newMap
    }

    fun nextStep(map: List<String>, maxX: Int, maxY: Int): List<String>{
        return nachUnten(nachRechts(map, maxY), maxX)
        //return nachRechts(nachUnten(map, maxX), maxY)
    }

    fun part1(input: List<String>): Int {
        var map = input
        val maxX = map.size
        val maxY = map[0].length

        var newmap = nextStep(map, maxX, maxY)
        var steps = 1
        while (newmap != map){
            map = newmap
            newmap = nextStep(map, maxX, maxY)
            steps++
        }
        return steps
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day25"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 58)
    //check(part2(testInput) == )

    println("Tests passed")

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
