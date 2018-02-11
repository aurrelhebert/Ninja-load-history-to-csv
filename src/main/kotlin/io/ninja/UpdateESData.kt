package io.ninja


import mbuhot.eskotlin.query.term.*

fun main(args: Array<String>) {
    println("Hello world!")


    val query = term {
        "user" to "Kimchy"
    }
}