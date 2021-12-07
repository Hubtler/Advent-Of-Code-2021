import java.math.BigInteger
//import kotlin.system.measureNanoTime

fun main() {
 /*   fun direkteNachfolger(alter: Int, days: Int): ArrayList<Int>{
        //Für einen Fisch mit Fortplanzungsdatum alter berechnen wir eine Liste aller direkten Nachkommen
        //Wir geben eine Liste mit dessen Zeitpunkt zur Fortplanzung (vom jetzigen Tag an) zurück
        //Wird ein Fisch in 2 Tagen geboren, und pflanzt sich in 8 Tagen fort, ist er jetzt 10. (bzw 11, da er sich erst nach Tag 0 fortpflanzt)
        val output = ArrayList<Int>()
        var rdays = days
        var nextZeugung = alter
        var x = 9 //9 Tage braucht er ja eh bis zur Erzeugung
        while (rdays-nextZeugung >= 0){
            x += nextZeugung //Soviele Tage noch bis er gezeugt wird
            output.add(x)
            rdays -= nextZeugung
            nextZeugung = 7 //Man soll auf 6 setzen (+1 da man erst einen Tag später zeugt)
        }
        return output
    }

    fun nachkommenGes(alter: Int, days: Int): Int{
        var anzNachfolger = 0
        var dN = direkteNachfolger(alter,days)
        anzNachfolger += dN.size
        //Nun die Nachfolger eines jedes Nachfolgers bestimmen
        for (n in dN){
            anzNachfolger += nachkommenGes(n,days)
        }
        return anzNachfolger
    }

    fun vorberechnen(days: Int): IntArray {
    //Für das Alter 0..9 eines Fisches, berechnen wir vor, wieviele Nachkommen er ingesamt (inkluse Nachkommen der Nachkommen) erzeugen wird
        val werte = IntArray(10){ _ -> 0 } //Für die Werte 0 bis 9 berechnen wir vor
        for (w in 1..9){ // Fall w=0 kann nicht eintreten, da am 0 Tag keiner kinder bekommt (ich rechne ja auch +1)
            werte[w] = nachkommenGes(w,days)
        }
        return werte
    }

    fun part1Old(input: List<String>): Int {
        val anzNachfolger = vorberechnen(80)
        var anzFisch = input.size
        for (inp in input){
            anzFisch += anzNachfolger[inp.toInt()+1]
        }
        return anzFisch
    }
*/
    fun vorberechnenP2(days: Int): ArrayList<BigInteger> {
        val v = ArrayList<BigInteger>(days)
        v.add(1.toBigInteger()) //Tag 0
        for (d in 1..7){
            v.add(2.toBigInteger()) //Tag 1 bis 7 (man hat ein Kind gekriegt + man selbst)
        }
        v.add(3.toBigInteger()) //Tag 8 (man selbst + Baby von jetzt + Baby an Tag 1)
        for (d in 9..days){
            v.add( v.get(d-7) + v.get(d-9)) //Man selbst bekommt 7 Tage später wieder ein Kind
            //Das Baby bekommt 9 Tage später wieder ein Kind
        }
        return v
    }

    fun part1(input: List<String>): Int {
        val days = 80
        val anzNachfolger = vorberechnenP2(days)
        var anzFisch = 0
        for (inp in input){
            anzFisch += anzNachfolger[days-inp.toInt()].toInt()
        }
        return anzFisch

    }

    fun part2(input: List<String>): BigInteger {
        val days = 256
        val anzNachfolger = vorberechnenP2(days)
        var anzFisch = 0.toBigInteger()
        for (inp in input){
            anzFisch += anzNachfolger[days-inp.toInt()]
        }
        return anzFisch
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day06_test")
    testInput = testInput[0].split(",")
    check(part1(testInput) == 5934)
    check(part2(testInput).equals(26984457539.toBigInteger()) )

    var input = readInput("Day06")
    input = input[0].split(",")
    println(part1(input))
    println(part2(input))

   /* var v = 0L
    val runs = 10
     for ( i in 0..runs-1){
         v+=measureNanoTime {   input = input[0].split(","); part2(input) } / 1000
     }
     println(v/runs)*/
}
