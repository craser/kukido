
(function() {
    module("jsonparse");

    test("parse infinity", function() {
    	var obj = json_parse("{ x: Infinity }");
    	ok(obj.x == Infinity);
    });
})();
                                   

     
