import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

public class CommandParser {
    // This variable stays at the top to track the game state
    private boolean isBeingRobbed = false;

    private boolean Line5Down = true;

    private Set<String> closedParts = new HashSet<>();

    private Set<String> powerStations = Set.of(
            "Power Station",
            "Don Valley Station",
            "Mount Dennis Station",
            "Eglinton Station");

    private Set<String> Line5Stations = Set.of(
            "Mount Dennis Station",
            "Cedarvale Station",
            "Eglinton Station",
            "Don Valley Station",
            "Kennedy Station");

    private Map<String, Set<String>> shuttleBuses = Map.of(
            "Mount Dennis Station", Set.of("Cedarvale Station"),
            "Cedarvale Station", Set.of("Mount Dennis Station", "Eglinton Station"),
            "Eglinton Station", Set.of("Cedarvale Station", "Don Valley Station"),
            "Don Valley Station", Set.of("Eglinton Station", "Kennedy Station"),
            "Kennedy Station", Set.of("Don Valley Station"));

    private String makeParts(String from, String to) {
        return from + "<->" + to;
    }

    public void parse(String input, Player player, Map<String, Room> rooms) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String command = words[0];

        if (isBeingRobbed && !command.equals("attack") && !command.equals("run") && !command.equals("inventory")) {
            System.out.println("The robber is blocking you! You must ATTACK or RUN!");
            return;
        }

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                } else {
                    String targetStation = input.substring(command.length()).trim();
                    Room currentRoom = rooms.get(player.getCurrentRoomId());
                    String nextRoomId = null;

                    for (String exit : currentRoom.getExits().values()) {
                        if (exit.equalsIgnoreCase(targetStation) ||
                                exit.toLowerCase().contains(targetStation.toLowerCase())) {
                            nextRoomId = exit;
                            break;
                        }
                    }

                    if (nextRoomId == null) {
                        System.out.println("You can't go directly to " + targetStation + " from here.");
                        return;
                    }

                    if (nextRoomId != null) {

                        Room nextRoom = rooms.get(nextRoomId);

                        List<String> currentLines = currentRoom.getLines();
                        List<String> nextLines = nextRoom.getLines();

                        String usedLine = null;

                        if (currentLines != null && nextLines != null) {
                            for (String line : currentLines) {
                                if (nextLines.contains(line)) {
                                    usedLine = line;
                                    break;
                                }
                            }
                        }

                        if (usedLine == null && nextLines != null && !nextLines.isEmpty()) {
                            usedLine = nextLines.get(0);
                        }

                        if (nextRoomId.equals("Power Station") && !player.hasItem("TTC_Employee_Card")) {
                            System.out.println("You cannot enter this room. Employee Only.");
                            return;
                        }

                        if (Line5Down) {
                            boolean currentIsLine5 = Line5Stations.contains(player.getCurrentRoomId());
                            boolean nextIsLine5 = Line5Stations.contains(nextRoomId);

                            if (currentIsLine5 && nextIsLine5) {
                                System.out.println("Line 5 Eglinton Is Down");
                                System.out.println(
                                        "Shuttle Buses Are Operating. The TTC apoligize for this inconvience.");
                                return;
                            }

                        }

                        String from = player.getCurrentRoomId();
                        String to = nextRoomId;

                        String parts = makeParts(from, to);

                        if (closedParts.contains(parts)) {
                            System.out.println("Subway Closures!");
                            return;
                        }

                        if (Math.random() < 0.01) {
                            System.out.println("Service Advisory!");
                            System.out.println("There is no subway service between " + from + " and " + to
                                    + " due to an emergency alarm activation. Shuttle buses are not operating!");

                            closedParts.add(parts);
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
                            System.out.println("You used your PRESTO CARD ($" + fare + ")");
                        }

                        player.setCurrentRoomId(nextRoomId);

                        if (!player.hasDiscovered(nextRoomId)) {
                            System.out.println("NEW STATION DISCOVERED!");
                            System.out.println("+$10");
                            player.addMoney(10.0);
                            player.discoverStation(nextRoomId);
                        }

                        player.resetBusking();

                        player.setCurrentLine(usedLine);

                        System.out.println("Please stand clear of the doors");

                        SoundManager.playDoorChimeBlocking();

                        SoundManager.playAnnouncement(nextRoomId, player.getCurrentLine());
                        System.out.println("Arriving at " + nextRoomId);

                        Room newRoom = rooms.get(nextRoomId);
                        System.out.println(newRoom.getLongDescription());

                        System.out.println("Remaining Balance: $" + player.getPrestoBalance());
                        System.out.println("You arrived at " + nextRoomId + ".");

                        if (player.getCurrentRoomId().equals("Broadview Station")) {
                            System.out.println("\n--- CONFRONTATION ---");
                            System.out.println(
                                    "A crackhead steps out: 'EUGHHGUEHUUGH! Give me your PRESTO card and your money!'");
                            System.out.println("The exits are blocked! What will you do?");
                            System.out.println("Commands: ATTACK [item], or RUN");
                            isBeingRobbed = true;
                        }

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
                    String itemName = input.substring(command.length()).trim();
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
                    if (itemToUse == null) {
                        System.out.println("You don't have that item.");
                        return;
                    }

                    Room room = rooms.get(player.getCurrentRoomId());

                    if (powerStations.contains(room.getId())) {

                        if (room.isPuzzleSolved()) {
                            System.out.println("This station is already repaired.");
                            return;
                        }

                        if (itemToUse.getName().equalsIgnoreCase("Screwdriver") ||
                                itemToUse.getName().equalsIgnoreCase("Crowbar")) {

                            System.out.println("You open the generator panel...");
                            System.out.println("You repair the power system!");

                            room.solvePuzzle();
                            player.repairPowerStation();

                            System.out.println("Power stations fixed: " +
                                    player.getPowerStationsRepaired() + "/4");

                            return;
                        } else {
                            System.out.println("That item cannot fix this station.");
                            return;
                        }
                    }

                    handleUseItem(itemToUse, player, rooms);
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
            case "reload":
                player.addMoney(10.0);
                System.out.println("You have added $10 onto your PRESTO CARD");
                System.out.println("Your new PRESTO Balance is: $" + player.getPrestoBalance());
                break;
            case "help":
                System.out.println(
                        "Commands: go [dir], look, take [item], drop [item], use [item], inventory, attack [item], run, busk, help");
                break;

            case "shuttle":
                if (words.length < 2) {
                    System.out.println("Use: shuttle [station name]");
                    return;
                }

                if (!Line5Down) {
                    System.out.println("Line 5 Eglinton is running.");
                    return;
                }

                String current = player.getCurrentRoomId();

                if (!Line5Stations.contains(current)) {
                    System.out.println("Shuttle buses only operate along Line 5 Eglinton.");
                    return;
                }

                Set<String> allowed = shuttleBuses.get(current);

                if (allowed == null || allowed.isEmpty()) {
                    System.out.println("There are no Shuttle Buses at this station.");
                    return;
                }

                String targetInput = input.substring(command.length()).trim();

                String matchedStation = null;

                for (String s : allowed) {
                    String normalizedStation = s.toLowerCase().replace(" station", "");
                    String userInput = targetInput.toLowerCase().replace(" station", "");

                    if (normalizedStation.contains(userInput)) {
                        matchedStation = s;
                        break;
                    }
                }

                if (matchedStation == null) {
                    System.out.println("Shuttle cannot go to " + targetInput + " from here.");
                    return;
                }

                player.setCurrentRoomId(matchedStation);

                System.out.println("Arriving at " + matchedStation);

                Room newRoom = rooms.get(matchedStation);
                if (newRoom != null) {
                    System.out.println(newRoom.getLongDescription());
                }

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
            System.out.println("You wave your " + weapon.getName() + " aggressively!");
            System.out.println("The crackhead's eyes widen. 'Woah! Take it easy! EUGHGUGHUGHGUGHHUG!'");
            System.out.println("He disappears into the tunnels. You are safe.");
            isBeingRobbed = false;
        } else if (name.contains("ttc_employee_card") || name.contains("presto")) {
            System.out.println("You try to defend yourself with a " + weapon.getName() + "...");
            System.out.println("The crackhead laughs. EUGUUUGHUHUUHGHG. 'Is that a joke?'");
            System.out.println("He shoves you and steals your PRESTO card from your pocket before running away.");
            player.addMoney(-5.0);
            isBeingRobbed = false;
        } else {
            System.out.println("That item isn't very effective as a weapon.");

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