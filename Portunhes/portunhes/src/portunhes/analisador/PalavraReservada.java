package portunhes.analisador;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PalavraReservada
{
    private ArrayList<Token> palreservadas = new ArrayList<>();
    Pattern quebrado = Pattern.compile("[-|+|0-9][0-9]+,[0-9]+|[0-9]+,[0-9]+");//numeros quebrados positivos, negotivos ou apenas numeros
    Pattern inteiro = Pattern.compile("[-|+|0-9][0-9]+|[0-9]+"); //numeros inteiros positivos, negotivos ou apenas numeros
    Pattern palavra = Pattern.compile("\\'[a-zA-ZçÇ]+\\'");//palavras de a a z ou A a Z com apostrofe
    Pattern id = Pattern.compile("[a-zA-ZçÇ]+");//palavras de a a z ou A a Z
    Pattern caracter = Pattern.compile("\\'[a-zA-ZçÇ]\\'");// caracter com apostrofe
    Pattern boleano = Pattern.compile("(false)|(true)");//numeros 0 ou 1
    public PalavraReservada()
    {
        palreservadas.add(new Token("inicio","inicio(){"));
        palreservadas.add(new Token("se","se("));
        palreservadas.add(new Token("enquanto","enquanto(")); 
        palreservadas.add(new Token("para","para("));
        palreservadas.add(new Token("sentido","++"));
        palreservadas.add(new Token("sentido","--"));
        palreservadas.add(new Token("s_igual","="));
        palreservadas.add(new Token("fim",";"));
        palreservadas.add(new Token("tipo","inteiro"));
        palreservadas.add(new Token("tipo","quebrado"));
        palreservadas.add(new Token("tipo","palavra"));
        palreservadas.add(new Token("tipo","caracter"));
        palreservadas.add(new Token("tipo","boleano"));
        palreservadas.add(new Token("logico","<"));
        palreservadas.add(new Token("logico",">"));
        palreservadas.add(new Token("logico","!="));
        palreservadas.add(new Token("logico","=="));
        palreservadas.add(new Token("logico","<="));
        palreservadas.add(new Token("logico",">="));
        palreservadas.add(new Token("operador","+"));
        palreservadas.add(new Token("operador","-"));
        palreservadas.add(new Token("operador","*"));
        palreservadas.add(new Token("operador","/"));
        palreservadas.add(new Token("s_a_parenteses","("));
        palreservadas.add(new Token("s_f_parenteses",")"));
        palreservadas.add(new Token("s_a_chaves","){"));
        palreservadas.add(new Token("s_f_chaves","}"));
    }
    
    public boolean isReservada(String str)
    {
        for (Token k : palreservadas)
            if (k.getLexema().toString().equals(str))
                return true;
        
        return false;
    }
    public String getToken_Palavra(String lexema)
    {
        String token = "";
        for (Token k : palreservadas)
            if (k.getLexema().toString().equals(lexema))
                token = k.getToken().toString();
        if(token == "")
        {
            Matcher matcher; 
            matcher = quebrado.matcher(lexema);
            if(matcher.matches())
                token = "quebrado";
            else
            {
                matcher = boleano.matcher(lexema);
                if(matcher.matches())
                    token = "boleano";
                else
                {
                    matcher = inteiro.matcher(lexema);
                    if(matcher.matches())
                        token = "inteiro";
                    else
                    {
                        matcher = caracter.matcher(lexema);
                        if(matcher.matches())
                            token = "caracter";
                        else
                        {
                            matcher = palavra.matcher(lexema);
                            if(matcher.matches())
                                token = "palavra";
                            else
                            {
                                matcher = id.matcher(lexema);
                                if(matcher.matches())
                                    token = "id";
                                else
                                token = "erro";
                            }
                            
                        }
                    }
                }
            }
        }
        return token;
    }
}
