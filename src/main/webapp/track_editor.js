var grass = new Path();
var track = new Path();
var innerGrass = new Path();
var pavement = new Path();

var activePath = null;



grass.strokeColor = 'lightGreen';
grass.fillColor = 'lightGreen';
grass.closed = true;
track.strokeColor = 'lightGray';
track.fillColor = 'lightGray';
track.closed = true;
innerGrass.fillColor = 'lightGreen';
innerGrass.strokeColor = 'lightGreen';
pavement.fillColor = 'black';
pavement.strokeColor = 'black';
pavement.closed = true;

activePath = grass;
var activePane = $("#txtOuterGrass");
$("#btnGrass").click(function() {
    activePath = grass;
    activePane = $("#txtOuterGrass");
});
$("#btnTrack").click(function() {
    activePath = track;
    activePane = $("#txtTrack");
});
$("#btnInGrass").click(function() {
    activePath = innerGrass;
    activePane= $("#txtInnerGrass");
});
$("#btnPavement").click(function() {
    activePath = pavement;
    activePane = $("#txtPavement");
});

$("#btnClear").click(function() {
    activePath.clear();
    activePane.html("");
})
// This function is called whenever the user
// clicks the mouse in the view:
function onMouseDown(event) {

    if (activePath != null) {
        // Add a segment to the path at the position of the mouse:
        activePath.add(event.point);
        if (activePane != null) {
            activePane.append("{ \"x\": " + event.point.x + " , \"y\": " + event.point.y + " },");
        }

    }

}

