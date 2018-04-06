package io.ninja

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.apache.commons.lang.math.NumberUtils.toInt
import org.jsoup.Jsoup
import java.io.File
import java.math.BigInteger

// Get french month
fun generateMonth(month: String): String {
    when (month) {
        "01" -> return "Janvier"
        "02" -> return "Fevrier"
        "03" -> return "Mars"
        "04" -> return "Avril"
        "05" -> return "Mai"
        "06" -> return "Juin"
        "07" -> return "Juillet"
        "08" -> return "Aout"
        "09" -> return "Septembre"
        "10" -> return "Octobre"
        "11" -> return "Novembre"
        "12" -> return "Decembre"
        else -> return "None"
    }
}

fun history() {
    // Current bulk buffer
    val sb = StringBuilder()

    // Initialize counter
    var count =  50000

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

                //println(month + ": " + numberMonth)

                // Generate an item
                val item = Item(items[0], items[1], items[2], items[3], month, "ninja13h", count, "")

                // Add item to current bulk
                sb.append(item.toString())
                sb.append("\n")

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


fun main(args: Array<String>) {

    history()

    // Current bulk buffer
    val sb = StringBuilder()

    //
    // Parse current file
    //

    var file = File("jt13h-tmp-id.csv")


    // For each line
    file.forEachLine {
        val items = it.split(",")

        // Remove item with empty date
        if (items[0] != "") {
            val numberMonth = items[0].split("-")[1]

            // Get current month in french
            val month: String = generateMonth(numberMonth)

            //println(month + ": " + numberMonth)

            // Remove blank from current id
            val currentId: Number = toInt(items[4].trim())

            // Generate an item
            val item = Item(items[0], items[1], items[2], items[3], month, "ninja13h", currentId, items[5])

            // Add item to current bulk
            sb.append(item.toString())
            sb.append("\n")
        }
    }

    // sb.removePrefix("\n")
    // Empty buffer at the end

    val bulk = File("bulk_id")
    bulk.writeText(sb.toString())
}



