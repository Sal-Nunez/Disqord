function darkModeCall(){
	darkModeCheck();
}


// =====================================
// Clicking 'Friends' tab on account dropdown 
// =====================================
 
var navFriendBtn = document.querySelector('#navFriendBtn');

navFriendBtn.addEventListener('click', function (e) {
	e.preventDefault()
	$('#search-results-container').html('');
	$('#search-modal').modal('show')
	fire_ajax_allFriends();
	darkModeCall();
})

// ====================================
// Searching for a user to add friend
// ====================================
$(document).ready(function () {
	$("#userSearchForm").submit(function (e) {
		e.preventDefault();
		fire_ajax_submit();
		darkModeCall();
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