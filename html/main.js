    var idx = -1;
    var gameid = -1;
    class UserEvent {
        Button = -1;
        PlayerIdx = 0;
        GameId = 0;
    }
    var connection = null;

    var serverUrl;
    serverUrl = "ws://" + window.location.hostname +":9880";
    // Create the connection with the server
    connection = new WebSocket(serverUrl);

    connection.onopen = function (evt) {
        console.log("open");
        document.getElementById("grid").style.display = "inline";
       

        var table = document.getElementById("grid");
 // Create rows and cells
 for (let i = 0; i < 30; i++) {
  const row = table.insertRow();
  for (let j = 0; j < 30; j++) {
    const cell = row.insertCell();
    cell.addEventListener("click", function() {
      // Change cell color on click
      if (this.style.backgroundColor === "yellow") {
        this.style.backgroundColor = "white";
      } else {
        this.style.backgroundColor = "yellow";
      }
    });
  }
}

    // Populate random letters in empty cells
    const cells = table.querySelectorAll("td");
    cells.forEach(function(cell) {
      if (!cell.innerHTML.trim()) {
        const randomLetter = String.fromCharCode(65 + Math.floor(Math.random() * 26)); // Generate a random uppercase letter
        cell.innerHTML = randomLetter;
      }
    });

    }

    connection.onclose = function (evt) {
        console.log("close");
        document.getElementById("topMessage").innerHTML = "Server Offline"
    }


    const ButtonStateToDisplay = new Map();
    ButtonStateToDisplay.set("XPLAYER", "X");
    ButtonStateToDisplay.set("OPLAYER", "O");
    ButtonStateToDisplay.set("NOPLAYER", " ");

    connection.onmessage = function (evt) {
        var msg;
        msg = evt.data;

        console.log("Message received: " + msg);
        const obj = JSON.parse(msg);

        try {

        }
      catch (error) {

      }
       
            }
        
    

    function buttonclick(i) {
        U = new UserEvent();
        U.Button = i;
        if (idx == 0)
            U.PlayerIdx = "XPLAYER";
        else
            U.PlayerIdx = "OPLAYER";
        U.GameId = gameid;
        connection.send(JSON.stringify(U));
        console.log(JSON.stringify(U))
    }

    function StartGame(){
        
      console.log("Requesting the server to start the game.");
      U = new UserMsg;
      U.name = this.name;
      U.code = 300;
      connection.send(JSON.stringify(U));
   



    }


    function sendMessage() {
        const messageInput = document.getElementById('chat');
        
      }
      
      
      function displayMessage(sender, content) {
        // Check if both sender and content are defined
       
      }

      function SubmitName(){

      }

      function Ready(){

      }

  