/**
 * Created by szymek on 08.03.15.
 */

mainApp.controller('TournamentsTreeShowcaseController', ['$scope', '$location', '$http', '$stateParams', 'SessionService', 'ngDialog',
    function ($scope, $location, $http, $stateParams, SessionService, ngDialog) {

        var container = document.getElementById('container'), dragging = false, previousmouse;
        container.x = 0; container.y = 0;

        window.onmousedown = function(e) { dragging = true; previousmouse = {x: e.pageX, y: e.pageY}; };

        window.onmouseup = function() { dragging = false; };

        window.ondragstart = function(e) { e.preventDefault(); };

        window.onmousemove = function(e) {
            if (dragging) {
                container.x += e.pageX - previousmouse.x; container.y += e.pageY - previousmouse.y;
                container.style.left = container.x + 'px'; container.style.top = container.y + 'px';
                previousmouse = {x: e.pageX, y: e.pageY};
            }
        };

        var margin = {top: 0, right: 0, bottom: 0, left: 0},
            width = null,
            halfWidth = null,
            height = null,
            i = 0,
            root;

        $scope.openScorePopup = function(matchID, team1, team2, discipline) {
            $scope.match = {};
            $scope.match.id = matchID;
            $scope.match.team1 = team1;
            $scope.match.team2 = team2;
            $scope.match.discipline = $scope.tournamentTree.discipline;
            $scope.match.tournamentID = $scope.tournament._id;
            ngDialog.open({
                template: '/assets/userapp/partials/match/matchScore.html',
                className: 'ngdialog-theme-plain',
                controller: 'MatchResultsController',
                data: $scope.match,
                closeByDocument: true
            });
        };

        $scope.getTournament = function(){
            $http.get('api/tournaments/' + $stateParams.id, {}).
                success(function(data, status, headers, config) {
                    $scope.tournament = data;
                    console.log($scope.tournament);
                }).error(function(data, status, headers, config, statusText) {

                });
        };
        $scope.getTournament();

        var wonSets = function(sets, who) {
            var arrayLength = sets.length;
            var counter = 0;
            var other;
            if(who == "host") {
                other = "guest"
            } else {
                other = "host"
            }
            for (var i = 0; i < arrayLength; i++) {
                if(sets[i][who] > sets[i][other]) {
                    counter++;
                }
            }
            return counter;
        };

        var getChildren = function(d){
                var a = [];
                if(d.lefts) for(var i = 0; i < d.lefts.length; i++){
                    d.lefts[i].isRight = false;
                    d.lefts[i].parent = d;
                    a.push(d.lefts[i]);
                }
                if(d.rights) for(var i = 0; i < d.rights.length; i++){
                    d.rights[i].isRight = true;
                    d.rights[i].parent = d;
                    a.push(d.rights[i]);
                }
                return a.length?a:null;
            }
            ;

        var tree = d3.layout.tree()
                .size([height, width])
            ;

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
            .attr("width", "100%")
            .attr("height", '100%')
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        function buildTree(json) {
            root = json;
            root.x0 = height / 2;
            root.y0 = width / 2;

            var t1 = d3.layout.tree().size([height, halfWidth]).children(function(d){return d.lefts;}),
                t2 = d3.layout.tree().size([height, halfWidth]).children(function(d){return d.rights;});
            t1.nodes(root);
            t2.nodes(root);

            var rebuildChildren = function(node){
                node.children = getChildren(node);
                if(node.children) node.children.forEach(rebuildChildren);
            };
            rebuildChildren(root);
            root.isRight = false;
            update(root);
        }

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
            nodes.forEach(function(d) { d.y = d.depth * 120 + halfWidth; });

            // Update the nodes…
            var node = vis.selectAll("g.node")
                .data(nodes, function(d) { return d.id || (d.id = ++i); });

            // Enter any new nodes at the parent's previous position.
            var nodeEnter = node.enter().append("g")
                .attr("class", "node")
                .attr("transform", function(d) { p = calcLeft(d); return "translate(" + p.y + "," + p.x + ")"; })
                .on("click", click);

            nodeEnter.append("rect")
                .attr("width", 90)
                .attr("height", 40)
                .style("stroke", "#303f9f")
                .style("stroke-width", "3px")
                .attr("transform", "translate(-45,-20)")
                .style("fill", "#fff");


            nodeEnter.append("text")
                .attr("dy", -6)
                .attr("dx", 0)
                .attr("text-anchor", "middle")
                .text(function(d) {
                    if(d.match.host!=null) {
                        return d.match.host.name.substring(0, 10);
                    } else {
                        return "";
                    }
                })
                .style("fill-opacity", 1)
                .style("fill", "#303f9f");

            nodeEnter.append("text")
                .attr("dy", -6)
                .attr("dx", 37)
                .attr("text-anchor", "middle")
                .text(function(d) {
                    console.log(d.match);
                    if(d.match.host!=null && d.match.score.pointsContainers.length > 0) {
                        return wonSets(d.match.score.pointsContainers, "host");
                    } else {
                        return "";
                    }
                })
                .style("fill-opacity", 1)
                .style("fill", "#303f9f");

            nodeEnter.append("text")
                .attr("dy", 14)
                .attr("dx", 37)
                .attr("text-anchor", "middle")
                .text(function(d) {
                    if(d.match.guest!=null && d.match.score.pointsContainers.length > 0) {
                        return wonSets(d.match.score.pointsContainers, "guest");
                    } else {
                        return "";
                    }
                })
                .style("fill-opacity", 1)
                .style("fill", "#303f9f");



            nodeEnter.append("text")
                .attr("dy", 14)
                .attr("dx", 0)
                .attr("text-anchor", "middle")
                .text(function(d) {
                    if(d.match.guest != null) {
                        return d.match.guest.name.substring(0, 10);
                    }else {
                        return "";
                    }
                })
                .style("fill-opacity", 1)
                .style("fill", "#303f9f");

            nodeEnter.append("line")
                .attr("x1", -45)
                .attr("x2", 45)
                .attr("y1", 0)
                .attr("y2", 0)
                .style("stroke", "#303f9f");

            nodeEnter.append("line")
                    .attr("x1", 30)
                    .attr("x2", 30)
                    .attr("y1", -20)
                    .attr("y2", 20)
                    .style("stroke", "#303f9f");




            // Update the links...
            var link = vis.selectAll("path.link")
                .data(tree.links(nodes), function(d) { return d.target.id; });

            // Enter any new links at the parent's previous position.
            link.enter().insert("path", "g")
                .attr("class", "link")
                .style("stroke", "#303f9f")
                .attr("d", connector);

            function click(d) {
                $scope.openScorePopup(d.match._id, d.match.host, d.match.guest);

            }


        }

        $http.get('/api/tournaments/' + $stateParams.id + "/tree").
            success(function(data, status, headers, config) {
                $scope.tournamentTree = data;
                $scope.chartWidth = (data.losersTreeDepth+data.winnersTreeDepth+1)*120+80;
                $scope.chartHeight = (Math.pow(2, data.winnersTreeDepth))*70;
                halfWidth = $scope.chartWidth/2 - (data.losersTreeDepth - data.winnersTreeDepth)*60;
                height = $scope.chartHeight;
                width = $scope.chartWidth;
                buildTree(data);
                console.log(data);

            }).error(function(data, status, headers, config, statusText) {
                $scope.message = "";
                alert(status + " " + data);
            });

        var actualScale = 1;
        $scope.enlarge = function() {
            actualScale = actualScale*1.1;
            $scope.scale = "scale("+actualScale+")";
        };
        $scope.reduce = function() {
            actualScale = actualScale/1.1;
            $scope.scale = "scale("+actualScale+")";
        };
    }]);