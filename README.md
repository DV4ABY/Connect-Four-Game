# Connect Four Game

A Connect Four implementation with Kotlin 1.7 and JavaFX desktop that allows two players to play Connect Four against each other. The game follows the classic rules and displays a visually pleasing user interface.

## Features

- 8 x 7 grid (width x height) in the center of the application window
- Grid resembles the physical version of Connect Four, with pieces visible only through the holes in the grid
- Player controls on the left and right sides of the grid
- Active player can move and drop their piece, while the other player's piece is non-droppable
- Pieces animate towards their final location in the grid or back to their original location, depending on the drop validity
- Game ends and displays appropriate messages when a player wins or the game ends in a draw

## Setup

1. Clone this repository.
2. Open the project in IntelliJ IDEA or another Kotlin-supporting IDE.
3. Ensure you have at least Kotlin 1.7, Java SDK, and JavaFX installed and configured.
4. Use Gradle to build and run the application

## Game Rules

1. Press the "Click here to start game!" button to initiate the game.
2. Player #1 starts by moving their piece over the grid and dropping it into a column.
3. Players take turns dropping their pieces into the grid.
4. The game ends when one player has four consecutive pieces in a horizontal, vertical, or diagonal line, or when the grid is completely filled (resulting in a draw).
5. Appropriate end-game messages will be displayed when a player wins or the game ends in a draw.
