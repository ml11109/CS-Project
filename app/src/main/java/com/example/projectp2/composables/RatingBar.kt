package com.example.projectp2.composables

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.round

/*
// Rating bar like the one in xml
var rating by remember { mutableFloatStateOf(0f) }
RatingBar(rating, isEditable = true) { rating = it } // Editable
RatingBar(3.5f) // Non-editable
 */

@Composable
fun RatingBar(
    rating: Float = 5f,
    maxRating: Int = 5,
    step: Float = 0.5f,
    size: Dp = 24.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    isEditable: Boolean = false,
    onRatingChanged: (Float) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                if (!isEditable) return@pointerInput
                detectTapGestures { offset ->
                    val temp = offset.x / size.toPx()
                    val newRating = round(temp / step) * step
                    onRatingChanged(newRating)
                }
            }
            .pointerInput(Unit) {
                if (!isEditable) return@pointerInput
                detectDragGestures { change, _ ->
                    val temp = change.position.x / size.toPx()
                    val newRating = round(temp / step) * step
                    onRatingChanged(newRating)
                }
            }
    ) {
        for (i in 1..maxRating) {
            if (i <= rating.toInt()) {
                // Full stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(size)
                )
            } else if (i == rating.toInt() + 1 && rating % 1 != 0f) {
                // Partial star
                PartialStar(rating % 1, size, color)
            } else {
                // Empty stars
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}

@Composable
private fun PartialStar(fraction: Float, size: Dp, color: Color) {
    val customShape = FractionalClipShape(fraction)

    Box {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(size)
        )
        Box(
            modifier = Modifier
                .graphicsLayer(
                    clip = true,
                    shape = customShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(size)
            )
        }
    }
}

private class FractionalClipShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}
