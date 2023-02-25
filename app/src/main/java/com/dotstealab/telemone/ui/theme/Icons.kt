package com.dotstealab.telemone.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object GooglesIcons {
    val SharpDarkMode: ImageVector = ImageVector.Builder(
        name = "SharpDarkMode", defaultWidth = 24.0.dp, defaultHeight =
        24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
            strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 3.0f)
            curveToRelative(-4.97f, 0.0f, -9.0f, 4.03f, -9.0f, 9.0f)
            reflectiveCurveToRelative(4.03f, 9.0f, 9.0f, 9.0f)
            reflectiveCurveToRelative(9.0f, -4.03f, 9.0f, -9.0f)
            curveToRelative(0.0f, -0.46f, -0.04f, -0.92f, -0.1f, -1.36f)
            curveToRelative(-0.98f, 1.37f, -2.58f, 2.26f, -4.4f, 2.26f)
            curveToRelative(-2.98f, 0.0f, -5.4f, -2.42f, -5.4f, -5.4f)
            curveToRelative(0.0f, -1.81f, 0.89f, -3.42f, 2.26f, -4.4f)
            curveTo(12.92f, 3.04f, 12.46f, 3.0f, 12.0f, 3.0f)
            lineTo(12.0f, 3.0f)
            close()
        }
    }.build()

    val SharpLightMode24: ImageVector = ImageVector.Builder(
        name = "SharpLightMode24", defaultWidth = 24.0.dp, defaultHeight
        = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
            strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 7.0f)
            curveToRelative(-2.76f, 0.0f, -5.0f, 2.24f, -5.0f, 5.0f)
            reflectiveCurveToRelative(2.24f, 5.0f, 5.0f, 5.0f)
            reflectiveCurveToRelative(5.0f, -2.24f, 5.0f, -5.0f)
            reflectiveCurveTo(14.76f, 7.0f, 12.0f, 7.0f)
            lineTo(12.0f, 7.0f)
            close()
            moveTo(11.0f, 1.0f)
            verticalLineToRelative(4.0f)
            horizontalLineToRelative(2.0f)
            verticalLineTo(1.0f)
            lineTo(11.0f, 1.0f)
            close()
            moveTo(11.0f, 19.0f)
            verticalLineToRelative(4.0f)
            horizontalLineToRelative(2.0f)
            verticalLineToRelative(-4.0f)
            lineTo(11.0f, 19.0f)
            close()
            moveTo(23.0f, 11.0f)
            lineToRelative(-4.0f, 0.0f)
            verticalLineToRelative(2.0f)
            lineToRelative(4.0f, 0.0f)
            verticalLineTo(11.0f)
            close()
            moveTo(5.0f, 11.0f)
            lineToRelative(-4.0f, 0.0f)
            lineToRelative(0.0f, 2.0f)
            lineToRelative(4.0f, 0.0f)
            lineTo(5.0f, 11.0f)
            close()
            moveTo(16.24f, 17.66f)
            lineToRelative(2.47f, 2.47f)
            lineToRelative(1.41f, -1.41f)
            lineToRelative(-2.47f, -2.47f)
            lineTo(16.24f, 17.66f)
            close()
            moveTo(3.87f, 5.28f)
            lineToRelative(2.47f, 2.47f)
            lineToRelative(1.41f, -1.41f)
            lineTo(5.28f, 3.87f)
            lineTo(3.87f, 5.28f)
            close()
            moveTo(6.34f, 16.24f)
            lineToRelative(-2.47f, 2.47f)
            lineToRelative(1.41f, 1.41f)
            lineToRelative(2.47f, -2.47f)
            lineTo(6.34f, 16.24f)
            close()
            moveTo(18.72f, 3.87f)
            lineToRelative(-2.47f, 2.47f)
            lineToRelative(1.41f, 1.41f)
            lineToRelative(2.47f, -2.47f)
            lineTo(18.72f, 3.87f)
            close()
        }
    }.build()
}

// credits to https://t.me/Design480 !!!
public object SolarSet {
    val Sun: ImageVector = ImageVector.Builder(
        name = "Sun 2", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
        viewportWidth = 24.0f, viewportHeight = 24.0f
    ).apply {
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 12.0f)
            moveToRelative(-5.0f, 0.0f)
            arcToRelative(5.0f, 5.0f, 0.0f, true, true, 10.0f, 0.0f)
            arcToRelative(5.0f, 5.0f, 0.0f, true, true, -10.0f, 0.0f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 2.0f)
            verticalLineTo(4.0f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 20.0f)
            verticalLineTo(22.0f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(4.0f, 12.0f)
            lineTo(2.0f, 12.0f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(22.0f, 12.0f)
            lineTo(20.0f, 12.0f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(19.7778f, 4.2227f)
            lineTo(17.5558f, 6.2542f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(4.2222f, 4.2227f)
            lineTo(6.4442f, 6.2542f)
            }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(6.4443f, 17.5557f)
            lineTo(4.2221f, 19.7779f)
        }
        path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF1C274C)),
            strokeLineWidth = 1.5f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0f, pathFillType = PathFillType.NonZero
        ) {
            moveTo(19.7778f, 19.7773f)
            lineTo(17.5558f, 17.5551f)
        }
    }.build()


    val Moon: ImageVector = ImageVector.Builder(
        name = "Moon stars", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
        viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
        path(fill = SolidColor(Color(0xFF1C274C)), stroke = null, strokeLineWidth = 0.0f,
            strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f,
            pathFillType = PathFillType.EvenOdd
        ) {
            moveTo(20.3655f, 2.1243f)
            curveTo(20.0384f, 1.2919f, 18.8624f, 1.2919f, 18.5353f, 2.1243f)
            lineTo(18.1073f, 3.2135f)
            lineTo(17.0227f, 3.6429f)
            curveTo(16.1933f, 3.9712f, 16.1933f, 5.1471f, 17.0227f, 5.4754f)
            lineTo(18.1073f, 5.9048f)
            lineTo(18.5353f, 6.994f)
            curveTo(18.8624f, 7.8264f, 20.0384f, 7.8265f, 20.3655f, 6.994f)
            lineTo(20.7935f, 5.9048f)
            lineTo(21.8781f, 5.4754f)
            curveTo(22.7075f, 5.1471f, 22.7075f, 3.9712f, 21.8781f, 3.6429f)
            lineTo(20.7935f, 3.2135f)
            lineTo(20.3655f, 2.1243f)
            close()
            moveTo(19.4504f, 2.5299f)
            lineTo(19.8651f, 3.5853f)
            curveTo(19.9648f, 3.8389f, 20.165f, 4.0403f, 20.4188f, 4.1407f)
            lineTo(21.4759f, 4.5592f)
            lineTo(20.4188f, 4.9776f)
            curveTo(20.165f, 5.0781f, 19.9648f, 5.2794f, 19.8651f, 5.533f)
            lineTo(19.4504f, 6.5885f)
            lineTo(19.0357f, 5.533f)
            curveTo(18.936f, 5.2794f, 18.7358f, 5.0781f, 18.482f, 4.9776f)
            lineTo(17.4249f, 4.5592f)
            lineTo(18.482f, 4.1407f)
            curveTo(18.7358f, 4.0403f, 18.936f, 3.8389f, 19.0357f, 3.5853f)
            lineTo(19.4504f, 2.5299f)
            close()
            moveTo(16.4981f, 7.9468f)
            curveTo(16.171f, 7.1144f, 14.9951f, 7.1144f, 14.668f, 7.9468f)
            lineTo(14.5134f, 8.3401f)
            lineTo(14.1222f, 8.495f)
            curveTo(13.2928f, 8.8233f, 13.2928f, 9.9992f, 14.1222f, 10.3275f)
            lineTo(14.5134f, 10.4824f)
            lineTo(14.668f, 10.8757f)
            curveTo(14.9951f, 11.7081f, 16.171f, 11.7081f, 16.4981f, 10.8757f)
            lineTo(16.6526f, 10.4824f)
            lineTo(17.0439f, 10.3275f)
            curveTo(17.8733f, 9.9992f, 17.8733f, 8.8233f, 17.0439f, 8.495f)
            lineTo(16.6526f, 8.3401f)
            lineTo(16.4981f, 7.9468f)
            close()
            moveTo(15.583f, 8.3524f)
            lineTo(15.7243f, 8.7119f)
            curveTo(15.824f, 8.9655f, 16.0242f, 9.1668f, 16.278f, 9.2673f)
            lineTo(16.6417f, 9.4112f)
            lineTo(16.278f, 9.5552f)
            curveTo(16.0242f, 9.6557f, 15.824f, 9.857f, 15.7243f, 10.1106f)
            lineTo(15.583f, 10.4701f)
            lineTo(15.4418f, 10.1106f)
            curveTo(15.3421f, 9.857f, 15.1419f, 9.6557f, 14.8881f, 9.5552f)
            lineTo(14.5244f, 9.4112f)
            lineTo(14.8881f, 9.2673f)
            curveTo(15.1419f, 9.1668f, 15.3421f, 8.9655f, 15.4418f, 8.7119f)
            lineTo(15.583f, 8.3524f)
            close()
        }
        path(fill = SolidColor(Color(0xFF1C274C)), stroke = null, strokeLineWidth = 0.0f,
            strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f,
            pathFillType = PathFillType.EvenOdd
        ) {
            moveTo(11.0174f, 2.8016f)
            curveTo(6.3707f, 3.2922f, 2.75f, 7.2233f, 2.75f, 12.0f)
            curveTo(2.75f, 17.1086f, 6.8914f, 21.25f, 12.0f, 21.25f)
            curveTo(16.7767f, 21.25f, 20.7078f, 17.6293f, 21.1984f, 12.9826f)
            curveTo(19.8717f, 14.6669f, 17.8126f, 15.75f, 15.5f, 15.75f)
            curveTo(11.4959f, 15.75f, 8.25f, 12.5041f, 8.25f, 8.5f)
            curveTo(8.25f, 6.1874f, 9.3331f, 4.1283f, 11.0174f, 2.8016f)
            close()
            moveTo(1.25f, 12.0f)
            curveTo(1.25f, 6.0629f, 6.0629f, 1.25f, 12.0f, 1.25f)
            curveTo(12.7166f, 1.25f, 13.0754f, 1.8213f, 13.1368f, 2.2763f)
            curveTo(13.196f, 2.714f, 13.0342f, 3.2706f, 12.531f, 3.5747f)
            curveTo(10.8627f, 4.5828f, 9.75f, 6.4118f, 9.75f, 8.5f)
            curveTo(9.75f, 11.6756f, 12.3244f, 14.25f, 15.5f, 14.25f)
            curveTo(17.5882f, 14.25f, 19.4172f, 13.1373f, 20.4253f, 11.469f)
            curveTo(20.7293f, 10.9658f, 21.286f, 10.804f, 21.7237f, 10.8632f)
            curveTo(22.1787f, 10.9246f, 22.75f, 11.2834f, 22.75f, 12.0f)
            curveTo(22.75f, 17.9371f, 17.9371f, 22.75f, 12.0f, 22.75f)
            curveTo(6.0629f, 22.75f, 1.25f, 17.9371f, 1.25f, 12.0f)
            close()
        }
    }.build()
}
