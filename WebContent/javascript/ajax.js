function Ajax(w) {
	w = w || window; // Make mocking/injection easier.
	with (w) {
	    function getRequest() {
	        return (XMLHttpRequest)
	            ? new XMLHttpRequest() // code for IE7+, Firefox, Chrome, Opera, Safari
	            : new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
	    }
	
	    function get(url, k, ek) {
	        var req = getRequest();
	        req.onreadystatechange = function() {
	            if (req.readyState == req.DONE) {
	                if (req.status == 200) {
	                    k(req.responseText);
	                }
	                else if (ek) {
	                    ek(req);
	                }
	                else {
	                    console.error("Error in ajax call to \"" + url + "\": " + req.statusText);
	                }                    
	            }
	        };
	        req.open("GET", url, true);
	        req.send();
	    }
	
	    return {
	        get: get
	    };
	}
}
