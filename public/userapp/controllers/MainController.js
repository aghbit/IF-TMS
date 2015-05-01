mainApp.controller('MainController', ['$scope', '$rootScope', 'ngDialog', 'SessionService', '$location', function ($scope, $rootScope, ngDialog, SessionService, $location) {

    $scope.loggedIn = SessionService.isLoggedIn;

    $rootScope.$on('LOGIN_EVENT', function (event) {
        $scope.loggedIn = SessionService.isLoggedIn;
    });

    $scope.signOut = function(){
        SessionService.token = " ";
        SessionService.isLoggedIn = false;
        notification("You have been successfully logged out.", 4000, true);
        $location.url("");
        $scope.loggedIn = SessionService.isLoggedIn;
    }

    $scope.loginPopUp = function () {
        ngDialog.open({
            template: '/assets/userapp/partials/login/login.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
}]);