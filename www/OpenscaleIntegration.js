var exec = require('cordova/exec');

exports.getUsers = function (arg0, success, error) {
    exec(success, error, 'OpenscaleIntegration', 'getUsers', [arg0]);
};

exports.getUserMeasurements = function(arg0, success, error) {
    exec(success, error, 'OpenscaleIntegration', 'getUserMeasurements', [arg0]);
};
