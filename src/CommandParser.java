import java.util.Map;

public class CommandParser {
    // This variable stays at the top to track the game state
    private boolean isBeingRobbed = false;

    public void parse(String input, Player player, Map<String, Room> rooms) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String command = words[0];

        // Block most commands if a robbery is happening
        if (isBeingRobbed && !command.equals("attack") && !command.equals("run") && !command.equals("inventory")) {
            System.out.println("The robber is blocking you! You must ATTACK or RUN!");
            return;
        }

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                } else {
                    String direction = words[1];
                    Room currentRoom = rooms.get(player.getCurrentRoomId());
                    String nextRoomId = currentRoom.getExits().get(direction);
                    
                    if (nextRoomId != null) {
                        // Restricted area check
                        if (nextRoomId.equals("Power Station") && !player.hasItem("TTC_Employee_Card")) {
                            System.out.println("You cannot enter this room. Employee Only.");
                            return;
                        }

                        // Fare logic
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
                            System.out.println("You used your PRESTO CARD ($" + fare + ")");
                        }

                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("Remaining Balance: $" + player.getPrestoBalance());
                        System.out.println("You move " + direction + ".");

                        Room newRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(newRoom.getLongDescription());

                        // Random fare inspector event
                        if (Math.random() < 0.3) {
                            System.out.println("The Provincial Offences Officers boarded your train.");
                            if (player.hasItem("TTC_Employee_Card")) {
                                System.out.println("You have pretended to be a TTC employee.");
                            } else if (player.hasItem("PRESTO")) {
                                System.out.println("Your transfer is valid.");
                            } else {
                                System.out.println("You are caught for fare evasion! Fined $100.");
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
                        rooms.get(player.getCurrentRoomId()).addItem(itemToDrop);
                        System.out.println("You drop the " + itemToDrop.getName() + ".");
                    } else {
                        System.out.println("You don't have a " + itemName + ".");
                    }
                }
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
                    if (itemToUse != null) {
                        handleUseItem(itemToUse, player, rooms);
                    } else {
                        System.out.println("You don't have that item.");
                    }
                }
                break;

            case "talk":
                if (player.getCurrentRoomId().equals("Broadview Station")) {
                    System.out.println("\n--- CONFRONTATION ---");
                    System.out.println("A hooded figure steps out: 'Give me your PRESTO card and cash!'");
                    System.out.println("The exits are blocked! What will you do?");
                    System.out.println("Commands: ATTACK [item], or RUN");
                    isBeingRobbed = true;
                } else {
                    System.out.println("You talk to yourself. People are staring.");
                }
                break;

            case "attack":
                if (!isBeingRobbed) {
                    System.out.println("There is nothing to attack here.");
                } else if (words.length < 2) {
                    System.out.println("Attack with what?");
                } else {
                    handleAttack(words[1], player);
                }
                break;

            case "run":
                if (isBeingRobbed) {
                    System.out.println("You bolt toward the stairs and escape back to Bloor-Yonge!");
                    player.setCurrentRoomId("Bloor-Yonge Station");
                    isBeingRobbed = false;
                } else {
                    System.out.println("You run in a small circle.");
                }
                break;

            case "admin":
                player.addMoney(1000.0);
                System.out.println("Admin funds added.");
                break;

            case "balance":
                System.out.println("PRESTO BALANCE: $" + player.getPrestoBalance());
                break;

            case "help":
                System.out.println("Commands: go [dir], look, take [item], drop [item], use [item], inventory, talk, attack [item], run, help");
                break;

            default:
                System.out.println("I don't understand that command.");
                break;
        }
    }

    private void handleAttack(String itemName, Player player) {
        Item weapon = null;
        for (Item i : player.getInventory()) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                weapon = i;
                break;
            }
        }

        if (weapon == null) {
            System.out.println("You don't have a " + itemName + "!");
            return;
        }

        String name = weapon.getName().toLowerCase();
        if (name.contains("screwdriver") || name.contains("crowbar") || name.contains("flashlight")) {
            System.out.println("You brandish your " + weapon.getName() + " aggressively!");
            System.out.println("The robber's eyes widen. 'Woah! Take it easy! I'm leaving!'");
            System.out.println("He disappears into the tunnels. You are safe.");
            isBeingRobbed = false;
        } else if (name.contains("ttc_employee_card") || name.contains("presto")) {
            System.out.println("You try to defend yourself with a " + weapon.getName() + "...");
            System.out.println("The robber laughs. 'Is that a joke?'");
            System.out.println("He shoves you and steals $5.00 from your pocket before running away.");
            player.addMoney(-5.0);
            isBeingRobbed = false;
        } else {
            System.out.println("That item isn't very effective as a weapon.");
            // You can decide if the robber stays or leaves here.
        }
    }

    private void handleUseItem(Item item, Player player, Map<String, Room> rooms) {
        String roomId = player.getCurrentRoomId();
        switch (item.getName().toLowerCase()) {
            case "screwdriver":
                if (roomId.equals("Power Station")) {
                    System.out.println("You open a panel and find a hidden passage.");
                    player.setCurrentRoomId("Hidden Passage");
                } else {
                    System.out.println("Nothing to screw here.");
                }
                break;
            case "flashlight":
                if (roomId.equalsIgnoreCase("Hidden Passage")) {
                    System.out.println("You find a key in the dark!");
                } else {
                    System.out.println("It's bright enough here.");
                }
                break;
            default:
                System.out.println("You aren't sure how to use this.");
                break;
        }
    }
}