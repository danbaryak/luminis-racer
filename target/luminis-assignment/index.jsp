<html>

<head>
    <link href="static/lib/bootstrap-3.1.1-dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="static/css/animate.css">
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <script type="text/javascript" src="static/lib/paperjs-v0.9.18/dist/paper-full.js"></script>
    <script type="text/javascript" src="static/lib/jquery-1.11.1.js"></script>
    <script src="static/lib/bootstrap-3.1.1-dist/js/bootstrap.min.js"></script>
    <script type="text/paperscript" src="static/js/main.js" canvas="gameCanvas"></script>
<body>
<div class="title animated bounceInDown">
    <h2>SKY RACER</h2>
</div>


<div id="join" class="section container center join-game">
    <h3>Join the next game!</h3>
    <div>
        <input id="txtPlayerName" type="text" placeholder="Enter your name">
        <button class="btn btn-primary btn-lg" id="btnJoin">Join Game</button>
    </div>
</div>

<div id="wait" class="section container center wait-for-game">
    <h3>Waiting for game to start</h3>

    <div id="waitMessage">

    </div>
</div>

<div id="play" class="section">
    <div class="message animated fadeIn">Game is in progress (<span id="msgTime"></span>) <span id="carColor"></span></div>
    <div class="col-md-2">
        LEFT
    </div>
    <div class="col-md-8">

        <canvas id="gameCanvas" class="animated flipInX" width="800" height="600"  keepalive="true"></canvas>
    </div>
    <div class="col-md-2">
        Right
    </div>
</div>

<div id="results" class="section container">
    <h3 class="game-over">GAME OVER</h3>
    <h2 id="resultMessage"></h2>
    <div class="text-center">
        <table id="resultsTable" class="results-table"></table>
    </div>

</div>




</body>
</html>
