/**
 * Created by Piotr on 2015-05-25.
 */

mainApp.controller('MatchResultsController', ['$scope', '$http', '$state', function($scope, $http, $state) {


    $scope.match = {};
    $scope.match._id = $scope.ngDialogData.match.id;
    $scope.match.host = {
        "_id": $scope.ngDialogData.match.team1._id,
        "name": $scope.ngDialogData.match.team1.name
    };
    $scope.match.guest = {
        "_id": $scope.ngDialogData.match.team2._id,
        "name": $scope.ngDialogData.match.team2.name
    };
    $scope.match.sets = [];
    $scope.discipline = $scope.ngDialogData.match.discipline;

    $scope.actualScale = $scope.ngDialogData.display.actualScale;
    $scope.x = $scope.ngDialogData.display.x;
    $scope.y = $scope.ngDialogData.display.y;



    $scope.submit = function() {
        console.log($scope.match);
        for(var i=0; i<$scope.match.sets.length; i++) {
            $scope.match.sets[i].type = $scope.discipline + "Set"
        }
        //should be changed. In tournament tree from rest api should be variable with sets number.
        if($scope.discipline == "Volleyball" && $scope.match.sets.length == 5){
            $scope.match.sets[4].type = $scope.discipline + "TieBreak"
        }else if($scope.discipline == "BeachVolleyball" && $scope.match.sets.length == 3) {
            $scope.match.sets[2].type = $scope.discipline + "TieBreak";
            $scope.match.sets = $scope.match.sets.slice(0, 3)
        }
        var url = '/api/tournaments/' + $scope.ngDialogData.tournamentID + '/match/' + $scope.match._id;
            $http.post(url, $scope.match).
                success(function(data, status, headers, config) {
                    notification("Score has been set.", 4000, true);
                    $scope.closeThisDialog();
                    $state.go($state.current, {actualScale: $scope.actualScale, x: $scope.x, y: $scope.y}, {reload: true});
                }).
                error(function(data, status, headers, config) {
                    $scope.closeThisDialog();
                    alert(data, status, headers);
                    notification("Sorry. Error occurred.", 4000, false);
                });

    }

}]);
