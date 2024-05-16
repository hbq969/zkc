package com.github.hbq969.code.zkc.model;

/**
 * @author hbq
 */
public class Tree {

  private String name;
  private boolean leaf = false;

  public Tree() {
  }

  public Tree(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean isLeaf() {
    return leaf;
  }
}
