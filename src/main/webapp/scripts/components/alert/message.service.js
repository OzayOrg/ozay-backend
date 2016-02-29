'use strict';

angular.module('ozayApp')
    .service('MessageService', function Principal($q, $stateParams, Account) {
        var _successMessage,
            _errorMessage = undefined;

        return {
            setSuccessMessage: function(message){
                _successMessage = message;
            },
            setErrorMessage: function(message){
                _errorMessage = message;
            },
            getSuccessMessage: function () {
                var message = _successMessage;
                _successMessage = undefined;
                return message;
            },
            getErrorMessage: function () {
                var message = _errorMessage;
                _errorMessage = undefined;
                return message;
            },
            clear:function(){
                _successMessage = undefined;
                _errorMessage = undefined;
            }


        };
    });
