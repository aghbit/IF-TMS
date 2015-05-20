mainApp.controller('MainController', ['$scope','$http', '$rootScope', 'ngDialog', 'SessionService', '$location', function ($scope,$http, $rootScope, ngDialog, SessionService, $location) {

    $scope.loggedIn = SessionService.isLoggedIn;

    $rootScope.$on('LOGIN_EVENT', function (event) {
        $scope.loggedIn = SessionService.isLoggedIn;
    });

    $scope.signOut = function(){
        SessionService.token = " ";
        SessionService.isLoggedIn = false;
        $.removeCookie('tms-token');
        notification("You have been successfully logged out.", 4000, true);
        $location.url("");
        $scope.loggedIn = SessionService.isLoggedIn;
    };

    $scope.loginPopUp = function () {
        ngDialog.open({
            template: '/assets/userapp/partials/login/login.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    };
    $scope.checkCookie = function () {
        var cookie = $.cookie('tms-token');
        console.log(cookie);
        if(cookie !== undefined){
            SessionService.token = cookie;
            $http.get('api/tournaments', {}).//check server's response.
                success(function(data, status, headers, config) {
                    SessionService.isLoggedIn = true;
                    $rootScope.$emit("LOGIN_EVENT", true);
                    notification("Session kept.", 4000, true);
                })
                .error(function () {
                    SessionService.isLoggedIn = false;
                    notification("Session expired.", 4000, true);
                });
        }
    };
    $scope.checkCookie();
}]);