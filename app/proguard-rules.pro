# Keep Gson models
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class io.perklens.data.model.** { *; }
