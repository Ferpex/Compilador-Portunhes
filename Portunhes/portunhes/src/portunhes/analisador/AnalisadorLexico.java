package portunhes.analisador;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.scene.control.TableView;

public class AnalisadorLexico {
    
    int index = 0;
    int linha = 1;
    String codigoFonte;
    AnalisadorSintatico sinta = new AnalisadorSintatico();
    Semantica sem = new Semantica();
    PalavraReservada reserva = new PalavraReservada();
    public String identificaPalavra(String codigo)
    { 
        String palavra = "";
        while (index < codigo.length() && (codigo.charAt(index) == ' ' || codigo.charAt(index) == '\n'))
        {
            if(codigo.charAt(index) == '\n')
                linha++;
            index++;
        }
        
        while (index < codigo.length() && codigo.charAt(index) != '\n' && codigo.charAt(index) != ' ')
        {
            if(codigo.charAt(index) == '\n')
                linha++;
            palavra += codigo.charAt(index);
            index++;
        }
        
        return palavra;      
    }
    
    public String Erros(List possiveis)
    {
        String erros = "";
        
        for(int i=0;i<possiveis.size();i++)
        {
            erros += " "+possiveis.get(i).toString();
            if(i+1<possiveis.size())
            {
                erros += " ou";
            }
        }
        
        return erros;
    }
    public ErroTK compilar(String codigo)
    {
        ErroTK e = new ErroTK();
        Stack pilha = new Stack();
        String msg = "";
        String erros = "";
        List<String> possiveis = null;
        List<String> aux = new ArrayList<>();
        codigo = codigo.toLowerCase();
        Simbolo s = new Simbolo();
        codigoFonte = codigo;
        String palavra = "";
        String token = "";
        
        String antToken = "";
        String antPalavra = "";
        
        while (index < codigo.length())
        {
            palavra = identificaPalavra(codigoFonte);
            if(palavra!="")
            {
                token = reserva.getToken_Palavra(palavra);
            
            s = sem.AnaliseSemantica(token, palavra, linha+"", antToken, antPalavra, e);
            if(!s.getToken().equals("nada"))
            {
                e.add(s);
            }
            if(token.equals("tipo")||token.equals("id"))
            {
                antToken = token;
                antPalavra = palavra;
            }
            if(token.equals("erro"))
            {
                index = codigo.length();
            }
            else
            {
               aux = possiveis;
               possiveis = sinta.executa(pilha, token, possiveis);
               int var = codigo.length();
               int tam = palavra.length();
               if(!pilha.isEmpty() && pilha.lastElement().equals("erro"))
               {
                   index = codigo.length();//sair da repeticao
               }
               if(pilha.isEmpty() && index+palavra.length() < codigo.length())
               {
                   pilha.push("erro_chaves");
                   index = codigo.length();//sair da repeticao
               }
               if(possiveis==null)
               {
                   possiveis = aux;
                   erros = Erros(possiveis);
                   index = codigo.length();//sair da repeticao
               }
            }
            }
        }
        if(token.equals("erro"))
        {
            msg = "ERRO na linha "+linha+"\n";
            msg += "Palavra: "+palavra+"\n";
            msg += "Palavra não é aceitavel\n";
        }
        else if(!pilha.isEmpty() && pilha.lastElement().equals("erro"))
        {
            msg = "ERRO na linha "+linha+"\n";
            msg += "Palavra: "+palavra+"\n";
            msg += "Esperava: inicio(){";
        }
        else if(!pilha.isEmpty() && pilha.lastElement().equals("erro_chaves"))
        {
            msg = "ERRO na linha "+linha+"\n";
            msg += "Palavra: "+palavra+"\n";
            msg += "Há mais } do que { ";
        }
        else if(erros!="")
        {
            msg = "ERRO na linha "+linha+"\n";
            erros = Erros(aux);
            msg += "Palavra: "+palavra+"\n";
            msg += "Esperava : " +erros;
        }
        else if(!pilha.isEmpty())
        {
            msg = "ERRO Ainda falta }";
            msg += "\n pilha:"+pilha.pop().toString();
        }
        sem.Usados(e);
        if(e.erro=="" && msg=="")
        {
            e.erro = "Compilado com sucesso!";
        }
        else
            e.erro += msg;
        return e;
        
    }              
}
