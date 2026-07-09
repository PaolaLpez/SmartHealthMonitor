@echo off
echo ==============================================
echo Configurando Git para Sesion 11 (SmartHealthMonitor3)
echo ==============================================

:: 1. Crear y cambiar a la rama feature
echo 1. Creando rama feature/s11-android-tv-leanback...
git checkout -b feature/s11-android-tv-leanback

:: 2. Añadir y commitear Ejercicio 01 + Reto de Colores
echo 2. Commiteando Ejercicio 01 y Colores...
git add settings.gradle.kts tv/build.gradle.kts tv/src/main/res/values/colors.xml tv/src/main/res/values/themes.xml tv/src/main/res/layout/activity_main.xml tv/src/main/res/drawable/banner_tv.xml tv/src/main/AndroidManifest.xml tv/src/main/java/mx/utng/smarthealthmonitor/tv/MainActivity.kt tv/src/main/java/mx/utng/smarthealthmonitor/tv/SmartHealthTVApp.kt
git commit -m "chore: add Android TV module with Leanback setup, MainActivity and Theme"
git commit -m "chore: add SmartHealth TV color palette"

:: 3. Añadir y commitear Ejercicio 02 + Reto de Alertas
echo 3. Commiteando Ejercicio 02 y Fila de Alertas...
git add tv/src/main/java/mx/utng/smarthealthmonitor/tv/FCCardPresenter.kt tv/src/main/java/mx/utng/smarthealthmonitor/tv/MainFragment.kt tv/src/main/java/mx/utng/smarthealthmonitor/data/MockData.kt
git commit -m "feat: add MainFragment (BrowseSupportFragment) with FCCardPresenter and 2 rows"
git commit -m "feat: add alerts row to SmartHealth TV BrowseFragment"

:: 4. Añadir y commitear Ejercicio 03
echo 4. Commiteando Ejercicio 03 (TvViewModel y Room)...
git add tv/src/main/java/mx/utng/smarthealthmonitor/tv/TvViewModel.kt tv/src/main/java/mx/utng/smarthealthmonitor/data/db/LecturaFC.kt tv/src/main/java/mx/utng/smarthealthmonitor/data/db/LecturaFCDao.kt tv/src/main/java/mx/utng/smarthealthmonitor/data/db/SmartHealthDB.kt tv/src/main/java/mx/utng/smarthealthmonitor/data/SmartHealthRepository.kt
git commit -m "feat: connect TvViewModel to Room DAO and StateFlow for reactive TV UI"

:: 5. Push a GitHub
echo 5. Subiendo la rama a GitHub...
git push origin feature/s11-android-tv-leanback

echo ==============================================
echo ¡Rama subida! Realiza el Pull Request en GitHub.
echo Una vez fusionado en GitHub (Merge), ejecuta git_finish.bat.
echo ==============================================
pause
