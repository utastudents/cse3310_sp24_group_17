var serverUrl = "ws://" + window.location.hostname + ":" + (parseInt(location.port) + 100);
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
    console.log("event :" + event);
    var message = event.data;
    console.log("message: " + message);


    //parse message for object
    var data = JSON.parse(message);

    

   switch(data.type){
    case 'loginSuccess':
        console.log("login success");
        showLobby();
        break;
    case 'subLobbySuccess':
        console.log("sublobby success");
        updatePlayerList(data);
        break;
    case 'StartGame':
            showGame(data.grid,data.words );
            break;
    case 'resetLobbyState':
            console.log("Lobby state reset.");
            break;
    case 'chatMessage':
            displayChatMessage(data.playerName, data.message);
            break;
    case 'errorMessage':
            displayErrorMessage(data.message);
            break;
    default:
            console.log("Unknown message type:", data.type);
   }
};





function displayChatMessage(playerName, message) {
    const chatDisplay = document.getElementById('chatDisplay');
    const messageElement = document.createElement('p');
    messageElement.textContent = playerName + ": " + message;
    chatDisplay.appendChild(messageElement);
}

function displayErrorMessage(message) {
    const errorDisplay = document.getElementById('errorDisplay'); // Ensure this element exists
    errorDisplay.textContent = message; // Display or handle error messages
}

function sendMessage() {
    var message = document.getElementById('chatInput').value;
    var data = {
        type: "chatMessage",
        playerName: "Current Player Name", // This should be dynamically set
        message: message
    };
    connection.send(JSON.stringify(data));
    document.getElementById('chatInput').value = ''; // Clear input after sending
}



function displayWordList(words) {
    var wordListElement = document.getElementById("wordList");
    wordListElement.innerHTML = ''; // Clear previous entries if any
    words.forEach(function(word) {
        var wordItem = document.createElement("li");
        wordItem.textContent = word;
        wordListElement.appendChild(wordItem);
        
    });
}

function back() {
    console.log('Back button clicked'); // Debugging statement

    if (document.getElementById('gamePage').style.display === 'block') {
        console.log('Currently on game page, switching to lobby page');
        document.getElementById('gamePage').style.display = 'none';
        document.getElementById('lobbyPage').style.display = 'block';
        resetGameState();
    } else if (document.getElementById('lobbyPage').style.display === 'block') {
        console.log('Currently on lobby page, switching to login page');
        document.getElementById('lobbyPage').style.display = 'none';
        document.getElementById('loginPage').style.display = 'block';
        resetLobbyState();
    } else {
        console.log('No page change needed');
    }
}

function resetLobbyState() {
    const username = document.getElementById('username').value;
    console.log('Resetting lobby state for', username); // Debugging
    document.getElementById('username').value = "";

    const playerListDiv = document.getElementById('playerList');
    playerListDiv.innerHTML = '';

    if (connection && connection.readyState === WebSocket.OPEN) {
        const data = {
            type: "resetLobbyState",
            username: username
        };
        connection.send(JSON.stringify(data));
        console.log('Reset lobby state message sent to server');
    } else {
        console.log('WebSocket is not open');
    }
}

function resetGameState() {
    console.log('Resetting game state'); // Debugging
    const grid = document.getElementById("grid");
    while (grid.firstChild) {
        grid.removeChild(grid.firstChild);
    }
}

function startGame() {
    console.log('Game started.');
    var data = {
        type: "startGame",
        eventData: {
        } // Include any necessary event data, if needed
    };
    //display game in UI
    // showGame();
    connection.send(JSON.stringify(data)); // Send the start game request to the server
    console.log("Start game request sent to the server., Display--");
    //  displayGridAndWords(data);
}

function displayGrid() {
    var gridElement = document.getElementById("grid");
    gridElement.innerHTML = ''; // Clear previous entries if any
    grid.forEach(function(row) {
        var rowElement = document.createElement("tr");
        row.split('').forEach(function(char) {
            var cell = document.createElement("td");
            cell.textContent = char;
            rowElement.appendChild(cell);
        });
        gridElement.appendChild(rowElement);
    });
}




function login(){
    console.log("function call");
    var username = document.getElementById("username").value;
    console.log("username: " + username);

    var data = {
        type: "login",
        eventData: {
            username : username
        }
    };

    connection.send(JSON.stringify(data));
};

function createSubLobby(subLobbySize){
    console.log("function 2 call");

    var data = {
        type: "createSubLobby",
        eventData: {
            subLobbySize : subLobbySize
        }
    };

    console.log("sublobby size :" + subLobbySize);
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

function showGame(grid, words){
    document.getElementById("loginPage").style.display = "none";
    document.getElementById("lobbyPage").style.display = "none";
    document.getElementById("gamePage").style.display = "block";
    document.getElementById('chatArea').style.display = 'block';
    
    initializeGrid(grid);
    displayWordList(words);
    

}


function initializeGrid() {
    const grid = document.getElementById("grid");
    let gridsize= 50;
    for (let i = 0; i < gridsize; i++) {
        const row = grid.insertRow();
        for (let j = 0; j < gridsize; j++) {
            const cell = row.insertCell();
            cell.id = `cell-${i}-${j}`;
            cell.addEventListener("click", function() { handleCellClick(i, j); });
            // Placeholder for letters, replace with actual game data
            cell.textContent = String.fromCharCode(65 + Math.floor(Math.random() * 26));
        }
    }
  }


function updatePlayerList(json){
    console.log("function call 3 js");
    var lobby = json.lobby;
    var players = json.players;

    var playersListElement = document.getElementById("playerList");
    playersListElement.innerHTML = "";

    players.forEach(function(player) {
        var playerListItem = document.createElement("li");
        playerListItem.textContent = player;
        playersListElement.appendChild(playerListItem);
        console.log(player);
    });

    function Mainlobby(){

    }


    

    
  
   
    

    
}