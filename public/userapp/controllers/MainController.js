mainApp.controller('MainController', ['$scope', 'ngDialog','SessionService','$location', function($scope, ngDialog,SessionService,$location) {

    $scope.loggedIn = SessionService.isLoggedIn;

    $scope.$on('LOGIN_EVENT', function(event) {
        $scope.loggedIn = SessionService.isLoggedIn;
    });

    $scope.signOut = function(){
        SessionService.token = " ";
        SessionService.isLoggedIn = false;
        notification("You have been successfully logged out.", 4000, true);
        $location.url("");
        $scope.loggedIn = SessionService.isLoggedIn;
    }
}]);