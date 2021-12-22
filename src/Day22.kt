import java.math.BigInteger

fun main() {
    data class Cuboid(val xmin: Int, val xmax: Int, val ymin: Int, val ymax: Int, val zmin:Int, val zmax: Int){
        fun volume(): BigInteger{
            if (isEmpty()){
                return 0.toBigInteger()
            }else{
                return ((xmax-xmin+1).toBigInteger()*(ymax-ymin+1).toBigInteger()*(zmax-zmin+1).toBigInteger())
            }

        }
        fun isEmpty(): Boolean{
            return (xmax < xmin)||(ymax < ymin)||(zmax < zmin)
        }

        fun intersection( c: Cuboid ): Cuboid{
            return Cuboid( maxOf(xmin,c.xmin), minOf(xmax,c.xmax), maxOf(ymin,c.ymin), minOf(ymax,c.ymax), maxOf(zmin,c.zmin), minOf(zmax,c.zmax) )
        }

        fun complement( c: Cuboid ): ArrayList<Cuboid>{
            fun addNonEmpty(list: ArrayList<Cuboid>, cube: Cuboid){
                val newCube = intersection(cube)
                if (!newCube.isEmpty()){
                    list.add(newCube)
                }
            }
            val ret = ArrayList<Cuboid>()
            addNonEmpty( ret, Cuboid(xmin, c.xmin-1, c.ymin, c.ymax, c.zmin, c.zmax) ) //links
            addNonEmpty( ret, Cuboid(c.xmax+1, xmax, c.ymin, c.ymax, c.zmin, c.zmax) ) //rechts
            addNonEmpty( ret, Cuboid(xmin, xmax, ymin, c.ymin-1, c.zmin, c.zmax) ) //unten
            addNonEmpty( ret, Cuboid(xmin, xmax, c.ymax+1, ymax, c.zmin, c.zmax) ) //oben
            addNonEmpty( ret, Cuboid(xmin, xmax, ymin, ymax, zmin, c.zmin-1) ) //vorne
            addNonEmpty( ret, Cuboid(xmin, xmax, ymin, ymax, c.zmax+1, zmax) ) //hinten
            return ret
        }

        override fun toString(): String{
            return xmin.toString() + ".." + xmax + " x " + ymin + ".." + ymax + " x " + zmin + ".." + zmax
        }
    }

    class Cuboidlist(){
        var cubes: ArrayList<Cuboid>

        init {
            cubes = ArrayList<Cuboid>()
        }

        fun turnOn(cube: Cuboid){
            if (!cube.isEmpty()){
                turnOff(cube) //damits disjunkt ist
                cubes.add(cube)
            }
        }

        fun turnOff(cube: Cuboid){
            val newCubes = ArrayList<Cuboid>()
            for (c in cubes){
                val ncs = c.complement(cube)
                for (nc in ncs){
                    newCubes.add(nc)
                }
            }
            cubes = newCubes
        }

        fun volume(): BigInteger{ //stimmt nur, falls disjunkt
            var vol = 0.toBigInteger()
            for (c1ind in cubes.indices){
                val x = cubes[c1ind].volume()
                if (x < 0.toBigInteger()){
                    println(cubes[c1ind].toString() + " hat Volumen " + cubes[c1ind].volume())
                }
                vol += cubes[c1ind].volume()
            }
            return vol
        }
    }

    fun parse(input: List<String>): Cuboidlist{
        val cubelist = Cuboidlist()
        for (inp in input){
            val s = inp.split("..")
            val c = Cuboid(
            ( s[0].split('=')[1] ).toInt(),
            ( s[1].split(',')[0] ).toInt(),
            ( s[1].split(',')[1].split('=')[1] ).toInt(),
            ( s[2].split(',')[0] ).toInt(),
            ( s[2].split(',')[1].split('=')[1] ).toInt(),
            ( s[3] ).toInt() )
            if (inp[1]=='n'){
                cubelist.turnOn(c)
            }else{
                cubelist.turnOff(c)
            }
        }
        return cubelist
    }

    fun part1(input: List<String>): BigInteger {
        val cubeList = parse(input)
        val c50 = Cuboid(-50,50,-50,50,-50,50)
        val newCubeList = Cuboidlist()
        for (cube in cubeList.cubes){
            newCubeList.turnOn(cube.intersection(c50))
        }
        return newCubeList.volume()
    }

    fun part2(input: List<String>): BigInteger {
        val cubeList = parse(input)
        return cubeList.volume()
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day22"

    var testInput = readInput(dayname+"_test")
    check(part1(testInput) == 590784.toBigInteger())
    testInput = readInput(dayname+"_test2")
    check(part1(testInput) == 474140.toBigInteger())
    check(part2(testInput) == 2758514936282235.toBigInteger())

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))
}
