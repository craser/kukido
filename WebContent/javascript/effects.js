function Effects() {
	var self = this;
	
	this.transition = function(time, f, k) {
		var start = new Date().getTime();
		var token = setInterval(function() {
			var now = new Date().getTime();
			var passed = now - start;
			var pct = Math.min(1, (passed/time));
			f(pct);
			if (pct == 1) {
				clearInterval(token);
				if (k) k();
			} 
		}, 33); // 30 fps?
	};
	
	this.fade = function(node, time, k) {
		self.transition(time, function(pct) {
			node.style.opacity = (1 - pct);
		}, k);
	};
	
	this.crush = function(node, time, k) {
		var h = node.offsetHeight;
		node.style.overflow = "hidden";
		self.transition(time, function(pct) {
			var newH = (1 - pct) * h;
			console.log("h: " + newH);
			node.style.height = newH + "px";
		}, k);
	};
};