/**
 * Created by Piotr on 2015-02-04.
 */

mainApp.controller('RegisterController', ['$scope', '$http', function($scope, $http) {

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
                    $scope.closeThisDialog()
                }).
                error(function (data, status, headers, config) {
                    window.alert("User exists or error occurred!")
                });
        }else {
            alert("You type wrong data!")
        }
    };
}]);
