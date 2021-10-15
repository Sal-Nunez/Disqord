function darkMode() {
  var checkBox = document.getElementById("flexSwitchCheckChecked");
  var element = document.body;
  var text = document.getElementsByClassName("darkModeText");
  if (checkBox){
    element.classList.toggle("dark-mode");
    text.classList.toggle("lightModeText");
  } else {
		element.classList.toggle("light-mode");
		text.classList.toggle("darkModeText");
	}
}