package com.girlkun.models.boss.list_boss.nappa;

import java.util.Random;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.services.TaskService;
import com.girlkun.utils.Util;

/**
 *
 * @Stole By Arriety
 */
public class Rambo extends Boss {

    public Rambo() throws Exception {
        super(BossID.RAMBO, BossesData.RAMBO);
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(50, 100)) {
            ItemMap it = new ItemMap(this.zone, 17, Util.nextInt(1,4), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
