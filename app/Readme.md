# Calculadora iOS - Android Nativo

Calculadora estilo iPhone desarrollada en Kotlin con Jetpack Compose y arquitectura MVVM. 
Incluye historial de operaciones y navegacion entre pantallas.

Proyecto I de Programación para Móviles


## Requisitos aplicados

Android Studio
Kotlin
Min SDK: API 24 (Android 7.0)

## Estructura del proyecto

com.example.calculator
MainActivity.kt: punto de entrada de la app
data/CalculatorModel.kt: logica pura de calculo (sin Android)
data/HistoryEntry.kt: modelo de datos del historial
data/HistoryRepository.kt: fuente unica de verdad del historial
navigation/AppNavigation.kt: navegacion entre pantallas
ui/calculator/CalculatorScreen.kt: pantalla de la calculadora
ui/calculator/CalculatorViewModel.kt: estado observable e intenciones
ui/components/CalculatorButton.kt: boton reutilizable
ui/history/HistoryScreen.kt: pantalla del historial
ui/theme/Theme.kt: tema de la app

## Arquitectura MVVM

Model: contiene la logica pura de calculo. No depende de Android ni de Compose.
ViewModel: expone el estado como StateFlow observable y recibe las intenciones de la UI. No usa recursos de texto ni colores.
View: Composables que recolectan el estado y traducen los errores semanticos a strings con stringResource().
Repository: almacena el historial de operaciones.

## Funcionalidades

Operaciones basicas: suma, resta, multiplicacion, division
Porcentaje y cambio de signo
Encadenamiento de operaciones (5 + 3 x 2 = 16)
Repeticion de "=" como en iPhone (8, 11, 14...)
Division entre cero manejada con errores semanticos, sin cerrar la app
Historial con formato "expresion = resultado"
Estado vacio en el historial cuando no hay operaciones
Diseno responsivo que se adapta al tamano de pantalla

## Recursos

Todos los textos visibles estan en res/values/strings.xml
Todos los colores estan en res/values/colors.xml
No se usan layouts XML ni librerias externas de UI

## Pruebas

Se incluyen pruebas unitarias de la logica pura en app/src/test/java/com/example/calculator/CalculatorModelTest.kt
Para ejecutarlas: clic derecho sobre el archivo y Run.

