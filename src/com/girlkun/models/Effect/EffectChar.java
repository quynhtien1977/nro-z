/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.Effect;

/**
 *
 * @author Administrator
 */
public class EffectChar 
{
    public int id;
    public int layer;
    public int loop;
    public int loopCount;
    public int isStand;
    public EffectChar(int id , int layer , int loop , int loopCount,int isStand)
    {
        this.id = id;
        this.layer = layer;
        this.loop = loop;
        this.loopCount = loopCount;
        this.isStand = isStand;
    }
}
