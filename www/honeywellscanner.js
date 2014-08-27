var exec = require("cordova/exec");

function honeywellScanner () {
};

honeywellScanner.prototype.initialize = function(onSuccess, onError) {
    exec(onSuccess, onError, 'HoneywellScanner', 'initialize', []);
};

honeywellScanner.prototype.trigger = function(onSuccess, onError) {
    exec(onSuccess, onError, 'HoneywellScanner', 'trigger', []);
};
var scanner = new honeywellScanner();
module.exports = scanner;
