/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', '$http', '$location', function($scope, $http, $location) {

    $scope.checkPasswordsEquality = function(){
        if($scope.password != $scope.password2){
            $scope.password2BorderBottom = "1px solid #f42334"
            $scope.password2BoxShadow = "0 1px 0 0 #f42334"
        }else {
            $scope.password2BorderBottom = ""
            $scope.password2BoxShadow = ""
        }
    };

    $scope.checkLoginAvailability = function(){
        if($scope.login != ""){
            $http.get('/api/users/login/'+$scope.login).
                success(function (data, status, headers, config) {
                    $scope.loginBorderBottom = "1px solid #f42334"
                    $scope.loginBoxShadow = "0 1px 0 0 #f42334"
                }).
                error(function (data, status, headers, config){
                    $scope.loginBorderBottom = ""
                    $scope.loginBoxShadow = ""
                })
        }
    }

    function checkForm($scope) {
        return $scope.password == $scope.password2
    }

    $scope.submit = function(){
        if(checkForm($scope)) {
            $http.post('/api/users', {
                "name": $scope.name,
                "login": $scope.login,
                "password": $scope.password,
                "phone": $scope.phone,
                "mail": $scope.mail
            }).
                success(function (data, status, headers, config) {
                    $scope.closeThisDialog();
                    $location.url("/login");
                    notification("You have been successfully registered. You may now log in.", 4000, true);
                }).
                error(function (data, status, headers, config) {
                    alert(status);
                    alert(data);
                    notification("Sorry. User exists or error occurred.", 4000, false);
                });
        }else {
            notification("Sorry. You have typed wrong data.", 4000, false);
        }
    };
}]);
