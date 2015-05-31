/*
Created by ludwik who rocks

 */

mainApp.controller('PasswordUserController', ['$scope', '$rootScope', '$http', '$location', 'ngDialog', 'SessionService', function ($scope, $rootScope, $http, $location, ngDialog, SessionService){
    $scope.firstClass=""
    $scope.secondClass=""
    $scope.first =""
    $scope.second =""
    $scope.validateFirstField = function(){
        if($scope.first == null || $scope.first.length<5 || $scope.first.length > 20){
            $scope.firstClass = "Password should have more than 5 and less than 20 letters.";
        }else{
            $scope.firstClass = "";
        }
        if($scope.second!=""){
            if($scope.first==$scope.second)
                $scope.firstClass = ""
            else
                $scope.firstClass = "Password should be equal."
        }
    };
    $scope.validateSecondField = function(){
        if($scope.second == null || $scope.second.length<5 || $scope.second.length > 20){
            $scope.secondClass = "Password should have more than 5 and less than 20 letters.";
        }else{
            $scope.secondClass = "";
        }
        if($scope.first!=""){
            if($scope.first==$scope.second)
            $scope.secondClass = ""
            else
                $scope.secondClass = "Password should be equal."
        }

    };
    $scope.checkForm = function(){
        $scope.validateFirstField();
        $scope.validateSecondField();
        return $scope.firstClass=="" &&
               $scope.secondClass=="" &&
               $scope.first == $scope.second
    }
    $scope.submit = function(){
    if($scope.checkForm()){
        $http.put("api/users/"+  SessionService.token.substr(0, 24)+"/password",{
            "password" : $scope.second
        }).success(function (data, status, headers, config) {
            $scope.first =""
            $scope.second =""
            $scope.closeThisDialog();
            SessionService.token = data;
            $rootScope.user_id = SessionService.token.substr(0, 24);
            $.cookie('tms-token', SessionService.token, { expires: 1 });
            $location.url("/users");
            notification("Your password has been successfully changed.", 4000, true);
        }).error(function (data, status, headers, config) {
            $scope.closeThisDialog();
            $location.url(status + "/" + data);
            notification("Sorry. Error occurred.", 4000, false);
        });


    }else{
        notification("Sorry. Wrong data.", 4000, false);
    }
    }

}]);
