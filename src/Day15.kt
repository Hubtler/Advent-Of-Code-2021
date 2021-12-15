fun main() {
    data class Knoten(var cost: Int){
        //var cost: Int
        var reachcost: Int
        var neighbours: ArrayList<Knoten>

        init {
            reachcost = Int.MAX_VALUE
            neighbours = ArrayList<Knoten>()
        }

        fun addNeighbour(n: Knoten) {
            neighbours.add(n)
            n.reachIt(n.cost + reachcost +1) //nur initialisieren, da wir aber nicht in alle Richtungen weitergehen, müssen wir das um einen erhöhen (sonst klappt reachIt) nicht
        }

        fun reachIt(newReachcosts: Int): Boolean{
            if (newReachcosts < reachcost){
                reachcost = newReachcosts
                return true
            }else{
                return false
            }
        }
    }

    data class Graph(var pflichtname: String) {
        var g: ArrayList<ArrayList<Knoten>>
        var start: Knoten? = null
        var end: Knoten? = null

        init{
            g = ArrayList<ArrayList<Knoten>>()
        }

        fun add(row: Int, cost: Int){
            end = Knoten(cost)
            if (start==null){
                start = end
                start!!.reachIt(0)
            }
            if (row >= g.size){
                g.add(ArrayList<Knoten>())
            }
            g[row].add(end!!)
        }

        fun setEdges(){
            for (i in g.indices){
                for (j in g[i].indices){
                    if (i+1 < g.size){
                        g[i][j].addNeighbour(g[i+1][j])
                    }
                    if (i-1 > 0){
                        g[i][j].addNeighbour(g[i-1][j])
                    }
                    if (j+1 < g[i].size){
                        g[i][j].addNeighbour(g[i][j+1])
                    }
                    if (j-1 > 0){
                        g[i][j].addNeighbour(g[i][j-1])
                    }
                }
            }
        }

        fun calcLowestCosts(k: Knoten, costs: Int){
            for (n in k.neighbours){
                if (n.reachIt(costs+n.cost)){
                    calcLowestCosts(n,n.reachcost)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val g = Graph("pflicht")
        for (ri in input.indices){
            input[ri].toCharArray().map{ g.add(ri, it.digitToInt()) }
        }
        g.setEdges()
        g.calcLowestCosts(g.start!!, 0)
        return g.g.last().last().reachcost
    }

    fun part2(input: List<String>): Int {
        val g = Graph("pflicht")
        for (cd in 0..4){ //untereinander kopieren
            for (ri in input.indices){
                for (cl in 0..4){ //nebeneinander kopieren
                    input[ri].toCharArray().map{ g.add(ri+cd*input.size, (it.digitToInt()+cl+cd-1)%9 + 1) }
                }
            }
        }
        g.setEdges()
        g.calcLowestCosts(g.start!!, 0)
        return g.g.last().last().reachcost
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day15"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
