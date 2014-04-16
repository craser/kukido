(function main() {
    try {
        var workDir = phantom.args[0];
        var testDir = phantom.args[1];
        var fs = require('fs');
        var files = fs.list(workDir);
        var failed = 0;
        var i = -1;
        var next = function() {
            i++;
            if (i >= files.length) phantom.exit(failed);
            var file = files[i];
            if (!file.match(/\.js$/)) return next();
            var workJs = workDir + "/" + file;
            var testJs = testDir + "/" + getTestJs(file);
            if (!fs.exists(testJs)) return next();
            runTests(workJs, testJs, function(n) { failed += n; next(); });
        };
        next();                                             // kick it off.
    }
    catch (e) {
        console.log(e);
        phantom.exit(1);
    }
})();

function runTests(workJs, testJs, k) {
    var page = (function() {
        var p = require('webpage').create();
        p.onConsoleMessage = function() { console.log.apply(console, arguments); };
        return p;
    })();
    page.open("test.html", function(status){
        if (status !== "success") {
            console.log("ERROR: page.open failed: " + status);
            phantom.exit(1);
        } 
        page.evaluate(qUnitBoilerplate);        // Set up logging.
        page.injectJs(workJs);                                  // Set up the thing we want to test.
        page.injectJs(testJs);                                  // Test the thing.
        page.evaluate(function() { QUnit.start(); });
        waitFor(
            function() {
                return page.evaluate(function() {
                    var el = document.getElementById('qunit-testresult');
                    var total = parseInt(el.getElementsByClassName('total')[0].innerHTML, 10);
                    return (el && el.innerText.match('completed'))
                        ? (total > 0)
                        : false;
                });
            },
            function() {
                var failed = page.evaluate(function() {
                    try {
                        var el = document.getElementById('qunit-testresult');
                        var total = parseInt(el.getElementsByClassName('total')[0].innerHTML, 10);
                        var failed = parseInt(el.getElementsByClassName('failed')[0].innerHTML, 10);
                        return failed;
                    }
                    catch (e) {
                        return 1;   // Signal that *something* went wrong.
                    }
                });
                k(failed);
            });
    });
}

/**
 * Sets up logging for QUnit use on the console.  Evaluated within the
 * page AFTER QUnit has loaded.
 */
function qUnitBoilerplate() {
    var messages = "";

    QUnit.stop();       // Stop everything and wait for tests to load.  QUnit.start() will be called once we inject the tests.

    QUnit.moduleStart(function(dets) {
        console.log("================================================================================");
        console.log(" Module: " + dets.name);
        console.log("================================================================================");
    });

    QUnit.testDone(function(d) {
        console.log("[ " + (d.module ? d.module + "::" : "") + d.name + " ]");
        console.log(messages);
        messages = "";
        console.log("    " + ((d.failed > 0) ? "FAILED" : "PASSED") + " (" + d.duration + "ms)");
    });

    QUnit.moduleDone(function(d) {
        console.log("================================================================================");
        console.log(" Results:")
        console.log("    total : " + d.total);
        console.log("    failed: " + d.failed);
        console.log("================================================================================");
        console.log("");
    });

    QUnit.log(function(d) {
        var line = d.message + (d.source ? " (" + d.source + ")" : "") + "\n";
        messages += "    " + line;
    });
}

/**
 * Wait for a condition, then call the callback.
 */
function waitFor(test, callback, maxtime) {
    maxtime = maxtime || 30001;
    var start = new Date().getTime();
    var interval = setInterval(function() {
        var elapsed = new Date().getTime() - start;
        var go = test();
        if (go || (elapsed > maxtime)) {
            clearInterval(interval);
        }
        if (go) {
            callback();
        }
    }, 250);
}

function getTestJs(file) {
    return file.replace(".js", "-test.js");
}