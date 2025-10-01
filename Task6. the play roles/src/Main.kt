import java.io.FileInputStream
import java.util.*
import java.io.File

fun printDialogues(dict: Map<String, List<String>>) {
    for ((speaker, lines) in dict) {
        println("$speaker:")
        for ((index, line) in lines.withIndex()) {
            println("${index + 1}) $line")
        }
        println()
    }
}

fun groupLinesBySpeaker(textLines: List<String>): MutableMap<String, MutableList<String>> {
    val dict: MutableMap<String, MutableList<String>> = mutableMapOf()
    for (t in textLines) {
        val colon = t.indexOf(":")
        val speaker = t.substring(0, colon)
        val line = t.substring(colon + 1)
        if (speaker !in dict) {
            dict[speaker] = mutableListOf(line)
        } else {
            dict[speaker]?.add(line)
        }
    }
    return dict
}

fun main() {
    val sc = Scanner(FileInputStream("src/roles.txt"))

    val roles = arrayListOf<String>()
    while (sc.hasNextLine())
        roles.add(sc.nextLine())

    val textLines = File("src/textLines.txt").readLines()

    val dict = groupLinesBySpeaker(textLines)
    printDialogues(dict)
}