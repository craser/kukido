/*
  getColor: getColor,
  getNextColor: getNextColor,
  getDefaultColor: function() { return colors[0]; }
*/
new OneBanana({name: "Colors"}).test(
    function test_getColor(test) {
        test.ok(Colors.getColor, "Function getColor should exist.");
        test.ok(isColor(Colors.getColor(90000)), "Can't run off end of available colors.");
        test.ok(isColor(Colors.getColor()), "Has a default value.");
    },
    function test_getNextColor(test) {
        var a = Colors.getNextColor();
        var b = Colors.getNextColor();
        test.ok(isColor(a));
        test.ok(isColor(b));        
        test.ok(a !== b, "Subsequent calls to getNextColor() should return different values.");
    },
    function test_getDefaultColor(test) {
        test.ok(isColor(Colors.getDefaultColor()), "Should return non-falsy value.");
    }
);

function isColor(c) {
    return c.match(/#[a-f0-9]{6}/i);
}

                                   

     
