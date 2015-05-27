package models.strategy.eliminationtrees

import models.strategy.Match

import scala.collection.mutable

/**
 * Created by Szymek Seget on 25.05.15.
 */
class TreeNode(var left:Option[TreeNode],
               var right:Option[TreeNode],
               var value:Match,
               val depth:Int) {


  var parent:Option[TreeNode] = None

  /**
   * Returns true when given node is a node in subtree, which root is this object.
   * Otherwise returns false.
   * @param treeNode - node
   * @return Boolean
   */
  def contains(treeNode: TreeNode):Boolean = {
    val stack = new mutable.Stack[TreeNode]()
    stack.push(this)
    var result = false
    while (stack.nonEmpty && !result) {
      val tmp = stack.pop()
      tmp.right match {
        case Some(r) => stack.push(r)
        case None =>
      }
      tmp.left match {
        case Some(l) => stack.push(l)
        case None =>
      }
      if(tmp.equals(treeNode)){
        result = true
      }
    }
    result
  }
  /**
   * Returns true when given match is a node in subtree, which root is this object.
   * Otherwise returns false.
   * @param m - match
   * @return Boolean
   */
  def contains(m: Match):Boolean = {
    val stack = new mutable.Stack[TreeNode]()
    stack.push(this)
    var result = false
    while (stack.nonEmpty && !result) {
      val tmp = stack.pop()
      tmp.right match {
        case Some(r) => stack.push(r)
        case None =>
      }
      tmp.left match {
        case Some(l) => stack.push(l)
        case None =>
      }
      if(tmp.value.id == m.id){
        result = true
      }
    }
    result
  }

  override def toString: String = {
    val builder = new StringBuilder()
    left match {
      case Some(l) => builder.append("LeftChild: "+l.value.id+" ")
      case None => builder.append("LeftChild: null ")
    }
    builder.append("MyValue: "+value.toJson+" ")
    parent match {
      case Some(p) => builder.append("Parent: " + p.value.id)
      case None => builder.append("Parent: null")
    }
    right match {
      case Some(r) => builder.append("RightChild: "+r.value.id+" ")
      case None => builder.append("RightChild: null ")
    }
    builder.append("\n")
    builder.result()
  }
}
