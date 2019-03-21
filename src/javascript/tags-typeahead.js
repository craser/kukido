TagsTypeAhead = (function() {
	var tags = [{ d: 0, name: "BOGUS" }]; // Array of { lev: 0, tag: name };
	var node = null;
	var div = null;
	
	function charHandler(e) {
		showOptions();
		return true;
	}
	
	function tabHandler(e) {
		if (e.keyCode == 9) {
			var options = getOptions();
			if (options.length > 0) {
				e.preventDefault();
				var option = options[0];
				replaceTag(option.name);
			}
		}
		return true;
	}
	
	function showOptions() {
		if (div) {
			try { document.body.removeChild(div); }
			catch (e) {} // ignore
			finally { div = null; }
		}
		var options = getOptions();
		if (options.length > 0) {
			div = getOptionsDiv();
			options.forEach(function(tag) {
				var od = createOptionDiv(tag);
				div.appendChild(od);
			});
			document.body.appendChild(div);
		}
	}
	
	function createOptionDiv(tag) {
		var prefix = getTag();
		var od = document.createElement("div");
		od.style.padding = "5px";
		var b = document.createElement("b"); // bold
		b.appendChild(document.createTextNode(prefix));
		var n = document.createTextNode(tag.name.substring(prefix.length)); // normal
		od.appendChild(b);
		od.appendChild(n);
		od.style.cursor = "pointer";
		od.addEventListener("click", function() {
			replaceTag(tag.name);
			node.focus();
			showOptions();
		});
		return od;
	}
	
	function getOptionsDiv() {
		var pos = getOptionsPosition();
		var div = document.createElement("div");
		div.style.position = "absolute";
		div.style.background = "white";
		div.style.border = "1px solid black";
		div.style.webkitBoxShadow = "2px 2px 7px";
		div.style.top = pos.t + "px";
		div.style.left = pos.l + "px";
		return div;
	}
	
	function getOptionsPosition() {
		var o = node.offset();
		var h = node.outerHeight();
		return {t: (o.top + h), l: o.left};
	}
	
	function getOptions() {
		var prefix = getTag();
		if (prefix == "") return [];
		var options = tags.filter(function(tag) { return tag.name.indexOf(prefix) == 0; });
		options = sortOptions(options, prefix);
		options = options.slice(0, 10);
		return options;
	}
	
	function sortOptions(options, prefix) {
		options.forEach(function(tag) {
			tag.d = lev(prefix, tag.name);
		});
		options.sort(function(a, b) {
			return a.d > b.d ? 1 : -1;
		});
		return options;
	}
	
	/**
	 * Returns the tag currently being typed, ie, the last tag in the list.
	 * Returns null if there's a problem getting the last item in the list.
	 */
	function getTag() {
		try {
			var t = getTags().pop();
			return t;
		}
		catch (e) {
			return null;
		}
	}
	
	function setTags(tags) {
		node.val(tags.join(" ") + " " );
	}
	
	function getTags() {
		val = node.val().replace(/ +/g, " ");
		var ts = val.split(" ");
		return ts;
	}
	
	function replaceTag(value) {
		var tags = getTags();
		tags.pop();
		tags.push(value);
		setTags(tags);
	}
	
	function init(inputId) {
		node = $(inputId);
	    var url = "JsonTagNames.do";
	    var k = function (ts) {
	        tags = ts.map(function(tag) {
	        	return {d: 0, name: tag};
	        });
	        node.keyup(charHandler);
	        node.keydown(tabHandler);
	    };
	    $.getJSON("JsonTagNames.do", k);
	}
	
	// Calculates Levenshtein distance.
	function lev(s, t)
	{
	    // create two work vectors of integer distances
	    var lim0 = t.length + 1;
	    var v0 = [];
	    var v1 = [];
	 
	    // initialize v0 (the previous row of distances)
	    // this row is A[0][i]: edit distance for an empty s
	    // the distance is just the number of characters to delete from t
	    for (var i = 0; i < lim0; i++)
	        v0[i] = i;
	 
	    for (var i = 0; i < s.length; i++)
	    {
	        // calculate v1 (current row distances) from the previous row v0
	        // first element of v1 is A[i+1][0]
	        //   edit distance is delete (i+1) chars from s to match empty t
	        v1[0] = i + 1;
	 
	        // use formula to fill in the rest of the row
	        for (var j = 0; j < t.length; j++)
	        {
	            var cost = (s[i] == t[j]) ? 0 : 1;
	            v1[j + 1] = Math.min(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
	        }
	 
	        // copy v1 (current row) to v0 (previous row) for next iteration
	        for (var j = 0; j < v0.length; j++)
	            v0[j] = v1[j];
	    }
	 
	    return v1[t.length];
	}


	
	return {
		init: init
	};
}());