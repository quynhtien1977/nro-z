package com.girlkun.models.boss.list_boss.cell;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.boss.list_boss.BDKB.TrungUyXanhLo;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.server.Manager;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;
import com.girlkun.services.PlayerService;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SieuBoHung extends Boss {
    private long lastTimeHapThu;
    private int timeHapThu;
    private int initSuper = 0;
    public SieuBoHung() throws Exception {
        super(BossID.SIEU_BO_HUNG, BossesData.SIEU_BO_HUNG_1, BossesData.SIEU_BO_HUNG_2, BossesData.SIEU_BO_HUNG_3);
    }
    
  
    @Override
     public void reward(Player plKill) {
         TaskService.gI().checkDoneTaskKillBoss(plKill, this);
          rewardFutureBoss(plKill);
          
    }
     
     @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

    @Override
    public void active() {
      if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
            this.hapThu();
        } catch (Exception ex) {
            Logger.getLogger(SieuBoHung.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.attack();
         super.active();
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
    
    //public void summonTrungUyXanhLo() {
    //for (int i = 0; i < 5; i++) {
        //try {
            //new TrungUyXanhLo(this.zone, 2, Util.nextInt(1000, 16000), Util.nextInt(16000000));
        //} catch (Exception ex) {
            //
        //}
    //}
//}
    
    private void hapThu() throws Exception {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        //this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        long HP =this.nPoint.hp + pl.nPoint.hp;
        if(HP > 2000000000)
        {
            HP = 2000000000;
        }
        if(this.nPoint.hpg < HP)
        {
            this.nPoint.hpg = (int)HP;
        }
        this.nPoint.hp = (int)HP;
        this.nPoint.critg++;
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        //pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000);
    }
}
