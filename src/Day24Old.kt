import java.io.File

fun main() {

    data class genBackward(var deep: Int){
    var c = 9
    var gen: genBackward?

    init {
        if (deep > 1){
            gen = genBackward(deep-1)
        }else{
            gen = null
        }
    }
    fun next(): Pair<String, Boolean>{
        var p: Pair<String, Boolean> //Anhang, Übertrag?
        if (deep > 1){
            p = gen!!.next()
        }else{
            p = Pair("", true)
        }
        val ret = Pair(c.toString() + p.first, p.second && (c==1))
        if (p.second) {
            c = ((c+7) % 9) +1
        }
        return ret
    }
}

    data class Range(var lh: Pair<Long,Long>){
        fun add(x: Range): Range{
            return Range(Pair(x.lh.first + lh.first, x.lh.second + lh.second))
        }
        fun mul(x: Range): Range{
            val p1 = lh.first * x.lh.first
            val p2 = lh.first * x.lh.second
            val p3 = lh.second * x.lh.first
            val p4 = lh.second * x.lh.second
            return Range(Pair( minOf(p1,p2,p3,p4), maxOf(p1,p2,p3,p4)))
        }
        fun div(x: Range): Range{
            val p1 = lh.first / x.lh.first
            val p2 = lh.first / x.lh.second
            val p3 = lh.second / x.lh.first
            val p4 = lh.second / x.lh.second
            return Range(Pair( minOf(p1,p2,p3,p4), maxOf(p1,p2,p3,p4)))
        }
        fun mod(x: Range): Range{ //TODO
            val p1 = lh.first * x.lh.first
            val p2 = lh.first * x.lh.second
            val p3 = lh.second * x.lh.first
            val p4 = lh.second * x.lh.second
            return Range(Pair( minOf(p1,p2,p3,p4), maxOf(p1,p2,p3,p4)))
        }
        fun canBeEqual(x: Range): Boolean{
            return ( minOf(lh.second,x.lh.second) >= maxOf(lh.first, x.lh.first) )
        }

        fun isOneElement(): Boolean{
            return (lh.first == lh.second)
        }
    }
    fun toRange(x: Int): Range{
        return Range(Pair(x.toLong(),x.toLong()))
    }

    fun AluToKotlin(code: List<String>): String{
        fun codeLine(lineNr: Int, digitNr: Int, wxyz: Pair<Pair<String,String>,Pair<String,String>>, range: Pair<Pair<Range,Range>,Pair<Range,Range>>, short: Boolean = false): String{
            //println("Params:" + lineNr + ","+ digitNr + wxyz + range + short)
            if (!short){
                var w = wxyz.first.first
                var x = wxyz.first.second
                var y = wxyz.second.first
                var z = wxyz.second.second

                if (range.first.first.isOneElement()){
                    w = range.first.first.lh.first.toString()
                }
                if (range.first.second.isOneElement()){
                    x = range.first.second.lh.first.toString()
                }
                if (range.second.first.isOneElement()){
                    y = range.second.first.lh.first.toString()
                }
                if (range.second.second.isOneElement()){
                    z = range.second.second.lh.first.toString()
                }
                return codeLine(lineNr, digitNr, Pair(Pair(w,x),Pair(y,z)), range, true)
            }

            fun isInput(str: String): Boolean{
                return (str.startsWith("inputs") && (str.length <= "inputs[14].digitToInt()".length))
            }
            if (lineNr == code.size){
                return "return ( (" + wxyz.second.second + ")== 0)\n"
                /*if (range.second.second.canBeEqual(toRange(0))){
                    return "return ( (" + wxyz.second.second + ")== 0)\n" + "// range: " + range.second.second.lh.first + " bis " + range.second.second.lh.second
                }else{
                    return "return false" + "// range: " + range.second.second.lh.first + " bis " + range.second.second.lh.second
                } */

            }

            fun setVar(v: String, to: String): Pair<Pair<String,String>,Pair<String,String>>{
                when (v.first()){
                    'w' -> return Pair(Pair(to, wxyz.first.second), Pair(wxyz.second.first, wxyz.second.second))
                    'x' -> return Pair(Pair(wxyz.first.first, to), Pair(wxyz.second.first, wxyz.second.second))
                    'y' -> return Pair(Pair(wxyz.first.first, wxyz.first.second), Pair(to, wxyz.second.second))
                    'z' -> return Pair(Pair(wxyz.first.first, wxyz.first.second), Pair(wxyz.second.first, to))
                }
                println("Error, ungültige Variablenbezeichung")
                return wxyz
            }
            fun setRange(v: String, to: Range): Pair<Pair<Range,Range>,Pair<Range,Range>>{
                when (v.first()){
                    'w' -> return Pair(Pair(to, range.first.second), Pair(range.second.first, range.second.second))
                    'x' -> return Pair(Pair(range.first.first, to), Pair(range.second.first, range.second.second))
                    'y' -> return Pair(Pair(range.first.first, range.first.second), Pair(to, range.second.second))
                    'z' -> return Pair(Pair(range.first.first, range.first.second), Pair(range.second.first, to))
                }
                println("Error, ungültige Variablenbezeichung")
                return range
            }
            fun getVar(v: String): String{
                when (v.first()){
                    'w' -> return wxyz.first.first
                    'x' -> return wxyz.first.second
                    'y' -> return wxyz.second.first
                    'z' -> return wxyz.second.second
                }
                return v
            }
            fun getRange(v: String): Range{
                when (v.first()){
                    'w' -> return range.first.first
                    'x' -> return range.first.second
                    'y' -> return range.second.first
                    'z' -> return range.second.second
                }
                return toRange(v.toInt())
            }

            val com = code[lineNr].split(" ")
            when (com[0]){
                "inp"   ->  return codeLine(lineNr+1,digitNr+1, setVar(com[1], "inputs[$digitNr].digitToInt()"), setRange(com[1],Range(Pair(1,9))) )
                "add"   ->  {
                    if ( (getVar(com[1])=="0") || (getVar(com[2])=="0")){
                        if (getVar(com[1])=="0"){
                            return codeLine(lineNr+1,digitNr, setVar(com[1],getVar(com[2])), setRange(com[1], getRange(com[2])) )
                        }else {
                            return codeLine(lineNr+1,digitNr, wxyz, range )
                        }
                    }else{
                        var v1 = getVar(com[1]).toIntOrNull()
                        var v2 = getVar(com[2]).toIntOrNull()
                        if ((v1 != null) && (v2 != null)) { //links und rechts stehen schon Zahlen drin
                            return codeLine(lineNr+1,digitNr, setVar(com[1], (v1+v2).toString()), setRange(com[1],toRange(v1+v2)) )
                        }else{
                            return codeLine(lineNr+1,digitNr, setVar(com[1], "(" + getVar(com[1]) + "+" + getVar(com[2]) + ")"), setRange(com[1],getRange(com[1]).add(getRange(com[2]))) )
                        }
                    }
                }
                "mul"   ->  {
                    if ( (getVar(com[1])=="0") || (getVar(com[2])=="0")){
                        return codeLine(lineNr+1,digitNr, setVar(com[1], "0"), setRange(com[1], toRange(0)) )
                    }
                    if (getVar(com[1])=="1"){
                        return codeLine(lineNr+1,digitNr, setVar(com[1], getVar(com[2])), setRange(com[1], getRange(com[2])) )
                    }
                    if (getVar(com[2])=="1"){
                        return codeLine(lineNr+1,digitNr, wxyz, range )
                    }
                    var v1 = getVar(com[1]).toIntOrNull()
                    var v2 = getVar(com[2]).toIntOrNull()
                    if ((v1 != null) && (v2 != null)) { //links und rechts stehen schon Zahlen drin
                        return codeLine(lineNr+1,digitNr, setVar(com[1], (v1*v2).toString()), setRange(com[1], toRange(v1*v2)) )
                    }else{
                        return codeLine(lineNr+1,digitNr, setVar(com[1], getVar(com[1]) + "*" + getVar(com[2])), setRange(com[1], getRange(com[1]).mul(getRange(com[2]))) )
                    }

                }
                "div"   -> {
                    if (getVar(com[1])=="0"){
                        return codeLine(lineNr+1,digitNr, setVar(com[1], "0"), setRange(com[1], toRange(0)) )
                    }
                    if (getVar(com[2])=="1"){
                        return codeLine(lineNr+1,digitNr, wxyz, range )
                    }
                    var v1 = getVar(com[1]).toIntOrNull()
                    var v2 = getVar(com[2]).toIntOrNull()
                    if ((v1 != null) && (v2 != null)) { //links und rechts stehen schon Zahlen drin
                        return codeLine(lineNr+1,digitNr, setVar(com[1], (v1/v2).toString()), setRange(com[1], toRange(v1/v2)) )
                    }else{
                        return codeLine(lineNr+1,digitNr, setVar(com[1], getVar(com[1]) + "/(" + getVar(com[2]) + ")"), setRange(com[1], getRange(com[1]).div(getRange(com[2]))) )
                    }
                }
                "mod"   -> {
                    if (getVar(com[1]) == "0") {
                        return codeLine(lineNr + 1, digitNr, setVar(com[1], "0"), setRange(com[1], toRange(0)))

                    } else {
                        var v1 = getVar(com[1]).toIntOrNull()
                        var v2 = getVar(com[2]).toIntOrNull()
                        if ((v1 != null) && (v2 != null)) { //links und rechts stehen schon Zahlen drin
                            return codeLine(lineNr+1,digitNr, setVar(com[1], (v1 % v2).toString()) , setRange(com[1], toRange(v1 % v2)))
                        }else{
                            return codeLine(lineNr+1,digitNr, setVar(com[1], getVar(com[1]) + "% (" + getVar(com[2]) + ")"), setRange(com[1], getRange(com[1]).mod(getRange(com[2]))) )
                        }
                    }
                }
                "eql"   -> {
                    //ausprobieren, ob links und rechts zahlen drinstehen, und wenn ja, nur den fall abarbeiten, der eintritt (gleich oder nicht gleich)
                    var v1 = getVar(com[1]).toIntOrNull()
                    var v2 = getVar(com[2]).toIntOrNull()
                    if ((v1 != null) && (v2 != null)){ //links und rechts stehen schon Zahlen drin
                        if (v1==v2){
                            return codeLine(lineNr+1,digitNr,setVar(com[1],"1"), setRange(com[1], toRange(1)))
                        }else{
                            return codeLine(lineNr+1,digitNr,setVar(com[1],"0"), setRange(com[1], toRange(0)))
                        }
                    }else{ //links oder rechts ist vom Input abhängig
                        //ist nun eine der beiden Seiten ein Input und die andere eine Zahl, so prüfe ob die Zahl eine einzelne Ziffer ist
                        if (isInput(getVar(com[1]))&&(v2 != null)){
                            if (v2 !in 1..9){
                                return codeLine(lineNr+1,digitNr,setVar(com[1],"0"), setRange(com[1], toRange(0)))
                            }
                        }
                        if (isInput(getVar(com[2]))&&(v1 != null)){
                            if (v1 !in 1..9){
                                return codeLine(lineNr+1,digitNr,setVar(com[1],"0"), setRange(com[1], toRange(0)))
                            }
                        }

                        if (getVar(com[1]) == getVar(com[2])) {
                            return codeLine(lineNr+1,digitNr,setVar(com[1],"1"), setRange(com[1], toRange(1)))
                        } else { //wenn wirklich beide Fälle eintreten können
                            if (getRange(com[1]).canBeEqual(getRange(com[2]))){
                                if (getRange(com[1]).isOneElement() && getRange(com[2]).isOneElement() ){
                                    return codeLine(lineNr+1,digitNr,setVar(com[1],"1"), setRange(com[1], toRange(1)))
                                }
                            }else{
                                return codeLine(lineNr+1,digitNr,setVar(com[1],"0"), setRange(com[1], toRange(0)))
                            }
                            val f1 = codeLine(lineNr+1,digitNr,setVar(com[1],"1"), setRange(com[1], toRange(1)))
                            val f2 = codeLine(lineNr+1,digitNr,setVar(com[1],"0"), setRange(com[1], toRange(0)))
                            //schaue ob der Output in jedem Fall derselbe ist
                            if (f1==f2){ //wenn ja, fasse zusammen
                                return f1
                            }else{ //andernfalls formuliere als 2 Fälle
                                return ("if ( " + getVar(com[1])  + " == " + getVar(com[2]) + " ){ \n" +
                                        f1
                                        + " }else{ \n "
                                        + f2 + "}\n")
                            }
                        }
                    }
                }
                else    ->  return "ungültiger Befehl"
            }
        }
        //return codeLine(0, 0, Pair(Pair("0","0"),Pair("0","0")))
        val wxyz = Pair(Pair("0","0"),Pair("0","0"))
        val wxyzRanges = Pair( Pair(Range(Pair(0,0)),Range(Pair(0,0))), Pair( Range(Pair(0,0)),Range(Pair(0,0)) ) )
        return codeLine(0, 0, wxyz, wxyzRanges)
    }

    fun writeFile(filename: String, content: String){
        File(filename).writeText( content )
    }

    fun part1(input: List<String>): Int {
        val gen = genBackward(14)
        var number = gen.next()
        var verlauf = "0000"
        while (!number.second){
            var nverlauf = number.first.substring(0,4)
            if (nverlauf!=verlauf){
                println(number.first)
                verlauf = nverlauf
            }
            if (Monad(number.first.toList())){
                return number.first.toInt()
            }else{
                number = gen.next()
            }
        }
        println("Keine gültige Nummer gefunden")
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day24"

    val input = readInput(dayname)

    writeFile("MONAD.txt", AluToKotlin(input))
    //println(part1(input))
    //println(part2(input))
}
fun Monad(inputs: List<Char>): Boolean{
    if ( (((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))% (26)+14) == inputs[8].digitToInt() ){
        if ( (((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))% (26)+-5) == inputs[9].digitToInt() ){
            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
        }else{
            if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))% (26)+-9) == inputs[10].digitToInt() ){
                return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
            }else{
                if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))% (26)+-5) == inputs[11].digitToInt() ){
                    return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                }else{
                    if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))% (26)+-2) == inputs[12].digitToInt() ){
                        return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                    }else{
                        if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }
            }
        }
    }else{
        if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))% (26)+-5) == inputs[9].digitToInt() ){
            if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)% (26)+-9) == inputs[10].digitToInt() ){
                if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)% (26)+-5) == inputs[11].digitToInt() ){
                    if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)% (26)+-2) == inputs[12].digitToInt() ){
                        if ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)/(26))== 0)
                        }else{
                            return ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }else{
                    if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))% (26)+-2) == inputs[12].digitToInt() ){
                        if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26))== 0)
                        }else{
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }
            }else{
                if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))% (26)+-5) == inputs[11].digitToInt() ){
                    if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)% (26)+-2) == inputs[12].digitToInt() ){
                        if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)/(26))== 0)
                        }else{
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }else{
                    if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))% (26)+-2) == inputs[12].digitToInt() ){
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }
            }
        }else{
            if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))% (26)+-9) == inputs[10].digitToInt() ){
                if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)% (26)+-5) == inputs[11].digitToInt() ){
                    if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)% (26)+-2) == inputs[12].digitToInt() ){
                        if ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)/(26))== 0)
                        }else{
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }else{
                    if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))% (26)+-2) == inputs[12].digitToInt() ){
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }
            }else{
                if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))% (26)+-5) == inputs[11].digitToInt() ){
                    if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)% (26)+-2) == inputs[12].digitToInt() ){
                        if ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)/(26))== 0)
                        }else{
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }else{
                    if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))% (26)+-2) == inputs[12].digitToInt() ){
                        if ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)% (26)+-7) == inputs[13].digitToInt() ){
                            return ( (((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26))== 0)
                        }else{
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }else{
                        if ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))% (26)+-7) == inputs[13].digitToInt() ){
                            return ( ((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26))== 0)
                        }else{
                            return ( (((((((((((((((inputs[0].digitToInt()+4)*26+(inputs[1].digitToInt()+10))*26+(inputs[2].digitToInt()+12))/(26)*26+(inputs[3].digitToInt()+14))*26+(inputs[4].digitToInt()+6))*26+(inputs[5].digitToInt()+16))/(26)*26+(inputs[6].digitToInt()+1))*26+(inputs[7].digitToInt()+7))*26+(inputs[8].digitToInt()+8))/(26)*26+(inputs[9].digitToInt()+11))/(26)*26+(inputs[10].digitToInt()+8))/(26)*26+(inputs[11].digitToInt()+3))/(26)*26+(inputs[12].digitToInt()+1))/(26)*26+(inputs[13].digitToInt()+8)))== 0)
                        }
                    }
                }
            }
        }
    }

}