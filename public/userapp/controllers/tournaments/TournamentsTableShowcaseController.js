/**
 * Created by szymek on 08.03.15.
 */

mainApp.controller('TournamentsTableShowcaseController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {

        $http.get('/api/tournaments/' + $stateParams.id + "/table").
            success(function(data, status, headers, config) {
                console.log(data);
                $scope.rounds = data.rounds;
                $('.collapsible').collapsible({
                    accordion : false
                });
            }).error(function(data, status, headers, config, statusText) {
                $scope.message = "";
                alert(status + " " + data);
            });



        $scope.rotateArrow = function(id){
            $("#list-icon"+id).toggleClass("rotate-clockwise");
            if($scope.openedTournamentItem !== undefined && $scope.openedTournamentItem!== id){
                $("#list-icon"+$scope.openedTournamentItem).toggleClass("rotate-clockwise");
            }
            if($scope.openedTournamentItem === id){
                $scope.openedTournamentItem = undefined;
            }
            else{
                $scope.openedTournamentItem = id;
            }
        }
    }]);