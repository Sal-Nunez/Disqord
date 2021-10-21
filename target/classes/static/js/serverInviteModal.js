//////////////////////////////////////////////////
///////// INVITE FRIENDS BUTTON SERVER ///////////
//////////////////////////////////////////////////

// =====================================
// Clicking Invite Friend on Server Page 
// =====================================
var serverFriendInviteBtn = document.querySelector('#serverFriendInviteBtn');
$('#serverFriendInviteBtn').click(function(){console.log("serverFriendInviteBtn clicked")})
 
serverFriendInviteBtn.addEventListener('click', function (e) {
	e.preventDefault()
	console.log("button clicked")
	$("#searchModalHeader").html(`<form class="d-flex align-items-center" id="inviteSearchForm"> 
                                	<input class="form-control me-2" type="search" placeholder="Add user by username" aria-label="userSearch" id="userSearch" name="userSearch"> 
                                	<button class="btn btn-outline-success btn-sm" type="submit" id="inviteServerSearchBtn">Search</button>
                           		 </form>`)
	// replace current modal header html w this so the listener for the form submit does not call both friend search and server search
	$('#search-results-container').html('');
	$('#search-modal').modal('show')
	inviteToServer_allFriends(); 
	// ====================================
	// Searching for a user to add to room
	// ====================================
	$(document).ready(function () {
		$("#inviteSearchForm").submit(function (e) {
			e.preventDefault();
			inviteToServer_nonFriends(); 
		});
	});
})


// ====================================
// GET REQUEST TO FIND USERS FRIENDS 
// 		-  
// ====================================
var serverId = document.querySelector("#server_id").value;
var activeUserId = document.querySelector("#user_id").value;


function inviteToServer_allFriends() {
	var myFriendsHTML = `<p class="h4">Add Friends to Room: </p>
					 <ul class="list-group light-mode" id="userSearchResultsList">`
					 
	// Find friends not in chat room		 
	$.get("/friends/servers/"+serverId, function(data){
		data.result.forEach(user => {
			console.log("user not in server " + user.userName)
			myFriendsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
									<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
									<a href="/friends/invite/${user.id}/servers/${serverId}" class="btn btn-sm btn-outline-success">Invite</a>
							 </li>`
		})			
		// Find friends already in chat room
		$.get("/friends/servers/" + serverId + "/serverMembers", function(data){
			console.log("in chat room")
			data.result.forEach(user => {
				console.log("user in server " + user.userName)
				myFriendsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
											<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
											<form action="/friends/kick/${user.id}/servers/${serverId}/" method="post">
										    	<input type="hidden" name="_method" value="delete">
										    	<input type="submit" value="Kick" class="btn btn-sm btn-outline-danger">
											</form>
									 </li>`
			})
			myFriendsHTML += "</ul>";
			$('#search-results-container').html(myFriendsHTML);
		})
	
	})
}

// ====================================
// POST REQUEST TO QUERY SEARCH FOR USERS
// 		- filter if users already in chatroom
// ====================================
function inviteToServer_nonFriends() {
	var search = {}
	search["input"] = $("#userSearch").val();

	$("#userSearchBtn").prop("disabled", true);

	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "/friends/search",
		data: JSON.stringify(search),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function (data) {

			// data.result is a dictionary of users and any mem var.s that are not annotated w @JsonIgnore in the model
			var searchHTML = `<p class="h4">Results: </p>
							   <ul class="list-group light-mode lightModeText" id="userSearchResultsList">
								`;
			if (Object.keys(data.result).length === 0) {
				searchHTML = `<h6 class="text-muted"><i> No users found </i></h6>`
			} else {
				data.result.forEach(user => {
					searchHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
										<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
										<a href="/friends/invite/${user.id}/servers/${serverId}" class="btn btn-sm btn-outline-success"> Invite </a>
									</li>
									`
				})
				searchHTML += "</ul>";
			}

			$('#search-results-container').html(searchHTML);

			console.log("SUCCESS : ", data);
			$("#userSearchBtn").prop("disabled", false);
		},
		error: function (e) {

			var json = "<h4>Ajax Response</h4><pre>"
				+ e.responseText + "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e)
			$("#userSearchBtn").prop("disabled", false);
		}
	})
	
}