var exec = require("cordova/exec");

function honeywellScanner () {
};

honeywellScanner.prototype.trigger = function() {
    exec(null, null, 'HoneywellScanner', 'trigger', []);
};

module.exports = honeywellScanner;
