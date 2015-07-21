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
    $scope.match.tournamentID = $scope.ngDialogData.tournamentID;
    $scope.match.guest = {
        "_id": $scope.ngDialogData.team2._id,
        "name": $scope.ngDialogData.team2.name
    };
    $scope.match.sets = [];



    $scope.submit = function() {
        console.log($scope.match);
        var url = '/api/tournaments/' + $scope.match.tournamentID + '/match/' + $scope.match._id;
        console.log(url);
            $http.post(url, {"match" : $scope.match}).
                success(function(data, status, headers, config) {
                    notification("Score has been set.", 4000, true);
                    history.back();
                }).
                error(function(data, status, headers, config) {
                    notification("Sorry. Error occurred.", 4000, false);
                    history.back();
                });

    }

}]);
