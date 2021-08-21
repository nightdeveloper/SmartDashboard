window.stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    window.stompClient = Stomp.over(socket);
    window.stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        window.stompClient.subscribe('/topic/register', function (msg) {
            showMessage(JSON.parse(msg.body).message);
        });
    });
}

function disconnect() {
    if (window.stompClient !== null) {
        window.stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    window.stompClient.send('/app/register', {}, JSON.stringify({'id': $("#testText").val()}));
}

function sendFile() {
    let file = $("#testFile")[0].files[0];

    let reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = function () {
        window.stompClient.send('/app/image', {},
            JSON.stringify(
                {
                    'position' : 'center',
                    'file': reader.result
                }));
    };
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
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
    $("#send").click(function () {
        sendMessage();
    });
    $("#sendFile").click(function () {
        sendFile();
    });
});