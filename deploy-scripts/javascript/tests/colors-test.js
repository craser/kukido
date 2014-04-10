/*
  getColor: getColor,
  getNextColor: getNextColor,
  getDefaultColor: function() { return colors[0]; }
*/
(function() {
    module("Colors");

    function isColor(c) {
        return c.match(/#[a-f0-9]{6}/i);
    }

    test("getColor()", function() {
        ok(Colors.getColor, "Function getColor should exist.");
        ok(isColor(Colors.getColor(90000)), "Can't run off end of available colors.");
        ok(isColor(Colors.getColor()), "Has a default value.");
    });

    test("getNextColor()", function() {
        var a = Colors.getNextColor();
        var b = Colors.getNextColor();
        ok(isColor(a));
        ok(isColor(b));        
        ok(a !== b, "Subsequent calls to getNextColor() should return different values.");
    });

    test("getDefaultColor()", function() {
        ok(isColor(Colors.getDefaultColor()), "Should return non-falsy value.");
    });
})();
                                   

     
