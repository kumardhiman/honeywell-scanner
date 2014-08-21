var exec = require("cordova/exec");

function honeywellScanner () {
};

honeywellScanner.prototype.trigger = function() {
    exec(null, null, 'HoneywellScanner', 'trigger', []);
};
var scanner = new honeywellScanner();
module.exports = scanner;
