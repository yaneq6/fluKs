package io.fluks.feature.scheme.data

data class Surface(
    val left: Int,
    val right: Int,
    val top: Int,
    val bottom: Int
)

data class Plane(
    val list: List<Surface>
)