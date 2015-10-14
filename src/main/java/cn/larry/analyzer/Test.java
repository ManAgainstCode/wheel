package cn.larry.analyzer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Test {
    public void invertBinaryTree(TreeNode root) {
        // write your code here
        Queue<TreeNode> current = new LinkedList<TreeNode>();
        current.offer(root);
        while(!current.isEmpty()){
            TreeNode node = current.remove();
            reverseNode(node);
            if(node.left != null) current.offer(node.left);
            if(node.right != null) current.offer(node.right);
        }
    }
    
    private void reverseNode(TreeNode node){
        TreeNode tmp = node.left;
        node.left = node.right;
        node.right = tmp;
    }
}

 class TreeNode {
     public int val;
     public TreeNode left, right;
     public TreeNode(int val) {
        this.val = val;
        this.left = this.right = null;  
        }
 }
