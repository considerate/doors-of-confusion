import java.util.ArrayList;

public class Inventory {
  private ArrayList<Item> items;
  private float maxWeight = 100;
  private float maxSize = 100;

  public Inventory () {
    items = new ArrayList<Item>();
  }

  public void addItem(Item item) {
    items.add(item);
  }

}