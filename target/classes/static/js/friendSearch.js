
console.log("friendSearch connected")
////////////////////////////////////////
/////// ACCOUNT FRIENDS DROPDOWN ///////
////////////////////////////////////////

// =====================================
// Clicking 'Friends' tab on account dropdown 
// =====================================
 
var navFriendBtn = document.querySelector('#navFriendBtn');

navFriendBtn.addEventListener('click', function (e) {
	e.preventDefault()
	$('#search-results-container').html('');
	$('#search-modal').modal('show')
	fire_ajax_allFriends();
})

// ====================================
// Searching for a user to add friend
// ====================================
$(document).ready(function () {
	$("#userSearchForm").submit(function (e) {
		e.preventDefault();
		fire_ajax_submit();
	});

});

// GET REQUEST TO FIND USERS FRIENDS
function fire_ajax_allFriends() {
	var myFriendsHTML = `<p class="h4">Friends: </p>
					 <ul class="list-group light-mode" id="userSearchResultsList">`
	$.get("/friends", function(data){
		console.log(data)
		if(Object.keys(data.result).length === 0){
			myFriendsHTML = `<h6 class="text-muted"><i> Time to make some friends! </i></h6>`;
		}else{
			data.result.forEach(user => {
				console.log(user)
				myFriendsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
										<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
										<form action="/friends/remove/${user.id}" method="post">
									    	<input type="hidden" name="_method" value="delete">
									    	<input type="submit" value="Delete" class="btn btn-sm btn-outline-danger">
										</form>
								 </li>`
			})	
			myFriendsHTML += "</ul>";		
		}
	$('#search-results-container').html(myFriendsHTML);
	})

}

// POST REQUEST TO QUERY SEARCH FOR USERS
function fire_ajax_submit() {
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
			var resultsHTML = `<p class="h4">Results: </p>
							   <ul class="list-group light-mode lightModeText" id="userSearchResultsList">
								`;
			if (Object.keys(data.result).length === 0) {
				resultsHTML = `<h6 class="text-muted"><i> No users found </i></h6>`
			} else {
				data.result.forEach(user => {
					resultsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
										<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
										<a href="/friends/add/${user.id}" class="btn btn-sm btn-outline-success">Add Friend</a>
									</li>
									`
				})
				resultsHTML += "</ul>";
			}

			$('#search-results-container').html(resultsHTML);

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

////////////////////////////////////////
///////// INVITE FRIENDS BUTTON ////////
////////////////////////////////////////


// =====================================
// Clicking Invite to Room 
// =====================================
var inviteFriendBtn = document.querySelector('#inviteFriendBtn');

 
inviteFriendBtn.addEventListener('click', function (e) {
	e.preventDefault()
	$("#searchModalHeader").html(`<form class="d-flex align-items-center" id="inviteSearchForm"> 
                                	<input class="form-control me-2" type="search" placeholder="Add user by username" aria-label="userSearch" id="userSearch" name="userSearch"> 
                                	<button class="btn btn-outline-success btn-sm" type="submit" id="inviteSearchBtn">Search</button>
                           		 </form>`)
	$('#search-results-container').html('');
	$('#search-modal').modal('show')
	inviteToRoom_allFriends(); 
	// ====================================
	// Searching for a user to add to room
	// ====================================
	$(document).ready(function () {
		$("#inviteSearchForm").submit(function (e) {
			e.preventDefault();
			inviteToRoom_nonFriends(); 
		});
	});
})


// ====================================
// GET REQUEST TO FIND USERS FRIENDS 
// 		- diff from above w/ delete vs. invite to room btn
// 		- separate get for friends already in room display below not in room
//		- filter if friends in room then display at bottom of list
// ====================================
var chatRoomId = document.querySelector("#chat_room_id").value;
var friendsInRoom = {};

function inviteToRoom_allFriends() {
	var myFriendsHTML = `<p class="h4">Add Friends to Room: </p>
					 <ul class="list-group light-mode" id="userSearchResultsList">`
					 
	// Find friends not in chat room		 
	$.get("/friends/"+chatRoomId, function(data){
		console.log("not in chat room")
		data.result.forEach(user => {
			console.log("user not in chatroom " + user.userName)
			myFriendsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
									<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
									<a href="/friends/invite/${user.id}/room/${chatRoomId}" class="btn btn-sm btn-outline-success">Add</a>
							 </li>`
		})			
		// Find friends already in chat room
		$.get("/friends/" + chatRoomId + "/members", function(data){
			console.log("in chat room")
			data.result.forEach(user => {
				console.log("user in chatroom " + user.userName)
				myFriendsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center light-mode">
											<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
											<form action="/friends/kick/${user.id}/room/${chatRoomId}/" method="post">
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
function inviteToRoom_nonFriends() {
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
										<a href="/friends/invite/${user.id}/room/${chatRoomId}" class="btn btn-sm btn-outline-success"> Invite </a>
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