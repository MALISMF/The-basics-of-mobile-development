fun calculate_rabbit_population(N: Int, M: Int, R: Int): Long {
    // массив возрастов
    val ages = MutableList(M) { 0L }
    ages[0] = R.toLong()  // изначально R пар новорожденных


    for (month in 1 until N) {
        val newborns = ages.subList(1, ages.size).sum() 

        for (i in M - 1 downTo 1) {
            // сдвигаем кроликов на следующий месяц
            ages[i] = ages[i - 1]
        }
        // добавляем новорожденных кроликов на первый месяц жизни
        ages[0] = newborns
    }

    return ages.sum()
}

fun main() {
    /*
        N - количество месяцев
        M - продолжительность жизни кроликов
        R - количество изначальных пар кроликов
    */
    println(calculate_rabbit_population(85, 19, 1))
}
