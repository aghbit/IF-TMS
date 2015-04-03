mainApp.controller('MainController', ['$scope', 'ngDialog','SessionService','$location', function($scope, ngDialog,SessionService,$location) {
    $scope.signOut = function(){
        SessionService.token = " ";
        SessionService.isLoggedIn = false;
        $location.url("")
    }
}]);