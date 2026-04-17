package com.example.tsumaps.data

class Node(val x: Int, val y: Int) {
    var g: Int = 0
    var h: Int = 0
    val f: Int get() = g + h
    var parent: Node? = null
}
