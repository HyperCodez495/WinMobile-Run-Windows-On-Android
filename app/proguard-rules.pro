# ProGuard rules for WinMobile

# Keep all classes in the app package
-keep class com.winmobile.** { *; }

# Keep data classes
-keepclassmembers class com.winmobile.** {
    *** get*();
    void set*(***);
}

# Keep Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.activity.compose.** { *; }

# Keep Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Keep AndroidX
-keep class androidx.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep serializable classes
-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

# Logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
