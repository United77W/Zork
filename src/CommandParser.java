import java.util.Map;

public class CommandParser {

    public void parse(String input, Player player, Map<String, Room> rooms) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

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

                        if (nextRoomId.equals("Power Station") && !player.hasItem("TTC_Employee_Card")) {
                            System.out.println("You cannot enter this room. Employee Only.");
                            return;
                        }

                        double fare = 3.30;

                        if (player.hasItem("TTC_Employee_Card")) {
                            System.out.println("You used your employee card.");
                        } else {

                            if (!player.hasItem("PRESTO")) {
                                System.out.println("You need a PRESTO card to travel on the TTC.");
                                return;
                            }

                            if (!player.deductFare(fare)) {
                                System.out.println("Declined. Insufficient Balance.");
                                return;
                            }
                            player.setCurrentRoomId(nextRoomId);
                            System.out.println("You used your PRESTO CARD ($" + fare + ")");
                        }

                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("Remaining Balance: $" + player.getPrestoBalance());
                        System.out.println("You move " + direction + ".");

                        currentRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(currentRoom.getLongDescription());

                        if (Math.random() < 0.3) {
                            System.out.println("The Provincial Offences Officers boarded your train.");

                            if (player.hasItem("TTC_Employee_Card")) {
                                System.out.println("You have pretended to be a TTC employee.");
                            } else if (player.hasItem("PRESTO")) {
                                System.out.println("You transfer is valid.");
                            } else {
                                System.out.println(
                                        "You are a fare evasion. You are fined $100. This will be a criminal record as well.");
                                player.addMoney(-100.0);
                            }
                        }

                    } else {
                        System.out.println("You can't go that way.");
                    }
                }
                break;
            case "look":
                Room lookRoom = rooms.get(player.getCurrentRoomId());
                System.out.println(lookRoom.getLongDescription());
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
            case "admin":
                player.addMoney(900000000000000000000000000000000000000000000000000000.0);
                System.out.println("You now have unlimited funds.");
                break;
            case "balance":
                System.out.println("PRESTO BALANCE: $" + player.getPrestoBalance());
                break;
            case "reload":
                player.addMoney(10.0);
                System.out.println("You have added $10 onto your PRESTO CARD");
                System.out.println("Your new PRESTO Balance is: $" + player.getPrestoBalance());
                break;
            case "use":
                if (words.length < 2) {
                    System.out.println("Use what?");
                } else {
                    String itemName = words[1];
                    Item itemToUse = null;
                    for (Item item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToUse = item;
                            break;
                        }
                    }
                    
                    if (itemName.equalsIgnoreCase("PRESTO")) {
                        if (player.hasItem("PRESTO")) {
                            System.out.println("You check your PRESTO card balance: $" + player.getPrestoBalance());
                        } else {
                            System.out.println("You don't have a PRESTO card.");
                        }
                    } else if (itemToUse != null) {
                        handleUseItem(itemToUse, player, rooms);
                    } else {
                        System.out.println("You don't have that item.");
                    }
                }
                break;
            default:
                System.out.println("I don't understand that command.");
                break;
        }
    }

    private void handleUseItem(Item item, Player player, Map<String, Room> rooms) {
        String roomId = player.getCurrentRoomId();

        switch (item.getName().toLowerCase()) {
            case "screwdriver":
                if (roomId.equals("Power Station")) {
                    System.out.println("You use the screwdriver to open a maintenance panel and find a hidden passage.");
                    player.setCurrentRoomId("Hidden Passage");
                } else {
                    System.out.println("You can't use the screwdriver here.");
                }
                break;
            case "flashlight":
                if (roomId.equals("Hidden Passage")) {
                    System.out.println("You use the flashlight to see in the dark passage and find a key.");
                } else {
                    System.out.println("You can't use the flashlight here.");
                }
                break;
            case "crowbar":
                if (roomId.equals("Maintenance Room")) {
                    System.out.println("You use the crowbar to pry open a locked door and find a secret room.");
                    player.setCurrentRoomId("Secret Room");
                } else {
                    System.out.println("You can't use the crowbar here.");
                }
                break;
        }
    }
}