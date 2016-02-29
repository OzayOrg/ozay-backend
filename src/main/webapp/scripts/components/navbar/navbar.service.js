'use strict';

angular.module('ozayApp')
    .service('MenuSearchState', function() {
        var searchBtnState = false;
        this.getState = function(){
            return searchBtnState;
        }
        this.changeState = function(){
            if(searchBtnState == false){
                searchBtnState = true;
            } else {
                searchBtnState = false;
            }
        }
    });

