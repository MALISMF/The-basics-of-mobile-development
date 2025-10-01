import java.io.FileInputStream
import java.util.*
import java.io.File

fun groupLinesBySpeaker(roles: List<String>, textLines: List<String>): LinkedHashMap<String, MutableList<String>> {
    val roleToLines = LinkedHashMap<String, MutableList<String>>(roles.size)
    for (role in roles) roleToLines[role] = mutableListOf()

    for ((idx, line) in textLines.withIndex()) {
        val colon = line.indexOf(':')
        if (colon < 0) continue
        val speaker = line.substring(0, colon)
        val text = line.substring(colon + 1).trimStart()
        roleToLines[speaker]?.add("${idx + 1}) $text")
    }
    return roleToLines
}

fun printDialogues(roles: List<String>, textLines: List<String>) {
    val dict = groupLinesBySpeaker(roles, textLines)
    for ((i, role) in roles.withIndex()) {
        println("$role:")
        val lines = dict[role] ?: emptyList()
        for (line in lines) println(line)
        if (i != roles.lastIndex) println()
    }
}

fun main() {
    val sc = Scanner(FileInputStream("src/roles.txt"))

    val roles = arrayListOf<String>()
    while (sc.hasNextLine())
        roles.add(sc.nextLine())

    val textLines = File("src/textLines.txt").readLines()

    printDialogues(roles, textLines)
}