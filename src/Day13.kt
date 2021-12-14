import kotlin.math.abs

fun main() {
    data class Point(var x: Int = 0, var y: Int = 0)

    fun foldX(dots: ArrayList<Point>, x0: Int): ArrayList<Point>{
        val newDots = ArrayList<Point>()
        for (dot in dots){
            val newP = Point()
            newP.x = x0 - abs(x0-dot.x)
            newP.y = dot.y
            if (!newDots.contains(newP)){
                newDots.add(newP)
            }
        }
        return newDots
    }

    fun foldY(dots: ArrayList<Point>, y0: Int): ArrayList<Point>{
        val newDots = ArrayList<Point>()
        for (dot in dots){
            val newP = Point()
            newP.x = dot.x
            newP.y = (y0) - abs(y0-dot.y)
                if (!newDots.contains(newP)){
                newDots.add(newP)
            }
        }
        return newDots
    }

    fun draw(dots: ArrayList<Point>): String{
        val maxx = dots.maxOf { it.x }
        val maxy = dots.maxOf { it.y }
        val s = ArrayList<String>()
        var str = ""
        for (i in 0..maxx){
            str += ' '
        }
        for (j in 0..maxy){
            s.add(str)
        }
        for (p in dots){
            val st =  s[p.y].toCharArray()
            st[p.x] = '0'
            s[p.y] = st.joinToString("")
        }
        var r = ""
        for (row in s){
            r += row + '\n'
        }
        return r
    }

    fun doParts(input: List<String>, onlyFirst: Boolean): ArrayList<Point>{
        var dots = ArrayList<Point>()
        for (inp in input){
            if (inp.isNotEmpty()) {
                if (inp.contains("fold along")) {
                    val s = inp.split('=')
                    val xy = s[1].toInt()
                    if (s[0].last() == 'x') {
                        dots = foldX(dots, xy)
                    } else {
                        dots = foldY(dots, xy)
                    }
                    if (onlyFirst){
                        break //nur erste Faltung durchführen
                    }
                } else {
                    val p = Point()
                    val s = inp.split(',')
                    p.x = s[0].toInt()
                    p.y = s[1].toInt()
                    dots.add(p)
                }
            }
        }
        return dots
    }

    fun part1(input: List<String>): Int {
        return doParts(input,true).size
    }

    fun part2(input: List<String>): Int {
        val dots =  doParts(input,false)
        println( draw(dots) )
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day13"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 17)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}
