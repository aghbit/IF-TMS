var myApp = angular.module('myApp', ['ngRoute']);

myApp.config(['$routeProvider', function($routeProvider){
    $routeProvider
        .when('/hello',{
            templateUrl : '/assets/javascripts/partials/view.html',
            controller : 'HelloController'
        })
        }]);