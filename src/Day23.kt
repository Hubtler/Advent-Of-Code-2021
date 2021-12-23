fun main() {
    fun move(map: List<List<Char>>, dran: Pair<Int,Int>, kommtVon: Pair<Pair<Int,Int>, Boolean>, tiefe: Byte = 2): Long{
        //kommtVon gibt die Koordinate woher er kommt an und, ob er auf dem Weg in sein Ziel ist (dann muss er bewegt werden)
        val goals = mutableMapOf('A' to 4, 'B' to 6, 'C' to 8, 'D' to 10)
        fun canGoToGoal(k: Pair<Int,Int>): Boolean{
            var b = (k.second == 1)
            val gx = goals[map[k.first][k.second]]!!
            if (k.first < gx){ //Zielspalte ist rechts von mir
                for (x in k.first+1..goals[map[k.first][k.second]]!!){
                    b = b&&(map[x][1]=='.')
                }
            }else{
                for (x in gx..k.first-1){
                    b = b&&(map[x][1]=='.')
                }
            }
            if (b){ //Sind alle Felder bis zur Zielspalte frei, prüfe ob in der Zielspalte nur freie Felder oder Zielfelder sind
                b = b&&(map[gx][2]!! == '.') //erste Feld muss auf jeden Fall frei sein, falls ichin die Spalte rein möchte
                for (y in 3..1+tiefe){ //restlichen Einträge dürfen frei sein, oder vom gleichen Buchstaben belegt sein
                    b = b && ( (map[gx][2]!! == '.') || (map[gx][y]!! == map[k.first][k.second]!!) )
                }
            }
            return b
        }

        fun movable(k: Pair<Int,Int>): Boolean{
            if (map[k.first][k.second] !in 'A'..'D'){
                return false
            }
            if (kommtVon.second){ //Bin ich aufm Weg ins Ziel, darf sich nur dran bewegen
                return k==dran
            }
            if (kommtVon.first.second > 1){ //kommt dran gerade aus einem Raum, so muss dran sich erstmal komplett rausbewegen
                return k==dran
            }
            else{ //andernfalls dürfen sich auch andere Bewegen
                if (map[k.first][k.second] in 'A'..'D'){
                    if (k.second == 1){ //wenn ich oben auf der Linie bin, dann darf ich mich nur bewgen, wenn ich dran bin, oder ich ins Ziel kann
                        return (k==dran)||(canGoToGoal(k))
                    }else{ //ansonsten darf ich mich bewegen,  die Frage ist dann nur, wohin ich mich bewegen kann
                        return true
                    }
                }else{
                    return false
                }
            }
        }
        fun goTo(x: Int, y: Int, zuX: Int, zuY:Int): Long{
            if ( (Pair(x,y)==dran)&&(Pair(zuX,zuY)==kommtVon.first) ){
                return Long.MAX_VALUE
            }
            var newmap = map.map { it.toMutableList() }.toMutableList()
            newmap[zuX]!![zuY] = map[x][y]
            newmap[x]!![zuY] = '.'
            return move(newmap, Pair(zuX,zuY), Pair(Pair(x,y), ((y==1)&&(Pair(x,y)!=dran)) || kommtVon.second))
        }
        //Habe ich Ziel erreicht? Falls ja, return,
        var b = true
        for (x in goals.keys){
            for (y in 2..1+tiefe){
                b = b && (map[goals[x]!!][y]==x)
            }
        }
        if (b){
            println("ready")
            return 0L
        }


        // andernfalls führe alle möglichen Moves aus
        val costs = mutableMapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
        var min = Long.MAX_VALUE
        for (x in map.indices){
            for (y in map[x].indices){
                if (movable((Pair(x,y)))){
                    //schaue links, rechts, oben, unten, ob dort '.' ist, wenn ja, gehe dorthin
                    if (map[x-1][y] == '.'){
                        min = minOf( min, goTo(x,y,x-1,y) )
                    }
                    if (map[x+1][y] == '.'){
                        min = minOf( min, goTo(x,y,x+1,y) )
                    }
                    if (map[x][y-1] == '.'){
                        min = minOf( min, goTo(x,y,x,y-1) )
                    }
                    if (map[x][y+1] == '.'){
                        min = minOf( min, goTo(x,y,x,y+1) )
                    }
                    min += costs[map[x][y]]!!
                }
            }
        }
        return min
        //Bewege alle möglichen Zeichen, die nicht '.', oder '#' sind, und in Reihe >1 stehen oder dran sind
        //in alle Richtungen die möglich sind ('.') haben, außer es ist dran und dieser kommtVon dieser Position
        //bewege ich nicht dran, so kann ich kommtVon auf 0,0 setzen, da dort eh ein '#' ist.
        //addiere abhängig vom bewegten Buchstaben 1,10,100 oder 1000 auf einen Zähler, addiere Unterschritt und berechne Minimum
        // (oder erst minimum und dan eigenen addieren, da eigener ja bei allen Gleich ist)
        //Gebe minimum inkl eigenem Minimum zurück

        //Beim bewegen: steht dran, auf einem Feld genau ein über dem wo er herkommt, so muss er sich bewegen (keine anderen Moves)
        // dran bewegt sich nicht auf das Feld wo er herkommt
        // dran geht nur nach unten, falls es seine Reihe ist, und in dieser ganzen Spalte kein anderer Buchstabe steht (außer ggf. der eigene)
        // ein Buchstabe bewegt sich nur dann aus seiner Spalte, falls er nicht in der richtigen Spalte ist, oder in seiner Spalte noch ein falscher Buchstabe zu finden ist
        // ein Buchstabe in Zeile 1, der nicht dran ist, darf sich nur bewegen, falls seine Reihe nur "richtige" Buchstaben beinhaltet
    }

    fun part1(input: List<String>): Long {
        val map = input.map { it.replace(' ','#').toList() }
        println(move(map, Pair(-1,-1),Pair(Pair(0,0),false)))
        return move(map, Pair(-1,-1),Pair(Pair(0,0),false))

    }

    fun part2(input: List<String>): Long {
        val map = input.map { it.replace(' ','#').toList() }
        return move(map, Pair(-1,-1),Pair(Pair(0,0),false))
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day23"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 12521L)
    //check(part2(testInput) == )

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}
