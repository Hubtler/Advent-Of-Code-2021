fun main() {
    class DumboOctopus {
        var lastFlash: Int //day
        var energy: Int //between zero and 9
        var totalFlashes: Int
        var neighbours: ArrayList<DumboOctopus>


        constructor(energyLevel: Int) {
            neighbours = ArrayList<DumboOctopus>()
            lastFlash = -1
            energy = energyLevel
            totalFlashes = 0
        }

        fun addNeighbour(n: DumboOctopus){
            neighbours.add(n)
        }

        override fun equals(other: Any?): Boolean{
            return ((other is DumboOctopus)&&(other.lastFlash==lastFlash)&&(other.energy==energy))
        }

        override fun hashCode(): Int {
            return lastFlash*10 + energy
        }

        fun addEnergy(day: Int){
            if (lastFlash < day){
                if (++energy>9){
                    flash(day)
                }
            }
        }

        fun flash(day: Int){
            if (lastFlash < day){
                energy = 0
                totalFlashes++
                lastFlash = day
                for (n in neighbours){
                    n.addEnergy(day)
                }
            }
        }
    }

    fun checkInd(i0: Int, j0: Int, iMax: Int, jMax: Int): Boolean{
        return ( (i0 >= 0) && (i0 <= iMax) && (j0 >= 0) && (j0 <= jMax) )
    }

    fun createOctis(input: List<String>): ArrayList<ArrayList<DumboOctopus>>{
        val octis = ArrayList<ArrayList<DumboOctopus>>()
        input.map{
            val row = ArrayList<DumboOctopus>()
            it.toCharArray().map{
                val e = it.digitToInt()
                row.add(DumboOctopus(e))
            }
            octis.add(row)
        }
        val n = octis.size
        val m = octis[0].size

        for (i0 in 0..n-1){
            for (j0 in 0..m-1){
                for (i in -1..1){
                    for (j in -1..1){
                        if ( (i!=0 || j!=0) && checkInd(i0+i,j0+j,n-1,m-1) ){
                            octis[i0][j0].addNeighbour(octis[i0+i][j0+j])
                        }
                    }
                }
            }
        }
        return octis
    }

    fun part1(input: List<String>): Int {
        val octis = createOctis(input)
        for (day in 1..100){
            for (ocRow in octis){
                for (oc in ocRow){
                    oc.addEnergy(day)
                }
            }
        }
        var totalFlashes = 0
        for (ocRow in octis){
            for (oc in ocRow){
                totalFlashes += oc.totalFlashes
            }
        }
        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        val octis = createOctis(input)
        var day = 0
        var allOctiesFlashed = false
        while (!allOctiesFlashed){
            day++
            for (ocRow in octis){
                for (oc in ocRow){
                    oc.addEnergy(day)
                }
            }
            allOctiesFlashed = true
            for (ocRow in octis){
                for (oc in ocRow){
                    allOctiesFlashed = allOctiesFlashed && (oc.lastFlash==day)
                }
            }
        }
        return day
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day11"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
