
new OneBanana({name: "jsonparse"}).test(
    function test_parseInfinity(test) {
    	var obj = json_parse("{ x: Infinity }");
    	test.ok(obj.x == Infinity);
    }
);
                                   

     
