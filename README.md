luminis-racer
=============

Implementation of a client-server 'real time' Racing game.



<h5>Playing the game</h5>

The user can control the car using the keyboard arrow keys as follows:

Key             | Functionality
--------------- | -------------------------------------------------------------------
Up Arrow        | Increases the car speed (from STOPPED to SLOW or from SLOW to FAST)
Down Arrow      | Decreases the car speed (from FAST to SLOW or from SLOW to STOPPED)
Right Arrow     | Change steering to the right (from STRAIGHT to RIGHT or from LEFT to STRAIGHT)
Left Arrow      | Change steering to the left (from STRAIGHT to LEFT or from RIGHT to STRAIGHT)

When the user presses a key, the client sends it to the server side. The server will only process a key
command once every configured time interval (initialized to 1 second).

The car is a little difficult to handle at first and it takes some practice, especially if there is a large communication delay between the client and server. I've improved it to some extent by using client side 'predication', guessing where the car will be after the server processes the key command (only if the server accepts the command, meaning that there are no previous keystroke that should be handled)


<h5>Technologies used:</h5>

<strong>Server</strong>

* Java 8 
* Tomcat 8 web application container
* Spring MVC 3 

<strong>Client</strong>

* HTML5 & CSS3
* JQuery
* Paper.js - An open source framework for easy HTML5 canvas manipulation (http://paperjs.org)
* Animate.CSS - An open source framework for easy CSS3 transitions (http://daneden.github.io/animate.css/)


