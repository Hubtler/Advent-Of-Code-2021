import java.io.File
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

        fun toOBJ(nr: Int): Pair<String, String>{
            fun createV(x: Double, y: Double, z: Double): String{
                return "v " + x + " " + y + " " + z + "\n"
            }
            fun createF(v1: Int, v2: Int, v3: Int): String{
                return "f " + (8*nr+v1) + " " + (8*nr+v2) + " " + (8*nr+v3) + "\n"
            }
            var v = "" //8 Knoten
            v += createV((xmin - 0.5), ymin - 0.5, zmin- 0.5) //1
            v += createV(xmin - 0.5, ymin - 0.5, zmax + 0.5) //2
            v += createV(xmin - 0.5, ymax + 0.5, zmin - 0.5) //3
            v += createV(xmin - 0.5, ymax + 0.5, zmax + 0.5) //4
            v += createV(xmax + 0.5, ymin - 0.5, zmin - 0.5) //5
            v += createV(xmax + 0.5, ymin - 0.5, zmax + 0.5) //6
            v += createV(xmax + 0.5, ymax + 0.5, zmin - 0.5) //7
            v += createV(xmax + 0.5, ymax + 0.5, zmax - 0.5) //8
            // Knoten x hat Index 8*nr+x
            var f = "" //6 Fl??chen a 2 Dreiecke
            f += createF(1,2,3)
            f += createF(2,3,4)
            f += createF(1,2,5)
            f += createF(2,5,6)
            f += createF(3,4,7)
            f += createF(4,7,8)
            f += createF(2,4,6)
            f += createF(4,6,8)
            f += createF(1,3,5)
            f += createF(3,5,7)
            f += createF(5,6,8)
            f += createF(5,7,8)

            return Pair(v,f)
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
            return cubes.sumOf{ it.volume() }
        }
        fun toOBJ(): Pair<StringBuilder, StringBuilder>{
            val v = StringBuilder()
            val f = StringBuilder()
            var ind = 0
            for (c in cubes){
                val s = c.toOBJ(ind++)
                v.append( s.first )
                f.append( s.second )
            }
            return Pair(v,f)
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

    fun writeFile(filename: String, vf: Pair<StringBuilder, StringBuilder>){
        File(filename).writeText( vf.first.toString() + vf.second.trim() )
    }

    fun part1(input: List<String>): BigInteger {
        val cubeList = parse(input)
        val c50 = Cuboid(-50,50,-50,50,-50,50)
        val newCubeList = Cuboidlist()
        for (cube in cubeList.cubes){
            newCubeList.turnOn(cube.intersection(c50))
        }
        //writeFile("src/Day22_Part1.obj", newCubeList.toOBJ() )
        return newCubeList.volume()
    }

    fun part2(input: List<String>): BigInteger {
        val cubeList = parse(input)
        //writeFile("src/Day22_Part2.obj", cubeList.toOBJ() )
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
