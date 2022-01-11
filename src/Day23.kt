import java.util.*
import kotlin.collections.ArrayList

//typealias Map = List<String> //zur Optimierung ggf. nochmal mit List<List<Char>>, oder fixen Arrays umsetzen.
//auf jeden Fall ohne Strings, und nur mit dem Abspeichern des nötigsten

fun main() {
    fun toString(map: List<String>): String{
        var s = ""
        for (row in map){
            s += row + "\n"
        }
        return s
    }


    /*data class Map(var map: List<String>){
        //Methoden "frei" (MatInd): boolean, "getPlayer": List<Char>, player(MatInd): Char
    }*/


    data class MatInd(var z: Byte, var s: Byte){
        constructor(z: Int, s: Int) : this(z.toByte(),s.toByte())
        constructor(z: Int, s: Byte) : this(z.toByte(),s)
        constructor(z: Byte, s: Int) : this(z,s.toByte())
        fun above(): MatInd{
            return MatInd(z-1, s)
        }
        fun left(): MatInd{
            return MatInd(z, s-1)
        }
        fun right(): MatInd{
            return MatInd(z, s+1)
        }
        fun under(): MatInd{
            return MatInd(z+1, s)
        }
        fun zeile(): Int{
            return this.z.toInt()
        }
        fun spalte(): Int{
            return this.s.toInt()
        }
    }

    data class Move(val from: MatInd, val to: MatInd)

    val goals = mutableMapOf('A' to 3, 'B' to 5, 'C' to 7, 'D' to 9)

    fun getAllPossibleMoves(map: List<String>, lastMove: Move?, deepth: Int): List<Move>{
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
                return (goals[map[p.zeile()][p.spalte()]] == p.spalte()) //Zielspalte = eigene Spalte
        }
        fun columnIsWrong(s: Int, deepth: Int): Boolean{
            for (z in 2..deepth+1){ //gehe alle Zeilen durch
                val p = map[z][s] //Der Eintrag in der Zeile,Spalte
                if ((p in goals.keys) && !playerInCorrectColumn(MatInd(z,s)) ){ //ist dieser Eintrag ein Spieler, und ist dieser in der falschen Spalte
                        return true //so ist die spalte falsch
                }
            }
            return false
        }

        fun immediatelyOutsideAnyRoom(p: MatInd): Boolean{
            val f = p.under()
            return ( (p.zeile()==1) && (map[f.zeile()][f.spalte()]!='#') )
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
            if ((to != lastMove.from) && (map[to.zeile()][to.spalte()]=='.')){ //wenn darüber frei ist
                //nach oben nur, wenn er in falschen Spalte ist, oder in seiner Spalte ein falscher Player ist
                //ist man in falschen spalte, so ist die Spalte auch falsch! (falls man IN dem Raum steht)
                if (columnIsWrong(p.spalte(), deepth)){
                    posMoves.add(Move(p,to))
                    return posMoves
                }
            }
            to = p.under() //nach unten nur, wenn er in richtige Spalte läuft
            val goalCol = goals[map[p.zeile()][p.spalte()]]!!
            if ((to != lastMove.from) && (!columnIsWrong(goalCol, deepth)) && playerInCorrectColumn(p) && (map[to.zeile()][to.spalte()]=='.') ){
                posMoves.add(Move(p,to))
                return posMoves
            }

            if ( (p.zeile()==1) && ( (!columnIsWrong(goalCol, deepth)) && freePath(p.spalte(),goalCol) )){
                to = p.right()
                if ( (to != lastMove.from) && (p.spalte() < goalCol) && (map[to.zeile()][to.spalte()]=='.') ){
                    posMoves.add(Move(p,to))
                }
                to = p.left()
                if ( (to != lastMove.from) && (p.spalte() > goalCol) && (map[to.zeile()][to.spalte()]=='.') ){
                    posMoves.add(Move(p,to))
                }
                return posMoves
            }else{
                to = p.left()
                if ( (to != lastMove.from) && (map[to.zeile()][to.spalte()]=='.')){
                    posMoves.add(Move(p,to))
                }
                to = p.right()
                if ( (to != lastMove.from) && (map[to.zeile()][to.spalte()]=='.')){
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
                if ( (map[to.zeile()][to.spalte()]=='.') && columnIsWrong(p.spalte(), deepth) ){ //wenn darüber frei ist
                    //nach oben nur, wenn er in falschen Spalte ist, oder in seiner Spalte ein falscher Player ist
                        //ist man in falschen spalte, so ist die Spalte auch falsch!
                    posMoves.add(Move(p,to))
                }
                val goalCol = goals[map[p.zeile()][p.spalte()]]!!
                if ( (p.zeile()==1) && ( (!columnIsWrong(goalCol, deepth)) && freePath(p.spalte(),goalCol) )){
                    //kann ich ins Ziel? = Zielspalte ist bisher richtig und WegZumZiel ist frei
                    to = p.under()
                    if (playerInCorrectColumn(p) && (map[to.zeile()][to.spalte()]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                    to = p.right()
                    if ( (p.spalte() < goalCol) && (map[to.zeile()][to.spalte()]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                    to = p.left()
                    if ( (p.spalte() > goalCol) && (map[to.zeile()][to.spalte()]=='.') ){
                        posMoves.add(Move(p,to))
                    }
                }
            }
        }
        return posMoves
    }

    fun isReady(map: List<String>, deepth: Int): Boolean{
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
        newmap[move.to.zeile()][move.to.spalte()] = map[move.from.zeile()][move.from.spalte()]
        newmap[move.from.zeile()][move.from.spalte()] = '.'
        return newmap.map { String(it) }
    }

    val costs = mutableMapOf('A' to 1UL, 'B' to 10UL, 'C' to 100UL, 'D' to 1000UL)

    data class ZustandLowRam(val player: List<Pair<MatInd,Char>>, val lastMove: Move?){
        override fun equals(other: Any?): Boolean {
            return (other is ZustandLowRam)&&(other.player==this.player)//&&(other.lastMove == this.lastMove ||( (other.lastMove!=null)&&(this.lastMove!=null)&&(other.lastMove.to == this.lastMove.to)) )
        }
    }
    /*data class Zustand(val map: List<String>, val lastMove: Move?){
        override fun equals(other: Any?): Boolean {
            return (other is Zustand)&&(other.map==this.map) //&&(other.lastMove == this.lastMove ||( (other.lastMove!=null)&&(this.lastMove!=null)&&(other.lastMove.to == this.lastMove.to)) )
        }
    }*/
    data class rekZustand(val map: List<String>, val lastMove: Move?, var lastZustand: rekZustand? = null)

    fun getPlayerAndPos(map: List<String>): List<Pair<MatInd,Char>>{
        val player = ArrayList<Pair<MatInd,Char>>()
        for (z in map.indices){
            for (s in map[z].indices){
                val c = map[z][s]
                if (c in 'A'..'D'){
                    player.add(Pair(MatInd(z,s),c))
                }
            }
        }
        return player
    }
    fun shortestPath(map: List<String>, deepth: Int): ULong{
        data class Knoten(val zustand: rekZustand, var costs: ULong){
            override fun equals(other: Any?): Boolean {
                return (other is Knoten) && (zustand.equals(other.zustand))
            }
        }
        val allReachableStates = PriorityQueue<Knoten>(compareBy{it.costs})
        val allReachableStatesMap = mutableMapOf<ZustandLowRam, ULong>()
        val alreadyReachedStates = mutableSetOf<ZustandLowRam>()


        var min = Knoten(rekZustand(map, null), 0UL)
        while ( !isReady(min.zustand.map, deepth) ){
            val posMoves = getAllPossibleMoves(min.zustand.map,min.zustand.lastMove, deepth)
            for (move in posMoves){
                var newZustand = rekZustand(makeMove(min.zustand.map,move),move)
                var newCosts = min.costs + costs[min.zustand.map[move.from.zeile()][move.from.spalte()]]!!
                var newMoves = getAllPossibleMoves(newZustand.map, newZustand.lastMove,deepth)
                while (newMoves.size==1){
                    newZustand = rekZustand(makeMove(newZustand.map,newMoves[0]), newMoves[0])
                    newCosts += costs[newZustand.map[newMoves[0].to.zeile()][newMoves[0].to.spalte()]]!!
                    newMoves = getAllPossibleMoves(newZustand.map, newMoves[0],deepth)
                }
                newZustand.lastZustand = min.zustand
                val newKnoten = Knoten(newZustand, newCosts)
                val newZustandLowRam = ZustandLowRam(getPlayerAndPos(newZustand.map), newZustand.lastMove)
                val oldCosts = allReachableStatesMap.getOrDefault(newZustandLowRam, ULong.MAX_VALUE)
                val c =  (oldCosts == ULong.MAX_VALUE)
                val b = (oldCosts > newKnoten.costs) // ist der Zustand aber schon mit geringeren Kosten vorhanden, dann nicht
                if (b){
                    allReachableStatesMap[newZustandLowRam] = newKnoten.costs
                    if (!c){
                        allReachableStates.remove(newKnoten)
                        allReachableStates.add(newKnoten)
                    }
                }
                if (!alreadyReachedStates.contains(newZustandLowRam) && c){
                    allReachableStates.add(newKnoten)
                }
            }
            min = allReachableStates.remove()
            alreadyReachedStates.add(ZustandLowRam(getPlayerAndPos(min.zustand.map), min.zustand.lastMove))
            allReachableStatesMap.remove(ZustandLowRam(getPlayerAndPos(min.zustand.map), min.zustand.lastMove))
        }
        /*fun gibAus(rZ: rekZustand){
            if (rZ.lastZustand!=null){
                gibAus(rZ.lastZustand!!)
            }
            println("")
            println(toString(rZ.map))
        }
        gibAus(min.zustand)*/
        return min.costs
    }

    fun part1(input: List<String>): ULong {
        val map = input
        return shortestPath(map, 2)
    }

    fun part2(input: List<String>): ULong {
        val map = (input.subList(0,3) + "  #D#C#B#A#" + "  #D#B#A#C#" + input.subList(3,5))
        return shortestPath(map,4)
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day23"

    val testInput = readInput(dayname+"_test")
    val input = readInput(dayname)

    check(part1(testInput) == 12521UL)
    println("Part 1, Tested passed")
    println("Part 1: " + part1(input))

    check(part2(testInput) == 44169UL)
    println("Part 2, Tested passed")
    println("Part 2: " + part2(input))




}
