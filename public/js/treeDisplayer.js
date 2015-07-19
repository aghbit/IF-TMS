/**
 * Created by szymek on 19.07.15.
 */

var margin = {top: 30, right: 10, bottom: 10, left: 10},
    width = 3000 - margin.left - margin.right,
    halfWidth = width / 2,
    height = 1000 - margin.top - margin.bottom,
    i = 0,
    root;

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
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.json("bracket2.json", function(json) {
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
    nodes.forEach(function(d) { d.y = d.depth * (width/(root.treeDepth*2)) + halfWidth; });

    // Update the nodes…
    var node = vis.selectAll("g.node")
        .data(nodes, function(d) { return d.id || (d.id = ++i); });

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append("g")
        .attr("class", "node")
        .attr("transform", function(d) { p = calcLeft(d); return "translate(" + p.y + "," + p.x + ")"; })

    nodeEnter.append("rect")
        .attr("width", 90)
        .attr("height", 40)
        .style("stroke", "#000")
        .attr("transform", "translate(-45,-20)")
        .style("fill", "#ccc");


    nodeEnter.append("text")
        .attr("dy", -6)
        .attr("dx", 0)
        .attr("text-anchor", "middle")
        .text(function(d) {
            if(d.match.host!=null) {
                return d.match.host.name;
            } else {
                return "";
            }
        })
        .style("fill-opacity", 1);

    nodeEnter.append("text")
        .attr("dy", 14)
        .attr("dx", 0)
        .attr("text-anchor", "middle")
        .text(function(d) {
            if(d.match.guest != null) {
                return d.match.guest.name;
            }else {
                return "";
            }
        })
        .style("fill-opacity", 1);

    nodeEnter.append("line")
        .attr("x1", -45)
        .attr("x2", 45)
        .attr("y1", 0)
        .attr("y2", 0)
        .style("stroke", "#000");

    // Update the links...
    var link = vis.selectAll("path.link")
        .data(tree.links(nodes), function(d) { return d.target.id; });

    // Enter any new links at the parent's previous position.
    link.enter().insert("path", "g")
        .attr("class", "link")
        .attr("d", connector);




}
