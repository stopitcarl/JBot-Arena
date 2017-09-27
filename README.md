# JBot-Arena
2D Java-written bots fight in rounds. No support for mahine learning just yet.

Each player must write his algorithm in  Player1Logic and Player2Logic classes, respectively, inside the "decide" function.
Player1Logic's bot can be set to manual mode anytime during the match.

The manual controls are:
Arrow keys - movement
Space - shoot

Each round calls the decide method of each bot and then paints it on the screen. 
Mainbot class makes the rules for the bots not allowing them to leave the screen.


Virtually unlimited arena - no fixed width or height, the screen just adapts to paint the bots inside it and zooms in and out accordingly -is yet to be implemented.
