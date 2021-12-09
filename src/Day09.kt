data class Point(var x: Int = 0, var y: Int = 0)

fun main() {
    fun getArray(input: List<String>): ArrayList<List<Int>>{
        val output = ArrayList<List<Int>>()
        for (inp in input){
            val r = inp.toCharArray().map { it.toString().toInt() }
            output.add(r)
        }
        return output
    }

    fun checkInd(i0: Int, j0: Int, iMax: Int, jMax: Int): Boolean{
       return ( (i0 >= 0) && (i0 <= iMax) && (j0 >= 0) && (j0 <= jMax) )
    }

    fun getMins(output: ArrayList<List<Int>>): ArrayList<Array<Int>>{
        val out = ArrayList<Array<Int>>()
        val oz = output.size
        for (i0 in 0..oz-1){
            val w = output[i0].size
            for (j0 in 0..w-1){
                var isMin = true
                for (i in -1..1){
                    for (j in -1..1){
                        if  ( checkInd (i+i0,j+j0,oz-1,w-1) && (kotlin.math.abs(i) + kotlin.math.abs(j) ==1) ){
                            isMin = isMin && (output[i0][j0] < output[i0+i][j0+j])
                        }
                    }
                }
                if (isMin){
                    out.add(arrayOf(i0,j0))
                }
            }
        }
        return out
    }

    fun part1(input: List<String>): Int {
        val output = getArray(input)
        val mins = getMins(output)
        var sum = 0
        for (min in mins) {
            sum += output[min[0]][min[1]] + 1
        }
        return sum
    }

    fun checkAround(output: ArrayList<List<Int>>, inBasis: ArrayList<Point>, i0: Int, j0: Int): Int{
        var basiselements = 0
        if (
            (checkInd (i0,j0,output.size-1,output[0].size-1)) &&
            (!inBasis.contains(Point(i0,j0))) &&
            (output[i0][j0] < 9)
        ){
            inBasis.add(Point(i0,j0))
            basiselements++
            basiselements += checkAround(output, inBasis, i0-1,j0)
            basiselements += checkAround(output, inBasis, i0+1,j0)
            basiselements += checkAround(output, inBasis, i0,j0-1)
            basiselements += checkAround(output, inBasis, i0,j0+1)
        }
        return basiselements
    }

    fun part2(input: List<String>): Int {
        val output = getArray(input)
        val mins = getMins(output)
        val anzmin = mins.size
        val basisSizes = IntArray(mins.size)
        for (i in 0..anzmin-1){
            val basisElements = ArrayList<Point>()
            basisSizes[i] = checkAround(output,basisElements,mins[i][0],mins[i][1])
        }
        basisSizes.sort()
        basisSizes.reverse()
        return basisSizes[0] * basisSizes[1] * basisSizes[2]
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day09"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
