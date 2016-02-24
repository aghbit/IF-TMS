mainApp.controller('TournamentsCreateController', ['$scope', '$http', '$location', 'SessionService', 'ErrorMessageService', '$compile',
    function ($scope, $http, $location, SessionService, ErrorMessageService, $compile) {
    //important! it loads js for datePicker.
    angular.element(document).ready(function(){
        $('ul.tabs').tabs();
        $('select').material_select();
        $('.datepicker').pickadate({
            closeOnSelect: true,
            format:"yyyy-mm-dd"
        });
        $('.timepicker').pickatime({
            format:'HH:i:00',
            min: [6, 00], 
            max:[23,00],
            closeOnSelect: true
        })
    })
    $scope.isLoggedIn = SessionService.isLoggedIn;
    $scope.submit = function(){

        if($scope.registerQueue.currentState == 3) {

            $scope.validateDates();

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
                properties: {
                    "description": {
                        "name": $scope.tournamentName,
                        "place": $scope.tournamentPlace,
                        "description": $scope.tournamentDescription
                    },
                    "term": {
                        "enrollDeadline": enrollDeadline + " " + enrollDeadlineTime,
                        "begin": begin + " " + beginTime,
                        "end": end + " " + endTime,
                        "extraBegin": extraBegin + " " + extraBeginTime,
                        "extraEnd": extraEnd + " " + extraEndTime
                    },
                    "settings": {
                        "numberOfPitches": $scope.numberOfPitches, "numberOfTeams": $scope.numberOfTeams,
                        "canEnroll": false, "level": $scope.level
                    }
                }, strategy: $scope.strategy, discipline: $scope.discipline
            }).success(function () {
                notification("Tournament created!", 4000, true)
                $location.path("tournaments/myTournaments")
            }).error(function (data, status) {
                ErrorMessageService.content = data;
                $location.url(status + "/");
                notification("Wrong data, tournament can't be created!", 4000, false)
            })
        } else {
            $scope.registerQueue.step_activate($scope.registerQueue.currentState+1);
            $('ul.tabs').tabs('select_tab', 'step'+$scope.registerQueue.currentState);
        }

    };
    $scope.registerQueue = {
        that: $scope.registerQueue,
        currentState: 0,
        class_active: ['active-progress', null, null, null, null],
        submitCaption: "Next",


        step_activate: function (n) {
            this.class_active[this.currentState] = null;
            this.currentState = (n);
            this.class_active[this.currentState] = "active-progress";
            if(n==3) this.submitCaption = "Create";
            else this.submitCaption = "Next"
        },
        test: function () {
            $scope.registerQueue.active[0] = "carousel-element-active";
        },
        go_to_next: function() {
            $scope.registerQueue.step_activate(this.currentState + 1);
        },
        go_to_prev: function() {
            $scope.registerQueue.step_activate(this.currentState - 1);
        }
    };

        $scope.validateLength = function(value, min, max) {
            if(value == null) {
                return "This field is required.";
            } else if(value.length < min) {
                return "At least "+min+" characters required.";
            } else if(value.length > max) {
                return "No more than "+max+" characters allowed.";
            } else {
                return "";
            }
        };

    $scope.validatePattern = function(value, pattern) {
        var regex = new RegExp(pattern, 'g');
        if(!regex.test(value)){
            return "invalid";
        }else {
            return "";
        }
    };


    $scope.validateEnrollmentTimeBeforeBegin = function() {

        var enrollDeadline = $('#enrollDeadline')[0].value;
        var enrollDeadlineTime = $('#enrollDeadlineTime')[0].value;
        var begin = $('#begin')[0].value;
        var beginTime = $('#beginTime')[0].value;

        if(begin != null && enrollDeadline != null && Date.parse(enrollDeadline)>Date.parse(begin)) {
            $scope.enrollDeadlineClass = "invalid";
            $scope.beginClass = "invalid";
        } else if(begin != null && enrollDeadline != null && beginTime != "" && enrollDeadlineTime != ""
                        && enrollDeadline==begin && Date.parse("01/01/1970 "+enrollDeadlineTime)>=Date.parse("01/01/1970 "+beginTime)) {
            $scope.enrollDeadlineClass = "";
            $scope.beginClass = "";
            $scope.enrollDeadlineTimeClass = "invalid";
            $scope.beginTimeClass = "invalid";
        } else {
            $scope.enrollDeadlineTimeClass = "";
            $scope.beginTimeClass = "";
        }
    };

    $scope.validateBeginTimeBeforeEnd = function() {

        var end = $('#end')[0].value;
        var endTime = $('#endTime')[0].value;
        var begin = $('#begin')[0].value;
        var beginTime = $('#beginTime')[0].value;

        if(begin != null && end != null && Date.parse(begin)>Date.parse(end)) {
            $scope.endClass = "invalid";
        } else if(begin != null && end != null && beginTime != "" && endTime != ""
                        && end==begin && Date.parse("01/01/1970 "+beginTime)>=Date.parse("01/01/1970 "+endTime)) {
            $scope.endClass = "";
            $scope.endTimeClass = "invalid";
        } else {
            $scope.endTimeClass = "";
        }
    };

    $scope.validateExtraBeginTimeBeforeExtraEnd = function() {

        var extraEnd = $('#extraEnd')[0].value;
        var extraEndTime = $('#extraEndTime')[0].value;
        var extraBegin = $('#extraBegin')[0].value;
        var extraBeginTime = $('#extraBeginTime')[0].value;

        if(extraBegin != null && extraEnd != null && Date.parse(extraBegin)>Date.parse(extraEnd)) {
            $scope.extraEndClass = "invalid";
        } else if(extraBegin != null && extraEnd != null && extraBeginTime != "" && extraEndTime != ""
                        && extraEnd==extraBegin && Date.parse("01/01/1970 "+extraBeginTime)>=Date.parse("01/01/1970 "+extraEndTime)) {
            $scope.extraEndClass = "";
            $scope.extraEndTimeClass = "invalid";
        } else {
            $scope.extraEndTimeClass = "";
        }
    };

    $scope.validateBeginTimeBeforeExtraBegin = function() {

        var begin = $('#begin')[0].value;
        var beginTime = $('#beginTime')[0].value;
        var extraBegin = $('#extraBegin')[0].value;
        var extraBeginTime = $('#extraBeginTime')[0].value;

        if(extraBegin != null && begin != null && Date.parse(begin)>Date.parse(extraBegin)) {
            $scope.extraBeginClass = "invalid";
        } else if(extraBegin != null && begin != null && extraBeginTime != "" && beginTime != ""
                        && begin==extraBegin && Date.parse("01/01/1970 "+beginTime)>=Date.parse("01/01/1970 "+extraBeginTime)) {
            $scope.extraBeginClass = "";
            $scope.extraBeginTimeClass = "invalid";
        } else {
            $scope.extraBeginTimeClass = "";
        }
    };

    $scope.validateDates = function() {
        $scope.validateEnrollmentTimeBeforeBegin();
        $scope.validateBeginTimeBeforeEnd();
        $scope.validateExtraBeginTimeBeforeExtraEnd();
        $scope.validateBeginTimeBeforeExtraBegin();
    }


}]);