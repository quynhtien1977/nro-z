package com.girlkun.models.player;
import com.girlkun.models.player.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Playerao {

    private String playerName;
    private long power;
    private int appearanceId;
    private long hp;
    private Location location;
    private String gender;
    private int currentMapId;
    private static List<String> possibleNames = new ArrayList<>(Arrays.asList("gojo"));
    private static List<String> usedNames = new ArrayList<>();
    private static final List<String> skills = Arrays.asList("DRAGON", "KHIEU_NANG_LUONG");
    private static final List<Integer> mapIds = Arrays.asList(14);

    public Playerao() {
        this.power = generateRandomPower();
        this.playerName = generateRandomName();
        this.appearanceId = selectRandomAppearance();
        this.hp = generateRandomHP();
        this.gender = generateRandomGender();
        this.location = generateRandomLocation();
    }

    private long generateRandomPower() {
        Random random = new Random();
        long min = 200_000_000L;
        long max = 89_090_900_000L;
        return random.nextLong() % (max - min + 1) + min;
    }

private String generateRandomName() {
    Random random = new Random();
    String randomName;

    if (possibleNames.isEmpty()) {
        randomName = "halica" + random.nextInt(100);
    } else {
        int randomIndex = random.nextInt(possibleNames.size());
        String selectedName = possibleNames.get(randomIndex);

        String randomSuffix = String.format("%04d", random.nextInt(100));
        randomName = selectedName + randomSuffix;

        usedNames.add(randomName);
        possibleNames.remove(selectedName);
    }

    return randomName;
}

    private int selectRandomAppearance() {
        Random random = new Random();
        List<Integer> appearanceIds = Arrays.asList(405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417,
                418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430,
                431, 432, 433 , 451);

        int index = random.nextInt(appearanceIds.size());
        return appearanceIds.get(index);
    }

    private long generateRandomHP() {
        Random random = new Random();
        long min = 293_084L;
        long max = 1_289_974L;
        return random.nextLong() % (max - min + 1) + min;
    }

private String generateRandomGender() {
    Random random = new Random();
    int randomValue = random.nextInt(3); // Sinh một số nguyên từ 0 đến 2

    switch (randomValue) {
        case 0:
            System.out.println("Trái đất");
            return "Trái đất";
        case 1:
                System.out.println("Namếc");
            return "Namếc";
        case 2:
                System.out.println("Xayda");
            return "Xayda";
    }
    return "1"; // hoặc giá trị mặc định khác nếu cần
}

    public void joinMap(int mapId) {
        currentMapId = mapId;
        System.out.println(playerName + " joined map " + mapId);
    }
    private Location generateRandomLocation() {
        Random random = new Random();
        Location newLocation = new Location();
        newLocation.x = random.nextInt(1401);
        newLocation.y = random.nextInt(401);
        return newLocation;
    }
public void moveWithinMap() {
    // Thực hiện logic di chuyển trong bản đồ ở đây
    // Ví dụ:
    Random random = new Random();
    int newX = location.x + random.nextInt(21) - 10; // Di chuyển từ -10 đến 10 đơn vị xung quanh tọa độ hiện tại
    int newY = location.y + random.nextInt(21) - 10;

    // Cập nhật tọa độ mới
    location.x = Math.max(0, Math.min(1400, newX)); // Đảm bảo người chơi không rời khỏi kích thước bản đồ
    location.y = Math.max(0, Math.min(400, newY));
}
    public void moveToRandomMap() {
        int newMapId = selectRandomMap();
        System.out.println(playerName + " moved to map " + newMapId);
    }

    private int selectRandomMap() {
        Random random = new Random();
        int index = random.nextInt(mapIds.size());
        return mapIds.get(index);
    }

public static void createVirtualPlayers(int totalPlayers) {
    List<Playerao> players = new ArrayList<>();
    Random random = new Random(); // Khởi tạo đối tượng Random

    for (int i = 0; i < totalPlayers; i++) {
        Playerao player = new Playerao();
        players.add(player);

        Thread playerThread = new Thread(() -> {
            while (true) {
                player.performRandomSkill();
                player.moveToRandomMap();
                player.moveWithinMap();

                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        playerThread.start();
    }
}

    public String performRandomSkill() {
        Random random = new Random();
        int index = random.nextInt(skills.size());
        return skills.get(index);
    }

    // Other getters and setters

class Location {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
}