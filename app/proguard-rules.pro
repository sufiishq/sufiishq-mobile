# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# made crashlytics errors for human readable
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn pk.sufiishq.aurora.components.SIButtonKt
-dontwarn pk.sufiishq.aurora.components.SICircularProgressIndicatorKt
-dontwarn pk.sufiishq.aurora.components.SIClickableTextKt
-dontwarn pk.sufiishq.aurora.components.SIDividerKt
-dontwarn pk.sufiishq.aurora.components.SIIconKt
-dontwarn pk.sufiishq.aurora.components.SIImageKt
-dontwarn pk.sufiishq.aurora.components.SILinearProgressIndicatorKt
-dontwarn pk.sufiishq.aurora.components.SIMarqueeTextKt
-dontwarn pk.sufiishq.aurora.components.SIOutlinedTextFieldKt
-dontwarn pk.sufiishq.aurora.components.SIRangeSliderKt
-dontwarn pk.sufiishq.aurora.components.SISliderKt
-dontwarn pk.sufiishq.aurora.components.SISpaceKt
-dontwarn pk.sufiishq.aurora.components.SITextKt
-dontwarn pk.sufiishq.aurora.components.TextSize
-dontwarn pk.sufiishq.aurora.config.AuroraConfig
-dontwarn pk.sufiishq.aurora.layout.SIAuroraSurfaceKt
-dontwarn pk.sufiishq.aurora.layout.SIBoxKt
-dontwarn pk.sufiishq.aurora.layout.SICardKt
-dontwarn pk.sufiishq.aurora.layout.SIColumnKt
-dontwarn pk.sufiishq.aurora.layout.SIDIalogKt
-dontwarn pk.sufiishq.aurora.layout.SILazyColumnKt
-dontwarn pk.sufiishq.aurora.layout.SILazyVerticalGridKt
-dontwarn pk.sufiishq.aurora.layout.SIParallaxLazyColumnKt
-dontwarn pk.sufiishq.aurora.layout.SIRowKt
-dontwarn pk.sufiishq.aurora.layout.SIScaffoldKt
-dontwarn pk.sufiishq.aurora.layout.SISurfaceKt
-dontwarn pk.sufiishq.aurora.models.ColorPalette
-dontwarn pk.sufiishq.aurora.models.DataMenuItem
-dontwarn pk.sufiishq.aurora.theme.AuroraColor
-dontwarn pk.sufiishq.aurora.theme.ColorKt
-dontwarn pk.sufiishq.aurora.theme.Theme
-dontwarn pk.sufiishq.aurora.theme.ThemeKt
-dontwarn pk.sufiishq.aurora.widgets.SIColorPaletteViewKt
-dontwarn pk.sufiishq.aurora.widgets.SIDataRowKt
-dontwarn pk.sufiishq.aurora.widgets.SIExpandleCardKt
-dontwarn pk.sufiishq.aurora.widgets.SIPopupMenuKt
-dontwarn pk.sufiishq.aurora.widgets.SITopAppBarKt
-dontwarn pk.sufiishq.aurora.widgets.SITransformableImageKt