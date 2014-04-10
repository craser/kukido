module("Dummy Tests");

test("Box", function() {
    ok(Box, "Box exists.");
    var box = new Box("foo");
    equal(box.get(), "foo", "Contents of box == 'foo'");
});

test("BoxHolder", function() {
    expect(1);
    var box = {
        get: function() { ok(true, "box.get called"); }
    };
    var h = new BoxHolder(box);
    h.get();
});
    