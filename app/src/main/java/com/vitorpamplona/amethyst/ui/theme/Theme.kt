package com.vitorpamplona.amethyst.ui.theme

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.ui.RichTextStyle
import com.vitorpamplona.amethyst.ui.components.DefaultHeadingStyle
import com.vitorpamplona.amethyst.ui.components.DefaultParagraphSpacing
import com.vitorpamplona.amethyst.ui.components.richTextDefaults

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Color(0xFFF7931A)
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Color(0xFFB66605)
)

private val DarkNewItemBackground = DarkColorPalette.primary.copy(0.12f)
private val LightNewItemBackground = LightColorPalette.primary.copy(0.12f)

private val DarkSelectedNote = DarkNewItemBackground.compositeOver(DarkColorPalette.background)
private val LightSelectedNote = LightNewItemBackground.compositeOver(LightColorPalette.background)

private val DarkButtonBackground = DarkColorPalette.primary.copy(alpha = 0.32f).compositeOver(DarkColorPalette.background)
private val LightButtonBackground = LightColorPalette.primary.copy(alpha = 0.32f).compositeOver(LightColorPalette.background)

private val DarkLessImportantLink = DarkColorPalette.primary.copy(alpha = 0.52f)
private val LightLessImportantLink = LightColorPalette.primary.copy(alpha = 0.52f)

private val DarkMediumImportantLink = DarkColorPalette.primary.copy(alpha = 0.32f)
private val LightMediumImportantLink = LightColorPalette.primary.copy(alpha = 0.32f)

private val DarkVeryImportantLink = DarkColorPalette.primary.copy(alpha = 0.12f)
private val LightVeryImportantLink = LightColorPalette.primary.copy(alpha = 0.12f)

private val DarkGrayText = DarkColorPalette.onSurface.copy(alpha = 0.52f)
private val LightGrayText = LightColorPalette.onSurface.copy(alpha = 0.52f)

private val DarkPlaceholderText = DarkColorPalette.onSurface.copy(alpha = 0.32f)
private val LightPlaceholderText = LightColorPalette.onSurface.copy(alpha = 0.32f)

private val DarkPlaceholderTextColorFilter = ColorFilter.tint(DarkPlaceholderText)
private val LightPlaceholderTextColorFilter = ColorFilter.tint(LightPlaceholderText)

private val DarkOnBackgroundColorFilter = ColorFilter.tint(DarkColorPalette.onBackground)
private val LightOnBackgroundColorFilter = ColorFilter.tint(LightColorPalette.onBackground)

private val DarkSubtleButton = DarkColorPalette.onSurface.copy(alpha = 0.22f)
private val LightSubtleButton = LightColorPalette.onSurface.copy(alpha = 0.22f)

private val DarkSubtleBorder = DarkColorPalette.onSurface.copy(alpha = 0.12f)
private val LightSubtleBorder = LightColorPalette.onSurface.copy(alpha = 0.12f)

private val DarkReplyItemBackground = DarkColorPalette.onSurface.copy(alpha = 0.05f)
private val LightReplyItemBackground = LightColorPalette.onSurface.copy(alpha = 0.05f)

private val DarkZapraiserBackground = BitcoinOrange.copy(0.52f).compositeOver(DarkColorPalette.background)
private val LightZapraiserBackground = BitcoinOrange.copy(0.52f).compositeOver(LightColorPalette.background)

private val DarkImageVerifier = Nip05.copy(0.52f).compositeOver(DarkColorPalette.background)
private val LightImageVerifier = Nip05.copy(0.52f).compositeOver(LightColorPalette.background)

private val DarkOverPictureBackground = DarkColorPalette.background.copy(0.62f)
private val LightOverPictureBackground = LightColorPalette.background.copy(0.62f)

val RepostPictureBorderDark = Modifier.border(
    2.dp,
    DarkColorPalette.background,
    CircleShape
)

val RepostPictureBorderLight = Modifier.border(
    2.dp,
    LightColorPalette.background,
    CircleShape
)

val DarkImageModifier = Modifier
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        DarkSubtleBorder,
        QuoteBorder
    )

val LightImageModifier = Modifier
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        LightSubtleBorder,
        QuoteBorder
    )

val DarkReplyBorderModifier = Modifier
    .padding(top = 5.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        DarkSubtleBorder,
        QuoteBorder
    )

val LightReplyBorderModifier = Modifier
    .padding(top = 5.dp)
    .fillMaxWidth()
    .clip(shape = QuoteBorder)
    .border(
        1.dp,
        LightSubtleBorder,
        QuoteBorder
    )

val MarkDownStyleOnDark = richTextDefaults.copy(
    paragraphSpacing = DefaultParagraphSpacing,
    headingStyle = DefaultHeadingStyle,
    codeBlockStyle = richTextDefaults.codeBlockStyle?.copy(
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP
        ),
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(shape = QuoteBorder)
            .border(
                1.dp,
                DarkSubtleBorder,
                QuoteBorder
            )
            .drawBehind {
                drawRect(DarkColorPalette.onSurface.copy(alpha = 0.05f))
            }
    ),
    stringStyle = richTextDefaults.stringStyle?.copy(
        linkStyle = SpanStyle(
            color = DarkColorPalette.primary
        ),
        codeStyle = SpanStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP,
            background = DarkColorPalette.onSurface.copy(alpha = 0.22f)
        )
    )
)

val MarkDownStyleOnLight = richTextDefaults.copy(
    paragraphSpacing = DefaultParagraphSpacing,
    headingStyle = DefaultHeadingStyle,
    codeBlockStyle = richTextDefaults.codeBlockStyle?.copy(
        textStyle = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP
        ),
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(shape = QuoteBorder)
            .border(
                1.dp,
                LightSubtleBorder,
                QuoteBorder
            )
            .drawBehind {
                drawRect(LightColorPalette.onSurface.copy(alpha = 0.05f))
            }
    ),
    stringStyle = richTextDefaults.stringStyle?.copy(
        linkStyle = SpanStyle(
            color = LightColorPalette.primary
        ),
        codeStyle = SpanStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = Font14SP,
            background = LightColorPalette.onSurface.copy(alpha = 0.22f)
        )
    )
)

val Colors.newItemBackgroundColor: Color
    get() = if (isLight) LightNewItemBackground else DarkNewItemBackground

val Colors.replyBackground: Color
    get() = if (isLight) LightReplyItemBackground else DarkReplyItemBackground

val Colors.selectedNote: Color
    get() = if (isLight) LightSelectedNote else DarkSelectedNote

val Colors.secondaryButtonBackground: Color
    get() = if (isLight) LightButtonBackground else DarkButtonBackground

val Colors.lessImportantLink: Color
    get() = if (isLight) LightLessImportantLink else DarkLessImportantLink

val Colors.zapraiserBackground: Color
    get() = if (isLight) LightZapraiserBackground else DarkZapraiserBackground

val Colors.mediumImportanceLink: Color
    get() = if (isLight) LightMediumImportantLink else DarkMediumImportantLink
val Colors.veryImportantLink: Color
    get() = if (isLight) LightVeryImportantLink else DarkVeryImportantLink

val Colors.placeholderText: Color
    get() = if (isLight) LightPlaceholderText else DarkPlaceholderText

val Colors.placeholderTextColorFilter: ColorFilter
    get() = if (isLight) LightPlaceholderTextColorFilter else DarkPlaceholderTextColorFilter

val Colors.onBackgroundColorFilter: ColorFilter
    get() = if (isLight) LightOnBackgroundColorFilter else DarkOnBackgroundColorFilter

val Colors.grayText: Color
    get() = if (isLight) LightGrayText else DarkGrayText

val Colors.subtleBorder: Color
    get() = if (isLight) LightSubtleBorder else DarkSubtleBorder

val Colors.subtleButton: Color
    get() = if (isLight) LightSubtleButton else DarkSubtleButton

val Colors.hashVerified: Color
    get() = if (isLight) LightImageVerifier else DarkImageVerifier

val Colors.overPictureBackground: Color
    get() = if (isLight) LightOverPictureBackground else DarkOverPictureBackground

val Colors.markdownStyle: RichTextStyle
    get() = if (isLight) MarkDownStyleOnLight else MarkDownStyleOnDark

val Colors.repostProfileBorder: Modifier
    get() = if (isLight) RepostPictureBorderLight else RepostPictureBorderDark

val Colors.imageModifier: Modifier
    get() = if (isLight) LightImageModifier else DarkImageModifier

val Colors.replyModifier: Modifier
    get() = if (isLight) LightReplyBorderModifier else DarkReplyBorderModifier

@Composable
fun AmethystTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    val view = LocalView.current
    if (!view.isInEditMode && darkTheme) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
        }
    }
}
