package com.girlkun.models.boss.list_boss.gas;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import static com.girlkun.models.boss.BossStatus.ACTIVE;
import static com.girlkun.models.boss.BossStatus.JOIN_MAP;
import static com.girlkun.models.boss.BossStatus.RESPAWN;
import com.girlkun.models.boss.list_boss.cell.SieuBoHung;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.SkillService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BTH sieu cap vippr0
 */
public class DrLyChee extends Boss {

    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
    private long lastTimeHapThu;
    private int timeHapThu;
    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    private int levell;
    private int initSuper = 1;
    protected Player playerAtt;
    private Player playerSpawn;
    private int timeLive = 2000000000;

    public DrLyChee(Player pl, int level, long dame, int hp, int id) throws Exception {
        super(id, new BossData(
                "DrLyChee", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{1228, 1229, 1230, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((15000 * level)), //dame    
                new int[]{(((int)150000000 * level))}, //hp
                new int[]{148}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));

        this.zone = pl.zone;
        this.levell = level;
        playerSpawn = pl;
    }

    

    @Override
    public void reward(Player plKill) 
    {
        int paramhp = 0;
        int parammp = 0;
        int paramsd = 0;
        int paramgiap = 0;
        for (int i = 0; i <= 4; i++) 
        {
            ItemMap it = new ItemMap(zone, 1965, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            it.x = Util.nextInt(100,300);
            if (levell < 10) {
                paramhp = Util.nextInt(3, 5);
                parammp = Util.nextInt(3, 5);
                paramsd = Util.nextInt(3, 5);
                paramgiap = Util.nextInt(2);
            } else if (levell < 20) {
                paramhp = Util.nextInt(5, 7);
                parammp = Util.nextInt(5, 7);
                paramsd = Util.nextInt(5, 7);
                paramgiap = Util.nextInt(3, 5);
            } else if (levell < 40) {
                paramhp = Util.nextInt(7, 10);
                parammp = Util.nextInt(7, 10);
                paramsd = Util.nextInt(7, 10);
                paramgiap = Util.nextInt(6, 8);
            } else if (levell < 60) {
                paramhp = Util.nextInt(10, 15);
                parammp = Util.nextInt(10, 15);
                paramsd = Util.nextInt(10, 15);
                paramgiap = Util.nextInt(9, 12);
            } else if (levell < 80) {
                paramhp = Util.nextInt(15, 20);
                paramgiap = Util.nextInt(13, 18);
                parammp = Util.nextInt(15, 20);
                paramsd = Util.nextInt(15, 20);
            } else if (levell < 99) {
                paramhp = Util.nextInt(24, 28);
                paramgiap = Util.nextInt(18, 24);
                parammp = Util.nextInt(24, 28);
                paramsd = Util.nextInt(24, 28);
            } else {
                paramhp = Util.nextInt(31, 35);
                parammp = Util.nextInt(31, 35);
                paramsd = Util.nextInt(31, 35);
                paramgiap = Util.nextInt(24, 28);
            }
            it.options.add(new Item.ItemOption(77, paramhp));
            it.options.add(new Item.ItemOption(103, parammp));
            it.options.add(new Item.ItemOption(94, paramgiap));
            it.options.add(new Item.ItemOption(93, 7));
            Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void active() {
        super.active();
    }

    
   public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(30, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                try {
                    new HaChiJack(playerSpawn, levell, (int) 1, (int) 2);
                } catch (Exception ex) {
                           System.err.print("\nError at 118\n");
                    Logger.getLogger(DrLyChee.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void leaveMap() {

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
