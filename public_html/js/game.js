/**
 * Created by neikila on 03.04.15.
 */
var ws;
var started = false;
var finished = false;

var enemyName = "";

var myPosition;

init = function () {
    ws = new WebSocket("ws://localhost:8080/gameplay");
    console.log("Create");
    ws.onopen = function (event) {
        console.log("open");
    };

    ws.onmessage = function (event) {
        var data = JSON.parse(event.data);
        if(data.status == "start"){
            myPosition = data.position;
            console.log("MyPosition = " + myPosition);
            console.log(data);
            document.getElementById("wait").style.display = "none";
            document.getElementById("gameplay").style.display = "block";
            document.getElementById("enemyName").innerHTML = data.enemyName;
            document.getElementById("sequenceToWrite").innerHTML = data.sequence;
        }

        if(data.status == "finish"){
            document.getElementById("gameOver").style.display = "block";
            document.getElementById("gameplay").style.display = "none";

            if(data.result == myPosition)
                document.getElementById("win").innerHTML = "winner!";
            else {
                if (data.result != 0)
                    document.getElementById("win").innerHTML = "loser!";
                else
                    document.getElementById("win").innerHTML = "loser... just as your opponent!";
            }
        }

        if(data.status == "increment") {
            if (data.name == document.getElementById("enemyName").innerHTML) {
                document.getElementById("enemyScore").innerHTML = data.score;
            } else {
                document.getElementById("myScore").innerHTML = data.score;
            }
        }

        if(data.status == "result"){
            document.getElementById("result").innerHTML = data.result;
        }
    };

    ws.onclose = function (event) {

    }

};

$(document).ready(function () {
    $('#but').click(function () {
        var temp = $('#sequenceInput').val();
        $('#sequenceInput').val("");
        var message = "{\"sequence\":\"" + temp + "\"}";
        ws.send(message);
        return false;
    });
});