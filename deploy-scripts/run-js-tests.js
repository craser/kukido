(function main() {
    try {
        var workDir = phantom.args[0];
        var baseDir = phantom.args[1];
        var oneBanana = baseDir + "/onebanana.js";
        var testDir = baseDir + "/tests";
        var fs = require('fs');
        var files = fs.list(workDir);
        var failed = 0;

        files.forEach(function(file) {
            if (!file.match(/\.js$/)) return 0; // Non-js file.
            var workJs = workDir + "/" + file;
            var testJs = testDir + "/" + getTestJs(file);
            if (!fs.exists(testJs)) return 0; // No tests to run.
            failed += runTests(fs, oneBanana, workJs, testJs);            
        });
        phantom.exit(failed);
    }
    catch (e) {
        console.log(e);
        phantom.exit(1);
    }
})();

function runTests(fs, oneBanana, workJs, testJs) {
    eval(fs.read(oneBanana));     // Initialize OneBanana framework.
    eval(fs.read(workJs));        // Set up the thing we want to test.
    var failed = eval(fs.read(testJs));             // Test the thing.
    return failed;
}

function getTestJs(file) {
    return file.replace(".js", "-test.js");
}