package com.girlkun.models.map.BDKB;

import com.girlkun.models.boss.list_boss.BDKB.TrungUyXanhLo;
//import com.girlkun.models.boss.list_boss.phoban.TrungUyXanhLoBdkb1;
import com.girlkun.models.item.Item;
import com.girlkun.models.boss.list_boss.BDKB.TrungUyXanhLo;
import com.girlkun.models.map.BDKB.BanDoKhoBau;
import static com.girlkun.models.map.BDKB.BanDoKhoBau.TIME_BAN_DO_KHO_BAU;
import com.girlkun.models.player.Player;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.Logger;
import com.girlkun.utils.Util;
import java.util.List;

/**
 *
 * @author BTH
 *
 */
public class BanDoKhoBauService {

    private static BanDoKhoBauService i;

    private BanDoKhoBauService() {

    }

    public static BanDoKhoBauService gI() {
        if (i == null) {
            i = new BanDoKhoBauService();
        }
        return i;
    }
    
    public void openBanDoKhoBau(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.BanDoKhoBau == null) {
                Item item = InventoryServiceNew.gI().findItemBag(player, 611);
                if (item != null && item.quantity > 0) {
                    BanDoKhoBau banDoKhoBau = null;
                    for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                        if (!bdkb.isOpened) {
                            banDoKhoBau = bdkb;
                            break;
                        }
                    }
                    if (banDoKhoBau != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                        banDoKhoBau.openBanDoKhoBau(player, player.clan, level);
                        try {
                            long bossDamage = (20 * level);
                            long bossMaxHealth = (2 * level);
                            bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                            TrungUyXanhLo boss = new TrungUyXanhLo(
                                    player.clan.BanDoKhoBau.getMapById(137),
                                    player.clan.BanDoKhoBau.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                        } catch (Exception e) {
                            Logger.logException(BanDoKhoBauService.class, e, "Error initializing boss");
                                     
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Bản đồ kho báu đã đầy, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu có bản đồ kho báu");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}
