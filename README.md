<h1 align="center">Arkanoid </h1>

<p align="center">
  <strong>A modern, high-performance take on the classic Breakout genre.</strong><br>
  <i>Developed as a capstone project for the Object-Oriented Programming (OOP) course.</i>
</p>

---

<p align="center">
  <img width="49%" alt="Gameplay Screenshot 1" src="https://github.com/user-attachments/assets/01dcc174-a1b8-4c6e-89f9-13e6772cfc35" />
  <img width="49%" alt="Gameplay Screenshot 2" src="https://github.com/user-attachments/assets/b08ac037-5073-4b97-9ff8-d6706c0926d6" />
</p>

## Table of Contents
- [About the Project](#about-the-project)
- [Key Features](#key-features)
- [Technical Showcase & OOP](#technical-showcase--oop)
- [Controls](#controls)
- [Installation & Setup](#installation--setup)
- [Project Architecture](#project-architecture)
- [Development Team](#development-team)

---

## About the Project

**Arkanoid Pro 2025** is not just a simple arcade game; it is a demonstration of robust software design. The project challenges players to destroy intricate walls of bricks using a bouncing ball and a movable paddle. We have expanded upon the classic formula by introducing dynamic physics, a rich power-up ecosystem, and a fully functioning local Co-op mode.

---

## Key Features

### Gameplay Mechanics
* **High-Precision Physics:** Custom collision detection engine ensures smooth and accurate interactions between the ball, paddle, and environment.
* **Level Progression:** 5 meticulously designed levels featuring increasingly complex brick layouts and scaling difficulty.
* **Local Co-op Mode:** Team up with a friend in real-time 2-player mode to clear stages together on the same screen.

### Dynamic Power-Ups System
A robust drop system that significantly alters gameplay:
* **Laser Gun:** Equips the paddle with dual lasers to shoot bricks directly.
* **Enlarge:** Dynamically scales the paddle's hitbox for easier saves.
* **Health Recovery:** Restores lost lives to extend gameplay.
* **Speed Buffs/Debuffs:** Alters ball trajectory and speed on the fly.

### Data Persistence & Leaderboard
* **Save & Load Functionality:** Seamlessly save your game state and resume anytime.
* **High Score Tracking:** Integrated leaderboard system to foster replayability and competition.

---

## Technical Showcase & OOP

This project was built from the ground up to strictly adhere to **Object-Oriented Programming** principles, ensuring a clean, scalable, and maintainable codebase:

* **Inheritance & Abstraction:** Utilized base abstract classes like `GameObject` and `MovableObject` to define common traits for the Ball, Paddle, and Bricks, reducing code duplication.
* **Polymorphism:** Different power-ups and brick types (Normal, Hard, Indestructible) implement custom behaviors through overridden methods.
* **Encapsulation:** Game state, physics variables, and scoring logic are strictly protected with private fields and controlled accessors (Getters/Setters).
* **Concurrency & Multithreading:** Implemented robust game loops and background rendering threads to maintain a stable FPS without locking the UI thread.

**Tech Stack:**
* **Language:** Java (JDK 8+)
* **Graphics Library:** Java Swing & AWT
* **IDE & Tools:** IntelliJ IDEA, Git & GitHub

---

## Controls

| Action | Player 1 | Player 2 (Co-op) |
| :--- | :--- | :--- |
| **Move Left/Right** | `A` / `D` or `Arrow Keys` | `J` / `L` |
| **Launch Ball / Shoot Laser** | `Space` | `Enter` |
| **Pause Game** | `P` | `P` |

---

## Installation & Setup

To run this project locally on your machine, follow these steps:

1. **Clone the repository:**
   ```bash
   https://github.com/ntcong2346/Arkanoid_Pro_2025.git
