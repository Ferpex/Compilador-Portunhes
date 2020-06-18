
package portunhes.analisador;

public class Semantica
{
    public Simbolo AnaliseSemantica(String token,String palavra,String linha,String antToken,String antPalavra,ErroTK e)
    {
        Simbolo s = new Simbolo();
        Simbolo var = new Simbolo();
        
        if(token.equals("tipo"))
        {
            antToken = token;
            antPalavra = palavra;
        }
        else if(token.equals("id"))
        {
            if(!declarado(palavra, e))
            {
                if(antPalavra.equals("inteiro")||antPalavra.equals("boleano")||antPalavra.equals("quebrado")||antPalavra.equals("palavra")||antPalavra.equals("caracter"))
                {
                    s = new Simbolo(token, linha, palavra);
                    s.tipo = antPalavra;
                    e.variaveis.add(s);
                }
                else
                {
                    e.erro+="Variavel "+s.lexema+" na linha "+s.linha+" não foi declarada e não possui tipo!\n";
                    e.ant= "";
                }     
            }
            else
            {
                /*s = new Simbolo(token, linha, palavra);
                s.tipo = antPalavra;*/
                if(antPalavra.equals("inteiro")||antPalavra.equals("boleano")||antPalavra.equals("quebrado")||antPalavra.equals("palavra")||antPalavra.equals("caracter"))
                {
                    e.erro+="Variavel "+s.lexema+" na linha "+s.linha+" já foi declarada\n";
                }
                else
                {
                    if(antPalavra.equals(palavra)&& !e.ant.isEmpty())
                    {
                        System.out.println(antPalavra);
                        for(int i=0;i<e.tab.size();i++)
                        {
                            if(e.ant.equals(e.get(i).getLexema()))
                            {
                                if(!get(palavra, e).getTipo().equals(e.get(i).getTipo()))
                                {
                                    e.erro+="Variavel "+palavra+" na linha "+e.get(i).linha+" esperava tipo "+e.get(i).tipo+ " e recebeu "+get(palavra,e).getTipo()+"\n";
                                }
                                else
                                {
                                    
                                }
                            }
                        }
                    }
                    else
                    {
                        e.ant = antPalavra;
                    }
                        
                }
            }
        }
        else if(token.equals("inteiro")||token.equals("boleano")||token.equals("quebrado")||token.equals("palavra")||token.equals("caracter"))
        {
            for(int i=0;i<e.tab.size();i++)
            {
                if(antPalavra.equals(e.get(i).getLexema()))
                {
                    if(token.equals(e.get(i).getTipo()))
                    {
                        e.tab.get(i).valor = palavra;
                        attVar(palavra, e, antPalavra, false);
                    }
                    else
                    {
                       e.erro+="Variavel "+e.get(i).lexema+" na linha "+e.get(i).linha+" esperava tipo "+e.get(i).tipo+ " e recebeu "+token+"\n";
                    }
                }
            }
        }
        else
        {
            s = new Simbolo(token,linha+"",palavra);
        }
        return s;
    }
    public void attVar(String palavra,ErroTK e,String antPalavra,boolean usado)
    {
        for(int i =0;i<e.variaveis.size();i++)
        {
            if(antPalavra.equals(e.getVar(i).getLexema()))
            {
                 e.variaveis.get(i).valor = palavra;
                 e.variaveis.get(i).usado = true;
            }
        }
    }
    public boolean declarado(String palavra,ErroTK e)
    {
        boolean vdd= false;
        for(int i =0;i<e.variaveis.size()&&vdd==false;i++)
            if(palavra.equals(e.getVar(i).getLexema()))
                vdd = true;
        return vdd;
    }
    public void Usados(ErroTK e)
    {
         for(int i =0;i<e.variaveis.size();i++)
         {
             if(e.getVar(i).usado==false)
                e.erro+="Variavel "+e.getVar(i).lexema+" na linha "+e.getVar(i).linha+" declarada mas não usada!\n";
         }
    }
    public Simbolo get(String palavra,ErroTK e)
    {
        Simbolo s = new Simbolo();
        for(int i =0;i<e.variaveis.size();i++)
        {
            if(e.getVar(i).getLexema().equals(palavra))
            {
                s = e.getVar(i);
            }
        }
        return s;
    }
}
