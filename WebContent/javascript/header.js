function Header(div) {
	var topDistance = div.offsetTop; // Original distance to top of page.
	var floating = false; // We start out NOT floating.
	function setLocation() {
		if (window.scrollY > topDistance && !floating) {
			var bounds = div.getBoundingClientRect();
			div.style.position = "fixed";
			div.style.top = "0px";
			div.style.left = bounds.left + "px";
			div.style.width = bounds.width + "px";
			div.style.height = bounds.height + "px";
			div.style.backgroundColor = "#555";
			floating = true;
		}
		else if (window.scrollY <= topDistance && floating) {
			div.style.position = "relative";
			div.style.top = null;
			div.style.left = null;
			floating = false;
		}
	}
	window.addEventListener("scroll", setLocation);
	window.addEventListener("resize", setLocation);
	setLocation(); // Make sure we START in the right location.
}