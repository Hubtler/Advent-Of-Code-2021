import kotlin.math.*
import kotlin.system.measureNanoTime

/*class point {
    var x: Int
    var y: Int

    constructor() {
        x = 0
        y = 0
    }

    constructor(initX: Int, initY: Int) {
        x = initX
        y = initY
    }

    override fun equals(other: Any?): Boolean{
        return ((other is point)&&(other.x==x)&&(other.y==y))
    }

    override fun hashCode(): Int {
        return x*100 + y
    }
}*/
data class point(var x: Int = 0, var y: Int = 0)

fun main() {
    fun readLn(input: List<String>): ArrayList<IntArray>{
        val output = ArrayList<IntArray>()
        for (z in input) {
            val r = z.split(" -> ").map { it.split(',').map { it.toInt() } }
            output.add((r[0]+r[1]).toIntArray())
        }
        return output
    }

     fun calcSchnittpunkt(x0: Int, x1: Int, y0: Int, y1:Int, xt0: Int, xt1: Int, yt0: Int, yt1:Int): ArrayList<point>{
        //println(listOf(x0, x1, y0, y1))

        val ps = ArrayList<point>()
        var m1 = 0
        var mt1 = 0
        if (x1==x0){
            //wenn x konstant ist, werden alle y Werte getroffen. Berechne also die y-Werte, die an x0 auf der zweiten Gerade liegen
            if (xt1==xt0) {
                //dann sind beide Parallel, schauen ob sie "aufeinander" liegen
                if (xt0==x0){
                    //ja, sie liegen aufeinander. Markiere alle (ganzzahligen) Punkte, die auf beiden Geraden liegen
                    for (y in max(min(y0,y1),min(yt0,yt1))..min(max(y0,y1),max(yt0,yt1))){
                        ps.add(point(x0,y))
                    }
                }else{
                    //andernfalls haben wir keinen Schnittpunkt
                }
            }else{ //wenn die zweite Gerade nicht parallel zur y-Achse ist, gibt es genau einen Schnittpunkt, an x = x0
                mt1 = (yt1-yt0)/(xt1-xt0)//Steigung der zweiten Gerade
                val p = point()
                p.x = x0
                p.y = (x0-xt0)*mt1 + yt0
                if ( (x0 <= max(xt0,xt1)) && (x0 >= min(xt0,xt1))
                    &&(p.y <= max(y0,y1)) && (p.y >= min(y0,y1))  ){
                    ps.add(p)
                }
            }
        }else {
            m1 = (y1 - y0) / (x1 - x0) //Steigung der ersten Gerade
        }
        if (xt1==xt0){ //erste Gerade ist nicht parellel zur y-Achse, erste schon
            if (x1!=x0){
                val p = point()
                p.x = xt0
                p.y = (xt0-x0)*m1 + y0
                if ( (xt0 <= max(x0,x1)) && (xt0 >= min(x0,x1))
                    &&(p.y <= max(yt0,yt1)) && (p.y >= min(yt0,yt1))  ){
                    ps.add(p)
                }
            }else{
                //dann habe ich schon die gemeinsamen Schnittpunkte hinzugefügt
            }
        }else {
            mt1 = (yt1-yt0)/(xt1-xt0)//Steigung der zweiten Gerade
        }

        val x = (yt0-y0) + x0*m1 - xt0*mt1
         val m = m1-mt1
        if (m == 0){
            //Linien sind parallel,
            if (y0 == (x0-xt0)*mt1 + yt0){ //nehme den Punkt an x0 und prüfe ob er auf beiden draufliegt, bzw dort gleich ist
                // markiere alle Punkte die auf beiden Geraden liegen
                for (x in max(min(x0, x1), min(xt0, xt1))..min(max(x0, x1), max(xt0, xt1))) {
                    ps.add(point(x, (x - x0) * m1 + y0))// = (x-xt0)*mt1 + yt0
                    //ps.add(point(x, (x-xt0)*mt1 + yt0))
                    //println("tritt ein mit x="+x.toString() + " y=" + ((x - x0) * m1 + y0).toString())
                 }
            }else {
                // liegen nebeneinander
            }
        }else{ //Sie sind nicht parallel, haben also einen Schnittpunkt
            val p = point()
            if (x%m==0){
                p.x = x / m
                if ( (p.x >= min(x0,x1)) && (p.x <= max(x0,x1))
                    && (p.x >= min(xt0,xt1)) && (p.x <= max(xt0,xt1))
                ) { //falls dieser auch gültig ist, und auf beiden Geraden liegt
                    p.y = (p.x-x0)*m1 + y0 // = (x-xt0)*mt1 + yt0
                    ps.add(p)
                }
            }
            //println((x-x0)*m1 + y0)
            //println((x-xt0)*mt1 + yt0)
        }
        return ps
    }

    fun part1(input: List<String>): Int {
        val lines = ArrayList<IntArray>()
        val xy = readLn(input)
        for (v in xy){
            if ( (v[0] == v[2])||(v[1]==v[3]) ){
                lines.add(v)
            }
        }
        //wieviele Schnittpunkte gibt es? (Ein Schnittpunkt, an dem sich x Linien schneiden ist 1,
        // bzw. es ist zu beachten, ob der Schnittpunkt zweier Linien schonmal Schnittpunkt zweier anderer Linien war
        val s = lines.size
        val sp = ArrayList<point>()
        for (i in 0..s-1){
            for (j in i+1..s-1){
                //Schnittpunkt von lines[i] mit lines[j] berechnen
                val ps = calcSchnittpunkt(lines[i][0],lines[i][2],lines[i][1],lines[i][3], lines[j][0],lines[j][2],lines[j][1],lines[j][3])
                for (p in ps){
                    /*println("Schnittpunkt")
                    println(p.x.toString() + "|" + p.y.toString())
                    println("Kommt von den Geraden:")
                    println(lines[i].toList())
                    println(lines[j].toList())*/
                    if (!sp.contains(p)){
                        sp.add(p)//Schnittpunkt abspeichern, falls noch nicht vorhanden
                    }
                }
            }
        }
        return sp.size
    }

    fun part2(input: List<String>): Int {
        val lines = readLn(input)
        val s = lines.size
        val sp = ArrayList<point>()
        for (i in 0..s-1){
            for (j in i+1..s-1){
                val ps = calcSchnittpunkt(lines[i][0],lines[i][2],lines[i][1],lines[i][3], lines[j][0],lines[j][2],lines[j][1],lines[j][3])
                sp.addAll(ps)
                /*for (p in ps){
                    if (!sp.contains(p)){
                        sp.add(p)//Schnittpunkt abspeichern, falls noch nicht vorhanden
                    }
                }*/
            }
        }
        //println(sp.groupingBy { it }.eachCount().map { it.key }.size)
        return sp.distinct().size
        //return sp.size
    }

    val dayname = "Day05"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12 )

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

    /*var v = 0L
    for ( i in 0..9){
        v+=measureNanoTime { part2(input) } / 1000
    }
    println(v/10)/*
}