var serverUrl = "ws://sp24.cse3310.org:" + (Number(location.port) + 100);
var connection = new WebSocket(serverUrl);


function UserEvent(type, eventData){
    this.type = type;
    this.eventData = eventData;
}

connection.onopen = function(event){
    console.log("Websocket open");
};

connection.onclose = function(event){
    console.log("Websocket closed");
};

connection.onmessage = function(event){
    //read in event
    var message = event.data;

    //parse message for object
    var data = JSON.parse(message);

    if(data == "loginSuccess"){
        showLobby();
    }
};

function login(){
    var username = document.getElementById("username").value;

    var data = {
        type: "login",
        eventData: {
            username : "username"
        }
    };

    connection.send(JSON.stringify(data));

};

function showLogin(){
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'none';
}

function showLobby(){
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('lobbyPage').style.display = 'block';
    document.getElementById('gamePage').style.display = 'none';
}

function showGame(){
    document.getElementById("loginPage").style.display = "none";
    document.getElementById("lobbyPage").style.display = "none";
    document.getElementById("gamePage").style.display = "block";
}