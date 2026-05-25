import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String currentRoomId;
    private List<Item> inventory;
    private double prestoBalance;
    private double cash;
    private String currentLine;
    private List<String> dihStations = new ArrayList<>();
    private List<String> bustedThisTime = new ArrayList<>();
    // anything to do wtih busting is busking like music playing it js sounded uh better...

    private int pStationsFixed;

    private boolean Line5Restored = false;

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.inventory = new ArrayList<>();
        this.prestoBalance = 5.00;
        this.cash = 5.00;

        this.pStationsFixed = 0;
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

    public boolean minusFare(double amount) {
        if (prestoBalance >= amount) {
            prestoBalance -= amount;
            return true;
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public int getPStationsFixed() {
        return pStationsFixed;
    }

    public void fixPStation() {
        pStationsFixed++;
    }

    public boolean isLine5Restored() {
        return Line5Restored;
    }

    public void setLine5Restored(boolean value) {
        Line5Restored = value;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(String line) {
        this.currentLine = line;
    }

    public boolean hasDih(String station) {
        return dihStations.contains(station);
    }

    public void dihStation(String station) {
        if (!dihStations.contains(station)) {
            dihStations.add(station);
        }
    }

    public boolean hasBustedHere(String station) {
        return bustedThisTime.contains(station);
    }

   

    public void markBusted(String station) {
    if (!bustedThisTime.contains(station)) {
        bustedThisTime.add(station);
    }
}

    public void resetBusting() {
        bustedThisTime.clear();
    }
}
