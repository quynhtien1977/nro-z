package com.girlkun.models.matches.pvp;

import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.jdbc.daos.SuperRankDAO;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.network.io.Message;
import com.girlkun.models.player.Player;
import com.girlkun.server.Client;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;

import java.util.List;

public class SuperRankService {

    private static SuperRankService instance;

    public static final String TEXT_DANG_THI_DAU = "Bạn đang trong thời gian thi đấu.";
    public static final String TEXT_DOI_THU_DANG_THI_DAU = "Đối thủ đang trong thời gian thi đấu.";
    public static final String TEXT_DANG_CHO = "Bạn đang trong hàng chờ thi đấu.";
    public static final String TEXT_DOI_THU_CHO_THI_DAU = "Đối thủ đang trong hàng chờ thi đấu.";
    public static final String TEXT_DUOI_HANG = "Bạn không thể thách đấu người hạng cao hơn quá nhiều.";
    public static final String TEXT_CHINH_MINH = "Không thể tự thách đấu bản thân.";
    public static final String TEXT_KHONG_THE_THI_DAU_TREN_2_HANG = "Chỉ có thể thách đấu người xếp trên tối đa 2 hạng.";
    public static final String TEXT_TOP_100 = "Danh sách Top 100 Super Rank";
    public static final String TEXT_CHO_IT_PHUT = "Hệ thống đang bận, vui lòng chờ ít phút hoặc chuyển khu để thi đấu.";
    public static final String TEXT_TOP_10 = "Chúc mừng %1 vươn lên hạng %2 tại Giải đấu Siêu Hạng!";
    public static final String TEXT_THANG = "Chúc mừng bạn giành chiến thắng và vươn lên hạng %1.";
    public static final String TEXT_THUA = "Rất tiếc bạn đã thất bại.";
    public static final String TEXT_SAN_SANG = "Sẵn sàng!";
    public static final String TEXT_SAN_SANG_CHUA = "Sẵn sàng chưa?";
    public static final String TEXT_CLONE_THUA = "Mạnh lắm, cậu thắng rồi!";
    public static final String TEXT_CLONE_THANG = "Bọn ta không dễ bị đánh bại đâu!";

    public static SuperRankService gI() {
        if (instance == null) {
            instance = new SuperRankService();
        }
        return instance;
    }

    public void competing(Player player, long id) {
        if (player.zone.map.mapId != 113 || id == -1) {
            return;
        }
        int menuType = player.iDMark.getIndexMenu();
        Player pl = loadPlayer((int) id);
        if (pl == null) {
            return;
        }
        if (SuperRankManager.gI().currentlyCompeting(player.id)) {
            Service.gI().sendThongBao(player, TEXT_DANG_THI_DAU);
            return;
        } else if (SuperRankManager.gI().currentlyCompeting(pl.id)) {
            Service.gI().sendThongBao(player, TEXT_DOI_THU_DANG_THI_DAU);
            return;
        } else if (SuperRankManager.gI().awaitingCompetition(player.id)) {
            Service.gI().sendThongBao(player, TEXT_DANG_CHO);
            return;
        } else if (SuperRankManager.gI().awaitingCompetition(pl.id)) {
            Service.gI().sendThongBao(player, TEXT_DOI_THU_CHO_THI_DAU);
            return;
        } else if (player.superRank < pl.superRank) {
            Service.gI().sendThongBao(player, TEXT_DUOI_HANG);
            return;
        } else if (player.superRank == pl.superRank || pl.id == player.id) {
            Service.gI().sendThongBao(player, TEXT_CHINH_MINH);
            return;
        } else if (pl.superRank < 10 && player.superRank - pl.superRank > 2) {
            Service.gI().sendThongBao(player, TEXT_KHONG_THE_THI_DAU_TREN_2_HANG);
            return;
        } else if (player.superRankTicket <= 0 && player.inventory.gem < 1) {
            Service.gI().sendThongBao(player, "Bạn không đủ ngọc, còn thiếu 1 ngọc nữa");
            return;
        }

        SuperRankDAO.loadSuperRank(player);

        switch (menuType) {
            case 0:
                // Case 0 is just checking top 100 which should not really trigger a match, but assuming menu context
                Service.gI().sendThongBao(player, TEXT_TOP_100);
                break;
            case 1:
                if (SuperRankManager.gI().SPRCheck(player.zone)) {
                    Service.gI().sendThongBao(player, TEXT_CHO_IT_PHUT);
                    SuperRankManager.gI().addWSPR(player.id, pl.id);
                } else {
                    SuperRankManager.gI().addSPR(new SuperRank(player, id, player.zone));
                }
                break;
            case 2:
                Zone freeZone = getZone(113);
                if (freeZone != null) {
                    SuperRankManager.gI().addSPR(new SuperRank(player, id, freeZone));
                } else {
                    Service.gI().sendThongBao(player, "Hết khu giải đấu trống. Vui lòng thử lại sau.");
                }
                break;
        }
    }

    public void topList(Player player, int type) {
        long st = System.currentTimeMillis();
        player.iDMark.setIndexMenu(type);
        Message msg = null;
        try {
            List<SuperRankBuilder> list = type == 0 ? SuperRankDAO.getPlayerListInRank(player.superRank, 100) : SuperRankDAO.getPlayerListInRankRange(player.superRank, 11);
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top 100 Cao Thủ");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                SuperRankBuilder sb = list.get(i);
                msg.writer().writeInt(sb.getRank());
                msg.writer().writeInt((int) sb.getId());
                msg.writer().writeShort(sb.getHead());
                msg.writer().writeShort(-1);
                msg.writer().writeShort(sb.getBody());
                msg.writer().writeShort(sb.getLeg());
                msg.writer().writeUTF(sb.getName());
                msg.writer().writeUTF(textStatus(sb));
                msg.writer().writeUTF(sb.getInfo());
            }
            player.sendMessage(msg);
            msg.cleanup();
            for (SuperRankBuilder sb : list) {
                sb.dispose();
            }
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.log("Processing time: " + (System.currentTimeMillis() - st) + " milliseconds", "INFO");
    }

    public Player loadPlayer(int id) {
        Player pl = GodGK.loadById(id);
        if (pl != null) {
            com.girlkun.jdbc.daos.SuperRankDAO.loadSuperRank(pl);
            if (pl.setClothes != null) pl.setClothes.setup();
            if (pl.nPoint != null) pl.nPoint.calPoint();
        }
        return pl;
    }

    public Player getPlayer(int id) {
        return Client.gI().getPlayer(id);
    }

    public String textStatus(SuperRankBuilder srb) {
        if (SuperRankManager.gI().awaitingCompetition(srb.getId())) {
            return TEXT_DANG_CHO;
        } else if (SuperRankManager.gI().currentlyCompeting(srb.getId())) {
            return SuperRankManager.gI().getCompeting(srb.getId());
        }
        return textReward(srb.getRank());
    }

    public String textReward(int rank) {
        String text = "";
        if (rank == 1) {
            text = "+1000 hồng ngọc/ ngày";
        } else if (rank >= 2 && rank <= 10) {
            text = "+200 hồng ngọc/ ngày";
        } else if (rank >= 11 && rank <= 100) {
            text = "+50 hồng ngọc/ ngày";
        } else if (rank >= 101 && rank <= 1000) {
            text = "+10 hồng ngọc/ ngày";
        }
        return text;
    }

    public Zone getZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        try {
            if (map != null) {
                int zoneId = 0;
                while (zoneId < map.zones.size()) {
                    Zone zonez = map.zones.get(zoneId);
                    if (!SuperRankManager.gI().SPRCheck(zonez)) {
                        return zonez;
                    }
                    zoneId++;
                }
            }
        } catch (Exception e) {
            Logger.logException(SuperRankService.class, e, "Error getZone");
        }
        return null;
    }

}
