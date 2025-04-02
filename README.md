# 🍕 Sistema de Gestión de Pedidos de Pizza en Línea

**Ejercicio 2** – Proyecto Java con interfaz gráfica Swing que simula un sistema completo para realizar pedidos de pizza. Incluye autenticación, gestión de pedidos, procesamiento de pagos e inyección de dependencias centralizada mediante el patrón Singleton.

---

## 📂 Estructura del Proyecto

```
src/
├── main/java/com/pizzasystem
│   ├── App.java                  # Clase principal
│   ├── di/                      # Inyector de dependencias centralizado
│   ├── interfaces/              # Interfaces (contratos de servicios)
│   ├── models/                  # Modelos de datos (Pizza, Order, User)
│   ├── services/                # Implementaciones de lógica de negocio
│   └── ui/                      # Interfaz gráfica con Swing
└── test/java/com/pizzasystem    # Tests unitarios con JUnit
```

---

## ✅ Funcionalidades

- Registro e inicio de sesión de usuarios
- Selección de pizzas y gestión de carrito
- Creación y cancelación de pedidos
- Procesamiento y reembolso de pagos
- Interfaz gráfica amigable
- Inyección de dependencias con patrón Singleton

---

## 🛠️ Principios SOLID aplicados

### 1. **S - Single Responsibility Principle (Responsabilidad Única)**  
Cada clase tiene una única razón para cambiar:
- `Authenticator.java`: gestiona autenticación.
- `OrderManager.java`: lógica de pedidos.
- `PaymentProcessor.java`: gestiona pagos.
- `DatabaseManager.java`: persistencia y consultas.

### 2. **O - Open/Closed Principle (Abierta para extensión, cerrada para modificación)**  
Las interfaces (`IAuthenticator`, `IOrderManager`, etc.) permiten extender funcionalidad sin modificar las clases que las usan.

### 3. **L - Liskov Substitution Principle (Sustitución de Liskov)**  
Cualquier clase que implemente una interfaz puede sustituirse sin alterar el funcionamiento:
- Por ejemplo, `Authenticator implements IAuthenticator` se puede sustituir por otro autenticador sin romper nada.

### 4. **I - Interface Segregation Principle (Segregación de Interfaces)**  
Cada interfaz define solo lo que necesita:
- `IPaymentProcessor` no incluye lógica de base de datos ni autenticación.
- `IDatabaseManager` se centra únicamente en operaciones CRUD.

### 5. **D - Dependency Inversion Principle (Inversión de Dependencias)**  
Las clases dependen de abstracciones (`interfaces`), no de implementaciones:
- Todas las dependencias se inyectan desde `DependencyInjector.java`, cumpliendo el principio de inversión.

---

## 🔌 Inyección de Dependencias

Se realiza mediante la clase `DependencyInjector` (patrón Singleton):

```java
this.authenticator = new Authenticator(databaseManager);
this.orderManager = new OrderManager(databaseManager);
this.paymentProcessor = new PaymentProcessor(databaseManager);
```

Estas instancias se inyectan en la UI (`LoginPanel`, `OrderPanel`, `PaymentPanel`) y servicios, evitando acoplamiento directo y facilitando testeo.

---

## 🧪 Testing

En `/test/java/com/pizzasystem/` se encuentran pruebas unitarias por servicio:
- `AuthenticatorTest`
- `OrderManagerTest`
- `DatabaseManagerTest`
- `PaymentProcessorTest`
- `DependencyInjectorTest`

---

## ▶️ Ejecución

1. Compila el proyecto (preferiblemente en IntelliJ o similar).
2. Ejecuta `App.java` (interfaz Swing).
3. Usa el usuario de prueba:  
   **usuario / password**

---

## 📌 Tecnologías

- Java 17
- Swing (GUI)
- JUnit (Testing)
- Inyección manual de dependencias
- Base de datos en memoria (simulada)

---

## Link al repositorio

https://github.com/AllerSec/sistema-gestion-pizza.git

