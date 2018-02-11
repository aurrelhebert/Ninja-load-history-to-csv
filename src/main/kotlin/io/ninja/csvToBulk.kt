package io.ninja

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.jsoup.Jsoup
import java.io.File

// Get french month
fun generateMonth(month: String): String {
    when (month) {
        "1" -> return "Janvier"
        "2" -> return "Fevrier"
        "3" -> return "Mars"
        "4" -> return "Avril"
        "5" -> return "Mai"
        "6" -> return "Juin"
        "7" -> return "Juillet"
        "8" -> return "Aout"
        "9" -> return "Septembre"
        "10" -> return "Octobre"
        "11" -> return "Novembre"
        "12" -> return "Decembre"
        else -> return "None"
    }
}

fun main(args: Array<String>) {

    // Get count id
    var count =  50000

    // Current bulk buffer
    val sb = StringBuilder()

    // loop over history
    for (i in 1..7) {

        // manage filename
        var currentfile= i.toString() + "0"
        if (i == 7) {
            currentfile = "last"
        }

        println("jt13h-full-${currentfile}.csv")

        var file = File("jt13h-full-${currentfile}.csv")

        // For each line
        file.forEachLine {
            val items = it.split(",")

            // Remove item with empty date
            if (items[0] != "") {
                val numberMonth = items[0].split("-")[1]

                // Get current month in french
                val month: String = generateMonth(numberMonth)

                // Generate an item
                val item = Item(items[0], items[1], items[2], items[3], month, "ninja13h", count)

                // Add item to current bulk
                sb.append(item.toString())

                // Decrease id
                count--
            }
        }
    }

    // println(sb.toString())

    // Empty buffer at the end
    val bulk = File("bulkText")
    bulk.writeText(sb.toString())

    // Keep trace of count idea
    println("Lines=" + count)
}

