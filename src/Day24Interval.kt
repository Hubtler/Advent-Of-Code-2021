import java.io.File
import kotlin.math.abs

fun main() {

    data class ValRan(var v: Long){
        //Value / inhalt
        var str: String//als String in abhängigkeit von "input[x]"
        var rangeFrom: Long //range, also ein Zahlenbereich
        var rangeTo: Long
        var f: Long//Faktor, falls es vielfaches von etwas ist (geht bei Add verloren, bei Div und Mod könnte es was bringen)

        init{
            str = ""
            f = 1
            rangeFrom = v
            rangeTo = v
            shortIt()
        }

        fun isFixValue(): Boolean{
            return (rangeTo == rangeFrom)
        }
        fun getValue(): Long{
            return rangeTo
        }

        fun shortIt(): ValRan{
            if (isFixValue()){
                v = getValue()
                str = v.toString()
                f = v
            }
            if (f == 0L) {
                f = 1L
            }
            return this
        }

        fun add(other: ValRan): ValRan{
            if (other.areSaveEqual(ValRan(0L))){
                return this
            }
            if (areSaveEqual(ValRan(0L))){
                return other
            }
            val output = ValRan(0)
            output.str = "(" + str + "+" + other.str + ")"
            output.rangeFrom = rangeFrom + other.rangeFrom
            output.rangeTo = rangeTo + other.rangeTo
            return output.shortIt()
        }

        fun mul(other: ValRan): ValRan{
            if (other.areSaveEqual(ValRan(1L))){
                return this
            }
            if (areSaveEqual(ValRan(1L))){
                return other
            }
            if (other.areSaveEqual(ValRan(0L))){
                return ValRan(0L)
            }
            if (!other.areSaveEqual(ValRan(26L))){
                println("mul : NICHT 26, sondern " + other.str)
            }
            val output = ValRan(0)
            output.str = "(" + str + "*" + other.str + ")"
            val ac = rangeFrom * other.rangeFrom
            val ad = rangeFrom * other.rangeTo
            val bc = rangeTo * other.rangeFrom
            val bd = rangeTo * other.rangeTo
            output.rangeFrom = minOf(ac,ad,bc,bd)
            output.rangeTo = maxOf(ac,ad,bc,bd)
            output.f = f * other.f
            return output.shortIt()
        }

        fun div(other: ValRan): ValRan{
            if (other.areSaveEqual(ValRan(1L))){
                return this
            }
            if (areSaveEqual(ValRan(1L))){
                return other
            }
            if (!other.areSaveEqual(ValRan(26L))){
                println("div : NICHT 26")
            }
            val output = ValRan(0)
            output.str = "(" + str + "/" + other.str + ")"
            val ac = rangeFrom / other.rangeFrom
            val ad = rangeFrom / other.rangeTo
            val bc = rangeTo / other.rangeFrom
            val bd = rangeTo / other.rangeTo
            output.rangeFrom = minOf(ac,ad,bc,bd)
            output.rangeTo = maxOf(ac,ad,bc,bd)
            if (f % other.f == 0L){
                output.f = f / other.f
            }else{
                output.f = 1
            }
            return output.shortIt()
        }

        fun mod(other: ValRan): ValRan{
            if (!other.areSaveEqual(ValRan(26L))){
                println("mod : NICHT 26")
            }
            val output = ValRan(1)
            output.str = "(" + str + "%" + other.str + ")"

            if (other.isFixValue()){
                if (f % other.getValue() == 0L){
                    return ValRan(0)
                }else{
                    val mod = other.getValue()
                    var min = mod
                    var max = 0L
                    for (x in rangeFrom..rangeTo){
                        var xmod = x % mod
                        xmod = (xmod + abs(mod)) % mod
                        min = minOf(xmod, min)
                        max = maxOf(xmod, max)
                    }
                    output.rangeFrom = min
                    output.rangeTo = max
                }
            }else{
                output.rangeFrom = 0
                output.rangeTo = abs(other.rangeTo)
            }
            return output.shortIt()
        }

        fun areSaveEqual(other: ValRan): Boolean{
            return (other.str == str)
        }

        fun canBeEqual(other: ValRan): Boolean{
            return ( minOf(rangeTo, other.rangeTo) >= maxOf(rangeFrom, other.rangeFrom) )
        }
    }

    fun writeFile(filename: String, content: String){
        File(filename).writeText( content )
    }

    fun AluToKotlin(code: List<String>): ValRan{
        //Kotlin muss inputvariable "input" nennen, und methode "equalsIO" implementieren, dass für zwei Long Werte 0 (falls verschieden) oder 1 (falls gleich) zurückgibt
        var variables = mutableMapOf<Char, ValRan>()
        var c = 0
        fun nextLine(line: String, variables: MutableMap<Char, ValRan>): MutableMap<Char, ValRan>{
            fun getVar(ind: String): ValRan{
                if (ind.first() in variables.keys){
                    return variables[ind.first()]!!
                }else{
                    return ValRan(ind.toLong())
                }
            }
            val com = line.split(" ")
            when (com[0]){
                "inp"   -> {    var newInput = ValRan(0)
                                newInput.rangeFrom = 1
                                newInput.rangeTo = 9
                                newInput.str = "input["+ c++ +"]"
                                variables[com[1].first()] = newInput
                }
                "add" ->  variables[com[1].first()] = variables[com[1].first()]!!.add( getVar(com[2]) )
                "mul" ->  variables[com[1].first()] = variables[com[1].first()]!!.mul( getVar(com[2]) )
                "div" ->  variables[com[1].first()] = variables[com[1].first()]!!.div( getVar(com[2]) )
                "mod" ->  variables[com[1].first()] = variables[com[1].first()]!!.mod( getVar(com[2]) )
                "eql" ->  {
                                if (variables[com[1].first()]!!.canBeEqual( getVar(com[2]) )){
                                    if (variables[com[1].first()]!!.areSaveEqual( getVar(com[2]) )){
                                        variables[com[1].first()] = ValRan(1)
                                    }else{
                                        var newValRan = ValRan(1)
                                        newValRan.rangeFrom = 1
                                        newValRan.rangeTo = 9
                                        newValRan.str = "equals(" + getVar(com[1]).str + "," + getVar(com[2]).str + ")"
                                        variables[com[1].first()] = newValRan
                                        //TODO Beide Fälle einzeln mit ifcase zurückliefern
                                    }
                                }else{
                                    variables[com[1].first()] = ValRan(0)
                                }
                }
            }
            return variables
        }
        variables.put('w', ValRan(0))
        variables.put('x', ValRan(0))
        variables.put('y', ValRan(0))
        variables.put('z', ValRan(0))
        for (line in code){
            nextLine(line, variables) //TODO testen, ob geänderte inhalte direkt ankommen, oder variables = davorgeschriebn werden muss
            //println(variables.map { it.value.str })
        }

        return variables['z']!!
    }


    fun part1(input: List<String>): Int {
        return 0

    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day24"

    val input = readInput(dayname)
    writeFile("MONAD.txt", AluToKotlin(input).str)
    println(part1(input))
    println(part2(input))

}
