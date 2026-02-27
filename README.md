# NRO-Z

Server-side source code cho game Ngọc Rồng Online (NRO) private server.

## Tech Stack

- **Language:** Java
- **Database:** MySQL
- **Build:** Apache Ant
- **Dependencies:** Lombok, OkHttp, RxJava, JSON Simple, Apache Commons Lang

## Cấu trúc dự án

```
src/com/girlkun/
├── consts/          # Hằng số game (map, mob, npc, player, task...)
├── data/            # Data loader
├── jdbc/daos/       # Database access (Player, Shop, Gift, History...)
├── models/          # Models chính
│   ├── boss/        # Hệ thống boss (DHVT, Mabu, list_boss...)
│   ├── matches/     # PVP system
│   ├── shop/        # Shop system
│   ├── player/      # Player data
│   ├── map/         # Map system
│   ├── npc/         # NPC system
│   ├── mob/         # Monster system
│   ├── task/        # Quest system
│   ├── skill/       # Skill system
│   └── clan/        # Clan system
├── services/        # Business logic (Item, Skill, Task, Reward...)
│   └── func/        # Functions (Combine, Trade, SummonDragon...)
├── network/         # Networking layer
├── server/          # Server core
└── utils/           # Utilities
```

## Cài đặt

### Yêu cầu
- JDK 8+
- MySQL Server
- Apache Ant (hoặc IDE hỗ trợ Ant)

### Bước 1: Database
1. Tạo database MySQL tên `nro`
2. Import file SQL schema (không bao gồm trong repo, liên hệ dev)

### Bước 2: Config
1. Copy `data/config/girlkundb.properties.example` thành `data/config/girlkundb.properties`
2. Cập nhật thông tin kết nối database

### Bước 3: Game Data
1. Đặt game data vào `data/girlkun/` (không bao gồm trong repo do kích thước lớn)

### Bước 4: Build & Run
```bash
# Build
ant build

# Hoặc chạy trực tiếp
java -server -jar -Dfile.encoding=UTF-8 -Xms1000M -Xmx1000M dist/Kaizv1.2.jar
```

## Tính năng đã triển khai

- ✅ Hệ thống tạo nhân vật mới
- ✅ Shop system (mua/bán vật phẩm, bán đồ thần/hd/thiên sứ)
- ✅ Hệ thống boss DHVT (Đại Hội Võ Thuật)
- ✅ Mabu system (boss + tầng)
- ✅ Doanh Trại Độc Nhãn (drop ngọc rồng)
- ✅ Ngũ Hành Sơn (shop Hồng đào, đổi thưởng, cải trang)
- ✅ Map capsule system
- 🔵 Hành tinh Yandart (đang phát triển)
- ⬜ Con Đường Rắn Độc (chưa bắt đầu)

## License

Private project. All rights reserved.
