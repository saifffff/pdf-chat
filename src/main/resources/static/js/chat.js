$(document).ready(function() {	
  // Send message button click handler

  $("#send-message").click(function() {
    var message = $("#chat-message").val();
    if (message.trim() === "") {
      return; // Prevent sending empty messages
    }
	$("#chat-log").append("<p>You: " + message + "</p>");
	 $("#chat-message").val(""); // Clear message input after sending
    // Send AJAX request to API gateway
    $.ajax({
      url: "http://localhost:8080/api/pdfchat/query",
      method: "POST",
      data: { message: message },
      dataType: "text",
      success: function(response) {
        // Update chat log with response
        $("#chat-log").append("<p>PDF : " + response + "</p>");
        $("#chat-message").val(""); // Clear message input after sending
      },
      error: function(error) {
        console.error("Error sending message:", error);
        // Handle error scenarios (e.g., display error message to user)
      }
    });
  });
});

