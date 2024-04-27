
connection.onmessage = function(event) {
    var data = JSON.parse(event.data);
    switch (data.type) {
        case 'chatMessage':
            var chatBox = document.getElementById('chatMessages');
            var messageElement = document.createElement('p');
            messageElement.textContent = data.playerName + ": " + data.message;
            chatBox.appendChild(messageElement);
            break;
        // handle other message types...
    }
};

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