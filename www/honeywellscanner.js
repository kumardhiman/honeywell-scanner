var cordova = require('cordova');
var exec = require('cordova/exec');

var HoneywellScannerPlugin = function() {
    this.initialize = function(success_cb, error_cb){
    exec(success_cb, error_cb, "HoneywellScanner", "initialize", []);
  };
this.scan  = function(success_cb, error_cb){
    exec(success_cb, error_cb, "HoneywellScanner", "trigger", []);
  };
};

var honeywellScannerPlugin = new HoneywellScannerPlugin();
module.exports = honeywellScannerPlugin;
