var element = document.body;
var text = document.querySelectorAll(".lightModeText");
var background = document.querySelectorAll(".light-mode");
var checkBox = document.getElementById("flexSwitchCheckChecked");
element.classList.add("light-mode");
element.classList.add("lightModeText");

function darkMode() {
	text = document.querySelectorAll(".lightModeText");
	if (checkBox.checked) {
		element.classList.add("dark-mode");
		background.forEach(e => {
			e.classList.add("dark-mode")
		})
		text.forEach(e => {
			e.classList.add("darkModeText");
		})
		localStorage.setItem(
			'theme', "dark-mode"
		)
	} else {
		element.classList.remove("dark-mode");
		background.forEach(e => {
			e.classList.remove("dark-mode")
		})
		text.forEach(e => {
			e.classList.remove("darkModeText");
		})
		localStorage.setItem(
			'theme', "light-mode"
		)
	}
}

function darkModeCheck() {
	if (localStorage.getItem('theme') == "dark-mode") {
		element.classList.add("dark-mode");
		background.forEach(e => {
			e.classList.add("dark-mode")
		})
		text.forEach(e => {
			e.classList.add("darkModeText");
		})
		checkBox.checked = true;
	}
	else {
		element.classList.remove("dark-mode");
		text.forEach(e => {
			e.classList.remove("darkModeText");
		})
		background.forEach(e => {
			e.classList.remove("dark-mode")
		})
		checkBox.checked = false;
	}
}

darkModeCheck();