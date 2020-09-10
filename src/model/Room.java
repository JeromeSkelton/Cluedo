package model;

public class Room implements Item
{
  String name;

  @Override
  public String getName() {
    return name;
  }

  public Room(String name) {
    this.name = name;
  }

  @Override
  public String toString() {return name;}

}