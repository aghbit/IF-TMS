/**
 * Created by Piotr on 2015-02-04.
 */
mainApp.controller('LoginController', ['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService','$cookieStore', function ($scope, $rootScope, $http, $location, ngDialog, SessionService, $cookieStore) {
    $scope.credentials = {
        login : "",
        password : ""
    };
    $scope.submit = function(){
        $http.post('/api/login', {"login" : $scope.credentials.login, "password": $scope.credentials.password}).
            success(function(data, status, headers, config) {
                SessionService.token = data;
                SessionService.isLoggedIn = true;
                $rootScope.$emit("LOGIN_EVENT", true);
                notification("You have been successfully logged in.", 4000, true);
                $scope.closeThisDialog();
                $cookieStore.put('tms-token',SessionService.token);

            }).
            error(function(data, status, headers, config) {
                notification("Sorry. Wrong credentials.", 4000, false);
            });
    };

    $scope.registerPopUp = function () {
        $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/register/register.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }
}]);