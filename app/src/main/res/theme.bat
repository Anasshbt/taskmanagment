@echo off
setlocal EnableDelayedExpansion

REM Liste des noms de couleurs
set colors=red green blue yellow orange purple pink black white gray brown cyan magenta teal lime

REM Création des répertoires
for %%i in (%colors%) do (
    mkdir "values-%%i"
)

echo 20 répertoires avec des noms de couleurs ont été créés.
pause
