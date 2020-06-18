/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portunhes.analisador;

import java.util.ArrayList;

/**
 *
 * @author ferna
 */
public class ErroTK
{
    String erro;
    ArrayList<Simbolo> tab;
    ArrayList<Simbolo> variaveis;
    String ant;
    public ErroTK()
    {
        tab = new ArrayList<>();
        variaveis = new ArrayList<>();
        erro="";
        ant = "";
    }

    public void setErro(String erro)
    {
        this.erro = erro;
    }

    public void setTab(ArrayList<Simbolo> tab)
    {
        this.tab = tab;
    }
    public void add(Simbolo s)
    {
        tab.add(s);
    }
    public void addVar(Simbolo s)
    {
        variaveis.add(s);
    }
    public Simbolo get(int i)
    {
        return tab.get(i);
    }
    public Simbolo getVar(int i)
    {
        return variaveis.get(i);
    }
    public String getErro()
    {
        return erro;
    }

    public ArrayList<Simbolo> getTab()
    {
        return tab;
    }
    public ArrayList<Simbolo> getVariaveis()
    {
        return variaveis;
    }
    public String getAnt()
    {
        return ant;
    }
    public void setAnt(String ant)
    {
        this.ant = ant;
    }
    
}
