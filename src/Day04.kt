class BingoField{
    var eintrag = ArrayList<List<Int>>()
    var n=5
    var m=5
    var abgehakt = ArrayList<MutableList<Boolean>>()



    init{
        //Falls ich was initialisieren möchte
    }

    constructor(zeile: ArrayList<List<Int>>) {
        eintrag = zeile
        if (zeile.size!=n){
            println("Falsche Zeilenanzahl")
        }
        for (z in zeile){
            if (z.size!=m){
                println("Falsche Spaltenanzahl")
            }
            abgehakt.add(MutableList(z.size) { false })
        }
    }

    fun won(i0: Int, j0: Int): Boolean{
        var w = true
        for (i in 0..n-1){
            w = w && abgehakt[i][j0] //nur wahr, falls alle wahr sind
        }
        if (w){
            return w
        }
        w = true
        for (j in 0..m-1){
            w = w && abgehakt[i0][j] //nur wahr, falls alle wahr sind
        }
        return w
    }

    fun hakeAb( value: Int): Boolean{
        //Suche Wert
        for (i in 0..n-1){
            for (j in 0..m-1){
                if (eintrag[i][j] == value){
                    abgehakt[i][j] = true
                    if (won(i,j)){
                        return true
                    }
                }
            }
        }
        return false
    }

    fun sum(): Int{
        var s = 0
        for (i in 0..n-1){
            for (j in 0..m-1) {
                if (!abgehakt[i][j]){
                    s += eintrag[i][j]
                }
            }
        }
        return s
    }

}

fun main() {
    fun getBingos(input: List<String>): ArrayList<BingoField>{
        var f = ArrayList<BingoField>()
        var j=1
        var z = ArrayList<List<Int>>()
        for (i in 1..input.size-1){
            if (input[i].length > 0){
                j++
                var zeilei: List<Int> = input[i].trim().replace("  "," ").split(' ').map { it.toInt() }
                //alternativ: = input[i].trim().split("\\s+".toRegex()).map { it.toInt() }
                z.add(zeilei)
                if (j > 5){
                    f.add(BingoField(z))
                    z = ArrayList<List<Int>>()
                    j=1
                }
            }
        }

        return f
    }

    fun part1(input: List<String>): Int {
        val bingonums = input[0].split(",").map{ it.toInt() }
        var bingofields = getBingos(input)
        for (num in bingonums){
            for (f in bingofields){
                if (f.hakeAb(num)){
                    return f.sum()*num
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val bingonums = input[0].split(",").map{ it.toInt() }
        var bingofields = getBingos(input)
        var solvedBingoFields = ArrayList<BingoField>()
        for (num in bingonums){
            for (f in bingofields){
                if (f.hakeAb(num)){
                    if (bingofields.size > 1){
                        solvedBingoFields.add(f)
                    }else{
                        return f.sum()*num
                    }
                }
            }
            for (f in solvedBingoFields){
                bingofields.remove(f)
            }
        }
        return 0
    }

    var dayname = "Day04"

    var testInput = readInput(dayname+"_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924 )

    var input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}