/**
 * Created by Piotr on 2015-05-20.
 */
mainApp.controller('TournamentTableController',  ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {
        

    $scope.rounds = {rounds:[
        {round: 5,
            matches:[
                {"_id":25,"host":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"guest":{"_id":"556c75ef44ae68697e2048a9","name":"team 11"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":26,"host":{"_id":"556c75ef44ae68697e2048a3","name":"team 5"},"guest":{"_id":"556c75ef44ae68697e2048ac","name":"team 14"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":27,"host":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"guest":{"_id":"556c75ef44ae68697e2048a1","name":"team 3"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":28,"host":{"_id":"556c75ef44ae68697e2048aa","name":"team 12"},"guest":{"_id":"556c75ef44ae68697e2048a0","name":"team 2"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]},
        {round: 4,
            matches:[
                {"_id":13,"host":{"_id":"556c75ef44ae68697e2048ad","name":"team 15"},"guest":{"_id":"556c75ef44ae68697e2048aa","name":"team 12"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":14,"host":{"_id":"556c75ef44ae68697e2048a4","name":"team 6"},"guest":{"_id":"556c75ef44ae68697e2048a0","name":"team 2"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":15,"host":{"_id":"556c75ef44ae68697e20489f","name":"team 1"},"guest":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":16,"host":{"_id":"556c75ef44ae68697e2048a8","name":"team 10"},"guest":{"_id":"556c75ef44ae68697e2048a1","name":"team 3"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":17,"host":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"guest":{"_id":"556c75ef44ae68697e2048a6","name":"team 8"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":18,"host":{"_id":"556c75ef44ae68697e2048a3","name":"team 5"},"guest":{"_id":"556c75ef44ae68697e20489e","name":"team 0"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":19,"host":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"guest":{"_id":"556c75ef44ae68697e2048a8","name":"team 10"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":20,"host":{"_id":"556c75ef44ae68697e2048aa","name":"team 12"},"guest":{"_id":"556c75ef44ae68697e2048a4","name":"team 6"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":21,"host":{"_id":"556c75ef44ae68697e2048a2","name":"team 4"},"guest":{"_id":"556c75ef44ae68697e2048a3","name":"team 5"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":22,"host":{"_id":"556c75ef44ae68697e20489e","name":"team 0"},"guest":{"_id":"556c75ef44ae68697e2048ac","name":"team 14"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":23,"host":{"_id":"556c75ef44ae68697e2048ab","name":"team 13"},"guest":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":24,"host":{"_id":"556c75ef44ae68697e2048a6","name":"team 8"},"guest":{"_id":"556c75ef44ae68697e2048a9","name":"team 11"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]},
        {round: 3,
            matches:[
                {"_id":7,"host":{"_id":"556c75ef44ae68697e2048ad","name":"team 15"},"guest":{"_id":"556c75ef44ae68697e2048a4","name":"team 6"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":8,"host":{"_id":"556c75ef44ae68697e20489f","name":"team 1"},"guest":{"_id":"556c75ef44ae68697e2048a8","name":"team 10"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":9,"host":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"guest":{"_id":"556c75ef44ae68697e2048a3","name":"team 5"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":10,"host":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"guest":{"_id":"556c75ef44ae68697e2048aa","name":"team 12"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":11,"host":{"_id":"556c75ef44ae68697e2048a2","name":"team 4"},"guest":{"_id":"556c75ef44ae68697e20489e","name":"team 0"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":12,"host":{"_id":"556c75ef44ae68697e2048ab","name":"team 13"},"guest":{"_id":"556c75ef44ae68697e2048a6","name":"team 8"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]},
        {round: 2,
            matches:[
                {"_id":3,"host":{"_id":"556c75ef44ae68697e2048ad","name":"team 15"},"guest":{"_id":"556c75ef44ae68697e20489f","name":"team 1"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":4,"host":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"guest":{"_id":"556c75ef44ae68697e2048ab","name":"team 13"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":5,"host":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"guest":{"_id":"556c75ef44ae68697e20489f","name":"team 1"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":6,"host":{"_id":"556c75ef44ae68697e2048a2","name":"team 4"},"guest":{"_id":"556c75ef44ae68697e2048ab","name":"team 13"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]},
        {round: 1,
            matches:[
                {"_id":1,"host":{"_id":"556c75ef44ae68697e2048ad","name":"team 15"},"guest":{"_id":"556c75ef44ae68697e2048a5","name":"team 7"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]},
                {"_id":2,"host":{"_id":"556c75ef44ae68697e2048a2","name":"team 4"},"guest":{"_id":"556c75ef44ae68697e2048a7","name":"team 9"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]},
        {round: 0,
            matches:[
                {"_id":0,"host":{"_id":"556c75ef44ae68697e2048ad","name":"team 15"},"guest":{"_id":"556c75ef44ae68697e2048a2","name":"team 4"},"sets":[{"1":{"host":21,"guest":18}},{"2":{"host":21,"guest":19}},{"3":{"host":null,"guest":null}}]}]} ]};
    $scope.begins = [-1, 0, 0, 0, 0, 0, 0];
    $scope.length = [0, 3, 1, 0, 0, 0, 0];
    $scope.ends = [4, 8, 4, 3, 0, 0 ,0 ];

        console.log($scope.rounds);
    $scope.getTournament = function(){
        $http.get('api/tournaments/' + $stateParams.id, {}).
            success(function(data, status, headers, config) {
                $scope.tournament = data;

            }).error(function(data, status, headers, config, statusText) {

            });
    };
    $scope.getTeams = function() {
        $http.get('api/tournaments/' + $stateParams.id + "/teams", {}).
            success(function (data, status, headers, config) {
                $scope.teams = data;
                $scope.treeHeight = Math.ceil(Math.log($scope.teams.length)/Math.LN2);
                $scope.leavesNumber = Math.pow(2, $scope.treeHeight);
                console.log($scope.treeHeight);
                console.log($scope.leavesNumber);
                $('.collapsible').collapsible({
                    accordion: false
                });
            }).error(function (data, status, headers, config, statusText) {

            });
    };


   $scope.openScorePopup = function(matchID, team1, team2) {
       $scope.match = {};
       $scope.match.id = matchID;
       $scope.match.team1 = team1;
       $scope.match.team2 = team2;
       ngDialog.open({
           template: '/assets/userapp/partials/match/matchScore.html',
           className: 'ngdialog-theme-plain',
           controller: 'MatchResultsController',
           data: $scope.match,
           closeByDocument: true
       });
   };

    $scope.range = function(min, max, step){
            step = step || 1;
            var input = [];
            for (var i = min; i <= max; i += step) input.push(i);
            return input;
        };

    $scope.secondPow = function(n) {
        return Math.pow(2, n);
    };

        $scope.getTournament();
        $scope.getTeams();

        var margin = {top: 30, right: 10, bottom: 10, left: 10},
            width = 960 - margin.left - margin.right,
            halfWidth = width / 2,
            height = 500 - margin.top - margin.bottom,
            i = 0,
            duration = 500,
            root;

        var getChildren = function(d){
                var a = [];
                if(d.winners) for(var i = 0; i < d.winners.length; i++){
                    d.winners[i].isRight = false;
                    d.winners[i].parent = d;
                    a.push(d.winners[i]);
                }
                if(d.challengers) for(var i = 0; i < d.challengers.length; i++){
                    d.challengers[i].isRight = true;
                    d.challengers[i].parent = d;
                    a.push(d.challengers[i]);
                }
                return a.length?a:null;
            }
            ;

        var tree = d3.layout.tree()
                .size([height, width])
            ;

        var diagonal = d3.svg.diagonal()
            .projection(function(d) { return [d.y, d.x]; });
        var elbow = function (d, i){
            var source = calcLeft(d.source);
            var target = calcLeft(d.target);
            var hy = (target.y-source.y)/2;
            if(d.isRight) hy = -hy;
            return "M" + source.y + "," + source.x
                + "H" + (source.y+hy)
                + "V" + target.x + "H" + target.y;
        };
        var connector = elbow;

        var calcLeft = function(d){
            var l = d.y;
            if(!d.isRight){
                l = d.y-halfWidth;
                l = halfWidth - l;
            }
            return {x : d.x, y : l};
        };

        var vis = d3.select("#chart").append("svg")
            .attr("width", width + margin.right + margin.left)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        d3.json("https://gist.githubusercontent.com/anonymous/65825490feb199e37112/raw/144d6e356b37c37265ed8e7b6d1dee3d65dd49d2/bracket", function(json) {
            root = json;
            root.x0 = height / 2;
            root.y0 = width / 2;

            var t1 = d3.layout.tree().size([height, halfWidth]).children(function(d){return d.winners;}),
                t2 = d3.layout.tree().size([height, halfWidth]).children(function(d){return d.challengers;});
            t1.nodes(root);
            t2.nodes(root);

            var rebuildChildren = function(node){
                node.children = getChildren(node);
                if(node.children) node.children.forEach(rebuildChildren);
            }
            rebuildChildren(root);
            root.isRight = false;
            update(root);
        });

        var toArray = function(item, arr){
            arr = arr || [];
            var i = 0, l = item.children?item.children.length:0;
            arr.push(item);
            for(; i < l; i++){
                toArray(item.children[i], arr);
            }
            return arr;
        };

        function update(source) {
            // Compute the new tree layout.
            var nodes = toArray(source);

            // Normalize for fixed-depth.
            nodes.forEach(function(d) { d.y = d.depth * 180 + halfWidth; });

            // Update the nodes…
            var node = vis.selectAll("g.node")
                .data(nodes, function(d) { return d.id || (d.id = ++i); });

            // Enter any new nodes at the parent's previous position.
            var nodeEnter = node.enter().append("g")
                .attr("class", "node")
                .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
                .on("click", click);

            nodeEnter.append("circle")
                .attr("r", 1e-6)
                .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

            nodeEnter.append("text")
                .attr("dy", function(d) { return d.isRight?14:-8;})
                .attr("text-anchor", "middle")
                .text(function(d) { return d.name; })
                .style("fill-opacity", 1e-6);

            // Transition nodes to their new position.
            var nodeUpdate = node.transition()
                    .duration(duration)
                    .attr("transform", function(d) { p = calcLeft(d); return "translate(" + p.y + "," + p.x + ")"; })
                ;

            nodeUpdate.select("circle")
                .attr("r", 4.5)
                .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

            nodeUpdate.select("text")
                .style("fill-opacity", 1);

            // Transition exiting nodes to the parent's new position.
            var nodeExit = node.exit().transition()
                .duration(duration)
                .attr("transform", function(d) { p = calcLeft(d.parent||source); return "translate(" + p.y + "," + p.x + ")"; })
                .remove();

            nodeExit.select("circle")
                .attr("r", 1e-6);

            nodeExit.select("text")
                .style("fill-opacity", 1e-6);

            // Update the links...
            var link = vis.selectAll("path.link")
                .data(tree.links(nodes), function(d) { return d.target.id; });

            // Enter any new links at the parent's previous position.
            link.enter().insert("path", "g")
                .attr("class", "link")
                .attr("d", function(d) {
                    var o = {x: source.x0, y: source.y0};
                    return connector({source: o, target: o});
                });

            // Transition links to their new position.
            link.transition()
                .duration(duration)
                .attr("d", connector);

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
                .duration(duration)
                .attr("d", function(d) {
                    var o = calcLeft(d.source||source);
                    if(d.source.isRight) o.y -= halfWidth - (d.target.y - d.source.y);
                    else o.y += halfWidth - (d.target.y - d.source.y);
                    return connector({source: o, target: o});
                })
                .remove();

            // Stash the old positions for transition.
            nodes.forEach(function(d) {
                var p = calcLeft(d);
                d.x0 = p.x;
                d.y0 = p.y;
            });

            // Toggle children on click.
            function click(d) {
                if (d.children) {
                    d._children = d.children;
                    d.children = null;
                } else {
                    d.children = d._children;
                    d._children = null;
                }
                update(source);
            }
        }

}]);