package com.algorithms.dynamicprogramming;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * http://homes.sice.indiana.edu/yye/lab/teaching/spring2014-C343/huffman.php
 * A simple algorithm:
 * Prepare a collection of n initial Huffman trees, each of which is a single leaf node. Put the n trees onto a
 * priority queue organized by weight (frequency). Remove the first two trees (the ones with lowest weight).
 * Join these two trees to create a new tree whose root has the two trees as children, and whose weight is the sum of
 * the weights of the two children trees.
 * Put this new tree into the priority queue.
 * Repeat steps 2-3 until all of the partial Huffman trees have been combined into one.
 */

public class HuffmanTree {
  Node root;
  Map<Character, Integer> charFrequencyMap;
  Map<Character, String> encodedMap;

  HuffmanTree(Map<Character, Integer> frequencyMap) {
    this.charFrequencyMap = new HashMap<>(frequencyMap);
    this.encodedMap = new HashMap<>();
    buildTree();
    buildEncodedMap();
  }
  //Object to represent node of Huffman Tree
  class Node implements Comparable<Node>{
    int freq;
    char letter;
    Node left;
    Node right;

    Node(char letter, int freq) {
      this.letter = letter;
      this.freq = freq;
    }
    public String toString() {
      StringBuffer sbuff = new StringBuffer();
      sbuff.append("("); sbuff.append(this.letter); sbuff.append(",");
      sbuff.append(this.freq); sbuff.append(")");
      return sbuff.toString();
    }

    @Override
    public int compareTo(Node node) {
      return node.freq < this.freq ? -1 : 1;
    }
  }

  // Building mapping for character -> encoded string
  private void buildEncodedMap(){
    recurse(this.root, "");
  }

  private void recurse(Node node, String path) {
    if(node.letter != 0) {
      this.encodedMap.put(node.letter, path);
      return;
    }
    recurse(node.left, path+"0");
    recurse(node.right, path+"1");
  }
  //look in the encodedMap for encoded string of character
  public String encode(String plainString) {
    StringBuffer sbuff = new StringBuffer();
    for(char ch:plainString.toCharArray()) {
      sbuff.append(this.encodedMap.get(ch));
    }
    return sbuff.toString();
  }
  //
  public String decode(String encodedString) {
    int i = 0; StringBuffer decodedBuff = new StringBuffer();
    while(i < encodedString.length()) {
      int[] decoded = traverseTree(this.root, encodedString, i);
      if(decoded[1] == -1) {
        return "Cannot decode from index " + i;
      }
      char letter = (char) decoded[0];
      decodedBuff.append(letter);
      i = decoded[1];
    }
    return decodedBuff.toString();
  }
  //traverse to the leaf and find the letter
  public int[] traverseTree(Node node, String encoded, int pos) {
    if(node.letter != 0) {
      return new int[] {node.letter, pos};
    }

    if(encoded.charAt(pos) == '1') {
      return traverseTree(node.right, encoded, pos + 1);
    } else {
      return traverseTree(node.left, encoded, pos + 1);
    }
  }

  //greedy approach to build Huffman Tree
  private void buildTree() {
    PriorityQueue<Node> pq = new PriorityQueue<>(Collections.reverseOrder());
    for(Map.Entry<Character, Integer> entry: this.charFrequencyMap.entrySet()) {
      Node node = new Node(entry.getKey(), entry.getValue());
      pq.add(node);
    }
    while(pq.size() > 1) {
      Node node1 = pq.poll();
      Node node2 = pq.poll();
      char c = 0; //NUL char assigned
      Node parent = new Node(c,node1.freq + node2.freq);
      parent.left = node1;
      parent.right = node2;
      pq.add(parent);
    }
    this.root = pq.poll();
  }

  /**
   * Print huffman tree
   */
  public void printTree() {
    System.out.println("***** Printing Tree ******");
    System.out.println("Node(char, frequency)");
    this.preOrderPrint(this.root, new StringBuffer(), "--");
  }

  private void preOrderPrint(Node node, StringBuffer level, String delim) {
    if(node == null)
      return;
    System.out.println(level.toString() + node);
    preOrderPrint(node.left, new StringBuffer(level).append(delim), delim);
    preOrderPrint(node.right, new StringBuffer(level).append(delim), delim);
  }
  //print the encoded string for letters
  public void printEncodedMap() {
    System.out.println("***** Printing Encoded Map ******");
    for(Map.Entry<Character, String> entry : this.encodedMap.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }
  }



  public static void main(String[] args) {
     Map<Character, Integer> freqMap = new HashMap<>();
     freqMap.put('Z', 2);
     freqMap.put('K', 7);
     freqMap.put('M', 24);
     freqMap.put('C', 32);
     freqMap.put('U', 37);
     freqMap.put('D', 42);
     freqMap.put('L', 42);
     freqMap.put('E', 120);

     HuffmanTree tree = new HuffmanTree(freqMap);
     tree.printTree();
     tree.printEncodedMap();

     String str = "EEEE";
     String encodedStr = tree.encode(str);

     String decodedStr = tree.decode(encodedStr);

     System.out.println(str + " -> " + encodedStr);
     System.out.println(encodedStr + " -> " + decodedStr);

  }



}

