function Box(x) {
    var contents = x;
    this.put = function(x) { contents = x; };
    this.get = function() { return contents; };
}

function BoxHolder(box) {
    this.get = function() { return box.get() };
}