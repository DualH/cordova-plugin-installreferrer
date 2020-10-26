var cordova = require('cordova');
var pluginName = 'InstallReferrer';

/**
 * InstallReferrer plugin for Cordova
 * 
 * @constructor
 */
function InstallReferrer () {}

InstallReferrer.prototype.open = function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, pluginName, 'open', []);
}

InstallReferrer.prototype.getParams = function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, pluginName, 'getParams', []);
}

InstallReferrer.prototype.isOpen = function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, pluginName, 'isOpen', []);
}

InstallReferrer.prototype.close = function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, pluginName, 'close', []);
}

// Register the plugin
module.exports = new InstallReferrer();
