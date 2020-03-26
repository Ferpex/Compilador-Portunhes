package portunhes.analisador;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AnalisadorLexico {
    
    int index = 0;
    int linha = 1;
    String codigoFonte;
    AnalisadorSintatico sinta = new AnalisadorSintatico();
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
    public String compilar(String codigo)
    {
        Stack pilha = new Stack();
        String msg = "Compilado com sucesso!";
        String erros = "";
        List<String> possiveis = null;
        List<String> aux = new ArrayList<>();
        codigo = codigo.toLowerCase();
        
        codigoFonte = codigo;
        String palavra = "";
        String token = "";
        while (index < codigo.length())
        {
            palavra = identificaPalavra(codigoFonte);
            if(palavra!="")
            {
            token = reserva.getToken_Palavra(palavra);
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
        }
        return msg;
        
    }              
}
