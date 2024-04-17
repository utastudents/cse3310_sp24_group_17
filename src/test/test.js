const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

let players = [];

wss.on('connection', function connection(ws) {
    // Send current players to just connected client
    ws.send(JSON.stringify({ type: 'playerListUpdate', players: players }));

    ws.on('message', function incoming(message) {
        const data = JSON.parse(message);
        switch (data.type) {
            case "login":
                // Add player and broadcast update
                players.push({ name: data.username, isReady: false });
                broadcastPlayerList();
                break;
            case "updateStatus":
                // Update player status and broadcast
                const player = players.find(p => p.name === data.username);
                if (player) {
                    player.isReady = data.isReady;
                    broadcastPlayerList();
                }
                break;
            // handle other messages...
        }
    });

    ws.on('close', function() {
        // Remove player from list and broadcast update
        players = players.filter(p => p.ws !== ws);
        broadcastPlayerList();
    });
});

function broadcastPlayerList() {
    wss.clients.forEach(client => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify({ type: 'playerListUpdate', players: players }));
        }
    });
}
