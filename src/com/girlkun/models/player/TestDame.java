package com.girlkun.models.player;

// đây
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.Zone;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.MapService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import java.util.List;

/**
 * @author BTH sieu cap vippr0
 */
public class TestDame extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initTraidat() {
        init();
    }

    @Override
    public short getHead() {
        return 83;
    }

    @Override
    public short getBody() {
        return 84;
    }

    @Override
    public short getLeg() {
        return 85;
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null && (plAtt.isPl() || plAtt.isPet)) {
        }
        if (plAtt != null && plAtt.isPl() && (plAtt.playerTask.taskMain.id == 11 || plAtt.playerTask.taskMain.id == 27)) {
            TaskService.gI().sendNextTaskMain(plAtt);
            this.chat("Á đù người hướng nội !");
        }
        if (this.nPoint.hp <= 500000000) {
            this.nPoint.hp = this.nPoint.hpMax;
            Service.gI().point(this);
        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            this.chat("Sát thương gây ra: " + Util.numberToMoney(damage));
            return damage;
        } else {
            return 0;
        }
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        String[] textBaoCat = {"|1|NGỌC RỒNG GREEN\nCome Back", "|1|NGỌC RỒNG GREEN\nCome Back", "|1|NGỌC RỒNG GREEN\nCome Back"};
        if (Util.canDoWithTime(lastTimeChat, 5000)) {
            Service.getInstance().chat(this, textBaoCat[Util.nextInt(textBaoCat.length - 1)]);
            lastTimeChat = System.currentTimeMillis();
            this.nPoint.setFullHpMp();
            Service.gI().point(this);
        }
    }

    private void init() {

        int id = Util.randomBossId();
        for (Map m : Manager.MAPS) {
            if (m.mapId == 164 || m.mapId == 165 || m.mapId == 166) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "mr pôro";
                    pl.gender = 0;
                    pl.id = id;
                    pl.isBuNhin = true;
                    pl.nPoint.hpMax = (int) 2000000000;
                    pl.nPoint.hpg = (int) 2000000000;
                    pl.nPoint.hp = (int) 2000000000;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 236;
                    pl.location.y = 336;
                    pl.nPoint.power = 99999999999L;
                    pl.cFlag = 8;
                    joinMap(z, pl);
                    z.setReferee(pl);

                }
            }
        }
    }
}

// }
