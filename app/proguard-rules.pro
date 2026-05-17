# Add project specific ProGuard rules here.
# Keep Retrofit and Gson classes
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.gymapp.model.** { *; }
