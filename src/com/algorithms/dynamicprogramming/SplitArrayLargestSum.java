package com.algorithms.dynamicprogramming;

import java.util.Arrays;


/**
 * https://leetcode.com/problems/split-array-largest-sum/
 * Given an array which consists of non-negative integers and an integer m, you can split the array into m non-empty continuous subarrays. Write an algorithm to minimize the largest sum among these m subarrays.
 *
 * Note:
 * If n is the length of array, assume the following constraints are satisfied:
 *
 * 1 ≤ n ≤ 1000
 * 1 ≤ m ≤ min(50, n)
 * Examples:
 *
 * Input:
 * nums = [7,2,5,10,8]
 * m = 2
 *
 * Output:
 * 18
 *
 * Explanation:
 * There are four ways to split nums into two subarrays.
 * The best way is to split it into [7,2,5] and [10,8],
 * where the largest sum among the two subarrays is only 18.
 */
public class SplitArrayLargestSum {

  public int splitArray(int[] nums, int m) {
    int len = nums.length;
    int[][] dp = new int[m][len];
    int sum = 0;
    //initialize dp array with prefix sum for m == 0
    for(int i=0; i<len; i++) {
      dp[0][i] = sum + nums[i];
      sum = dp[0][i];
    }
    //loop through all feasible partitions
    for(int p=1; p<m; p++) {
      //loop through feasible substring with start index 0
      for(int i=1; i<len; i++) {
        int min = Integer.MAX_VALUE;
        //loop through all feasible partition in the boundary
        for(int k=0; k<i; k++) {
          int leftValue = dp[p-1][k];
          int rightValue = dp[0][i] - dp[0][k];
          //take max of left and right and min of all possible subarrays in range (0,i)
          min = Math.min(min, Math.max(leftValue, rightValue));
        }
        dp[p][i] = min;
      }
    }
    return dp[m-1][len-1];
  }

  public static void main(String[] args) {
    SplitArrayLargestSum splitArraySum = new SplitArrayLargestSum();
    int[] nums = new int[] {1,2147483647};
    int min = splitArraySum.splitArray(nums, 2);
    System.out.println("Min Largest Sum: " + min);

  }
}
