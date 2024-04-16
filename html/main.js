
    document.getElementById('login-form').addEventListener('submit', function(event) {
      event.preventDefault();
      const username = document.getElementById('username').value;
      // Copy username to the lobby
      document.getElementById('playerList').textContent = 'Player: ' + username;
      // Hide login and show lobby
      document.getElementById('loginPage').style.display = 'none';
      document.getElementById('lobbyPage').style.display = 'block';
  });
  
 
  

    let gridsize =30;


document.addEventListener("DOMContentLoaded", function() {
  initializeGrid();
  loadWordList();
  initializeWebSocket();
    // Initialize chat functionality
    document.getElementById("chatInput").addEventListener("keypress", function(e) {
      if (e.key === 'Enter') {
          sendMessage();
      }
  });
});


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

document.addEventListener("DOMContentLoaded", function() {
  
});

let connection = null;

function initializeWebSocket() {
  const serverUrl = 'ws://127.0.0.1:5500'; //  WebSocket server URL here
  connection = new WebSocket(serverUrl);

  connection.onopen = function() {
      console.log("Connected to the server.");
  };

  connection.onerror = function(error) {
      console.error("WebSocket Error:", error);
  };

  connection.onmessage = function(event) {
      // Display received message
      displayMessage(event.data);
  };
}




function sendMessage() {
  const input = document.getElementById('chatInput');
  const message = input.value;
  if (message.trim()) {
      connection.send(JSON.stringify({ type: 'chat', message: message }));
      input.value = ''; // Clear input field after sending
  }
}

function displayMessage(message) {
  const chatDisplay = document.getElementById('chatDisplay');
  const messageElement = document.createElement('div');
  messageElement.textContent = message;
  chatDisplay.appendChild(messageElement);
  chatDisplay.scrollTop = chatDisplay.scrollHeight; // Auto-scroll to the latest message
}




// Additional functions to update scores and word list dynamically
function updateScores(scores) {
  // Populate player scores
}

function updateWordList(words) {
  // Populate word list
}



function updatePlayerList(players) {
  const playerListDiv = document.getElementById('playerList');
  playerListDiv.innerHTML = ''; // Clear existing player list entries

  players.forEach(player => {
      const playerEntry = document.createElement('div');
      playerEntry.className = 'player-entry';
      playerEntry.innerHTML = `<span>${player.name}</span> - <span style="color: ${player.ready ? 'green' : 'red'}">${player.ready ? 'Ready' : 'Not Ready'}</span>`;
      playerListDiv.appendChild(playerEntry);
  });
}


function updateStatus(status) {
  const message = { type: 'updateStatus', status: status };
  connection.send(JSON.stringify(message));
}

function changeLobby(lobbySize) {
  const message = { type: 'changeLobby', lobbySize: lobbySize };
  connection.send(JSON.stringify(message));
}




      function SubmitName() {
        const usernameInput = document.getElementById('username');
        const username = usernameInput.value;
    
        if (!username.match(/^[A-Za-z0-9_]{2,12}$/)) {
            alert("Username must be 2-12 characters long and can only include letters, numbers, and underscores.");
            return false;
        }
    
        // Here, add the WebSocket or HTTP request logic to send the username to your server
        // Example for WebSocket:
        if (connection && connection.readyState === WebSocket.OPEN) {
            connection.send(JSON.stringify({ type: "login", username: username }));
        } else {
            console.log("WebSocket is not connected.");
        }
    
        return false; // Return false to prevent form submission
    }

// Assume a global WebSocket connection is already established

function updatePlayerList(players) {
  const playerListDiv = document.getElementById('playerList');
  playerListDiv.innerHTML = ''; // Clear current list
  players.forEach(player => {
    const playerElem = document.createElement('div');
    playerElem.innerHTML = `${player.name} - <span style="color: ${player.isReady ? 'green' : 'red'}">${player.isReady ? 'Ready' : 'Not Ready'}</span>`;
    playerListDiv.appendChild(playerElem);
  });
}

function updateStatus(status) {
  const message = { type: 'updateStatus', status: status };
  connection.send(JSON.stringify(message));
}

function changeLobby(lobbySize) {
  const message = { type: 'changeLobby', lobbySize: lobbySize };
  connection.send(JSON.stringify(message));
}

function startGame() {
  const message = { type: 'startGame' };
  connection.send(JSON.stringify(message));
  document.getElementById('lobbyPage').style.display = 'none';
  document.getElementById('gamePage').style.display = 'block';
  // Initialize the game area
}

// WebSocket handlers
connection.onmessage = function(event) {
  const data = JSON.parse(event.data);
  switch (data.type) {
    case 'playerListUpdate':
      updatePlayerList(data.players);
      break;
    case 'gameStarted':
      // Handle game start, perhaps redirect to game page
      window.location.href = '/index.html';
      break;
    // Add more handlers as needed
  }
};
    



  

      function readyState(){

      }
    }