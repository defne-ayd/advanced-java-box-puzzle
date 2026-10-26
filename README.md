# Advanced Box Puzzle Game

A complex 8x8 grid puzzle game utilizing advanced Java concepts such as Reflection, Generics, and Custom Exception handling. The game involves manipulating a matrix of various box types to align specific target characters.
This project was originally developed as an advanced coursework assignment for the Object-Oriented Programming (CENG211) class at Izmir Institute of Technology.

## Key Features
* **Java Reflection API:** Dynamically invokes Enum values and board manipulation methods at runtime (`Method.invoke()`) to process player directional inputs.
* **Generics:** Implements a versatile `SpecialTool<T>` abstract class, allowing type-safe execution of diverse tool behaviors (e.g., `MassRowStamp`, `PlusShapeStamp`, `BoxFixer`) across different context targets.
* **Custom Exception Handling:** Uses custom runtime exceptions (`EmptyBoxException`, `UnmovableFixedBoxException`, `BoxAlreadyFixedException`) to gracefully manage invalid moves and game rules without breaking the execution loop.
* **Deep Polymorphic Logic:** Differentiates grid behaviors through specialized box types (`RegularBox`, `FixedBox`, `UnchangingBox`) that selectively override rolling and stamping mechanics.

## Technologies
* Java (Reflection API, Generics, Custom Exceptions, Advanced OOP Architecture)
