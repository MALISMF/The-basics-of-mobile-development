// Ответ: 983


fun decimal_period(numer: Int, denom: Int): List<Int> {
    var int_part: Int = numer / denom
    var rem: Int = numer % denom

    val digits = mutableListOf<Int>()
    val first_pos_of_rem = mutableMapOf<Int, Int>()
    var period_start: Int? = 0

    while (rem != 0) {
        if (rem in first_pos_of_rem) {
            period_start = first_pos_of_rem[rem]
            break
        }
        first_pos_of_rem[rem] = digits.size

        rem *= 10
        val digit: Int = rem / denom
        digits.add(digit)
        rem = rem % denom
    }

    if (rem == 0) {
        return mutableListOf<Int>()
    }
    else {
        val period = digits.slice(period_start!!..digits.lastIndex)
        return period
    }
}

fun main() {
    var max_period: List<Int> = listOf<Int>()
    var max_period_len: Int = Int.MIN_VALUE
    var d: Int = 0

    for (n in 3..1000) {
        val period: List<Int> = decimal_period(1, n)

        if (period.size > max_period_len) {
            max_period_len = period.size
            max_period = period
            d = n
        }
    }

    print(d)

}

