# SmartHealth Monitor

![Android CI](https://img.shields.io/badge/Android-API26+-green)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-blue)

Aplicación Android de monitoreo de salud personal en tiempo real.

Desarrollada como proyecto integrador — UTNG 9° Cuatrimestre 2025.

---

## Stack tecnológico

| Tecnología                    | Uso                                            |
| ----------------------------- | ---------------------------------------------- |
| Kotlin + Jetpack Compose      | UI declarativa con Material Design 3           |
| Wearable Data Layer API       | Comunicación reloj ↔ teléfono (BLE)            |
| Health Services API           | Sensor FC real en background (Wear OS)         |
| Room Database                 | Historial persistente de lecturas FC           |
| Jetpack Navigation            | Navegación entre pantallas mediante NavHost    |
| StateFlow                     | Actualización reactiva de datos en tiempo real |
| GitHub + Conventional Commits | Control de versiones profesional               |

---

## Pantallas

| Pantalla        | Descripción                                        |
| --------------- | -------------------------------------------------- |
| LoginScreen     | Autenticación con validación y manejo de estado    |
| DashboardScreen | Visualización de FC y pasos en tiempo real         |
| HistorialScreen | Historial de lecturas de frecuencia cardíaca       |
| AlertaScreen    | AlertDialog Material 3 con confirmación y Snackbar |

---

## Funcionalidades implementadas

* Inicio de sesión con validaciones.
* Dashboard de monitoreo de salud.
* Historial de lecturas cardíacas.
* Alertas de emergencia con confirmación.
* Comunicación Wear OS ↔ Smartphone mediante Data Layer API.
* Actualización reactiva utilizando StateFlow.
* Arquitectura basada en Repository + ViewModel.
* Navegación con Jetpack Navigation Compose.

---

## Capturas de pantalla

### LoginScreen

<img width="367" height="816" alt="LoginScreen" src="https://github.com/user-attachments/assets/08d0ba20-424f-438e-b2a0-b77803ac43a4" />

### DashboardScreen

<img width="379" height="843" alt="DashboardScreen" src="https://github.com/user-attachments/assets/d2ddd3fa-d49d-4596-b03c-026db8943bfc" />

### HistorialScreen

*(Agregar captura cuando esté disponible)*

### AlertaScreen

*(Agregar captura cuando esté disponible)*

---

## Autor

**Paola Jaqueline López Mata**
Universidad Tecnológica del Norte de Guanajuato (UTNG)
Ingeniería en Desarrollo y Gestión de Software

---

## Estado del proyecto

* [x] LoginScreen — S4
* [x] DashboardScreen — S5
* [x] HistorialScreen — S6
* [x] AlertaScreen — S6
* [x] Integración Wearable Data Layer API — S6
* [ ] Persistencia completa con Room — S7
* [ ] Android TV (Leanback + Media3) — S10–S12
