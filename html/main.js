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
    console.log("Handling message type: ", data.type);

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
            showGame(data.grid, data.words);
            break;
    case 'resetLobbyState':
            console.log("Lobby state reset.");
            break;
    case 'chatMessage':
        console.log("Displaying chat message from:", data.playerName);
            displayChatMessage(data.playerName, data.message);
            break;
    case 'updateReadiness':
        console.log('Readiness Updated:', message.players);
            break;
    case 'gameStateUpdate':
        displayGrid(message.grid);  // Assuming 'grid' is the key for the matrix data
    break;
    
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
    const errorDisplay = document.getElementById('errorDisplay'); // Ensure this element exists
    errorDisplay.textContent = message; // Display or handle error messages
}

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
    } else if (document.getElementById('lobbyPage').style.display === 'block') {
        console.log('Currently on lobby page, switching to login page');
        document.getElementById('lobbyPage').style.display = 'none';
        document.getElementById('loginPage').style.display = 'block';
        resetLobbyState();
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



function startGame() {
    console.log('Game started.');
    var data = {
        type: "startGame",
        eventData: {
        } 
    };
    
    connection.send(JSON.stringify(data)); // Send the start game request to the server
    console.log("Start game request sent to the server., Display--");
}

function displayGrid(grid) {
    const gridElement = document.getElementById("grid");
    gridElement.innerHTML = '';  // Clear any existing grid

    grid.forEach(function(row) {
        const rowElement = document.createElement('tr');
        row.forEach(function(cellChar) {
            const cellElement = document.createElement('td');
            cellElement.textContent = cellChar;
            rowElement.appendChild(cellElement);
        });
        gridElement.appendChild(rowElement);
    });
}




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
    /*var button1 = document.getElementById("2playerButton");
    var button2 = document.getElementById("3playerButton");
    var button3 = document.getElementById("4playerButton");

    if(subLobbySize === 2){
        button1.disabled = true;
        button2.disabled = false;
        button3.disabled = false;
    }
    else if(subLobbySize === 3){
        button1.disabled = false;
        button2.disabled = true;
        button3.disabled = false;
    }
    else if(subLobbySize === 4){
        button1.disabled = false;
        button2.disabled = false;
        button3.disabled = true;
    }*/

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

function showGame(grid, words){
    document.getElementById("loginPage").style.display = "none";
    document.getElementById("lobbyPage").style.display = "none";
    document.getElementById("gamePage").style.display = "block";
    document.getElementById('chatArea').style.display = 'block';
    
    
    
    

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

  function updateStatus(isReady) {
    
    const message = { type: 'updateStatus', username: username, isReady: isReady };
    connection.send(JSON.stringify(message));
}


function updateLocalPlayerReadyStatus() {
    const playerListDiv = document.getElementById('playerList');
    // Assuming each player's entry is wrapped in a div, and username is directly within this div
    Array.from(playerListDiv.children).forEach(child => {
        if (child.textContent.includes(username)) {
            // Correctly update the innerHTML to reflect the new status
            child.innerHTML = `${username} - <span style="color: ${isReady ? 'green' : 'red'}">${isReady ? 'Ready' : 'Not Ready'}</span>`;
        }
    });
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
        playersListElement.appendChild(playerListItem);
        console.log(player);
    });

    function Mainlobby(){

    }
    
}
function displayWordList(words) {
    const wordListElement = document.getElementById("wordList");
    wordListElement.innerHTML = '';  // Clear previous words
    words.forEach(word => {
        const wordItem = document.createElement("li");
        wordItem.textContent = word;
        wordListElement.appendChild(wordItem);
    });
}
function displayGrid(grid) {
    const gridElement = document.getElementById("grid");
    gridElement.innerHTML = '';  // Clear any previous content
    grid.forEach(row => {
        const rowElement = document.createElement('tr');
        row.split('').forEach(char => {
            const cellElement = document.createElement('td');
            cellElement.textContent = char;
            rowElement.appendChild(cellElement);
        });
        gridElement.appendChild(rowElement);
    });
}
function showGame(grid, words) {
    // Logic to display the game grid and word list
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'block';
    displayGrid(grid);
    displayWordList(words);
}
function toggleReady() {
    
        var data = {
            type: "toggleReady",
            username: currentPlayerName
        };
        connection.send(JSON.stringify(data));
    }

    function updateReadinessDisplay(players) {
        console.log("Updating readiness display.");
        players.forEach(function(player) {
            var playerElement = document.getElementById(player.username + '_ready');
            if (playerElement) {
                playerElement.textContent = player.isReady ? "Ready" : "Not Ready";
            }
        });
    }
    
