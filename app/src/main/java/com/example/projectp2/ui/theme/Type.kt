package com.example.projectp2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.example.projectp2.R

val Roboto = FontFamily(
    Font(R.font.roboto, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

object TypefaceTokens {
    val Brand = Roboto
    val Plain = Roboto
    val WeightBold = FontWeight.Bold
    val WeightMedium = FontWeight.Medium
    val WeightRegular = FontWeight.Normal
}

object TypeScaleTokens {
    val BodyLargeFont = TypefaceTokens.Plain
    val BodyLargeLineHeight = 24.0.sp
    val BodyLargeSize = 16.sp
    val BodyLargeTracking = 0.5.sp
    val BodyLargeWeight = TypefaceTokens.WeightRegular
    val BodyMediumFont = TypefaceTokens.Plain
    val BodyMediumLineHeight = 20.0.sp
    val BodyMediumSize = 14.sp
    val BodyMediumTracking = 0.2.sp
    val BodyMediumWeight = TypefaceTokens.WeightRegular
    val BodySmallFont = TypefaceTokens.Plain
    val BodySmallLineHeight = 16.0.sp
    val BodySmallSize = 12.sp
    val BodySmallTracking = 0.4.sp
    val BodySmallWeight = TypefaceTokens.WeightRegular
    val DisplayLargeFont = TypefaceTokens.Brand
    val DisplayLargeLineHeight = 64.0.sp
    val DisplayLargeSize = 57.sp
    val DisplayLargeTracking = -0.2.sp
    val DisplayLargeWeight = TypefaceTokens.WeightRegular
    val DisplayMediumFont = TypefaceTokens.Brand
    val DisplayMediumLineHeight = 52.0.sp
    val DisplayMediumSize = 45.sp
    val DisplayMediumTracking = 0.0.sp
    val DisplayMediumWeight = TypefaceTokens.WeightRegular
    val DisplaySmallFont = TypefaceTokens.Brand
    val DisplaySmallLineHeight = 44.0.sp
    val DisplaySmallSize = 36.sp
    val DisplaySmallTracking = 0.0.sp
    val DisplaySmallWeight = TypefaceTokens.WeightRegular
    val HeadlineLargeFont = TypefaceTokens.Brand
    val HeadlineLargeLineHeight = 40.0.sp
    val HeadlineLargeSize = 32.sp
    val HeadlineLargeTracking = 0.0.sp
    val HeadlineLargeWeight = TypefaceTokens.WeightRegular
    val HeadlineMediumFont = TypefaceTokens.Brand
    val HeadlineMediumLineHeight = 36.0.sp
    val HeadlineMediumSize = 28.sp
    val HeadlineMediumTracking = 0.0.sp
    val HeadlineMediumWeight = TypefaceTokens.WeightRegular
    val HeadlineSmallFont = TypefaceTokens.Brand
    val HeadlineSmallLineHeight = 32.0.sp
    val HeadlineSmallSize = 24.sp
    val HeadlineSmallTracking = 0.0.sp
    val HeadlineSmallWeight = TypefaceTokens.WeightRegular
    val LabelLargeFont = TypefaceTokens.Plain
    val LabelLargeLineHeight = 20.0.sp
    val LabelLargeSize = 14.sp
    val LabelLargeTracking = 0.1.sp
    val LabelLargeWeight = TypefaceTokens.WeightMedium
    val LabelMediumFont = TypefaceTokens.Plain
    val LabelMediumLineHeight = 16.0.sp
    val LabelMediumSize = 12.sp
    val LabelMediumTracking = 0.5.sp
    val LabelMediumWeight = TypefaceTokens.WeightMedium
    val LabelSmallFont = TypefaceTokens.Plain
    val LabelSmallLineHeight = 16.0.sp
    val LabelSmallSize = 11.sp
    val LabelSmallTracking = 0.5.sp
    val LabelSmallWeight = TypefaceTokens.WeightMedium
    val TitleLargeFont = TypefaceTokens.Brand
    val TitleLargeLineHeight = 28.0.sp
    val TitleLargeSize = 22.sp
    val TitleLargeTracking = 0.0.sp
    val TitleLargeWeight = TypefaceTokens.WeightRegular
    val TitleMediumFont = TypefaceTokens.Plain
    val TitleMediumLineHeight = 24.0.sp
    val TitleMediumSize = 16.sp
    val TitleMediumTracking = 0.2.sp
    val TitleMediumWeight = TypefaceTokens.WeightMedium
    val TitleSmallFont = TypefaceTokens.Plain
    val TitleSmallLineHeight = 20.0.sp
    val TitleSmallSize = 14.sp
    val TitleSmallTracking = 0.1.sp
    val TitleSmallWeight = TypefaceTokens.WeightMedium
}

val DefaultTextStyle =
    TextStyle.Default.copy(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)
    )

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.BodyLargeFont,
        fontWeight = TypeScaleTokens.BodyLargeWeight,
        fontSize = TypeScaleTokens.BodyLargeSize,
        lineHeight = TypeScaleTokens.BodyLargeLineHeight,
        letterSpacing = TypeScaleTokens.BodyLargeTracking,
    ),
    bodyMedium =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.BodyMediumFont,
        fontWeight = TypeScaleTokens.BodyMediumWeight,
        fontSize = TypeScaleTokens.BodyMediumSize,
        lineHeight = TypeScaleTokens.BodyMediumLineHeight,
        letterSpacing = TypeScaleTokens.BodyMediumTracking,
    ),
    bodySmall =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.BodySmallFont,
        fontWeight = TypeScaleTokens.BodySmallWeight,
        fontSize = TypeScaleTokens.BodySmallSize,
        lineHeight = TypeScaleTokens.BodySmallLineHeight,
        letterSpacing = TypeScaleTokens.BodySmallTracking,
    ),
    displayLarge =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.DisplayLargeFont,
        fontWeight = TypeScaleTokens.DisplayLargeWeight,
        fontSize = TypeScaleTokens.DisplayLargeSize,
        lineHeight = TypeScaleTokens.DisplayLargeLineHeight,
        letterSpacing = TypeScaleTokens.DisplayLargeTracking,
    ),
    displayMedium =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.DisplayMediumFont,
        fontWeight = TypeScaleTokens.DisplayMediumWeight,
        fontSize = TypeScaleTokens.DisplayMediumSize,
        lineHeight = TypeScaleTokens.DisplayMediumLineHeight,
        letterSpacing = TypeScaleTokens.DisplayMediumTracking,
    ),
    displaySmall =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.DisplaySmallFont,
        fontWeight = TypeScaleTokens.DisplaySmallWeight,
        fontSize = TypeScaleTokens.DisplaySmallSize,
        lineHeight = TypeScaleTokens.DisplaySmallLineHeight,
        letterSpacing = TypeScaleTokens.DisplaySmallTracking,
    ),
    headlineLarge =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.HeadlineLargeFont,
        fontWeight = TypeScaleTokens.HeadlineLargeWeight,
        fontSize = TypeScaleTokens.HeadlineLargeSize,
        lineHeight = TypeScaleTokens.HeadlineLargeLineHeight,
        letterSpacing = TypeScaleTokens.HeadlineLargeTracking,
    ),
    headlineMedium =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.HeadlineMediumFont,
        fontWeight = TypeScaleTokens.HeadlineMediumWeight,
        fontSize = TypeScaleTokens.HeadlineMediumSize,
        lineHeight = TypeScaleTokens.HeadlineMediumLineHeight,
        letterSpacing = TypeScaleTokens.HeadlineMediumTracking,
    ),
    headlineSmall =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.HeadlineSmallFont,
        fontWeight = TypeScaleTokens.HeadlineSmallWeight,
        fontSize = TypeScaleTokens.HeadlineSmallSize,
        lineHeight = TypeScaleTokens.HeadlineSmallLineHeight,
        letterSpacing = TypeScaleTokens.HeadlineSmallTracking,
    ),
    labelLarge =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.LabelLargeFont,
        fontWeight = TypeScaleTokens.LabelLargeWeight,
        fontSize = TypeScaleTokens.LabelLargeSize,
        lineHeight = TypeScaleTokens.LabelLargeLineHeight,
        letterSpacing = TypeScaleTokens.LabelLargeTracking,
    ),
    labelMedium =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.LabelMediumFont,
        fontWeight = TypeScaleTokens.LabelMediumWeight,
        fontSize = TypeScaleTokens.LabelMediumSize,
        lineHeight = TypeScaleTokens.LabelMediumLineHeight,
        letterSpacing = TypeScaleTokens.LabelMediumTracking,
    ),
    labelSmall =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.LabelSmallFont,
        fontWeight = TypeScaleTokens.LabelSmallWeight,
        fontSize = TypeScaleTokens.LabelSmallSize,
        lineHeight = TypeScaleTokens.LabelSmallLineHeight,
        letterSpacing = TypeScaleTokens.LabelSmallTracking,
    ),
    titleLarge =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.TitleLargeFont,
        fontWeight = TypeScaleTokens.TitleLargeWeight,
        fontSize = TypeScaleTokens.TitleLargeSize,
        lineHeight = TypeScaleTokens.TitleLargeLineHeight,
        letterSpacing = TypeScaleTokens.TitleLargeTracking,
    ),
    titleMedium =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.TitleMediumFont,
        fontWeight = TypeScaleTokens.TitleMediumWeight,
        fontSize = TypeScaleTokens.TitleMediumSize,
        lineHeight = TypeScaleTokens.TitleMediumLineHeight,
        letterSpacing = TypeScaleTokens.TitleMediumTracking,
    ),
    titleSmall =
    DefaultTextStyle.copy(
        fontFamily = TypeScaleTokens.TitleSmallFont,
        fontWeight = TypeScaleTokens.TitleSmallWeight,
        fontSize = TypeScaleTokens.TitleSmallSize,
        lineHeight = TypeScaleTokens.TitleSmallLineHeight,
        letterSpacing = TypeScaleTokens.TitleSmallTracking,
    )
)