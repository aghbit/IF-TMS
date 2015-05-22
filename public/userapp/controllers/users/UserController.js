/*
Created by ludwik
on i dont remember
thanks to Piotr for
RegisterController

 */

mainApp.controller('UserController', ['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService){
    $scope.message = "You have to be logged in to see user details!"
        $http.get('api/users/'+SessionService.token.substr(0,24), {}).
        success(function(data, status, headers, config) {
                $scope.user = data;

        }).error(function(data, status, headers, config, statusText) {

        });

    $scope.checkLoginAvailability = function(){
        if($scope.login == null ||( ($scope.login.length>=5 && $scope.login.length<=20)) ){
            $http.get('/api/users/login/'+$scope.login).
                success(function (data, status, headers, config) {
                    $scope.loginClass = "invalid";
                }).
                error(function (data, status, headers, config){
                    if(status==404){
                        $scope.loginClass ="";
                    }else{
                        $scope.closeThisDialog();
                        $location.url(status + "/" + "Internal Server Error. ")
                        notification("Sorry. Error occurred.", 4000, false);
                    }
                })
        }else{
            $scope.loginClass = "invalid";
        }
    };
    $scope.validateNameField= function(){
        if(($scope.name != null) &&( $scope.name.length < 3 || $scope.name.length > 20)){
            $scope.nameClass = "invalid";
        }else {
            $scope.nameClass = "";
        }
    };


    $scope.validateMailField = function() {
        //regex for email address RFC 5322
        var pattern= /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
        if(!pattern.test($scope.mail)){
            if($scope.mail==null)
                $scope.mailClass = "";
            else
                $scope.mailClass = "invalid";

        }else {
            $scope.mailClass = "";
        }

    };
    $scope.validatePhoneField = function() {
        //regex for email address RFC 5322
        var pattern= /^[0-9]{9}$/
        if(!pattern.test($scope.phone)){
            if($scope.phone==null)
                $scope.phoneClass = "";
            else
                $scope.phoneClass = "invalid";

        }else {
            $scope.phoneClass = "";
        }

    };

    $scope.checkForm = function() {
        return $scope.mailClass == "" &&
            $scope.nameClass == "" &&
            $scope.passwordClass == "" &&
            $scope.mailClass == "" &&
            $scope.phoneClass == "" &&
            $scope.loginClass == ""
    };

    $scope.submit = function(){
//        if($scope.checkForm()) {
//        //    $http.put
////            $http.post('/api/users', {
////                "name": $scope.name,
////                "login": $scope.login,
////                "password": $scope.password,
////                "phone": $scope.phone,
////                "mail": $scope.mail
//            }).
//                success(function (data, status, headers, config) {
//                    $scope.closeThisDialog();
//                    $location.url("/login");
//                    notification("You have successfully edited your account.", 4000, true);
//                }).
//                error(function (data, status, headers, config) {
//                    $scope.closeThisDialog();
//                    $location.url(status + "/" + data);
//                    notification("Sorry. Error occurred.", 4000, false);
//                });
//        }else {
//            notification("Sorry. You have typed wrong data.", 4000, false);
//        }
    };


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
    $scope.deletePopUp = function () {
        //  $scope.closeThisDialog();
        ngDialog.open({
            template: '/assets/userapp/partials/users/delete.html',
            className: 'ngdialog-theme-plain',
            closeByDocument: true
        });
    }


}]);

//['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService)