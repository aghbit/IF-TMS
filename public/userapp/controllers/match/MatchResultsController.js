/**
 * Created by Piotr on 2015-05-25.
 */

mainApp.controller('MatchResultsController', ['$scope', '$http', function($scope, $http) {


    $scope.match = {};
    $scope.match._id = $scope.ngDialogData.id;
    $scope.match.host = {
        "_id": $scope.ngDialogData.team1._id,
        "name": $scope.ngDialogData.team1.name
    };
    $scope.match.guest = {
        "_id": $scope.ngDialogData.team2._id,
        "name": $scope.ngDialogData.team2.name
    };
    $scope.match.sets = [];



    $scope.submit = function() {
        console.log($scope.match);
        $scope.submit = function(){
            $http.post('/api/score', {"match" : $scope.match}).
                success(function(data, status, headers, config) {
                    notification("Score has been set.", 4000, true);
                    history.back();
                }).
                error(function(data, status, headers, config) {
                    notification("Sorry. Error occurred.", 4000, false);
                    history.back();
                });
        };
    }

}]);
