// Start the game
function startGame() {
    // if (!isReady) {  // Check if the player is ready before starting the game
    //     alert("Please mark yourself as ready before starting the game!");
    //     return;
    // }
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'block';
    document.getElementById('chatArea').style.display = 'block';
    initializeGrid();
    //loadWordList();
    console.log('Game started.');
}

// function loadWordList() {
//     fetch('http://yourserver.com/words')  // Assuming 'words' is the endpoint set up
//         .then(response => response.json())
//         .then(data => {
//             const words = data.words;  // Assuming the server sends a JSON object with a 'words' array
//             displayWords(words);
//         })
//         .catch(error => console.error('Error loading word list:', error));
// }

  
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
    System.out.println();
  const grid = document.getElementById("grid");
  for (let i = 0; i < 50; i++) {
      const row = grid.insertRow();
      for (let j = 0; j < 50; j++) {
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

  window.initializeGrid = initializeGrid;