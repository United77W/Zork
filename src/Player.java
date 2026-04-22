import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private String currentRoomId;
    private List<Item> inventory;
    private double prestoBalance;
    private double cash;
    private String currentLine;
    private Set<String> discoveredStations = new HashSet<>();
    private Set<String> buskedThisVisit = new HashSet<>();

    private int powerStationsRepaired;

    private boolean Line5Restored = false;

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.inventory = new ArrayList<>();
        this.prestoBalance = 10.00;
        this.cash = 10.00;

        this.powerStationsRepaired = 0;
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
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public int getPowerStationsRepaired() {
        return powerStationsRepaired;
    }

    public void repairPowerStation() {
        powerStationsRepaired++;
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
public boolean hasDiscovered(String station) {
    return discoveredStations.contains(station);
}

public void discoverStation(String station) {
    discoveredStations.add(station);
}

public boolean hasBuskedHere(String station) {
    return buskedThisVisit.contains(station);
}

public void markBusked(String station) {
    buskedThisVisit.add(station);
}

public void resetBusking() {
    buskedThisVisit.clear();
}
}
