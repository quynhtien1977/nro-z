/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.map.gas;

import com.girlkun.models.clan.Clan;

/**
 *
 * @author Administrator
 */
public class TopGas {

    public Clan clan;
    public String Name;
    public int Level;
    public long TimeDone;

    public TopGas(Clan clan) {
        Name = clan.name;
        Level = clan.levelKhiGas;
        TimeDone = clan.TimeDoneKhiGas;
        this.clan = clan;
    }
}
