package com.example.tsumaps.data.Algorithms

import kotlin.math.pow
import kotlin.math.sqrt

class KMeans(
    private val k: Int,
    private val maxIterations: Int = 100
) {

    private val centroids = mutableListOf<Point>()

    private fun distance(a: Point, b: Point): Double {
        return sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2))
    }

    private fun initializeCentroids(points: List<Point>) {
        centroids.clear()
        val shuffled = points.shuffled()
        for (i in 0 until k) {
            centroids.add(shuffled[i].copy())
        }
    }

    private fun assignClusters(points: List<Point>): Boolean {
        var changed = false

        for (point in points) {
            var minDistance = Double.MAX_VALUE
            var bestCluster = 0

            for (i in centroids.indices) {
                val dist = distance(point, centroids[i])
                if (dist < minDistance) {
                    minDistance = dist
                    bestCluster = i
                }
            }

            if (point.cluster != bestCluster) {
                point.cluster = bestCluster
                changed = true
            }
        }

        return changed
    }

    private fun updateCentroids(points: List<Point>) {
        for (i in centroids.indices) {
            val clusterPoints = points.filter { it.cluster == i }

            if (clusterPoints.isEmpty()) continue

            val avgX = clusterPoints.map { it.x }.average()
            val avgY = clusterPoints.map { it.y }.average()

            centroids[i] = Point(avgX, avgY)
        }
    }

    fun fit(points: List<Point>): List<Point> {
        initializeCentroids(points)

        repeat(maxIterations) {
            val changed = assignClusters(points)
            updateCentroids(points)

            if (!changed) return points
        }

        return points
    }

    fun getCentroids(): List<Point> = centroids
}