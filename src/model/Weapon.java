package model;

public class Weapon implements Item
{
  String name;

  @Override
  public String getName() {
    return name;
  }

  public Weapon(String name){
    this.name = name;
  }

  @Override
  public String toString() {return name;}


}