var serverUrl = "ws://" + window.location.hostname + ":" + (parseInt(location.port) + 100);
var connection = new WebSocket(serverUrl);
var currentPlayerName = null;
var wordList = [];
var timer_shown = false;
var temp = 1;
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
    if (temp === 1){
    					var commit = JSON.parse(event.data);
    					document.getElementById("commit").innerHTML = "Commit hash:  "+commit;
    					document.title = commit;
    					temp+=1;
    
    }
    
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
        generateGrid(data);
        if (timer_shown === false)
        {
        	startGameTimer(); 
        	timer_shown = true;
        }
        
        showGame();
        wordList = JSON.parse(data.eventData2); // Store the word list
        break;
    case "highlightSuccess":
        applyHighlightFromServer(data);
        updateWordList(data);
        break;
    case "updateScoreboard":
        updateScoreboard(data.scores);
        break;
    case 'hint':
        highlightCell(data.row,data.col);
    default:
        console.log("Unknown message type:", data.type);
   }
};

// SEND JSONS TO JAVA --------------------------------------------------------------------

// Send login request on login submit button press
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

//send json of which sublobby player wants to join on lobby option buttons
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

// Send JSON of start game button
function startGame() {
    console.log('Game started.');
    console.log('current player', currentPlayerName);
    var data = {
        type: "startGame",
        eventData: {
        } 
    };
    
    connection.send(JSON.stringify(data)); // Send the start game request to the server
    console.log("Start game request sent to the server., Display--");
}

// Send players' input for word guess to server
function sendHighlightToServer(start, end, playerName, word) {
    const highlightData = {
        type: "highlight",
        "eventData":{
        start: start,
        end: end,
        playerName: playerName,
        word: word
        }
    };
    connection.send(JSON.stringify(highlightData));
}

// Send JSON of Ready Up button
function toggleReady() {
        var data = {
            type: "toggleReady",
            username: currentPlayerName
        };
        connection.send(JSON.stringify(data));
};

// Send JSON of back to main lobby button (REMOVED?)
function backToMainLobby() {
    var data = {
        type: "leaveSubLobby"
    };
    connection.send(JSON.stringify(data));
};

function sendHintRequest() {
    var data = {
        type: "giveMehint",
        eventData: {}
    };
    connection.send(JSON.stringify(data));
    console.log("Hint request sent.");
}





// RECEIVE INCOMING JSON AND HANDLE-----------------------------------------------------------------

// Creates Word Grid and Word List on array inputs from java
function generateGrid(json) {
    const gridData = JSON.parse(json.eventData);
    const wordList = JSON.parse(json.eventData2);

    const rows = gridData.length;
    const cols = gridData[0].length;
    const gridElement = document.getElementById('grid');
    const wordListContainer = document.getElementById('wordListContainer');
    
    gridElement.innerHTML = '';
    wordListContainer.innerHTML = '';

    for (let i = 0; i < rows; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < cols; j++) {
            const cell = document.createElement('td');
            cell.textContent = gridData[i][j]; 
            cell.id = `cell-${i}-${j}`; 
            cell.onclick = function() { handleCellClick(i, j); };
            row.appendChild(cell);
        }
        gridElement.appendChild(row);
    }

    // Generate word list
    const headingElement = document.createElement('h2');
    headingElement.textContent = 'Words To Find';
    wordListContainer.appendChild(headingElement);

    const wordListElement = document.createElement('ul');
    wordList.forEach(word => {
        const listItem = document.createElement('li');
        listItem.textContent = word;
        wordListElement.appendChild(listItem);
    });
    wordListContainer.appendChild(wordListElement);
}

function updateWordList(json){
    const wordList = JSON.parse(json.updatedWords);
    const wordListContainer = document.getElementById('wordListContainer');
    wordListContainer.innerHTML = '';

    const headingElement = document.createElement('h2');
    headingElement.textContent = 'Words To Find';
    wordListContainer.appendChild(headingElement);

    const wordListElement = document.createElement('ul');
    wordList.forEach(word => {
        const listItem = document.createElement('li');
        listItem.textContent = word;
        wordListElement.appendChild(listItem);
    });
    wordListContainer.appendChild(wordListElement);
}

// Updates the sublobby playerlist upon player joining/leaving
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
    
}

// Highlight based upon correct guess
function applyHighlightFromServer(data) {
    
    highlightPath(data.start, data.end, data.color);
}

function highlightPath(start, end, color) {
    
    if (start.row === end.row) {
        // Horizontal path
        for (let j = Math.min(start.col, end.col); j <= Math.max(start.col, end.col); j++) {
            document.getElementById(`cell-${start.row}-${j}`).style.backgroundColor = color;
        }
    } 
    else if (start.col === end.col) {
        // Vertical path
        for (let i = Math.min(start.row, end.row); i <= Math.max(start.row, end.row); i++) {
            document.getElementById(`cell-${i}-${start.col}`).style.backgroundColor = color;
        }
    } 
    else {
        // Diagonal path
        const rowIncrement = start.row < end.row ? 1 : -1;
        const colIncrement = start.col < end.col ? 1 : -1;
        let row = start.row;
        let col = start.col;
        while (row !== end.row + rowIncrement && col !== end.col + colIncrement) {
            document.getElementById(`cell-${row}-${col}`).style.backgroundColor = color;
            row += rowIncrement;
            col += colIncrement;
        }
    }
  }

  function highlightCell(row, col, color) {
    const cellId = `cell-${row}-${col}`;
    const cell = document.getElementById(cellId);
    
    if (cell) {
        // Check if the cell is already highlighted with the same player's color
        const isHighlighted = cell.classList.contains('highlighted');
        const isSameColor = cell.dataset.playerColor === color;
        
        if (isHighlighted && isSameColor) {
            // If the cell is already highlighted with the same player's color, remove the highlight
            removeHighlightCell(row, col);
        } else {
            // If the cell is not already highlighted with the same player's color,
            // highlight it with light green
            cell.style.backgroundColor = "lightgreen";
            cell.classList.add('highlighted'); 
            
            // Store the player's color
            cell.dataset.playerColor = color;
        }
    } else {
        console.error('Cell not found:', cellId);
    }
}

function removeHighlightCell(row, col) {
    const cellId = `cell-${row}-${col}`;
    const cell = document.getElementById(cellId);
    
    if (cell) {
        // Remove the light green highlight
        cell.classList.remove('highlighted');
        
        // Restore the player's color
        if (cell.dataset.playerColor) {
            cell.style.backgroundColor = cell.dataset.playerColor;
            delete cell.dataset.playerColor;
        }
    } else {
        console.error('Cell not found:', cellId);
    }
}

function updateScoreboard(scores) {
    const scoreboard = document.getElementById('scoreBoard');
    if (!scoreboard) {
        console.error("Scoreboard element not found!");
        return;
    }
    
    scoreboard.innerHTML = ''; // Clear existing scoreboard

    scores.forEach(player => {
        const scoreItem = document.createElement('li');
        scoreItem.classList.add('playerScore');
        scoreItem.textContent = `${player.name}: ${player.score}`;
        scoreItem.style.color = player.color || 'black'; 
        scoreboard.appendChild(scoreItem);
    });
}

function updateStatus(isReady) {
    const message = { type: 'updateStatus', username: username, isReady: isReady };
    connection.send(JSON.stringify(message));
}

// Updates the ready display 
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





// CHAT METHODS ----------------------------------------------------------------------------------------------

// Send JSON of chat message to server
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

// Update chat UI
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




// MISC METHODS -----------------------------------------------------------------------------------------

// Responds to Back button press
function back() {
    console.log('Back button clicked'); 

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
    const gridContainer = document.getElementById("gridContainer");
    if (gridContainer) {
        gridContainer.innerHTML = ''; // This will remove all children elements
    } else {
        console.log('Grid container element not found');
    }

    // Clear the word list
    const wordListContainer = document.getElementById("wordListContainer");
    if (wordListContainer) {
        wordListContainer.innerHTML = ''; // This will remove all word list elements
    } else {
        console.log('Word list container element not found');
    }

    console.log('Game state has been reset');
}

let startPoint = null;
//need correction currentPlayerName
function handleCellClick(row, col) {
    const cellId = `cell-${row}-${col}`;
    const cell = document.getElementById(cellId);
    if (!startPoint) {
        // Mark the start point
        startPoint = { row, col };
        cell.style.backgroundColor = "yellow"; // Temporary highlight for start point
    } 
    else {
        // Call a function to check if the selected word is valid
        const selectedWord = getWordFromSelection(startPoint, { row, col });
        if (wordList.includes(selectedWord)) {
            highlightPath(startPoint, {row,col});
            sendHighlightToServer(startPoint, { row, col }, currentPlayerName, selectedWord);
        } else {
            // Clear the temporary start point highlight if word is not valid
            const startCell = document.getElementById(`cell-${startPoint.row}-${startPoint.col}`);
            startCell.style.backgroundColor = ""; // Reset the background color
        }
        startPoint = null; // Reset start point for next word
    }
}


// This function needs to be implemented to retrieve the word from the grid
function getWordFromSelection(start, end) {
    let word = "";
    const xDirection = Math.sign(end.col - start.col);
    const yDirection = Math.sign(end.row - start.row);
    let currentRow = start.row;
    let currentCol = start.col;

    while (currentRow !== end.row + yDirection || currentCol !== end.col + xDirection) {
        const cell = document.getElementById(`cell-${currentRow}-${currentCol}`);
        word += cell.textContent;
        currentRow += yDirection;
        currentCol += xDirection;
    }

    return word;
}

function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    var lastHintTime = 0;

    var timerInterval = setInterval(function() {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        
        var currentTime = duration - timer;

        
        if (currentTime >= lastHintTime + 30) {
            sendHintRequest();
            lastHintTime = currentTime;
        }

        if (--timer < 0) {
            clearInterval(timerInterval);
            alert("Time's up!");
            display.textContent = "00:00";
        }
    }, 1000);
}

function startGameTimer() {
    var twentyMinutes = 60 * 20; // 20 minutes in seconds
    var display = document.querySelector('#timer');
    startTimer(twentyMinutes, display);
}

/*window.onload = function() {
    startGameTimer(); // Start the game timer when the window loads
};*/





// ERROR HANDLING ---------------------------------------------------------------------------------------

function displayErrorMessage(message) {
    const errorDisplay = document.getElementById('errorDisplay'); 
    errorDisplay.textContent = message; // Display or handle error messages
}

// handle invalid username
function handleLoginError(){
    alert('Username Invalid');
}

//handle if max games reached
function handleSubLobbyError(){
    alert('Lobbies are all full - Try again later');
}





// DISPLAY METHODS ----------------------------------------------------------------------------------------------

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
    document.getElementById('lobbyPage').style.display = 'none';
    document.getElementById('gamePage').style.display = 'block';
    document.getElementById("loginPage").style.display = "none";
    document.getElementById('chatArea').style.display = 'block'
}



    
