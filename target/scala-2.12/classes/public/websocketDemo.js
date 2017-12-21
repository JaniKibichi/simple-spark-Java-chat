//establish the WebSocket connection and set up handlers
var webSocket = new WebSocket("ws://"+ location.hostname + ":" + location.port + "/chat");
webSocket.onmessage = function (msg) {
	updateChat(msg);
};

webSocket.onclose = function () {
	alert("WebSocket connection closed")
};

//Send message if "send" is clicked
id("send").addEventListener("click", function(){
	sendMessage(id("message").value);
});

id("message").addEventListener("keypress", function(e){
	if(e.keyCode === 13){
		sendMessage(e.target.value);
	}
});

//send a message if it's not empty, clear input field
function sendMessage(message){
	if(message !== ""){
		webSocket.send(message);
		id("message").value = "";
	}
}

//update chat-panel, list of connected users
function updateChat(msg){
	var data = JSON.parse(msg.data);
	insert("chat", data.userMessage);
	id("userlist").innerHTML = "";
	data.userlist.forEach(function (user){
		insert("userlist", "<li>" + user+"</li>");
	});
}

//helper function for inserting HTML as the first child of an element
function insert(targetId, message){
	id(targetId).insertAdjacentHTML("afterbegin",message);
}

//helper function for selecting element by id
function id(id){
	return document.getElementById(id);
}