import java.util.ArrayList;
import java.util.List;

public class Player {
    private String currentRoomId;
    private List<Item> inventory;
    private double prestoBalance;
    private double cash;

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.inventory = new ArrayList<>();
        this.prestoBalance = 0.00;
        this.cash = 10.00;
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }
        //
    public void setCurrentRoomId(String roomId) {
        this.currentRoomId = roomId;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }
}
