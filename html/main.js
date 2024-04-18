var gridsize = 30;
var isReady = false;  // Global variable to keep track of the player's ready status
// Variables for player information and WebSocket connection
var username;
var connection;

// Wait for the DOM to fully load before running the script
document.addEventListener("DOMContentLoaded", function() {
    // Initialize WebSocket connection
    initializeWebSocket();

    // Attach event listener to the login form
    var loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            username = document.getElementById('username').value.trim();

            // Send username to the server
            if (connection && connection.readyState === WebSocket.OPEN) {
                connection.send(JSON.stringify({ type: "login", username: username }));
            }

            // Update UI to show the lobby page
            document.getElementById('loginPage').style.display = 'none';
            document.getElementById('lobbyPage').style.display = 'block';
            document.getElementById('chatArea').style.display = 'block';
            updatePlayerList([{name: username, isReady: false}]); // Add player to the list
        });
    } else {
        console.error('Login form not found!');
    }
});

// Initialize WebSocket and setup handlers
function initializeWebSocket() {
    // Assuming you're running the WebSocket server locally on port 8080
    var serverUrl = "ws://localhost:8080";
    console.log("Connecting to WebSocket server at " + serverUrl);

    connection = new WebSocket(serverUrl);

    connection.onopen = function () {
        console.log("Connected to the server.");
        // Possibly update the UI to show connection status
    };

    // When a message from the server is received
connection.onmessage = function (event) {
    var data = JSON.parse(event.data);
    console.log("Message received:", data);

    // Handle different types of messages
    switch (data.type) {
        case 'playerListUpdate':
            updatePlayerList(data.players);
            break;
        case 'gameStarted':
            startGame();
            break;
        case 'chat':
            displayChatMessage(data.username, data.message);
            break;    

    // If the message type indicates an update to the player list
    //if (data.type === 'playerListUpdate') {
      //  updateLobbyWithPlayers(data.players); // data.players should be an array of player objects
      //}
    }
};

    connection.onerror = function (error) {
        console.error("WebSocket Error:", error);
        // Possibly update the UI to reflect the error status without using alert
    };

    connection.onclose = function () {
        console.log("Connection closed by the server.");
        // Update the UI to show the connection is not currently active
        // Avoid using alert here; instead, you might want to show a message in the UI
    };
}

// Handle different types of messages from the server
function handleServerMessages(data) {
    switch (data.type) {
        case 'playerListUpdate':
            updatePlayerList(data.players); // Use this function to update the lobby
            break;
        case 'gameStarted':
            startGame();
            break;
        // Add more cases as needed
    }
}

function sendMessage() {
    var input = document.getElementById('chatInput');
    var message = input.value.trim();
    if (message) {
        if (connection && connection.readyState === WebSocket.OPEN) {
            connection.send(JSON.stringify({ type: "chat", username: username, message: message }));
        }
        displayChatMessage(username, message);  // Ensure this is called
        input.value = '';  // Clear the input field after sending
    }
}


function displayChatMessage(username, message) {
    const chatDisplay = document.getElementById('chatDisplay');
    const messageElem = document.createElement('p');
    messageElem.innerHTML = `<strong>${username}</strong>: ${message}`;
    chatDisplay.appendChild(messageElem);
    chatDisplay.scrollTop = chatDisplay.scrollHeight; // Automatically scroll to the latest message
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
}

  //update player status
  function updateStatus(status) {
    const message = { type: 'updateStatus', status: status };
    connection.send(JSON.stringify(message));
  }

  function changeLobby(lobbySize) {
    const message = { type: 'changeLobby', lobbySize: lobbySize };
    connection.send(JSON.stringify(message));
  }

// Start the game
function startGame() {
    if (!isReady) {  // Check if the player is ready before starting the game
        alert("Please mark yourself as ready before starting the game!");
        return;
    }
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'block';
    document.getElementById('chatArea').style.display = 'block';
    initializeGrid();
    loadWordList();
    console.log('Game started.');
}

function loadWordList() {
    fetch('http://yourserver.com/wordlist.txt')
        .then(response => response.text())
        .then(data => {
            const words = data.split(/\r?\n/); // Split the text file into an array of words
            displayWords(words);
        })
        .catch(error => console.error('Error loading word list:', error));
  }
  
  function displayWords(words) {
    const wordListContainer = document.getElementById('wordList');
    wordListContainer.innerHTML = ''; // Clear existing content
    words.forEach(word => {
        if (word.trim().length > 0) { // Avoid empty lines
            const wordElement = document.createElement('div');
            wordElement.textContent = word;
            wordListContainer.appendChild(wordElement);
        }
    });
  }

let startPoint = null;

//create grid

function initializeGrid() {
  const grid = document.getElementById("grid");
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

function handleCellClick(row, col) {
  const cellId = `cell-${row}-${col}`;
  const cell = document.getElementById(cellId);
  if (!startPoint) {
      // Mark the start point
      startPoint = { row, col };
      cell.style.backgroundColor = "yellow"; // Highlight starting cell
  } else {
      // Determine the direction and highlight the path
      highlightPath(startPoint, { row, col });
      startPoint = null; // Reset start point for next word
  }
}

function highlightPath(start, end) {
  if (start.row === end.row) {
      // Horizontal path
      for (let j = Math.min(start.col, end.col); j <= Math.max(start.col, end.col); j++) {
          document.getElementById(`cell-${start.row}-${j}`).style.backgroundColor = "lightgreen";
      }
  } else if (start.col === end.col) {
      // Vertical path
      for (let i = Math.min(start.row, end.row); i <= Math.max(start.row, end.row); i++) {
          document.getElementById(`cell-${i}-${start.col}`).style.backgroundColor = "lightgreen";
      }
  } else {
      // Diagonal path
      const rowIncrement = start.row < end.row ? 1 : -1;
      const colIncrement = start.col < end.col ? 1 : -1;
      let row = start.row;
      let col = start.col;
      while (row !== end.row + rowIncrement && col !== end.col + colIncrement) {
          document.getElementById(`cell-${row}-${col}`).style.backgroundColor = "lightgreen";
          row += rowIncrement;
          col += colIncrement;
      }
  }
}

function ready() {
    isReady = !isReady;  // Toggle the player's ready status
    updateStatus(isReady);  // Send the new status to the server
    updateLocalPlayerReadyStatus();  // Update the UI to reflect the change
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



function twoplayers(){
    
}

function threeplayers(){
    
}

function fourplayers(){
    
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







