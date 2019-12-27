package com.algorithms.dynamicprogramming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MinCostWithConstraints {
  //map of node number and Node like (0, Node0)
  ArrayList<Node> nodeList;
  MinCostWithConstraints(){
    this.nodeList = new ArrayList<>();
  }

  class Node {
    String name;
    List<Mode> modes;
    Node(String name) {
      this.name = name;
      modes = new ArrayList();
    }
    void add(Mode mode) {
      this.modes.add(mode);
    }
  }
  class Mode {
    String name;
    List<TaskUnit> taskUnits;
    Mode(String name){
      this.name = name;
      taskUnits = new ArrayList();
    }
    void add(TaskUnit tu){
      taskUnits.add(tu);
    }
  }
  class TaskUnit {
    String name;
    int time;
    double probability;
    int cost;
    TaskUnit(String name, int time, double probability, int cost) {
      this.name = name;
      this.time = time;
      this.probability = probability;
      this.cost = cost;
    }
  }

  private void scheduleMe(int timeConstraintUnit, int totalNode){
    Map<Integer, List<Task>> resultMap = new HashMap<>();
    List<Task> resultList = new LinkedList<>();
    System.out.println("Running for time constraint: " + timeConstraintUnit);
    //initialize the task
    Task initTask = new Task("Root", -1, 0, 1, 0, timeConstraintUnit);
    List<Task> queue = new LinkedList<>();
    queue.add(initTask);

    while(!queue.isEmpty()) {
      List<Task> nextLevelQueue = new LinkedList<>();
      for(Task task:queue) {
        int nextNode = task.node + 1;
        if(nextNode < this.nodeList.size()) {
          Node nodeMap = this.nodeList.get(nextNode);
          //iterate to all available modes for the node
          for(Mode mode: nodeMap.modes) {
            //iterate to all available taskUnits
            for(TaskUnit tu: mode.taskUnits) {
              if(task.runningTime + tu.time <= task.totalTime) { //insert into Queue if run time is less than total time
                String pathName = task.name + " -> " + String.format("(%s,  %s, %s)", nodeMap.name, mode.name, tu.name);
                int newTaskRunTime = task.runningTime + tu.time;
                Task nextTask = new Task(pathName,  nextNode, newTaskRunTime, task.runningProb * tu.probability,
                    task.runningCost + tu.cost, task.totalTime);
                if(nextNode == totalNode - 1) { // if next node is the last node, store the Task to corresponding finish Time in map
                  List<Task> temp = resultMap.getOrDefault(newTaskRunTime, new LinkedList<>());
                  temp.add(nextTask);
                  resultMap.put(newTaskRunTime, temp);
                }
                nextLevelQueue.add(nextTask);
              }
            }
          }
        }
      }
      queue = nextLevelQueue;
    }

    List<Task> probableTasks = new LinkedList<>();
    for(int i=1; i<=timeConstraintUnit; i++) {
      if(resultMap.containsKey(i)) {
        probableTasks.addAll(resultMap.get(i));
      }
      resultMap.put(i, filter(probableTasks));
    }

    for(Integer key : resultMap.keySet()) {
      System.out.println("key: " + key);
      System.out.println(resultMap.get(key));
    }

  }

  private List<Task> filter(List<Task> tasks) {
    List<Task> minCostTasks = filterMinCostTasks(tasks);
    List<Task> maxProbTasks = filterMaxProbablityTasks(minCostTasks);
    return maxProbTasks;
  }
  //sort tasks by propability in desc order and filter out min cost tasks
  private List<Task> filterMinCostTasks(List<Task> tasks) {
    Collections.sort(tasks, new Comparator<Task>() {
      public int compare(Task o1, Task o2) {
        return Double.compare(o2.runningProb, o1.runningProb);
      }
    });
    Task prev = null;
    List<Task> minCostTasks = new LinkedList<>();
    for(Task task:tasks) {
      if(prev == null){
        minCostTasks.add(task);
        prev = task;
      } else if(task.runningCost < prev.runningCost) {
        prev = task;
        minCostTasks.add(task);
      }
    }
    return minCostTasks;
  }
  //sort tasks by runningCost and filter out max probability tasks
  private List<Task> filterMaxProbablityTasks(List<Task> tasks) {
    Collections.sort(tasks, new Comparator<Task>() {
      public int compare(Task o1, Task o2) {
        return Integer.compare(o1.runningCost, o2.runningCost);
      }
    });
    List<Task> maxProbTasks = new LinkedList<>();
    Task prev = null;
    for(Task task:tasks) {
      if(prev == null){
        maxProbTasks.add(task);
        prev = task;
      } else if(task.runningProb != prev.runningProb) {
        maxProbTasks.add(task);
        prev = task;
      }
    }
    return maxProbTasks;
  }

  class Task {
    String name;
    int node;
    int runningTime;
    double runningProb;
    int runningCost;
    int totalTime;
    Task(String name, int n, int r, double rp, int rc, int tt){
      this.name = name;
      this.node = n;
      this.runningTime = r;
      this.runningProb = rp;
      this.runningCost= rc;
      this.totalTime = tt;
    }
    public String toString(){
      return String.format("Node Path: %s \t \u001B[31m probability %.2f \t cost %s \u001B[30m", this.name,
          this.runningProb, this.runningCost);
    }

  }

  public static void main(String[] args){
    MinCostWithConstraints cmtc = new MinCostWithConstraints();
    TaskUnit t1 = cmtc.new TaskUnit("t1",1, 0.9, 10);
    TaskUnit t2 = cmtc.new TaskUnit("t2",3, 1, 10);
    TaskUnit t3 = cmtc.new TaskUnit("t3", 2, 0.7, 4);
    TaskUnit t4 = cmtc.new TaskUnit("t4", 4, 1, 4);
    Mode R1N0 = cmtc.new Mode("R1");
    R1N0.add(t1);
    R1N0.add(t2);
    Mode R2N0 = cmtc.new Mode("R2");
    R2N0.add(t3);
    R2N0.add(t4);
    Node zero = cmtc.new Node("Node 0");
    zero.add(R1N0);
    zero.add(R2N0);

    TaskUnit t5 = cmtc.new TaskUnit("t5",1, 0.8, 8);
    TaskUnit t6 = cmtc.new TaskUnit("t6", 4, 1, 8);
    TaskUnit t7 = cmtc.new TaskUnit("t7",3, 0.6, 3);
    TaskUnit t8 = cmtc.new TaskUnit("t8", 5, 1, 3);
    Mode R1N1 = cmtc.new Mode("R1");
    R1N1.add(t5);
    R1N1.add(t6);
    Mode R2N1 = cmtc.new Mode("R2");
    R2N1.add((t7));
    R2N1.add(t8);
    Node one = cmtc.new Node("Node 1");
    one.add(R1N1);
    one.add(R2N1);

    TaskUnit t9 = cmtc.new TaskUnit("t9", 1, 0.9, 9);
    TaskUnit t10 = cmtc.new TaskUnit("t10", 4, 1, 9);
    TaskUnit t11 = cmtc.new TaskUnit("t11", 2, 0.8, 5);
    TaskUnit t12 = cmtc.new TaskUnit("t12",6, 1, 5);
    Mode R1N2 = cmtc.new Mode("R1");
    R1N2.add(t9);
    R1N2.add(t10);
    Mode R2N2 = cmtc.new Mode("R2");
    R2N2.add(t11);
    R2N2.add(t12);
    Node two = cmtc.new Node("Node 2");
    two.add(R1N2);
    two.add(R2N2);

    cmtc.nodeList.add(0, zero);
    cmtc.nodeList.add(1, one);
    cmtc.nodeList.add(2, two);

    cmtc.scheduleMe(19, 3);

  }

}
