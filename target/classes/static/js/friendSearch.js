

const url = new URL("localhost:8080")

var navFriendBtn = document.querySelector('#navFriendBtn');

navFriendBtn.addEventListener('click', function (e) {
	e.preventDefault()
	// console.log('clicked')
	$('#search-modal').modal('show')

	var userSearchForm = document.querySelector('#userSearchForm')
	var form = new FormData(userSearchForm);
	userSearchForm.addEventListener('submit', function (e) {
		e.preventDefault();
		var userSearchEntry = document.querySelector('#userSearch').value
		fetch("/friends/search/" + userSearchEntry)
			.then(resp => resp.json())
			.then(results => {
				// console.log(results[0].firstName)

			})

	})

	let userSearch = document.querySelector("#search-term").value

	console.log(userSearch);
})

// Tutorial ====================================

$(document).ready(function () {
	$("#userSearchForm").submit(function (e) {
		e.preventDefault();
		fire_ajax_submit();

	});

});

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

			document.querySelector("#search-results-container").style.display = 'block';

			// data.result is a dictionary of users and any mem var.s that are not annotated w @JsonIgnore in the model
			var resultsHTML = "";
			if (Object.keys(data.result).length === 0) {
				resultsHTML = `<h6 class="text-muted"><i> No users found </i></h6>`
			} else {
				data.result.forEach(user => {
					resultsHTML += `<li class="list-group-item d-flex justify-content-between friend-result align-items-center">
										<p class="h6 mt-2">${user.firstName} ${user.lastName} - <span class="text-muted">${user.userName}</span></p>
										<a href="/addFriend/${user.id}" class="btn btn-sm btn-outline-success">Add Friend</a>
									</li>
									`
				})
			}

			// var json ="<h4> Search results for: <i>" + search["input"]+ "<i>" + data.forEach(user => function{})
			$('#userSearchResultsList').html(resultsHTML);

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