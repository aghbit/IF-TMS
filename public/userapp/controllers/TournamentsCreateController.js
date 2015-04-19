mainApp.controller('TournamentsCreateController', ['$scope',  '$http', '$location', 'SessionService', function ($scope, $http, $location, SessionService) {
    //important! it loads js for datePicker.
    angular.element(document).ready(function(){
        $('select').material_select();
        $('.datepicker').pickadate({
            closeOnSelect: true,
            format:"yyyy-mm-dd"
        });
        $('.timepicker').pickatime({
            format:'HH:i:00',
            min:[6,00],
            max:[23,00],
            closeOnSelect: true
        })
    })
    $scope.isLoggedIn = SessionService.isLoggedIn
    $scope.submit = function(){
        var enrollDeadline = $('#enrollDeadline')[0].value;
        var enrollDeadlineTime = $('#enrollDeadlineTime')[0].value;
        var begin = $('#begin')[0].value;
        var beginTime = $('#beginTime')[0].value;
        var end = $('#end')[0].value;
        var endTime = $('#endTime')[0].value;
        var extraBegin = $('#extraBegin')[0].value;
        var extraBeginTime = $('#extraBeginTime')[0].value;
        var extraEnd = $('#extraEnd')[0].value;
        var extraEndTime = $('#extraEndTime')[0].value;
        $http.post('/api/tournaments', {
            "description":{"name":$scope.tournamentName, "place":$scope.tournamentPlace, "description":$scope.tournamentDescription},
            "term":{"enrollDeadline": enrollDeadline+" "+enrollDeadlineTime,
                    "begin": begin+" "+beginTime,
                    "end": end+" "+endTime,
                    "extraBegin": extraBegin+" "+extraBeginTime,
                    "extraEnd": extraEnd+" "+extraEndTime},
            "settings":{"numberOfPitches":$scope.numberOfPitches, "numberOfTeams":$scope.numberOfTeams,
                        "canEnroll": true, "level":$scope.level, "discipline":$scope.discipline}
        }).success(function(){
            notification("Tournament created!", 4000, true)
            $location.path("tournaments/myTournaments")
        }).error(function(){
            notification("Wrong data, tournament can't be created!", 4000, false)
        })

    }

}]);