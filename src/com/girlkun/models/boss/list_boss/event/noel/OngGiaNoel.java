package com.girlkun.models.boss.list_boss.event.noel;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

public class OngGiaNoel extends Boss {

    private long lastTimeDrop;

    public OngGiaNoel() throws Exception {
        super(BossID.ONG_GIA_NOEL, BossesData.ONG_GIA_NOEL);
    }

    @Override
    public void reward(Player plKill) {
    }

    @Override
    public void active() {
        this.attack();
    }

    private void giftBox() {
        if (Util.canDoWithTime(lastTimeDrop, 60000)) {
            this.chat("Hô hô hô, quà của các cháu đây!");
            ItemMap item = new ItemMap(zone, 648, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            ItemMap item2 = new ItemMap(zone, 648, 1, this.location.x + Util.nextInt(50), this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            ItemMap item3 = new ItemMap(zone, 648, 1, this.location.x - Util.nextInt(50), this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), -1);
            if (Util.isTrue(1, 3)) {
                Service.gI().dropItemMap(this.zone, item);
            }
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, item2);
            }
            if (Util.isTrue(1, 7)) {
                Service.gI().dropItemMap(this.zone, item3);
            }
            lastTimeDrop = System.currentTimeMillis();
        }
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100)) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.location == null) {
                    return;
                }
                if (Util.getDistance(this, pl) <= 752002) {
                    if (Util.isTrue(5, 20) && Util.getDistance(this, pl) > 50) {
                        this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)), pl.location.y);
                    } else if (Util.getDistance(this, pl) <= 50) {
                        this.giftBox();
                    }
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return 0; 
    }
}
