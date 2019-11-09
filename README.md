Service
=======

Libraries Used
--------------
* [KotlinxCoroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Asynchronous or non-blocking programming is the new reality
* [Android KTX](https://developer.android.com/kotlin/ktx) - Write more concise, idiomatic Kotlin code.
  * [androidx.fragment:fragment-ktx:1.2.0-beta01]() - easy fragment transactions / method *by viewModels() {SavedStateViewModelFactory()}* instead ViewModelProvider
  * [androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-beta01]() - viewModelScope for view Model
  * [androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-alpha01]() - coroutines for liveData
  * [androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha01]() - lifecycleScope for Fragment/Activity  and launchWhenResumed
  * [androidx.navigation:navigation-runtime-ktx:2.2.0-beta01]() - Acitivty.findNavController + Activity.navArgs + View.findNavController
  * [androidx.navigation:navigation-fragment-ktx:2.2.0-beta01]() - Fragment.findNavController + Fragment.navArgs
  * [androidx.navigation:navigation-ui-ktx:2.2.0-beta01]() - setupActionBarWithNavController for toolbar + setupWithNavController for bottomAppBar
* [Architecture](https://developer.android.com/jetpack/arch/) - A collection of libraries that help you design robust, testable, and maintainable apps.
    * [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declaratively bind observable data to UI elements.
*  [DarkTheme](https://habr.com/ru/company/redmadrobot/blog/461201/) - Article how to implement dark theme
*  [ConstraintLayout2](https://proandroiddev.com/awesomeness-of-constraintlayout-flow-aa0b5edd5df) - flow for many elements
*  [ColorTheme](https://stackoverflow.com/questions/30470437/android-theme-using-multiple-colors-xml) - set theme color via xml
![image](screen3.png)![image](screen2.png)![image](screen1.png)