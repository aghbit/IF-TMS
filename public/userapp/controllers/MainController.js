mainApp.controller('MainController', ['$scope', '$rootScope', 'ngDialog', 'SessionService', '$location','$cookieStore' , function ($scope, $rootScope, ngDialog, SessionService, $location, $cookieStore) {

    $scope.loggedIn = SessionService.isLoggedIn;

    $rootScope.$on('LOGIN_EVENT', function (event) {
        $scope.loggedIn = SessionService.isLoggedIn;
    });

    $scope.signOut = function(){
        SessionService.token = " ";
        SessionService.isLoggedIn = false;
        $cookieStore.remove("tms-token");
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
        var cookie = $cookieStore.get('tms-token');
        console.log(cookie);
        if(cookie !== undefined){
           console.log("ok");
            SessionService.token = cookie;
            SessionService.isLoggedIn = true;
            $rootScope.$emit("LOGIN_EVENT", true);
            notification("Session kept.", 4000, true);
        }
    };
    $scope.checkCookie();
}]);