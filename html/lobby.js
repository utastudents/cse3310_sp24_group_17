

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

function resetPlayerDiv(playerDiv) {
    playerDiv.querySelector(".playerName").textContent = "Waiting...";
    playerDiv.querySelector(".playerStatus").classList.remove("ready");
}
function ready() {
    isReady = !isReady;
    updateLocalPlayerReadyStatus(); // Update UI immediately
    if (connection.readyState === WebSocket.OPEN) {
        updateStatus(isReady);
    } else {
        console.log("Waiting for the WebSocket to open...");
        setTimeout(() => {
            if (connection.readyState === WebSocket.OPEN) {
                updateStatus(isReady);
            } else {
                alert("Cannot set ready status. WebSocket is not connected.");
            }
        }, 1000); // Wait for 1 second before trying again
    }
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

window.updatePlayerList = updatePlayerList;
