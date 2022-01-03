fun main() {
    fun toString(map: List<String>): String{
        var s = ""
        for (row in map){
            s += row + "\n"
        }
        return s
    }

    var deepth = 2
    var maxCosts = ULong.MAX_VALUE

    data class MatInd(val zeile: Int, val spalte: Int){
        fun above(): MatInd{
            return MatInd(zeile-1, spalte)
        }
        fun left(): MatInd{
            return MatInd(zeile, spalte-1)
        }
        fun right(): MatInd{
            return MatInd(zeile, spalte+1)
        }
        fun under(): MatInd{
            return MatInd(zeile+1, spalte)
        }
    }

    data class Move(val from: MatInd, val to: MatInd)

    val goals = mutableMapOf('A' to 3, 'B' to 5, 'C' to 7, 'D' to 9)


    fun getAllPossibleMoves(map: List<String>, lastMove: Move?): List<Move>{
        fun getPlayer(): List<MatInd>{
            val player = ArrayList<MatInd>()
            for (z in map.indices){
                for (s in map[z].indices){
                    if (map[z][s] in 'A'..'D'){
                        player.add(MatInd(z,s))
                    }
                }
            }
            return player
        }
        fun playerInCorrectColumn(p: MatInd): Boolean{
                return (goals[map[p.zeile][p.spalte]] == p.spalte) //Zielspalte = eigene Spalte
        }
        fun columnIsWrong(s: Int): Boolean{
            for (z in 2..deepth+1){ //gehe alle Zeilen durch
                val p = map[z][s]!! //Der Eintrag in der Zeile,Spalte
                if ((p in goals.keys) && !playerInCorrectColumn(MatInd(z,s)) ){ //ist dieser Eintrag ein Spieler, und ist dieser in der falschen Spalte
                        return true //so ist die spalte falsch
                }
            }
            return false
        }

        fun immediatelyOutsideAnyRoom(p: MatInd): Boolean{
            val f = p.under()
            return ( (p.zeile==1) && (map[f.zeile][f.spalte]!='#') )
        }

        fun freePath(fromC: Int, toC: Int): Boolean{
            if (fromC <= toC){
                for (s in fromC+1..toC){
                    if (map[1][s]!='.'){
                        return false
                    }
                }
            }else{
                for (s in toC..fromC-1){
                    if (map[1][s]!='.'){
                        return false
                    }
                }
            }
            return (map[2][toC]=='.')
        }

        val posMoves = ArrayList<Move>()
        val player = getPlayer()

        if (lastMove!=null){
            val p = lastMove.to
            //possible moves von dran (dran = p)
            //links, rechts oben, unten (aber nicht wo er herkam)
            var to = p.above()//nach oben nur, wenn er in falschen Spalte ist, oder in seiner Spalte ein falscher Player ist
            if ((to != lastMove.from) && (map[to.zeile][to.spalte]=='.')){ //wenn darüber frei ist
                //nach oben nur, wenn er in falschen Spalte ist, oder in seiner Spalte ein falscher Player ist
                //ist man in falschen spalte, so ist die Spalte auch falsch! (falls man IN dem Raum steht)
                if (columnIsWrong(p.spalte)){
                    posMoves.add(Move(p,to))
                }
            }
            to = p.under() //nach unten nur, wenn er in richtige Spalte läuft
            val goalCol = goals[map[p.zeile][p.spalte]]!!
            if ((to != lastMove.from) && (!columnIsWrong(goalCol)) && playerInCorrectColumn(p) && (map[to.zeile][to.spalte]=='.') ){
                posMoves.add(Move(p,to))
                return posMoves
            }

            if ( (p.zeile==1) && ( (!columnIsWrong(goalCol)) && freePath(p.spalte,goalCol) )){
                to = p.right()
                if ( (to != lastMove.from) && (p.spalte < goalCol) && (map[to.zeile][to.spalte]=='.') ){
                    posMoves.add(Move(p,to))
                }
                to = p.left()
                if ( (to != lastMove.from) && (p.spalte > goalCol) && (map[to.zeile][to.spalte]=='.') ){
                    posMoves.add(Move(p,to))
                }
                return posMoves
            }else{
                to = p.left()
                if ( (to != lastMove.from) && (map[to.zeile][to.spalte]=='.')){
                    posMoves.add(Move(p,to))
                }
                to = p.right()
                if ( (to != lastMove.from) && (map[to.zeile][to.spalte]=='.')){
                    posMoves.add(Move(p,to))
                }
            }

            if (immediatelyOutsideAnyRoom(p)){ //hier könnte ich ggf. früher returnen, z.B. damit man immer erst nach ganz unten läuft noch || !columnIsWrong(p.spalte) hinzufügen
                return posMoves
            }
        }
        //hier könnte ich optimieren, dass zuerst die Züge auftauchen, die sinnvoll erscheinen (z.B. erst ins Ziel gehen, danach neuen aus spalte holen)
        for (p in player){ //Schaue für jeden Spieler
            if ( (lastMove==null) || (p != lastMove.to) ){ //der nicht dran ist
                var to = p.above()
                if ( (map[to.zeile][to.spalte]=='.') && columnIsWrong(p.spalte) ){ //wenn darüber frei ist
                    //nach oben nur, wenn er in falschen Spalte ist, oder in seiner Spalte ein falscher Player ist
                        //ist man in falschen spalte, so ist die Spalte auch falsch!
                    posMoves.add(Move(p,to))
                }
                val goalCol = goals[map[p.zeile][p.spalte]]!!
                if ( (p.zeile==1) && ( (!columnIsWrong(goalCol)) && freePath(p.spalte,goalCol) )){
                    //kann ich ins Ziel? = Zielspalte ist bisher richtig und WegZumZiel ist frei
                    to = p.under()
                    if (playerInCorrectColumn(p) && (map[to.zeile][to.spalte]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                    to = p.right()
                    if ( (p.spalte < goalCol) && (map[to.zeile][to.spalte]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                    to = p.left()
                    if ( (p.spalte > goalCol) && (map[to.zeile][to.spalte]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                }
            }
        }
        return posMoves
    }

    fun isReady(map: List<String>): Boolean{
        for (pc in goals.keys){ //Für jeden möglichen Buchstaben
            for (z in 2..deepth+1){ //schau in allen Zeilen der Bucht
                if (map[z][goals[pc]!!] != pc){ //ob in der zugehörigen Zielspalte das Feld mit dem Buchstaben belegt ist
                    return false //falls nicht, sind wir nicht fertig
                }
            }
        }
        return true
    }

    fun makeMove(map: List<String>, move: Move): List<String>{
        val newmap = map.map { it.toCharArray()}
        newmap[move.to.zeile][move.to.spalte] = map[move.from.zeile][move.from.spalte]
        newmap[move.from.zeile][move.from.spalte] = '.'
        return newmap.map { String(it) }
    }

    val costs = mutableMapOf('A' to 1UL, 'B' to 10UL, 'C' to 100UL, 'D' to 1000UL)

    fun makeBestMoves(map: List<String>, lastMove: Move? = null, cost: ULong = 0UL): ULong{
        if (cost > maxCosts){ //Pfad wird zu teuer
            return ULong.MAX_VALUE
        }
        if (isReady(map)){ //Fertig
            if (cost < maxCosts){
                println("aktuell geringsten Kosten: " + cost)
                maxCosts = cost
            }
            return cost
        }
        val allMoves = getAllPossibleMoves(map, lastMove)
        var minCost = ULong.MAX_VALUE
        //hier gegebenfalls moves sortieren
        for (move in allMoves){
            val newmap = makeMove(map, move)
            minCost = minOf( minCost, makeBestMoves(newmap, move, cost+costs[map[move.from.zeile][move.from.spalte]]!!) )
        }
        return minCost
    }

    fun shortestPath(map: List<String>, lastMove: Move? = null, cost: ULong = 0UL): ULong{
        class Zustand(val map: List<String>, val lastMove: Move?){
            override fun equals(other: Any?): Boolean {
                return (other is Zustand)&&(other.map==this.map)&&(other.lastMove == this.lastMove ||( (other.lastMove!=null)&&(this.lastMove!=null)&&(other.lastMove.to == this.lastMove.to)) )
            }
        }
        class Knoten(val zustand: Zustand, var costs: ULong){
            override fun equals(other: Any?): Boolean {
                return (other is Knoten) && (zustand.equals(other.zustand))
            }

        }
        val allReachableStates = ArrayList<Knoten>()
        allReachableStates.add(Knoten(Zustand(map, null), 0UL))
        //allReachableStates[Zustand(map, null)] = 0UL

        var debugCost = ULong.MAX_VALUE
        while ( !isReady(allReachableStates[0].zustand.map) ){
            //Minimum aller ReachableStates bestimmen, dann ReachableState entfernen und die von ihm Reachable States hinzufügen inkl Kosten, falls State noch nicht vorhanden
            val min = allReachableStates[0]
            if (min.costs > debugCost + 100UL){
                println("Kosten: " + min.costs + " | Anzahl möglicher Züge: " + allReachableStates.size + " | maximalKosten: " + allReachableStates.last().costs)
                debugCost = min.costs
            }
            allReachableStates.removeFirst()
            val posMoves = getAllPossibleMoves(min.zustand.map,min.zustand.lastMove)
            for (move in posMoves){
                val newZustand = Zustand(makeMove(min.zustand.map,move),move)
                val newCosts = min.costs + costs[min.zustand.map[move.from.zeile][move.from.spalte]]!!
                val newKnoten = Knoten(newZustand, newCosts)
                if (!allReachableStates.contains(newKnoten)){
                    allReachableStates.add(newKnoten)
                }else{
                    val ind = allReachableStates.indexOf(newKnoten)
                    val oldKnoten = allReachableStates[ind]
                    oldKnoten.costs = minOf(oldKnoten.costs, newKnoten.costs)
                }
            }
            //sortieren
            allReachableStates.sortBy { it.costs }
        }
        return allReachableStates[0].costs
    }

    fun part1(input: List<String>): ULong {
        val map = input.map { it.replace(' ','#') }
        deepth = 2
        /*maxCosts = 15000UL
        return makeBestMoves(map)*/
        return shortestPath(map)
    }

    fun part2(input: List<String>): ULong {
        val map = input.map { it.replace(' ','#') }
        deepth = 4
        return shortestPath(map)
        //maxCosts = 44200UL
        //maxCosts = ULong.MAX_VALUE
        //return makeBestMoves(map)
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day23"

    //check(part1(readInput(dayname+"_test")) == 12521UL)
    //println("Part 1, Tested passed")
    //print("Part 1: ")
    //println(part1(readInput(dayname)))

    //check(part2(readInput(dayname+"_p2_test")) == 44169UL)
    //println("Part 2, Tested passed")
    print("Part 2: ")
    println(part2(readInput(dayname+"_p2")))


}
