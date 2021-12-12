fun main() {
    class Knoten {
        var name: String
        var big: Boolean
        var neighbours: ArrayList<Knoten>


        constructor(name: String) {
            neighbours = ArrayList<Knoten>()
            this.name = name
            big = name[0].isUpperCase()
        }

        override fun equals(other: Any?): Boolean{
            return ((other is Knoten)&&(other.name==name))
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun toString(): String {
            //return name + " | " + big.toString() + " | neighbours-count: " + neighbours.size
            return name
        }

        fun addNeighbour(n: Knoten) {
            neighbours.add(n)
        }
    }

    class Graph {
        var g: MutableMap<String, Knoten>
        var start: Knoten
        var end: Knoten

        constructor(){
            g = mutableMapOf<String, Knoten>()
            start = Knoten("start")
            end = Knoten("end")
            g.put("end",end)
        }

        fun getWithAdd(kName: String): Knoten{
            var k: Knoten
            if (g.containsKey(kName)) {
                k = g.get(kName)!!
            } else {
                k = Knoten(kName)
                if (kName=="start") {
                    k = start
                }else{
                    g.put(kName, k)
                }
            }
            return k
        }

        fun addKante(kName1: String, kName2: String) {
            val vK = getWithAdd(kName1)
            val zK = getWithAdd(kName2)
            if (vK!=end && zK!=start){
                vK.addNeighbour(zK)
            }
            if (zK!=end && vK!=start){
                zK.addNeighbour(vK)
            }
        }
    }

    fun getGraph(input: List<String>): Graph {
        val g = Graph()
        for (inp in input) {
            val ks = inp.split('-')
            g.addKante( ks[0], ks[1])
        }
        return g
    }

    //return: Wieviele mögliche Wege haben zum Ende geführt
    fun goToKnoten(von: Knoten, abgelaufen: MutableList<Knoten>, ende: Knoten): Int{
        var c = 0
        if (abgelaufen.contains(von)){
            return 0
        }else{
            if (von.equals(ende)){
                return 1
            }
        }
        if (!von.big){ //falls ich nur einmal ablaufen darf, mache ich das ja gerade
            abgelaufen.add(von)
        }
        for (n in von.neighbours){
            val abgelaufenN = abgelaufen.toMutableList()
            c += goToKnoten(n,abgelaufenN, ende)
        }
        return c
    }

    fun part1(input: List<String>): Int {
        val g = getGraph(input)
        val abgelaufen = ArrayList<Knoten>()
        val anz = goToKnoten(g.start,abgelaufen,g.end)
        return anz
    }

    fun goToKnoten2(von: Knoten, abgelaufen1: MutableList<Knoten>, abgelaufen2: MutableList<Knoten>, ende: Knoten): Int{
        var c = 0
        if (abgelaufen2.size==1 && abgelaufen1.contains(von)){
            return 0
        }else{
            if (von.equals(ende)){

                return 1
            }
        }
        if (!von.big){ //falls ich nur einmal ablaufen darf, mache ich das ja gerade
            if (abgelaufen1.contains(von)){
                abgelaufen2.add(von)
            }else{
                abgelaufen1.add(von)
            }
        }
        for (n in von.neighbours){
            val abgelaufen1N = abgelaufen1.toMutableList()
            val abgelaufen2N = abgelaufen2.toMutableList()
            c += goToKnoten2(n,abgelaufen1N, abgelaufen2N, ende)
        }
        return c
    }

    fun part2(input: List<String>): Int {
        val g = getGraph(input)
        val abgelaufen = ArrayList<Knoten>()
        val abgelaufen2 = ArrayList<Knoten>()
        val anz = goToKnoten2(g.start,abgelaufen,abgelaufen2,g.end)
        return anz
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day12"

    var testInput = readInput(dayname+"_test0")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    testInput = readInput(dayname+"_test")
    check(part1(testInput) == 19)
    check(part2(testInput) == 103)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
