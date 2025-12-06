# === BiliPai ProGuard Rules ===
# Fixes: java.lang.Class cannot be cast to java.lang.reflect.ParameterizedType

# --- General ---
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses,EnclosingMethod
-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**

# === CRITICAL: Retrofit + OkHttp ===
# Keep generic signature for Retrofit API interfaces
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Keep Call/Response generic types
-keep,allowobfuscation,allowshrinking class retrofit2.Response
# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# === CRITICAL: Kotlinx Serialization ===
# Keep @Serializable classes and their serializers
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keep class * implements kotlinx.serialization.KSerializer { *; }
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# === Data Models (MUST keep for serialization) ===
-keep class com.android.purebilibili.data.model.** { *; }
-keepclassmembers class com.android.purebilibili.data.model.** { *; }

# === API Interfaces ===
-keep interface com.android.purebilibili.core.network.** { *; }
-keep class com.android.purebilibili.core.network.** { *; }

# === DanmakuFlameMaster ===
-keep class master.flame.danmaku.** { *; }

# === Room Database ===
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# === Media3 / ExoPlayer ===
-keep class androidx.media3.** { *; }

# === Coil Image Loading ===
-keep class coil.** { *; }