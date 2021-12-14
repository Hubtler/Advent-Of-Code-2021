fun main(){

    fun doIt(input: List<String>, days: Int): Int{
        var nP = ""
        var cP = input[0]
        val rules = mutableMapOf<String, String>()
        for (i in input.indices){
            if (input[i].contains(" -> ")){
                val r = input[i].split(" -> ")
                rules.put(r[0], r[1])
            }
        }
        for (i in 1..days){
            for (ci in 0..cP.length-2){
                nP += cP[ci]
                val k = cP[ci].toString()+cP[ci+1]
                if (rules.contains(k)){
                    nP += rules[k]
                }
            }
            nP += cP.last()
            cP = nP
            nP = ""
        }
        val count = cP.toCharArray().groupBy{ it }.map { it.value.size }
        return count.maxOf { it } - count.minOf { it }
    }

    fun part1(input: List<String>): Int {
        return doIt(input,10)
    }

    fun doItBetter(input: List<String>, days: Int): ULong{
        fun addOne(m: MutableMap<String, ULong>, k: String, v: ULong = 1UL){
            if (m.containsKey(k)){
                m.replace(k,v + m.get(k)!!)
            }else{
                m.put(k, v)
            }
        }
        var cP = mutableMapOf<String, ULong>()
        for (ci in 0..input[0].length-2){
            val k = input[0][ci].toString()+input[0][ci+1]
            addOne(cP,k)
        }
        val rules = mutableMapOf<String, String>()
        for (i in input.indices){
            if (input[i].contains(" -> ")){
                val r = input[i].split(" -> ")
                rules.put(r[0], r[1])
            }
        }
        for (i in 1..days){
            val nP = mutableMapOf<String, ULong>()
            for (k in cP.keys){
                if (rules.contains(k)){
                    val nl = rules.get(k)!!
                    addOne(nP,k[0]+nl,cP.get(k)!!)
                    addOne(nP,nl+k[1],cP.get(k)!!)
                }
            }
            cP = nP //.toMutableMap()
        }
        val alpCount = ArrayList<ULong>()
        for (a in 'A'..'Z'){
            var c = 0UL
            for (k in cP.keys){ //immer nur linken zählen, sonst zähle ich doppelt
                if (k[0]==a){
                    c += cP.get(k)!!
                }
            }
            //letzte Buchstabe muss noch gezählt werden. Zum Glück ändert dieser sich nie:
            if (a == input[0].last()){
                c += 1UL
            }
            alpCount.add(c)
        }
        return alpCount.maxOf { it } - alpCount.filter { it > 0UL }.minOf { it }
    }

    fun part2(input: List<String>): ULong {
        return doItBetter(input,40)
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day14"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529UL)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
