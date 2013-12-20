package se.considerate.doors.world;

public class Item {
  private String name;
  private String description;
  private float spawnRate;

  public Item(String name) {
    this(name,"");
  }

  public Item(String name, String description) {
    this.name = name;
    this.description = description;
    this.spawnRate = 0f;
  }

  public String toString() {
    return name + ", " + description;
  }

  public void setSpawnRate(float rate) {
    this.spawnRate = rate;
  }

  public float getSpawnRate() {
    return this.spawnRate;
  }
}