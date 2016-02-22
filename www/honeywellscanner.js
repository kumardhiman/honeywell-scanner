//var exec = require("cordova/exec");
//function honeywellScanner () {
//};
//honeywellScanner.prototype.initialize = function(onSuccess, onError) {
//    exec(onSuccess, onError, 'HoneywellScanner', 'initialize', []);
//};
//honeywellScanner.prototype.trigger = function(onSuccess, onError) {
//    exec(onSuccess, onError, 'HoneywellScanner', 'trigger', []);
//};
//var scanner = new honeywellScanner();
//module.exports = scanner;
var cordova = require('cordova');
var exec = require('cordova/exec');

var HoneywellScannerPlugin = function() {
    this.initialize = function(success_cb, error_cb){
    exec(success_cb, error_cb, "HoneywellScanner", "initialize", []);
  };
  //this.scan = function(success_cb, error_cb){
    //exec(success_cb, error_cb, "HoneywellScanner", "initialize", []);
  //};
this.scan  = function(success_cb, error_cb){
    exec(success_cb, error_cb, "HoneywellScanner", "trigger", []);
  };
  //this.startListen = function(success_cb, error_cb){
//    exec(success_cb, error_cb, "BarcodeScannerPlugin", "start", []);
//  };

};

var honeywellScannerPlugin = new HoneywellScannerPlugin();
module.exports = honeywellScannerPlugin;
