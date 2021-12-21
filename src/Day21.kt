import java.math.BigInteger

fun main() {
    fun part1(p1: Int, p2: Int): Int {
        var n = 0
        var x1 = p1
        var x2 = p2
        var points1 = 0
        var points2 = 0
        var turn1 = true
        var rolled = 0
        while (points1 < 1000 && points2 < 1000){
            rolled++
            if (turn1){
                for (i in 1..3){
                    n = n%100 + 1
                    x1 = ((x1+n-1) % 10)+1
                }
                points1 += x1
            }else{
                for (i in 1..3){
                    n = n%100 + 1
                    x2 = ((x2+n-1) % 10)+1
                }
                points2 += x2
            }
            turn1 = !turn1
        }
        if (points1 >= 1000){
            return points2*rolled*3
        }else{
            return points1*rolled*3
        }
    }

    fun createWonList(pos: Int, points: Int, anzTurns: Int, gew: BigInteger, list: MutableMap<Int,BigInteger>){
        if (points >= 21){
            if (list.containsKey(anzTurns)){
                list[anzTurns] = list[anzTurns]!! + gew
            }else{
                list.put(anzTurns, gew)
            }
        }else{
            createWonList(((pos+3 -1 )%10)+1, points+((pos+3 -1 )%10)+1, anzTurns+1, gew, list)
            createWonList(((pos+4 -1 )%10)+1, points+((pos+4 -1 )%10)+1, anzTurns+1, gew*3.toBigInteger(), list)
            createWonList(((pos+5 -1 )%10)+1, points+((pos+5 -1 )%10)+1, anzTurns+1, gew*6.toBigInteger(), list)
            createWonList(((pos+6 -1 )%10)+1, points+((pos+6 -1 )%10)+1, anzTurns+1, gew*7.toBigInteger(), list)
            createWonList(((pos+7 -1 )%10)+1, points+((pos+7 -1 )%10)+1, anzTurns+1, gew*6.toBigInteger(), list)
            createWonList(((pos+8 -1 )%10)+1, points+((pos+8 -1 )%10)+1, anzTurns+1, gew*3.toBigInteger(), list)
            createWonList(((pos+9 -1 )%10)+1, points+((pos+9 -1 )%10)+1, anzTurns+1, gew, list)
        }
    }

    fun createNotWonList(pos: Int, points: Int, anzTurns: Int, gew: BigInteger, list: MutableMap<Int,BigInteger>){
        if (points < 21){
            if (list.containsKey(anzTurns)){
                list[anzTurns] = list[anzTurns]!! + gew
            }else{
                list.put(anzTurns, gew)
            }
            createNotWonList(((pos+3 -1 )%10)+1, points+((pos+3 -1 )%10)+1, anzTurns+1, gew, list)
            createNotWonList(((pos+4 -1 )%10)+1, points+((pos+4 -1 )%10)+1, anzTurns+1, gew*3.toBigInteger(), list)
            createNotWonList(((pos+5 -1 )%10)+1, points+((pos+5 -1 )%10)+1, anzTurns+1, gew*6.toBigInteger(), list)
            createNotWonList(((pos+6 -1 )%10)+1, points+((pos+6 -1 )%10)+1, anzTurns+1, gew*7.toBigInteger(), list)
            createNotWonList(((pos+7 -1 )%10)+1, points+((pos+7 -1 )%10)+1, anzTurns+1, gew*6.toBigInteger(), list)
            createNotWonList(((pos+8 -1 )%10)+1, points+((pos+8 -1 )%10)+1, anzTurns+1, gew*3.toBigInteger(), list)
            createNotWonList(((pos+9 -1 )%10)+1, points+((pos+9 -1 )%10)+1, anzTurns+1, gew, list)
        }
    }

    fun part2(p1: Int, p2: Int): BigInteger {
        val wons1 = mutableMapOf<Int,BigInteger>()
        createWonList(p1,0,0,1.toBigInteger(),wons1)
        val notWons1 = mutableMapOf<Int,BigInteger>()
        createNotWonList(p1,0,0,1.toBigInteger(),notWons1)

        val wons2 = mutableMapOf<Int,BigInteger>()
        createWonList(p2,0,0,1.toBigInteger(),wons2)
        val notWons2 = mutableMapOf<Int,BigInteger>()
        createNotWonList(p2,0,0,1.toBigInteger(),notWons2)

        var w1 = 0.toBigInteger()
        var w2 = 0.toBigInteger()
        for (m1 in wons1.keys){
            if (notWons2.containsKey(m1-1)){
                w1 += wons1[m1]!! * notWons2[m1-1]!!
            }
        }
        for (m2 in wons2.keys){
            if (notWons1.containsKey(m2)) {
                w2 += wons2[m2]!! * notWons1[m2]!!
            }
        }
        return maxOf(w1,w2)
    }

    check(part1(4,8) == 739785)
    check(part2(4,8) == 444356092776315.toBigInteger())


    println(part1(10,3))
    println(part2(10,3))
}
