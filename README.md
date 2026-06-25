# Memory Match Game

A classic card-matching memory game built in Java using a NetBeans project structure.

## About

Memory Match Game is a desktop application where players flip cards to find matching pairs. The goal is to match all pairs on the board while tracking your score across sessions.

## Features

- Flip two cards per turn to find matching pairs
- Persistent high-score tracking via `scores.dat`
- Java Swing GUI for an interactive desktop experience
- Built and managed with Apache Ant (`build.xml`)

## Requirements

- Java Development Kit (JDK) 8 or higher
- [NetBeans IDE](https://netbeans.apache.org/) (recommended) **or** Apache Ant for command-line builds

## Getting Started

### Running in NetBeans

1. Clone or download this repository:
   ```bash
   git clone https://github.com/juliej0hn/Memory-Match-Game.git
   ```
2. Open NetBeans and go to **File → Open Project**.
3. Navigate to the cloned folder and open it as a project.
4. Click the **Run** button (or press `F6`) to build and launch the game.

### Running from the Command Line

Make sure Apache Ant is installed, then:

```bash
cd Memory-Match-Game
ant run
```

Or compile and run manually:

```bash
javac -d build/classes src/memorymatchgame01/*.java
java -cp build/classes memorymatchgame01.MemoryMatchGame01
```

## How to Play

1. Launch the game — the board will display a grid of face-down cards.
2. Click any card to reveal it.
3. Click a second card to try to find its match.
   - **Match found:** Both cards stay face-up and your score increases.
   - **No match:** Both cards flip back face-down after a brief pause.
4. Continue until all pairs have been matched.
5. Your score is saved to `scores.dat` for future sessions.

## Project Structure

```
Memory-Match-Game/
├── src/
│   └── memorymatchgame01/      # Java source files
├── build/
│   └── classes/                # Compiled .class files
├── nbproject/                  # NetBeans project metadata
├── build.xml                   # Apache Ant build script
├── manifest.mf                 # JAR manifest
└── scores.dat                  # Persistent score storage
```

## License

This project is open source. Feel free to use, modify, and distribute it.
