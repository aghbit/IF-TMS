package models.strategy.eliminationtrees

import models.strategy.Match

/**
 * Created by Szymek Seget on 25.05.15.
 */
class TreeNode(var left:Option[TreeNode],
           var right:Option[TreeNode],
           var value:Match) {

}
