package com.algorithms.dynamicprogramming;

import java.util.Arrays;


/**
 * https://leetcode.com/problems/minimum-cost-to-merge-stones/
 *
 There are N piles of stones arranged in a row.  The i-th pile has stones[i] stones.

 A move consists of merging exactly K consecutive piles into one pile, and the cost of this move is equal to the total number of stones in these K piles.

 Find the minimum cost to merge all piles of stones into one pile.  If it is impossible, return -1.



 Example 1:

 Input: stones = [3,2,4,1], K = 2
 Output: 20
 Explanation:
 We start with [3, 2, 4, 1].
 We merge [3, 2] for a cost of 5, and we are left with [5, 4, 1].
 We merge [4, 1] for a cost of 5, and we are left with [5, 5].
 We merge [5, 5] for a cost of 10, and we are left with [10].
 The total cost was 20, and this is the minimum possible.
 */
public class MergeStones {

  public int mergeStones(int[] stones, int K) {
    int len = stones.length; int sum = 0;
    if((len-1) % (K-1) != 0)
      return -1;

    int[] sumArr = new int[len];
    for(int i=0; i<len; i++) {
      sumArr[i] = sum + stones[i];
      sum = sumArr[i];
    }
    System.out.println(Arrays.toString(sumArr));
    int[][] dp = new int[len][len];
    for(int l=K; l<=len; l++) { //take subArray of sizes K -> len
      for(int i=0; i<=len-l; i++) { //starting index of the subArray boundary
        int j = i + l - 1; // end index of subArray of size l
        int min = Integer.MAX_VALUE;
        for(int k=i; k<j; k = k + K-1) { // increase k by K-1
          min = Math.min(min, dp[i][k] + dp[k+1][j]); // min of left subArray & right subArray
        }
        dp[i][j] = (j-i) % (K-1) == 0 ? min + sumArr[j] - sumArr[i] + stones[i] : min; //do not add sum of stones to cost
        // if there is no solution for the subArray length because in the end it will be added.
      }
      System.out.println("l = " + l);
      for(int i=0; i<len; i++) {
        System.out.println(Arrays.toString(dp[i]));
      }
    }
    return dp[0][len-1];
  }

  public static void main(String[] args) {
    MergeStones mergeStones = new MergeStones();
    int[] stones = {3,5,1,2,6,2,3};
    int minCost = mergeStones.mergeStones(stones, 3);
    System.out.println("Min cost: " + minCost);

  }
}


