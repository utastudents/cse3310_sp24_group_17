var serverUrl = "ws://" + window.location.hostname + ":" + (parseInt(location.port) + 100);
var connection = new WebSocket(serverUrl);
var currentPlayerName = null;

//format for json
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
    
    //Handle incoming json messages
   switch(data.type){
    case 'loginSuccess':
        if (data.username) {
            currentPlayerName = data.username;
            console.log("Username set to: ", currentPlayerName);
            showLobby();
        } else {
            console.log("Username not provided in loginSuccess message");
        }
        break;
    case 'loginError':
        console.log("login error");
        handleLoginError();
        break;
    case 'subLobbySuccess':
        console.log("sublobby success");
        updatePlayerList(data);
        break;
    case 'subLobbyError':
        console.log("sublobby error");
        handleSubLobbyError();
        break;
    case 'StartGame':
        showGame(data);
        break;
    //case 'resetLobbyState':
    //        console.log("Lobby state reset.");
    //        break;
    case 'chatMessage':
        console.log("Displaying chat message from:", data.playerName);
            displayChatMessage(data.playerName, data.message);
            break;
    case 'gameStateUpdate':
        console.log("nothing to print"); 
        displayGrid(data.grid);
        break;
    case 'toggleReady':
        updateReadinessDisplay(data);
        break;
    case 'matrixCreated':
        generateGrid(json.eventData);
    default:
            console.log("Unknown message type:", data.type);
   }
};

function displayChatMessage(playerName, message) {
    console.log("display message");
    const chatDisplay = document.getElementById('chatDisplay');
    if (chatDisplay) {
        const messageElement = document.createElement('p');
        messageElement.textContent = `${playerName}: ${message}`;
        chatDisplay.appendChild(messageElement);
        chatDisplay.scrollTop = chatDisplay.scrollHeight; // Scroll to the bottom
    } else {
        console.log('Chat display element not found');
    }
}



function displayErrorMessage(message) {
    const errorDisplay = document.getElementById('errorDisplay'); 
    errorDisplay.textContent = message; // Display or handle error messages
}

//Chat function
function sendMessage() {
    if (connection.readyState !== WebSocket.OPEN) {
        console.log('WebSocket is not open. Current state:', connection.readyState);
        return;
    }
    console.log("Attempting to send message", currentPlayerName);
    var message = document.getElementById('chatInput').value.trim();
    if (!message) {
        console.log('Message is empty');
        return;
    }
    if (!currentPlayerName) {
        console.log('Player name is not set');
        return;
    }

    var data = {
        type: "chatMessage",
        playerName: currentPlayerName,
        message: message
    };
    connection.send(JSON.stringify(data));
    document.getElementById('chatInput').value = '';
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
    } else {
        console.log('No page change needed');
    }
}

function resetGameState() {
    console.log('Resetting game state');

    // Clear the grid
    const gridContainer = document.getElementById("gridContainer"); // Adjust this ID to match your HTML
    if (gridContainer) {
        gridContainer.innerHTML = ''; // This will remove all children elements, effectively clearing the grid
    } else {
        console.log('Grid container element not found');
    }

    // Clear the word list
    const wordListContainer = document.getElementById("wordListContainer"); // Adjust this ID to match your HTML
    if (wordListContainer) {
        wordListContainer.innerHTML = ''; // This will remove all word list elements
    } else {
        console.log('Word list container element not found');
    }

    console.log('Game state has been reset');
}


/*function resetLobbyState() {
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
}*/



// function startGame() {
//     console.log('Game started.');
//     var data = {
//         type: "startGame",
//         eventData: {
//         } 
//     };
    
//     connection.send(JSON.stringify(data)); // Send the start game request to the server
//     console.log("Start game request sent to the server., Display--");
// }

function login(){
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

//send json of which sublobby player wants to join
function createSubLobby(subLobbySize){

    var data = {
        type: "createSubLobby",
        eventData: {
            subLobbySize : subLobbySize
        }
    };

    console.log("sublobby size :" + subLobbySize);
    connection.send(JSON.stringify(data));
};
// handle invalid username
function handleLoginError(){
    alert('Username Invalid');
}

//handle if max games reached
function handleSubLobbyError(){
    alert('Lobbies are all full - Try again later');
}


  function generateGrid(json) {
    const rows = Data.grid.length;
    const cols = Data.grid[0].length;
    const gridElement = document.getElementById('grid');
    gridElement.innerHTML = ''; // Clear previous grid if any

    for (let i = 0; i < rows; i++) {
        const row = document.createElement('div');
        for (let j = 0; j < cols; j++) {
            const cellButton = document.createElement('button');
            cellButton.id = `cell-${i}-${j}`; // Assign unique ID
            cellButton.innerHTML = Data.grid[i][j]; // Set letter from JSON data
            cellButton.onclick = function() { handleCellClick(i, j); };
            row.appendChild(cellButton);
        }
        gridElement.appendChild(row);
    }
}
function handleCellClick(row, col) {
    const cellId = `cell-${row}-${col}`;
    const cell = document.getElementById(cellId);
    if (!startPoint) {
        // Mark the start point
        startPoint = { row, col };
        cell.style.backgroundColor = "yellow"; 
    } else {
        
        highlightPath(startPoint, { row, col });
        startPoint = null; // Reset start point for next word
    }
  }

  function updateStatus(isReady) {
    
    const message = { type: 'updateStatus', username: username, isReady: isReady };
    connection.send(JSON.stringify(message));
}



// Updates the sublobby playerlist
function updatePlayerList(json){
    console.log("function call 3 js");
    var lobby = json.lobby;
    var players = json.players;

    var playersListElement = document.getElementById("playerList");
    playersListElement.innerHTML = "";

    players.forEach(function(player) {
        var playerListItem = document.createElement("li");
        playerListItem.textContent = player;
        playerListItem.style.color = 'red';
        playersListElement.appendChild(playerListItem);
        console.log(player);
    });

    function Mainlobby(){

    }
    
}
function showLogin(){
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'none';
}

function showLobby(){
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('lobbyPage').style.display = 'block';
    document.getElementById('gamePage').style.display = 'none';
    document.getElementById('chatArea').style.display = 'block';
}
function showGame() {
    // Logic to display the game grid and word list
    console.log('showGame called with grid:', grid, 'and words:', words);
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'block';
    document.getElementById("loginPage").style.display = "none";
    document.getElementById('chatArea').style.display = 'block'
}
function toggleReady() {
    
        var data = {
            type: "toggleReady",
            username: currentPlayerName
        };
        connection.send(JSON.stringify(data));
    }

function updateReadinessDisplay(json) {
    console.log("Function call to toggle readiness status");
        
    const playersList = document.getElementById('playerList');
    playersList.innerHTML = '';

    const players = json.eventData;

    players.forEach(player => {
        const playerElement = document.createElement('div');
        playerElement.textContent = player.name + ' - ';

        const statusSpan = document.createElement('span');
        statusSpan.textContent = player.ready ? 'Ready' : 'Not Ready';
        statusSpan.className = player.ready ? 'ready' : 'not-ready';
        statusSpan.className += ' ' + (player.ready ? 'green-text' : '');
        statusSpan.onclick = function() { toggleReady(player.name); };

        playerElement.appendChild(statusSpan);
        playersList.appendChild(playerElement);
    });

}
    
    
