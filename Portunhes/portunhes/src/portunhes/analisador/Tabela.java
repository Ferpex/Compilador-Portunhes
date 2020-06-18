
package portunhes.analisador;

import java.util.ArrayList;

public class Tabela
{
    ArrayList<Simbolo> tab;

    public Tabela()
    {
        tab = new ArrayList<>();
    }
    public void add(Simbolo e)
    {
        tab.add(e);
    }
    public void addValor(String val,int x)
    {
        tab.get(x).valor = val;
    }
    public void usado(boolean foi,int x)
    {
        tab.get(x).usado = foi;
    }
    public ArrayList<Simbolo> getTable()
    {
        return tab;
    }
    
}
