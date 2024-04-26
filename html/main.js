<<<<<<<<< Temporary merge branch 1
var gridsize = 30;
var isReady = false;
var username; // Global variable to store the user's username
var connection; // WebSocket connection

document.addEventListener('DOMContentLoaded', function() {
    initializeWebSocket(); // Initialize WebSocket connection on page load
    var submitButton = document.getElementById('submit');
    if (submitButton) {
        submitButton.addEventListener('click', submitLogin);
    } else {
        console.error('Submit button not found!');
    }
});

function initializeWebSocket() {
    var serverUrl = "ws://" + window.location.hostname + ":" + (parseInt(location.port) + 100);
    console.log("Connecting to WebSocket server at " + serverUrl);
    connection = new WebSocket(serverUrl);

    connection.onopen = function() {
        console.log("Connected to the server.");
        
    };

    connection.onmessage = function(event) {
        try {
            var data = JSON.parse(event.data);
            console.log("Received from server:", data);
        
            if (data.type) {
                handleServerMessages(data);
            } else {
                console.log("Message does not contain a type:", data);
            }
        } catch (error) {
            console.error("Error parsing JSON from the server:", error);
            console.error("Raw data:", event.data);
        }
    };
    
    

    connection.onerror = function(error) {
        console.error("WebSocket Error:", error);
    };

    connection.onclose = function() {
        console.log("Connection closed by the server.");
    };
}

function submitLogin() {
    var username = document.getElementById('username').value.trim();
    if (!username) {
        alert("Please enter a username.");
        return;
    }

    var message = JSON.stringify({
        type: "login",
        username: username
    });

    if (connection.readyState === WebSocket.OPEN) {
        connection.send(message);
        console.log("Login request sent:", message);
    } else {
        console.error('WebSocket connection is not open.');
        alert("Cannot connect to the server. Please try again later.");
    }

        // Update UI to show the lobby page
        document.getElementById('loginPage').style.display = 'none';
        document.getElementById('lobbyPage').style.display = 'block';
        document.getElementById('chatArea').style.display = 'block';
        updatePlayerList([{name: username, isReady: false}]); // Add player to the list
}


function handleServerMessages(data) {
    if (data.type) {
        switch (data.type) {
            case 'welcome':
                console.log("Welcome message received:", data.message);
                break;
            case 'playerListUpdate':
                updatePlayerList(data.players);
                break;
            case 'gameStarted':
                startGame();
                break;
            case 'chat':
                displayChatMessage(data.username, data.message);
                break;
            case 'broadcast':
                console.log("Broadcast message:", data.content);
                break;
            default:
                console.log("Unknown message type:", data.type);
        }
    } else {
        console.log("Received message without type:", data);
    }
}


// Function to update the player list in the lobby
function updatePlayerList(players) {
    const playerListDiv = document.getElementById('playerList');
    playerListDiv.innerHTML = '';  // Clear the existing list
    players.forEach(player => {
        const playerElem = document.createElement('div');
        playerElem.innerHTML = `${player.name} - <span style="color: ${player.isReady ? 'green' : 'red'}">${player.isReady ? 'Ready' : 'Not Ready'}</span>`;
        playerListDiv.appendChild(playerElem);
    });
    checkReadyPlayers(); // Check if the start condition is met after updating the list
}




  //update player status
  function updateStatus(status) {
    // Make sure the WebSocket is connected before sending a message
    if (connection.readyState === WebSocket.OPEN) {
        const message = { type: 'updateStatus', status: status };
        connection.send(JSON.stringify(message));
    } else {
        console.log("WebSocket is not yet open. Waiting to send the status update.");
        // Optionally, wait and try again or handle according to your needs
    }
}


  function changeLobby(lobbySize) {
    const message = { type: 'changeLobby', lobbySize: lobbySize };
    connection.send(JSON.stringify(message));
  }


  

  function ready() {
    isReady = !isReady;
    // If the WebSocket is not open, wait for a bit and then try to update the status
    if (connection.readyState !== WebSocket.OPEN) {
        console.log("Waiting for the WebSocket to open...");
        setTimeout(() => {
            updateStatus(isReady);
        }, 1000); // Wait for 1 second before trying again
    } else {
        updateStatus(isReady);
    }
    updateLocalPlayerReadyStatus();
}

function checkReadyPlayers() {
    const listId = `${requiredPlayers}PlayerList`;
    const players = Array.from(document.querySelectorAll(`#${listId} > div`));
    const readyCount = players.reduce((count, player) => {
        return count + (player.textContent.includes('Ready') ? 1 : 0);
    }, 0);

    const startButton = document.getElementById('startGameButton');
    startButton.disabled = readyCount < requiredPlayers;
}



function updateStatus(isReady) {
    // Construct the message to send the updated status to the server
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

function setPlayerCount(playerCount) {
    requiredPlayers = playerCount;

    // Define the lists for different modes
    const lists = {
        2: document.getElementById('twoPlayerList'),
        3: document.getElementById('threePlayerList'),
        4: document.getElementById('fourPlayerList')
    };

    // Hide all lists and then display the selected one
    Object.values(lists).forEach(list => list.style.display = 'none');
    lists[requiredPlayers].style.display = 'block';

    // Clear the selected list and move players from the main list to the selected one
    var players = Array.from(document.querySelectorAll('#playerList > div'));
    var selectedListDiv = lists[requiredPlayers];
    selectedListDiv.innerHTML = '';  // Clear the existing list
    
    // Move players to the selected list (assuming all players are moved for simplicity)
    players.forEach(player => {
        selectedListDiv.appendChild(player.cloneNode(true)); // Clone the player element
    });

    // Check the readiness of players based on the selected mode
    checkReadyPlayers();
}

// Now use this function for the buttons in HTML
// For 2 players: onclick="setPlayerCount(2)"
// For 3 players: onclick="setPlayerCount(3)"
// For 4 players: onclick="setPlayerCount(4)"




function updatePlayerMode(modeListId) {
    // Hide all player mode lists
    ['twoPlayerList', 'threePlayerList', 'fourPlayerList'].forEach(listId => {
        document.getElementById(listId).style.display = 'none';
    });

    // Get the selected mode's list and display it
    var selectedModeList = document.getElementById(modeListId);
    selectedModeList.innerHTML = `<h3>${modeListId.split('PlayerList')[0]} Mode</h3>`;
    selectedModeList.style.display = 'block';

    // Move players to the selected mode list
    var players = document.querySelectorAll('#playerList > div');
    players.forEach(player => {
        // Clone the node to avoid removing it from the original playerList
        selectedModeList.appendChild(player.cloneNode(true));
    });

    // Update the start button state based on the new mode
    checkReadyPlayers();
}



function back() {
    // Check if the user is currently on the game page
    if (document.getElementById('gamePage').style.display === 'block') {
        // Hide the game page and show the lobby
        document.getElementById('gamePage').style.display = 'none';
        document.getElementById('lobbyPage').style.display = 'block';
        // Reset the game state here if needed
        resetGameState();
    }
    // Check if the user is currently on the lobby page
    else if (document.getElementById('lobbyPage').style.display === 'block') {
        // Hide the lobby and show the login page
        document.getElementById('lobbyPage').style.display = 'none';
        document.getElementById('loginPage').style.display = 'block';
        // Reset any lobby specific states or data
        resetLobbyState();
    }
}

// Function to reset the game state
function resetGameState() {
    // Clear the game grid or any game-related data
    const grid = document.getElementById("grid");
    while (grid.firstChild) {
        grid.removeChild(grid.firstChild);
    }
    // Reset any other game-specific variables
}

// Function to reset lobby state, like clearing the player list or other lobby data
function resetLobbyState() {
    // Reset username or other sensitive data
    username = ""; // Reset the username global variable
    document.getElementById('username').value = ""; // Clear the input field
    // Clear the player list displayed in the lobby
    const playerListDiv = document.getElementById('playerList');
    playerListDiv.innerHTML = '';
}







=========
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
   }
};

function displayWordList(words) {
    var wordListElement = document.getElementById("wordList");
    wordListElement.innerHTML = ''; // Clear previous entries if any
    words.forEach(function(word) {
        var wordItem = document.createElement("li");
        wordItem.textContent = word;
        wordListElement.appendChild(wordItem);
        
    });
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
    
  
    
    
    
    
    
}
>>>>>>>>> Temporary merge branch 2
