@echo off
echo ==============================================
echo Finalizando entrega de la Sesion 11 (Tags)
echo ==============================================

:: 1. Cambiar a main y actualizar
echo 1. Actualizando rama main...
git checkout main
git pull origin main

:: 2. Crear etiqueta v2.0.0 y subir
echo 2. Creando tag v2.0.0...
git tag -a v2.0.0 -m "feat: SmartHealth TV - Android TV Leanback Library - Unidad III S11"
git push origin v2.0.0

echo ==============================================
echo ¡Listo! El Tag v2.0.0 esta en GitHub y listo para Moodle.
echo ==============================================
pause
