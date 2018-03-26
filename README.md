# JBot-Arena
2D Java-written bots fight in rounds. No support for machine learning just yet.

## How it works
Each player must write his algorithm in  Player1Logic and Player2Logic classes, inside the "decide" function.
Each round calls the decide method of each bot and then renders it on screen. 
Mainbot class makes the rules for the bots and defines what they can do (e.g. move, shoot, not allowing them to leave the screen, etc...).

Player1Logic's bot can be set to manual mode anytime during the match for testing and debugging.
The manual controls are:
 - Movement: arrow keys.
 - Shoot: space.


## To be implemented:
  * Have bots be controlled with Python, Javascript or other languages.
  * Virtually unlimited arena - no fixed width or height, the screen just adapts to paint the bots inside it and zooms in and out accordingly.
  * Weapon and life upgrades that a bot can purchase while in-match.
  * Support for data serialization (using external programs to control bots - machine learning).
