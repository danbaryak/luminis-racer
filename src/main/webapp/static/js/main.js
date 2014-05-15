// disable arrow key scrolling
document.addEventListener("keydown", function (e) {
    if([37,38,39,40].indexOf(e.keyCode) > -1){
        e.preventDefault();
        // Do whatever else you want with the keydown event (i.e. your navigation).
    }
}, false);

window.onbeforeunload = function() {
    return "This will leave the game. Are you sure?";
}

hideAll();


var shapeHeadings = {};

// frequency of server updates (in milliseconds)
var SERVER_POLL_INTERVAL = 200;
var trackPaths = null;
var players = null;
var playerNextPositions =  {};

var player = null;
var carColor = null;

var CAR_COLORS = ['black', '#C40606', '#0B4DBF', '#D4BC0B', '#E07A04', '#BB06C4', '#025247'];
var CAR_COLOR_NAMES = ['black', 'red', 'blue', 'yellow', 'orange', 'purple', 'cyan'];
var canvas;
var game = null;

var heading = 0;
var distance = 0;
var angle = 0;

$(function() {




    setInterval(function() {
        update();
    }, SERVER_POLL_INTERVAL);
    update();

//    // drawing loop
//    setInterval(function() {
//        draw();
//    }, 1000/FPS);

    $("#btnJoin").click(joinGame);
    $("#txtPlayerName").keypress(function(e) {
        if (e.which == 13) {
            joinGame();
        }
    });

});

function hideAll() {
    $("#join").hide();
    $("#play").hide();
    $("#results").hide();
    $("#wait").hide();
}


/**
 * Gets the updated game world state from server and updates the client
 * accordingly.
 */
function update() {
    getGameState(function(state) {

        game = state;
        var status = game.status;

        var timeInSec = (state.gameTime  / 1000) | 0;

//        console.log("Current game time is " + timeInSec);
        if (status == "WAITING") {
            $("#play").hide();
            $("#results").hide();
            if (player == null) {
                $("#wait").hide();
                $("#btnJoin").prop("disabled",false);
                $("#join").show();
                $('#join').addClass('animated bounceIn');
            } else {
                $("#join").hide();
                $("#wait").fadeIn();

                if (timeInSec < 0) {
                    var countdown = 0 - timeInSec + 1;
                    // update message
                    $("#waitMessage").html("Game starts in " + countdown + " seconds");
                    $("#waitMessage").fadeIn();
                } else {
                    $("#waitMessage").hide();
                    $("#waitMessage").html("");
                }

            }

        } else if (status == "IN_PROGRESS") {
            $("#join").hide();
            $("#wait").hide();
            $("#results").hide();
            $("#play").fadeIn();

            if (trackPaths == null) {
                createTrack(game.track);
            }
            if (players == null) {
                createPlayers(game.players);
            }
            for (var i in game.players) {
                var p = game.players[i];


                var playerShape = players[p.id];
//                console.log('player shape for id ' + p.id);
                if (playerShape != null) {
                    if (p.crashed == true) {
                        console.log('player has crashed');
                        playerShape.remove();
                        players[p.id] = null;
                    } else {
//                        console.log('still alive');
                        if (p.id == player.id) {
                            // this is me. let's predict where I might actually be now in the server
                            var pos = new Point(p.x, p.y);
                            // create a horizontal vector, then rotate it by the current heading and angle
                            var vector = new Point(distance, 0);

                            vector.angleInRadians = p.heading + angle;
                            vector.length = distance;

                            pos += vector;

                            playerNextPositions[p.id] = pos;
//                            playerNextHeading[p.id] =
                        }
                        // still alive
                        var shapePrevHeading = shapeHeadings[p.id];
                        if (shapePrevHeading == null) {
                            shapePrevHeading = 0;
                        }
                        var newHeadingRadians = p.heading;
                        if (p.id == player.id && distance != 0) {
                            newHeadingRadians += angle;
                        }
                        var headingDeg = 0;

                        headingDeg = newHeadingRadians * 180 / Math.PI;
                        var prevHeadDegs = shapePrevHeading * 180 / Math.PI;
                        playerShape.rotate(-prevHeadDegs + headingDeg);
                        shapeHeadings[p.id] = newHeadingRadians;

                        playerNextPositions[p.id] = new Point(p.x, p.y);
//                    playerPath.position = new Point(p.x, p.y);
                    }

                }

            }

//            console.log("Game track is " + JSON.stringify(state.track));
            var time = secondsToTime(timeInSec)
            $("#msgTime").html(time.m + " : " + time.s);

        } else if (status == "DONE" && player != null) {
            showResults();
        }

    })
}

function showResults() {
    $("#play").hide();
    $("#wait").hide();
    $("#join").hide();

    var myFinishTime;
    var myPlace;
    var allPlayers = [];
    for (var i in game.players) {
        var p = game.players[i];
        allPlayers.push(p);

    }
    allPlayers.sort(function(a, b) {
        if (a.finishTime == 0 && b.finishTime == 0) {
            return 0;
        }
        if (a.finishTime == 0 || (b.finishTime != 0 && a.finishTime > b.finishTime)) {
            return 1;
        }
        return -1;
    });
    for (var i in allPlayers) {
        var p = allPlayers[i];
        if (player != null && p.id == player.id) {
            myFinishTime = p.finishTime;
            myPlace = i;
        }
    }

//    console.log('players (sorted) are: ' + JSON.stringify(allPlayers));
//
//            for (var i in game.players) {
//                var p = game.players[i];
//                if (p.id = player.id) {
//                    // this is me
//                    myFinishTime = p.finishTime;
//                }
//            }
    if (myFinishTime != 0 && myPlace == 0) {
        $("#resultMessage").html("You won!");
    } else {
        $("#resultMessage").html("You lost :(");
    }

    // build score table
    $("#resultsTable").html('<tr class="table-headers"><td>  Place  </td><td>  Name  </td><td>  Finish Time  </td></tr>');
    for (var i in allPlayers) {
        var p = allPlayers[i];
        var position = p.crashed ? "-" : parseInt(i, 10) + 1;
        var time = secondsToTime(p.finishTime / 1000);
        var timeStr = time.m + ":" + time.s;
        var finishStr = p.finishTime != 0 ?  timeStr : p.crashed ? "<span style=\"color:red\">CRASHED</span>" : "<span style=\"color:red\">DIDN'T FINISH</span>";
        $("#resultsTable").append("<tr class=\"table-content\"><td>" + position + "</td><td>" + p.name + "</td><td>" + finishStr + "</td></tr>");
    }

    $("#results").fadeIn();

    initState();
}

/**
 * Initializes the game state, clearing all local variables.
 */
function initState() {
    player = null;
    players = null;
    trackPaths = null;
    // remove all paperjs graphics from the canvas
    project.activeLayer.removeChildren();
    playerNextPositions = {};
    shapeHeadings = {};
    game = null;
    heading = 0;
    angle = 0;
    distance = 0;
    carColor = null;
}

function createTrack(track) {
    trackPaths = {
        outerGrass: new Path(),
        track: new Path(),
        innerGrass: new Path(),
        pavement: new Path()
    };

    trackPaths.outerGrass = new CompoundPath(createTrackPath(track.outerGrass), createTrackPath(track.pavement));
    trackPaths.outerGrass.fillColor = '#45DE31';
    trackPaths.outerGrass.strokeWidth = 2;
    trackPaths.outerGrass.strokeColor = '#2C8A32';
    trackPaths.track = new CompoundPath(createTrackPath(track.track), createTrackPath(track.pavement));
    trackPaths.track.fillColor = '#919191';
    trackPaths.track.strokeColor = '#2C8A32';
    trackPaths.track.strokeWidth = 2;
    trackPaths.innerGrass = new CompoundPath(createTrackPath(track.innerGrass), createTrackPath(track.pavement));
    trackPaths.innerGrass.fillColor = '#45DE31';
    trackPaths.innerGrass.strokeColor = '#2C8A32';
    trackPaths.innerGrass.strokeWidth = 2;

    // create start / finish line
    var lineX = 410 ;
    var lineHeight = 200;
    var line = new Path();
    line.moveTo(lineX, trackPaths.track.bounds.y + 2);
    line.lineTo(lineX, trackPaths.innerGrass.bounds.y - 2);


    line.strokeWidth = 3;
    line.strokeColor = 'orange';
    line.dashArray = [8, 4];
}

function createPlayers(data) {
    players = {};
    for (var i in data) {
        var p = data[i];
        console.log('handling player ' + JSON.stringify(p));
        var wheel1 = new Path.RoundRectangle(new Rectangle(new Point(p.x + 2, p.y + 3 ), new Point(p.x + 12, p.y + 10)), new Size(4, 4));
        var wheel2 = new Path.RoundRectangle(new Rectangle(new Point(p.x + 2, p.y - 10 ), new Point(p.x + 12, p.y - 3)), new Size(4, 4));
        var wheel3 = new Path.RoundRectangle(new Rectangle(new Point(p.x - 12, p.y - 10 ), new Point(p.x - 2, p.y - 3)), new Size(4, 4));
        var wheel4 = new Path.RoundRectangle(new Rectangle(new Point(p.x - 12, p.y + 3 ), new Point(p.x - 2, p.y + 10)), new Size(4, 4));

        wheel1.fillColor = 'black';
        wheel2.fillColor = 'black';
        wheel3.fillColor = 'black';
        wheel4.fillColor = 'black';

        var body = new Path.Circle(new Point(p.x, p.y), 10);
        body.fillColor = CAR_COLORS[p.id];;
//        body.strokeColor = 'white';

        var window = new Path.Circle(new Point(p.x, p.y), 7);
        window.removeSegment(0);
        window.opacity = 0.5;
        window.fillColor = 'white';


        var player = new Group([wheel1, wheel2, wheel3, wheel4, body, window]);

        players[p.id] = player;
    }
}

function secondsToTime(secs)
{
    var hours = Math.floor(secs / (60 * 60));

    var divisor_for_minutes = secs % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);

    var obj = {
        "h": hours,
        "m": minutes,
        "s": seconds
    };
    return obj;
}


function onFrame(event) {
//    // the number of times the frame event was fired:
//    console.log(event.count);
//
//    // The total amount of time passed since
//    // the first frame event in seconds:
//    console.log(event.time);
//
//    // The time passed in seconds since the last frame event:
//    console.log(event.delta);
//    console.log("onFrame");
//    var str = "";
    for (var i in players) {
        var playerPath = players[i];
        if (playerPath == null) {
            continue;
        }

        var nextPos = playerNextPositions[i];
        if (nextPos != null) {
            var vector = nextPos - playerPath.position;
            playerPath.position += vector / 30;
//            console.log("new vector: " + vector / 30);
//            console.log("New path position = " + playerPath.position);
        }
    }
}

function createTrackPath(data) {

    var path = new Path();

    path.closed = true;
    for (var i in data) {
        var point = data[i];
        path.add(new Point(point.x, point.y));
    }
    return path;
}

function onKeyDown(event) {
    // When a key is pressed, set the content of the text item:
    console.log('The ' + event.key + ' key was pressed!');
    if (player == null) {
        return;
    }
    if (event.key == 'up') {
        sendCommand('FASTER', function() {
            if (distance == game.distanceUnit) {
                distance = game.distanceUnit * 2;
            } else if (distance == 0) {
                distance = game.distanceUnit;
            }
            console.log('angle: ' + angle + ', distance = ' + distance);
        });
    } else if (event.key == 'down') {
        sendCommand('SLOWER', function() {
            if (distance == game.distanceUnit * 2) {
                distance = game.distanceUnit;
            } else if (distance == game.distanceUnit) {
                distance = 0;
            }
            console.log('angle: ' + angle + ', distance = ' + distance);
        });
    } else if (event.key == 'right') {
        sendCommand('TURN_RIGHT', function() {
            if (angle == 0) {
                angle = game.angleUnit;
            } else if (angle == - game.angleUnit) {
                angle = 0;
            }
            console.log('angle: ' + angle + ', distance = ' + distance);
        });
    } else if (event.key == 'left') {
        sendCommand('TURN_LEFT', function() {
            if (angle == game.angleUnit) {
                angle = 0;
            } else if (angle == 0) {
                angle = - game.angleUnit;
            }
            console.log('angle: ' + angle + ', distance = ' + distance);
        });
    }


}

/**
 * Sends a command to the server.
 *
 * @param command The name of the command to send
 * @param onAccept Callback function to invoke if the command was accepted by the server
 */
function sendCommand(command, onAccept) {
    $.get("command?name=" + command + "&id=" + player.id, function(result) {
        if (result.accepted) {
            console.log("command " + command + " was accepted");
            onAccept();
        } else {
            console.log("command " + command + " was NOT accepted");
        }
    });
}

/**
 * Joins a new game.
 */
function joinGame() {
    var myName = $("#txtPlayerName").val();
    $.get("join?name=" + myName, function(p) {
        console.log("Joined game. I'm " + JSON.stringify(p));
        $("#btnJoin").prop("disabled",true);
        player = p;
        carColor = CAR_COLORS[p.id];
        $("#carColor").html("<span class=\"color-label\" style=\"background: " + carColor + "\">Your color is " + CAR_COLOR_NAMES[p.id] + "</span>");
        console.log("My car color is " + carColor);
    });
}

/**
 * Retrieves the game state from the server
 */
function getGameState(callback) {
    $.get("state", function(state) {
//        console.log("Game state is: " + JSON.stringify(state));
        callback(state);
    })
}

