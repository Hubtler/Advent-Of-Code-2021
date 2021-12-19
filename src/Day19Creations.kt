    fun main() {
        fun rotate(rotation: Int, xyz: Triple<Int,Int,Int>): Triple<Int,Int,Int> {
            var rxyz = xyz
            when (rotation){
                0 -> rxyz = Triple(xyz.first,xyz.second,xyz.third)
                1 -> rxyz = Triple(xyz.first,xyz.third,-xyz.second)
                2 -> rxyz = Triple(xyz.first,-xyz.second,-xyz.third)
                3 -> rxyz = Triple(xyz.first,-xyz.third,xyz.second)
                4 -> rxyz = Triple(-xyz.second,xyz.first,xyz.third)
                5 -> rxyz = Triple(-xyz.second,xyz.third,-xyz.first)
                6 -> rxyz = Triple(-xyz.second,-xyz.first,-xyz.third)
                7 -> rxyz = Triple(-xyz.second,-xyz.third,xyz.first)
                8 -> rxyz = Triple(-xyz.first,-xyz.second,xyz.third)
                9 -> rxyz = Triple(-xyz.first,xyz.third,xyz.second)
                10 -> rxyz = Triple(-xyz.first,xyz.second,-xyz.third)
                11 -> rxyz = Triple(-xyz.first,-xyz.third,-xyz.second)
                12 -> rxyz = Triple(xyz.second,-xyz.first,xyz.third)
                13 -> rxyz = Triple(xyz.second,xyz.third,xyz.first)
                14 -> rxyz = Triple(xyz.second,xyz.first,-xyz.third)
                15 -> rxyz = Triple(xyz.second,-xyz.third,-xyz.first)
                16 -> rxyz = Triple(-xyz.third,xyz.second,xyz.first)
                17 -> rxyz = Triple(-xyz.third,xyz.first,-xyz.second)
                18 -> rxyz = Triple(-xyz.third,-xyz.second,-xyz.first)
                19 -> rxyz = Triple(-xyz.third,-xyz.first,xyz.second)
                20 -> rxyz = Triple(xyz.third,-xyz.second,xyz.first)
                21 -> rxyz = Triple(xyz.third,xyz.first,xyz.second)
                22 -> rxyz = Triple(xyz.third,xyz.second,-xyz.first)
                23 -> rxyz = Triple(xyz.third,-xyz.first,-xyz.second)
            }
            return rxyz
        }

        fun zahl(s: Int): String{
            var ret = s.toString()
            while (ret.length < 2){
                ret = " " + ret
            }
            return ret
        }

        fun CreateRotations(){
            fun negative(v: String):String{
                if (v[0]=='-'){
                    return v.drop(1)
                }else{
                    return "-"+v
                }
            }
            var q = "when (rotation){ \n"
            val rots = ArrayList<Triple<String,String,String>>()
            for (h in 0..3){
                for (r in 0..3){
                    for (s in 0..3){
                        var xyz = Triple("xyz.first","xyz.second","xyz.third")
                        for (hh in 1..h){
                            xyz = Triple(negative(xyz.third),xyz.second,xyz.first)
                        }
                        for (rr in 1..r){
                            xyz = Triple(negative(xyz.second),xyz.first,xyz.third)
                        }
                        for (ss in 1..s){
                            xyz = Triple(xyz.first,xyz.third,negative(xyz.second))
                        }
                        rots.add(xyz)
                    }
                }
            }
            val x = rots.distinct()
            for (ind in x.indices){
                q += "    " + zahl(ind) + " -> return Triple("  + x[ind].first + "," + x[ind].second + "," + x[ind].third + ") \n"
            }
            q += "}"
            println(q)
        }

        fun findInverseRot(){
            var s = "when(rotation){ \n"
            val g = Triple(1,2,3)
            for (i1 in 0..23){
                val xyz = rotate(i1,g)
                for (i2 in 0..23){
                    if (rotate(i2,xyz)==g){
                        s += "    " + zahl(i1) + " -> return rotate(" + i2.toString() +  ", xyz) \n"
                    }
                }
            }
            s += "}"
            println(s)
        }



        CreateRotations()
        findInverseRot()


    }

