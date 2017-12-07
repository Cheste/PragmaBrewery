var stompClient = null;


var properties = {
    "ipa": {
        high: 6.0,
        low: 5.0
    },
    "pilsner": {
        high: 6.0,
        low: 4.0
    },
    "lager": {
        high: 7.0,
        low: 4.0
    },

    "stout": {
        high: 8.0,
        low: 6.0
    },
    "wheat_beer": {
        high: 5.0,
        low: 3.0
    },
    "pale_ale": {
        high: 6.0,
        low: 4.0
    },
    "dev_beer_controller": {
        high: 6.0,
        low: 3.0
    }
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature', function (message) {
            showTemperature(JSON.parse(message.body).temperature, JSON.parse(message.body).elementId);
        });
        stompClient.subscribe('/topic/alert', function (message) {
            showAlert(JSON.parse(message.body).temperature, JSON.parse(message.body).elementId);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function showAlert(temperature, elementId) {
    $("td#" + elementId).append("<div class='alert-box error error"+elementId+"'><span>alarm: </span>"+elementId+" has critical temperature.</div>");
}


function showTemperature(temperature, elementId) {
    addThermometerIfNotExist(elementId);
    var splitTemperature = temperature.split(".");
    var highTemp;
    for (var key in properties) {
        if(key == elementId) {
            highTemp = properties[key].high
        }
    }
    if(temperature > highTemp){
        $("div#" + elementId).css("color","#df3341");
    } else {
        $("div#" + elementId).css("color","#555");
        if ($(".error"+elementId).length) {
            $(".error"+elementId).remove();
        }
    }

    $("div#" + elementId).html(splitTemperature[0] + "<span>." + splitTemperature[1] + "</span><strong>&deg;</strong>");
}

function addThermometerIfNotExist(elementId) {
    if (!$("#" + elementId).length) {
        if ($("#appendPoint").children().length == 3) {
            $("#appendPoint").removeAttr("id");
            $(".container").append("<tr id='appendPoint'></tr>");
        }
        $("#appendPoint").append("<td id='"+elementId+"'><div class='de'> <div class='den'> <div class='dene'> <div class='denem'><div class='deneme'><div id='" + elementId + "'></div> </div> </div> </div> </div>  </div> <label id='container_label'>" + elementId + "</label></td>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
});