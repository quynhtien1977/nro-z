/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.ThanhTich;

import com.girlkun.models.Template;

/**
 *
 * @author Administrator
 */
public class ThanhTich 
{
    public Template.ArchivementTemplate Template;
    public boolean isFinish;
    public boolean isRecieve;
    public ThanhTich()
    {
        
    }
    public ThanhTich(ThanhTich a)
    {
        this.Template = a.Template;
        this.isFinish = a.isFinish;
        this.isRecieve = a.isRecieve;
    }
}
