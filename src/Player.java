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
        this.prestoBalance = 10.00;
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

    public double getPrestoBalance() {
        return prestoBalance;
    }

    public void addMoney(double amount) {
        prestoBalance += amount;
    }

    public boolean deductFare(double amount) {
        if (prestoBalance >= amount) {
            prestoBalance -= amount;
            return true;
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if(item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

}
