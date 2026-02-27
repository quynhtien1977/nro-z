package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import static java.time.temporal.TemporalQueries.zone;
import java.util.Random;

public class ThanThienSu extends Boss {

    public ThanThienSu() throws Exception {
        super(BossID.THAN_HUY_DIET, BossesData.THAN_HUY_DIET);
    }

    public void reward(Player plKill) {
        int[] itemDos1 = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int[] itemDos2 = new int[]{14, 15, 16, 17};
        int[] itemDos3 = new int[]{650, 652, 654, 651, 653, 655, 657, 659, 661, 658, 660, 662};
        int randomDo = new Random().nextInt(itemDos1.length);
        int randomDo1 = new Random().nextInt(itemDos2.length);
        int randomDo2 = new Random().nextInt(itemDos3.length);
        if (Util.isTrue(1, 30)) {
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos1[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
        if (Util.isTrue(30, 100)) {
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos2[randomDo1], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
   
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
