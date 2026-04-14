import java.util.Map;

public class CommandParser {
    public void parse(String input, Player player, Map<String, Room> rooms) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        //
        String command = words[0];

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                } else {
                    String direction = words[1];
                    Room currentRoom = rooms.get(player.getCurrentRoomId());
                    String nextRoomId = currentRoom.getExits().get(direction);
                    if (nextRoomId != null) {
                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("You move " + direction + ".");
                        currentRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(currentRoom.getLongDescription());

                    } else {
                        System.out.println("You can't go that way.");
                    }
                }
                break;
            case "look":
                Room currentRoom = rooms.get(player.getCurrentRoomId());
                System.out.println(currentRoom.getLongDescription());
                break;
            case "inventory":
                if (player.getInventory().isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("You are carrying:");
                    for (Item item : player.getInventory()) {
                        System.out.println("- " + item.getName());
                    }
                }
                break;
            case "take":
                if (words.length < 2) {
                    System.out.println("Take what?");
                } else {
                    String itemName = words[1];
                    Room room = rooms.get(player.getCurrentRoomId());
                    Item itemToTake = null;
                    for (Item item : room.getItems()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToTake = item;
                            break;
                        }
                    }
                    if (itemToTake != null) {
                        room.removeItem(itemToTake);
                        player.addItem(itemToTake);
                        System.out.println("You take the " + itemToTake.getName() + ".");
                    } else {
                        System.out.println("There is no " + itemName + " here.");
                    }
                }
                break;
            case "drop":
                if (words.length < 2) {
                    System.out.println("Drop what?");
                } else {
                    String itemName = words[1];
                    Item itemToDrop = null;
                    for (Item item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToDrop = item;
                            break;
                        }
                    }
                    if (itemToDrop != null) {
                        player.removeItem(itemToDrop);
                        Room room = rooms.get(player.getCurrentRoomId());
                        room.addItem(itemToDrop);
                        System.out.println("You drop the " + itemToDrop.getName() + ".");
                    } else {
                        System.out.println("You don't have a " + itemName + ".");
                    }
                }
                break;
            case "help":
                System.out.println("Available commands: go [direction], look, take [item], drop [item], inventory, help");
                break;
            default:
                System.out.println("I don't understand that command.");
                break;
        }
    }
}
