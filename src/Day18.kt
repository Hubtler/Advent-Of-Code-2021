fun main() {
    class Pairnumber(){
        var above: Pairnumber?
        var left: Pairnumber?
        var leftValue: Int
        var right: Pairnumber?
        var rightValue: Int

        init{
            above = null
            left = null
            right = null
            leftValue = 0
            rightValue = 0
        }
        constructor(above: Pairnumber?, left: Pairnumber?, right: Pairnumber?, leftValue: Int = 0, rightValue: Int = 0) : this() {
            this.above = above
            this.left = left
            this.right = right
            this.leftValue = leftValue
            this.rightValue = rightValue
        }

        fun getValue(): ULong{
            var lv = leftValue.toULong()
            if (left != null){
                lv = left!!.getValue()
            }
            var rv = rightValue.toULong()
            if (right != null){
                rv = right!!.getValue()
            }
            return (3UL * lv) + (2UL * rv)
        }

        fun add(with: Pairnumber): Pairnumber{
            val p = Pairnumber(null, this, with)
            this.above = p
            with.above = p
            return p.reduceComlpetly()
        }

        fun reduceComlpetly(): Pairnumber{
            while (reduceExplode()||reduceSplit()){}
            return this
        }

        fun reduceExplode(deepth: Int = 0): Boolean{
            if (left != null){
                if (left!!.reduceExplode(deepth+1)){
                    return true
                }
            }
            if (deepth==4){
                explode()
                return true
            }
            if (right != null){
                if (right!!.reduceExplode(deepth+1)){
                    return true
                }
            }
            return false
        }

        fun reduceSplit(deepth: Int = 0): Boolean{
            if (left != null){
                if (left!!.reduceSplit(deepth+1))
                    return true
            }else{
                if (leftValue > 9){
                    splitLeft()
                    return true
                }
            }
            if (right != null){
                if (right!!.reduceSplit(deepth+1)){
                    return true
                }
            }else{
                if (rightValue > 9){
                    splitRight()
                    return true
                }
            }
            return false
        }

        fun splitLeft(){
            left = Pairnumber(this, null, null,leftValue/2,(leftValue+1)/2)
            leftValue = 0
        }
        fun splitRight(){
            right = Pairnumber(this, null, null,rightValue/2,(rightValue+1)/2)
            rightValue = 0
        }

        fun explode(){
            if (above!!.left == this){
                var p = above!!.right
                if (p==null){
                    above!!.rightValue += rightValue
                }else{
                    var pabove = above
                    while (p!=null){
                        pabove = p
                        p = p!!.left
                    }
                    pabove!!.leftValue += rightValue
                }

                var pabove = above
                p = this
                while ((pabove!=null)&&(pabove!!.right!=p)){
                    p = pabove!!
                    pabove = p!!.above
                }

                if ((pabove!=null)&&(pabove.right == p)){
                    p = pabove.left
                    if (p == null){
                        pabove!!.leftValue += leftValue
                    }else{
                        while (p!=null){
                            pabove = p
                            p = p!!.right
                        }
                        pabove!!.rightValue += leftValue
                    }
                }
                above!!.left = null
                above!!.leftValue = 0
            }else{
                var p = above!!.left
                if (p==null){
                    above!!.leftValue += leftValue
                }else{
                    var pabove = above
                    while (p!=null){
                        pabove = p
                        p = p!!.right
                    }
                    pabove!!.rightValue += leftValue
                }

                var pabove = above
                p = this
                while ((pabove!=null)&&(pabove!!.left!=p)){
                    p = pabove!!
                    pabove = p!!.above
                }

                if ((pabove!=null)&&(pabove.left == p)){
                    p = pabove.right
                    if (p == null){
                        pabove!!.rightValue += rightValue
                    }else{
                        while (p!=null){
                            pabove = p
                            p = p!!.left
                        }
                        pabove!!.leftValue += rightValue
                    }
                }
                above!!.right = null
                above!!.rightValue = 0
            }
        }

        override fun toString(): String {
            var lv = leftValue.toString()
            if (left != null){
                lv = left.toString()
            }
            var rv = rightValue.toString()
            if (right != null){
                rv = right.toString()
            }
            return "["+ lv + "," + rv +"]"
        }

        override fun equals(other: Any?): Boolean {
            return ( (other is Pairnumber) && (other===this) )
        }
    }

    fun parse(s: String, above: Pairnumber? = null): Pairnumber{
        fun isNumber(st: String):Boolean{
            return (st[0]!='[')
        }
        var i = 0
        var ind = 0
        for (c in s){
            when(c){
                '[' ->  i++
                ']' ->  i--
                ',' ->  if (i==1) break
            }
            ind++
        }
        var left = s.substring(1,ind)
        var right = s.substring(ind+1).dropLast(1)
        var p: Pairnumber
        if (isNumber(left)){
            if (isNumber(right)){
                p = Pairnumber(above, null, null, left.toInt(),right.toInt())
            }else{
                p = Pairnumber(above, null, null, left.toInt(),-1)
                p.right = parse(right,p)
            }
        }else{
            if (isNumber(right)){
                p = Pairnumber(above, null, null, -1,right.toInt())
            }else{
                p = Pairnumber(above, null, null, -1,-1)
                p.right = parse(right,p)
            }
            p.left = parse(left,p)
        }
        return p
    }

    fun part1(input: List<String>): ULong{
        var p = parse(input[0]).reduceComlpetly()
        for (i in 1..input.size-1){
            p = p.add(parse(input[i]).reduceComlpetly())
        }
        return p.getValue()
    }

    fun part2(input: List<String>): ULong {
        var m = 0UL
        for (i in input.indices){
            for (j in input.indices){
                if (i != j){
                     m = maxOf(m, parse(input[i]).add(parse(input[j])).getValue() )
                }
            }
        }
        return m
    }

    // test if implementation meets criteria from the description, like:
    val dayname = "Day18"

    val testInput = readInput(dayname+"_test")
    check(part1(testInput) == 4140UL)
    check(part2(testInput) == 3993UL)

    val input = readInput(dayname)
    println(part1(input))
    println(part2(input))

}
