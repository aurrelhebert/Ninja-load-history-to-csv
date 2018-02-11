package io.ninja

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.jsoup.Jsoup
import java.io.File

fun main(args: Array<String>) {
    println("Hello world!")

    val parser = Parser()

    val result = StringBuilder()
    // Html parsing

    // Apply treatment on 70 existing pages
    for (i in 61 .. 68) {

        var index = ""

        println("Start page ${i}")

        // Manage first index particularity
        if (i > 1) {
            index = "/${i}"
        }

        // Load main lci - TF1 13 h page with all articles
        Jsoup.connect("https://www.lci.fr/emission/le-13h${index}").get().run {
            select("a").forEachIndexed { _, element ->
                if (element.attr("class") == "medium-3col-article-block-article-link") {

                    // Then connect to specific page element
                    Jsoup.connect("https://www.lci.fr${element.attr("href")}").get().run {

                        // Load it's details
                        select("script").forEachIndexed { _, element ->
                            if (element.attr("type") == "application/ld+json") {
                                if (element.data().contains("uploadDate")) {

                                    // Generate String builder
                                    val sb = StringBuilder()
                                    sb.append(element.data())

                                    // And load existing JSON data
                                    val json: JsonObject = parser.parse(sb) as JsonObject

                                    // Apply specific treatment to get category from JSON
                                    var category = "None"
                                    if (json.containsKey("contentUrl")) {
                                        category = json.get("contentUrl").toString().split("/").get(1)
                                    }

                                    // Save result as CSV line saving: uploadDate, name, description, category
                                    result.append("${json.get("uploadDate")}, ${json.get("name").toString().replace(",","").replace("\n", "")}, ${json.get("description").toString().replace(",","").replace("\n", "")}, $category\n")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Every 10 files empty buffer
        if (i % 10 == 0) {
            val file = File("jt13h-full-$i.csv")

            file.writeText(result.toString())
            result.delete(0, result.length)
        }
    }

    // Empty buffer at the end
    val file = File("jt13h-full-last.csv")

    file.writeText(result.toString())
    result.delete(0, result.length)

    // Notify at end
    println("OK")
}