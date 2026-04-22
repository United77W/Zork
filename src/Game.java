import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private CommandParser commandParser;

    public Game() {
        RoomLoader roomLoader = new RoomLoader();
        rooms = roomLoader.loadRooms("rooms.json");
        player = new Player("Union Station");
        commandParser = new CommandParser();
    }

    //
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The TTC is failing.\r\n" + //
                "\r\n" + //
                "Across the city, multiple TTC sub-stations have suddenly failed, causing widespread delays and shutting down any chance of Line 5 opening. No one knows exactly what went wrong but if nothing is done soon, the entire network could collapse.\r\n"
                + //
                "\r\n" + //
                "You have been given a task no one else could handle: travel across the GTA, locate the broken sub-stations, and restore them one by one.\r\n"
                + //
                "\r\n" + //
                "With no map to guide you, you will have to rely on clues, items, and your own decisions to navigate the system. Manage your PRESTO balance carefully, avoid fare enforcement, and be ready for random setbacks like delays or breakdowns.\r\n"
                + //
                "\r\n" + //
                "Fix the system. Restore the power. Open Line 5.\r\n" + //
                "\r\n" + //
                "The city is counting on you.\r\n" + //
                "");
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        System.out.println(currentRoom.getLongDescription());

        while (true) {

            System.out.print("> ");
            String input = scanner.nextLine();
            commandParser.parse(input, player, rooms);

            if (player.getPowerStationsRepaired() >= 4) {
                player.setLine5Restored(true);
                commandParser.setLine5Down(false);
                System.out.println("LINE 5 RESTORED");
                System.out.println("You have successfully fixed all power stations!");
                System.out.println("The slow Shuttle Buses are no longer needed");
                SoundManager.playLine5RestoredAnnouncement();

            }
        }
    }
}
