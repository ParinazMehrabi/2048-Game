# 🧩 2048: High-Performance Logical Engine

<div align="center">

<img src="https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/JavaFX-17-blue?style=for-the-badge&logo=openjdk&logoColor=white" alt="JavaFX">
<img src="https://img.shields.io/badge/Build-Maven-green?style=for-the-badge&logo=apache-maven" alt="Maven">
<img src="https://img.shields.io/badge/Architecture-MVC-red?style=for-the-badge" alt="Architecture">

<p align="center">
    <strong>An Optimized, Grid-Based Logical Strategy Engine</strong><br>
    A professional Maven-based implementation of 2048, featuring efficient matrix manipulation, Command-pattern undo logic, and decoupled JavaFX architecture.
</p>

</div>

---

## Core Engine Logic
The engine is built on a high-performance **4x4 Matrix** representation, focusing on state immutability and efficient grid transformations within a modular Maven environment.

### Game Processing Logic
- **Slide & Merge Mechanics:** Implemented using vectorized row/column transformations. Each move ensures logical validation, merge-collision handling, and zero-tile replenishment protocols.
- **Move History & Undo:** Utilizes the **Command Design Pattern** to track state snapshots, enabling seamless undo functionality.
- **Score Optimization:** Real-time scoring engine calculating chain-reaction merges via recursive summation protocols.

---

## Key Architectural Pillars

### 1. MVC Decoupling
- **Model:** Manages the mathematical state of the grid, move validation, and game-over heuristics.
- **View:** Reactive JavaFX rendering pipeline that maps the integer grid to aesthetic UI tiles with smooth CSS transitions.
- **Controller:** Orchestrates user input (Keyboard/Touch events) and translates them into atomic game state transitions.

### 2. Maven Modular Architecture
- **Dependency Management:** Managed via `pom.xml` for reproducible builds and cross-platform compatibility.
- **Modular Design:** Enforced through `module-info.java` ensuring strong encapsulation of internal API layers.

---

## Technical Project Topology
```text
2048-Game/
├── src/main/java/org/example/game/    # Source code (Model-View-Controller)
├── src/main/resources/org/example/game/ # FXML layouts, CSS skins, and assets
├── pom.xml                            # Maven project configuration
└── module-info.java                   # Java Platform Module System configuration
