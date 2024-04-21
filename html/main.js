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
            const wordElement = document.createElement('li');
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

function updateScoreboard(players) {
    const scoreboard = document.getElementById('scoreBoard');
    scoreboard.innerHTML = ''; // Clear existing scoreboard
  
    players.forEach(player => {
      const scoreItem = document.createElement('li');
      scoreItem.classList.add('playerScore');
      scoreItem.textContent = `${player.name}: ${player.score}`;
      scoreItem.style.color = player.color; // Assign color dynamically
      scoreboard.appendChild(scoreItem);
    });
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




function twoplayers() {
    requiredPlayers = 3;
    updatePlayerMode('twoPlayerList');
}


function threeplayers() {
    requiredPlayers = 3;
    updatePlayerMode('threePlayerList');
}

function fourplayers() {
    requiredPlayers = 4;
    updatePlayerMode('fourPlayerList');
}

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