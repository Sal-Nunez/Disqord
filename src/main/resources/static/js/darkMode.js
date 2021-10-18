var element = document.body;
var text = document.querySelector(".lightModeText");
var checkBox = document.getElementById("flexSwitchCheckChecked");
element.classList.add("light-mode");
element.classList.add("lightModeText");

function darkMode() {
  if (checkBox.checked){
    element.classList.add("dark-mode");
    text.classList.add("darkModeText");
    localStorage.setItem(
    'theme', "dark-mode"
  )
  } else {
		element.classList.remove("dark-mode");
		text.classList.remove("darkModeText");
		localStorage.setItem(
    'theme', "light-mode"
  )
	}
}

function darkModeCheck() {
	if (localStorage.getItem('theme') == "dark-mode") {
		element.classList.add("dark-mode");
		text.classList.add("darkModeText");
    	checkBox.checked = true;
	}
	else {
		element.classList.remove("dark-mode");
		text.classList.remove("darkModeText");
		checkBox.checked = false;
	}
}

function helloWorld() {
	console.log("Helo World!")
}

darkModeCheck();