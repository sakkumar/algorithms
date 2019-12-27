package com.algorithms.dynamicprogramming;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**https://leetcode.com/problems/burst-balloons/
 * bottom up approach
 * for [3,1,5,8]
 * solve [3], [1], [5], [8]
 * then [3,1], [3,5], [3,8], [1,5], [1,8], [5,8]
 * then [3,1,5], [3,1,8], [3,5,8], [1,5,8]
 * then [3,1,5,8]
 */

public class BurstBalloons {

  class Node {
    ArrayList<Integer> list;
    int pos;
    Node(ArrayList list, int pos) {
      this.list = list;
      this.pos = pos;
    }
    public String toString() {
      return list.toString();
    }
  }

  public int maxCoins(ArrayList<Integer> nums) {
    Map<String, Integer> indexToMaxCoinMap = new HashMap<>();

    LinkedList<Node> indexCombinationQueue = new LinkedList<>();
    for(int i=0; i<nums.size(); i++){
      ArrayList<Integer> list = new ArrayList<>();
      list.add(nums.get(i));
      Node node = new Node(list, i+1);
      indexCombinationQueue.addLast(node);
      indexToMaxCoinMap.put(createKey(list, -1), nums.get(i));
    }
    System.out.println(indexCombinationQueue);
    System.out.println(indexToMaxCoinMap);

    while(!indexCombinationQueue.isEmpty()) {
      LinkedList<Node> nextLevelIndexQueue = new LinkedList<>();
      while(!indexCombinationQueue.isEmpty()) {
        Node node = indexCombinationQueue.poll();
        ArrayList<Integer> list = node.list;

        for(int i = node.pos; i<nums.size(); i++) {
          ArrayList<Integer> temp = new ArrayList<>();
          temp.addAll(list);
          temp.add(nums.get(i));

          int maxCost = 0;
          String nextKey = createKey(temp, -1);
          for(int k=0; k<temp.size(); k++) {
            int cost = 0;
            if(k == 0) {
              cost = 1 * temp.get(k) * temp.get(k+1);
            } else if (k == temp.size() - 1){
              cost = temp.get(k-1) * temp.get(k) * 1;
            } else {
              cost = temp.get(k-1) * temp.get(k) * temp.get(k+1);
            }
            String tempKey = createKey(temp, k);
            //System.out.println(tempKey);
            maxCost = Math.max(maxCost, cost + indexToMaxCoinMap.get(tempKey));
          }
          indexToMaxCoinMap.put(nextKey, maxCost);
          nextLevelIndexQueue.addLast(new Node(temp, i+1));
        }
      }
      //System.out.println(nextLevelIndexQueue);
      indexCombinationQueue = nextLevelIndexQueue;
    }
    System.out.println(indexToMaxCoinMap);
    return indexToMaxCoinMap.get(createKey(nums, -1));
  }

  private static String createKey(ArrayList<Integer> nums, int skip) {
    StringBuffer sbuff = new StringBuffer();
    for(int i=0; i<nums.size(); i++) {
      if(i != skip) {
        if(sbuff.length() > 0)
          sbuff.append(",");
        sbuff.append(nums.get(i));
      }
    }
    return sbuff.toString();
  }

  public static void main(String[] args) {
    ArrayList<Integer> nums = new ArrayList<>();
    nums.add(3); nums.add(1); nums.add(5);
    BurstBalloons balloons = new BurstBalloons();
    int result = balloons.maxCoins(nums);
    System.out.println("Result: " + result);
  }
}
