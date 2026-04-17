package com.example.tsumaps.data

import kotlin.random.Random

class AntAlgorithm(
    val startPoint: Point,
    val pointsToVisit: List<Point>,
    val map: Array<IntArray>
) {
    val antCount = pointsToVisit.size
    val evaporationRate = 0.5
    val n = 100

    val allPoints = mutableListOf<Point>()

    lateinit var pheromones: Array<DoubleArray>

    init {
        allPoints.add(startPoint)
        allPoints.addAll(pointsToVisit)

        val n = allPoints.size
        pheromones = Array(n) { DoubleArray(n) }

        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i != j) {
                    pheromones[i][j] = 1.0
                } else {
                    pheromones[i][j] = 0.0
                }
            }
        }
    }

    fun getDistance(p1: Point, p2: Point): Int {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y)
    }

    fun getWayLength(way: List<Point>): Int {
        var length = 0
        for (i in 0 until way.size - 1) {
            length += getDistance(way[i], way[i + 1])
        }
        return length
    }

    fun findBestWay(): List<Point> {
        var bestWay: List<Point>? = null
        var bestLength = 999999

        for (n in 0 until n) {
            val allWays = mutableListOf<List<Point>>()

            for (ant in 0 until antCount) {
                val way = findWay()
                allWays.add(way)

                val wayLen = getWayLength(way)
                if (wayLen < bestLength) {
                    bestLength = wayLen
                    bestWay = way
                }
            }

            updatePheromones(allWays)
        }

        return bestWay ?: emptyList()
    }

    fun findWay(): List<Point> {
        val visited = BooleanArray(allPoints.size)
        val way = mutableListOf<Point>()

        var currentIndex = 0
        way.add(allPoints[currentIndex])
        visited[currentIndex] = true

        while (way.size < allPoints.size) {
            val notVisited = mutableListOf<Int>()
            for (i in allPoints.indices) {
                if (!visited[i]) {
                    notVisited.add(i)
                }
            }

            val nextIndex = nextPoint(currentIndex, notVisited)
            way.add(allPoints[nextIndex])
            visited[nextIndex] = true
            currentIndex = nextIndex
        }

        return way
    }

    fun nextPoint(currentIndex: Int, candidates: List<Int>): Int {
        val chances = DoubleArray(candidates.size)
        var totalChance = 0.0

        for (idx in candidates.indices) {
            val candidateIndex = candidates[idx]

            val pheromone = pheromones[currentIndex][candidateIndex]

            val distance = getDistance(
                allPoints[currentIndex],
                allPoints[candidateIndex]
            )

            val chance = pheromone * (1.0 / (distance + 1))

            chances[idx] = chance
            totalChance += chance
        }

        var random = Random.nextDouble() * totalChance
        for (idx in chances.indices) {
            random -= chances[idx]
            if (random <= 0) {
                return candidates[idx]
            }
        }

        return candidates[0]
    }

    fun updatePheromones(allWays: List<List<Point>>) {
        for (i in pheromones.indices) {
            for (j in pheromones[i].indices) {
                pheromones[i][j] = pheromones[i][j] * (1 - evaporationRate)
            }
        }

        for (way in allWays) {
            val wayLength = getWayLength(way)
            val pheromoneToAdd = 1.0 / wayLength

            for (step in 0 until way.size - 1) {
                val from = way[step]
                val to = way[step + 1]

                val fromIndex = allPoints.indexOf(from)
                val toIndex = allPoints.indexOf(to)

                pheromones[fromIndex][toIndex] += pheromoneToAdd
            }
        }
    }
}