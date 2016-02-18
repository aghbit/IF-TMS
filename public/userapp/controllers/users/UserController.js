mainApp.controller('UserController', ['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService){
    $scope.message = "You have to be logged in to see user details!"
        $http.get('api/users/'+SessionService.token.substr(0,24), {}).
        success(function(data, status, headers, config) {
                $scope.user = data;

        }).error(function(data, status, headers, config, statusText) {

        });
    $scope.editPopUp = function () {
      //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/edit.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
    $scope.passwordChangePopUp = function () {
        //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/passwordChange.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }


}]);

//['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService)