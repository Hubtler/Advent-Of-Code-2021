import java.io.File

fun main() {
    data class AbstractDigit(var value: Int ){
        var inputNr: Int? = null
        var biggerDigit: AbstractDigit? = null
        var smallerDigit: AbstractDigit? = null

        fun copyWithoutBigSmall(): AbstractDigit{
            val s = AbstractDigit(value)
            s.inputNr = inputNr
            return s
        }

        fun isDigit(): Boolean{
            return (inputNr==null)
        }

        fun minValue(): Int{
            if (isDigit()){
                return value
            }else{
                return value + 1
            }
        }
        fun maxValue(): Int{
            if (isDigit()){
                return value
            }else{
                return value + 9
            }
        }

        override fun toString(): String {
            if (isDigit()){
                return value.toString()
            }else{
                if (value > 0){
                    return "input[" + inputNr + "] + " + value
                }
                if (value == 0){
                    return "input[" + inputNr + "]"
                }
                //otherwise value < 0
                return "input[" + inputNr + "] " + value
            }
        }
    }

    class Sys26{
        var smallest: AbstractDigit
        var biggest: AbstractDigit

        constructor( initValue: Int = 0){
            smallest = AbstractDigit(initValue % 26)
            biggest = smallest
            var next = initValue / 26
            while (next > 0){
                val nextDigit = AbstractDigit(next % 26)
                newBiggest(nextDigit)
                next = next / 26
            }
        }
        fun newSmallest( s: AbstractDigit ){
            //s.smallerDigit = null
            smallest.smallerDigit = s
            s.biggerDigit = smallest
            smallest = s
        }
        fun newBiggest( b: AbstractDigit ){
            //b.biggerDigit = null
            biggest.biggerDigit = b
            b.smallerDigit = biggest
            biggest = b
        }
        fun copy(): Sys26{
            var s = Sys26(smallest.value)
            s.smallest.inputNr = smallest.inputNr

            var next = smallest
            while (next.biggerDigit != null){
                next = next.biggerDigit!!
                val sNext = next.copyWithoutBigSmall()
                s.newBiggest(sNext)
            }
            return s
        }
        override fun toString(): String {
            var s = biggest.toString()
            var next = biggest
            while(next.smallerDigit != null){
                next = next.smallerDigit!!
                s += " | " + next.toString()
            }
            return s
        }
        fun isZero(): Boolean{
            var next = smallest
            var b = (next.isDigit() && (next.value==0) )
            while (next.biggerDigit != null){
                next = next.biggerDigit!!
                b = b && (next.isDigit() && (next.value==0) )
            }
            return b
        }
        fun isOne(): Boolean{
            var next = smallest
            var b = (next.isDigit() && (next.value==1) )
            while (next.biggerDigit != null){
                next = next.biggerDigit!!
                b = b && (next.isDigit() && (next.value==0) )
            }
            return b
        }
        fun inputIndepent(): Boolean{
            var b = biggest.isDigit()
            var next = biggest
            while (next.smallerDigit != null) {
                next = next.smallerDigit!!
                b = b && next.isDigit()
            }
            return b
        }
        fun value(): Long {
            var v = biggest.value.toLong()
            var next = biggest
            while (next.smallerDigit != null) {
                next = next.smallerDigit!!
                v = v * 26L + next.value.toLong()
            }
            return v
        }
        fun minValue(): Long{
            var v = biggest.minValue().toLong()
            var next = biggest
            while (next.smallerDigit != null) {
                next = next.smallerDigit!!
                v = v * 26L + next.minValue().toLong()
            }
            return v
        }
        fun maxValue(): Long{
            var v = biggest.maxValue().toLong()
            var next = biggest
            while (next.smallerDigit != null) {
                next = next.smallerDigit!!
                v = v * 26L + next.maxValue().toLong()
            }
            return v
        }
    } //Ende der Klasse Sys26

    fun getNextInput( inputNr: Int ): Sys26{
        val s = Sys26()
        s.smallest.inputNr = inputNr
        return s
    }
    fun div26(s: Sys26): Sys26{
        var newS = s.copy()
        if (newS.smallest.maxValue() < 26){
            if (newS.smallest.biggerDigit != null){
                newS.smallest = newS.smallest.biggerDigit!!
                newS.smallest.smallerDigit = null
            }else{
                newS = Sys26(0)
            }
        }else{
            println("ERROR, beim Dividieren könnte ein Input zu einem Überschlag führen (z.B. Input+23 / 26 kann ja auch 1 sein)")
        }
        return newS
    }

    fun mod26(s: Sys26): Sys26{
        if (s.smallest.maxValue() < 26){
            val newS = Sys26( s.smallest.value )
            newS.smallest.inputNr = s.smallest.inputNr
            return newS
        }else{
            println("ERROR, Mod könnte auch was anderes sein, falls input groß genug ist")
            return s
        }
    }

    fun mult26(s: Sys26): Sys26{
        val newS = s.copy()
        newS.newSmallest(AbstractDigit(0))
        return newS
    }
    fun add( a: Sys26, b: Sys26 ): Sys26 {
        fun addDigits(aD: AbstractDigit, bD: AbstractDigit): Pair<AbstractDigit, Boolean> {
            var inputNr: Int? = aD.inputNr
            if (inputNr == null) {
                inputNr = bD.inputNr
            }
            val value = aD.value + bD.value
            if (value >= 26) {
                //println("Fehler, es gibt beim Addieren zweier Digits einen Übertrag (im 26er System): " + aD.toString() + " + " + bD.toString())
                return Pair(AbstractDigit(value % 26), true)
            } else {
                val s = AbstractDigit(value)
                s.inputNr = inputNr
                return Pair(s, false)
            }
        }
        val s = Sys26(0)
        var aNext: AbstractDigit? = a.smallest
        var bNext: AbstractDigit? = b.smallest
        var uebertrag = false
        while ((aNext != null) && (bNext != null)) {
            var sNext = addDigits(aNext, bNext)
            if (uebertrag){
                var sNext2 = addDigits(sNext.first, AbstractDigit(1))
                uebertrag = sNext.second || sNext2.second
                s.newBiggest(sNext2.first)
            }else{
                uebertrag = sNext.second
                s.newBiggest(sNext.first)
            }

            aNext = aNext.biggerDigit
            bNext = bNext.biggerDigit
        }
        if (uebertrag){
            if (aNext!=null){
                val sNext = addDigits(AbstractDigit(aNext.value),AbstractDigit(1)).first //TODO Annahme, es entstehen keine weiteren Überschläge
                sNext.inputNr = aNext.inputNr
                s.newBiggest(sNext)
                aNext = aNext.biggerDigit
            }
            if (bNext!=null){
                val sNext = addDigits(AbstractDigit(bNext.value),AbstractDigit(1)).first //TODO Annahme, es entstehen keine weiteren Überschläge
                sNext.inputNr = bNext.inputNr
                s.newBiggest(sNext)
                bNext = bNext.biggerDigit
            }
            if ( (aNext==null) && (bNext == null) ) {
                val sNext = AbstractDigit(1)
                s.newBiggest(sNext)
            }
        }
        while (aNext != null) {
            val sNext = AbstractDigit(aNext.value)
            sNext.inputNr = aNext.inputNr
            s.newBiggest(sNext)
            aNext = aNext.biggerDigit
        }
        while (bNext != null) {
            val sNext = AbstractDigit(bNext.value)
            sNext.inputNr = bNext.inputNr
            s.newBiggest(sNext)
            bNext = bNext.biggerDigit
        }
        return div26(s) //da ich am Anfang mit 0 starte, und ich nur neuen Biggest erhöhe, behalte ich die Ziffer 0 als Smallest
    }

    data class WXYZ(var w: Sys26, var x: Sys26, var y: Sys26, var z: Sys26){
        fun getWXYZ(c: String): Sys26{
            when (c){
                "w" ->  return w
                "x" ->  return x
                "y" ->  return y
                "z" ->  return z
                else    ->  return Sys26(c.toInt())
            }
        }
        fun setWXYZ(c: String, newVal: Sys26){
            when (c){
                "w" ->  w = newVal
                "x" ->  x = newVal
                "y" ->  y = newVal
                "z" ->  z = newVal
                else -> println("ERROR: Variablenname nicht bekannt")
            }
        }
        fun copy(): WXYZ{
            return WXYZ(w.copy(),x.copy(),y.copy(),z.copy())
        }
    }

    fun canBeEqual(a: Sys26, b: Sys26): Boolean{
        val amin = a.minValue()
        val amax = a.maxValue()
        val bmin = b.minValue()
        val bmax = b.maxValue()
        //Schnitt der beiden muss ein Element enthalten
        val smin = maxOf(amin, bmin)
        val smax = minOf(amax, bmax)
        return (smax >= smin)
    }

    fun AluToKotlin(code: List<String>, codeNr: Int, wxyz: WXYZ, inputNr: Int): String{
        if (codeNr >= code.size){
            if (canBeEqual(wxyz.z,Sys26(0))){
                if (wxyz.z.inputIndepent()){
                    if (wxyz.z.isZero()){
                        return "return true"
                    }else{
                        println("Fall tritt nie ein, bei Return Z")
                        return "return false"
                    }
                }else{
                    return "return (" + wxyz.z.toString() + "==0)"
                }
            }else{
                return "return false"
            }
        }
        /*if (codeNr >= 1){
            println("" + (codeNr) + ", "+ code[codeNr-1] +": " +  wxyz)
        }*/
        val newwxyz = wxyz.copy()
        val com = code[codeNr].split(" ")
        when (com[0]){
            "inp"   ->
                {   newwxyz.setWXYZ(com[1], getNextInput(inputNr))
                    return AluToKotlin(code, codeNr+1, newwxyz, inputNr+1)
                }
            "add"   ->
                {   newwxyz.setWXYZ(com[1], add( newwxyz.getWXYZ(com[1]), newwxyz.getWXYZ(com[2])) )
                    return AluToKotlin(code, codeNr+1, newwxyz, inputNr)
                }
            "mul"   ->
                {   if (newwxyz.getWXYZ(com[1]).isZero() || newwxyz.getWXYZ(com[2]).isZero()){
                        newwxyz.setWXYZ(com[1], Sys26(0))
                    }else{
                        if (!newwxyz.getWXYZ(com[2]).isOne()){
                            //gehe davon aus, dass die Zahl den Wert 26 hat
                            if (newwxyz.getWXYZ(com[2]).value()!=26L){
                                println("ERROR: Faktor ist ungleich 0,1 oder 26, sondern " + newwxyz.getWXYZ(com[2]).value() + " in Zeile " + (codeNr+1))
                            }
                            newwxyz.setWXYZ( com[1], mult26(newwxyz.getWXYZ(com[1])) )
                        }
                    }
                    return AluToKotlin(code, codeNr+1, newwxyz, inputNr)
                }
            "div"   ->
                {   if (!newwxyz.getWXYZ(com[1]).isZero()){
                        if (!newwxyz.getWXYZ(com[2]).isOne()){
                            //gehe davon aus, dass die Zahl den Wer 26 hat
                            if (newwxyz.getWXYZ(com[2]).value()!=26L){
                                println("ERROR: Divisor ist ungleich 1 oder 26, und Wert ungleich 0")
                            }
                            newwxyz.setWXYZ( com[1], div26(newwxyz.getWXYZ(com[1])) )
                        }
                    }
                    return AluToKotlin(code, codeNr+1, newwxyz, inputNr)
                }
            "mod"   ->
                {   if (newwxyz.getWXYZ(com[2]).value()!=26L){
                        println("ERROR, wir rechnen Modulo einer Zahl, die nicht 26 ist")
                    }
                    newwxyz.setWXYZ(com[1], mod26(newwxyz.getWXYZ(com[1])))
                    return AluToKotlin(code, codeNr+1, newwxyz, inputNr)
                }
            "eql"   -> //TODO hier kann man noch besser vergleichen, z.B. ist digit in 0..9, digit+12 dann in 12..21, diese können also nicht gleich sein
                {   val wxyzCaseTrue = newwxyz.copy()
                    wxyzCaseTrue.setWXYZ(com[1],Sys26(1))
                    val wxyzCaseFalse = newwxyz.copy()
                    wxyzCaseFalse.setWXYZ(com[1],Sys26(0))
                    val v1 = newwxyz.getWXYZ(com[1])
                    val v2 = newwxyz.getWXYZ(com[2])
                    if (canBeEqual(v1,v2)){
                        if (v1.inputIndepent() && v2.inputIndepent()){
                            if (v1.value() == v2.value()){
                                return AluToKotlin(code,  codeNr+1, wxyzCaseTrue, inputNr)
                            }else{
                                println("FALL TRITT NIE EIN, da v1 ja nicht gleich v2 sein kann")
                                return AluToKotlin(code,  codeNr+1, wxyzCaseFalse, inputNr)
                            }
                        }
                        val cTrue = AluToKotlin(code,  codeNr+1, wxyzCaseTrue, inputNr)
                        val cFalse = AluToKotlin(code,  codeNr+1, wxyzCaseFalse, inputNr)
                        if (cTrue == cFalse){ //liefern sie denselben Output
                            return cTrue
                        }
                        var s = "if (" + v1 + " == " + v2 + "){\n"
                        s += "   " + cTrue + "\n"
                        s += "}else{ \n"
                        s += "   " + cFalse  + "\n"
                        s += "}"
                        return s
                    }else{
                        return AluToKotlin(code,  codeNr+1, wxyzCaseFalse, inputNr)
                    }
                }
        }
        println("ERROR, gibt keinen Befehl mehr")
        return ""
    }

    fun monad(input: List<Int>): Boolean{
        return (input[2] + 6 == input[3]) && (input[5] + 7 == input[6]) && (input[8] + 3 == input[9]) && (input[7] -2 == input[10]) && (input[4] + 1 == input[11]) && (input[1] + 8 == input[12]) && (input[0] -3 == input[13])
    }


    fun part1(input: List<String>): String {
        //aus monadInKotlin ablesbar
        val s = "91398299697996"
        if(monad(s.map { it.digitToInt() })){
            return s
        }
        return "FEHLER bei der Handarbeit"
    }

    fun part2(input: List<String>): String {
        //aus monadInKotlin ablesbar
        val s = "41171183141291"
        if(monad(s.map { it.digitToInt() })){
            return s
        }
        return "FEHLER bei der Handarbeit"
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day24"

    val input = readInput(dayname)

    //val monadInKotlin = AluToKotlin(input, 0, WXYZ(Sys26(0),Sys26(0),Sys26(0),Sys26(0)), 0)
    //println(monadInKotlin)

    println(part1(input))
    println(part2(input))


}
