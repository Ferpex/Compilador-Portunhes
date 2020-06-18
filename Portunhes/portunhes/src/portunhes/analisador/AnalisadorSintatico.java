package portunhes.analisador;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class AnalisadorSintatico {
    public boolean isPossivel(List<String> possivel, String token)
    {
        boolean vdd = false;
        for(int i= 0;i<possivel.size();i++)
        {
            if(possivel.get(i).equals(token))
                vdd = true;
        }
        return vdd;
    }
    public List<String> executa(Stack pilha,String token,List oldPossiveis)
    {
        Stack pilhaAux = pilha;
        List<String> possiveis =  new ArrayList<>();
        //se for inicio
        if(pilhaAux.empty())
        {
            if(token.equals("inicio"))
            {
                pilhaAux.push("inicio");
                //comandos
                possiveis.add("id");
                possiveis.add("enquanto");
                possiveis.add("para");
                possiveis.add("se");
                possiveis.add("s_f_chaves");
                //atribuicoes
                possiveis.add("tipo");
                
            }
            else
            {
                possiveis.add("inicio");
                pilhaAux.push("erro");
            }
        }
        else
        {
            if(isPossivel(oldPossiveis, token))
            {
                if(token.equals("para"))
                {
                    pilhaAux.push("para");
                    //proximo do para
                    possiveis.add("id");
                }
                if(token.equals("enquanto"))
                {
                    pilhaAux.push("enquanto");
                    //proxmo do enquanto
                    possiveis.add("id");
                }
                if(token.equals("se"))
                {
                    pilhaAux.push("se");
                    //proximo do se
                    possiveis.add("id");
                }
                if(token.equals("operador"))
                {
                    pilhaAux.push("operador");
                    possiveis.add("id");
                    possiveis.add("palavra");
                    possiveis.add("caracter");
                    possiveis.add("inteiro");
                    possiveis.add("quebrado");
                }
                if(token.equals("tipo"))
                {
                    pilhaAux.push("tipo");
                     //proximo do se
                    possiveis.add("id");
                }
                if(token.equals("id"))
                {
                    String tokenAnt = pilhaAux.pop().toString();
                    pilhaAux.push(tokenAnt);
                    if(tokenAnt.equals("tipo"))
                    {
                        //se o anterior era tipo e agr recebeu id
                        //o proximo é 
                        possiveis.add("s_igual");
                    }
                    if(tokenAnt.equals("enquanto"))
                    {
                        //se o anterior era enquanto e agr recebeu id
                        //o proximo é 
                        possiveis.add("logico");
                    }
                    if(tokenAnt.equals("para"))
                    {
                         //se o anterior era para e agr recebeu id
                        //o proximo é 
                        possiveis.add("s_igual");
                    }
                    if(tokenAnt.equals("se"))
                    {
                        //se o anterior era se e agr recebeu id
                        //o proximo é 
                        possiveis.add("logico");
                    }
                    if(tokenAnt.equals("logico"))
                    {
                        //desempilha o logico porque nao há necessidade dele ficar salvo
                        pilhaAux.pop().toString();
                        //se o anterior era logico e agr recebeu id
                        //ve se está em um para
                        String tokenAntant = pilhaAux.pop().toString();
                        pilhaAux.push(tokenAntant);
                        if(tokenAntant.equals("para"))
                        {
                            //se for dentro de um para
                            //o proximo é ;
                            pilhaAux.push("fim2");
                            possiveis.add("fim");
                        }
                        else
                            possiveis.add("s_a_chaves"); // no caso do enquanto
                    }
                    if(tokenAnt.equals("fim1"))
                    {
                        //desempilha fim1 pois nao vai ser mais necessario
                        pilhaAux.pop().toString();
                        //se o anterior era fim1 e agr recebeu id
                        //o proximo é 
                        possiveis.add("logico");
                    }
                    if(tokenAnt.equals("fim2"))
                    {
                        //desempilha fim2 pois nao vai ser mais necessario
                        pilhaAux.pop().toString();
                        //se o anterior era fim1 e agr recebeu id
                        //o proximo é 
                        possiveis.add("sentido");
                    }
                    if(tokenAnt.equals("inicio"))
                    {
                        pilhaAux.push("atribui");
                        possiveis.add("s_igual");
                    }
                    if(tokenAnt.equals("atribui"))
                    {
                        pilhaAux.pop();
                        possiveis.add("operador");
                    }
                    if(tokenAnt.equals("operador"))
                    {
                        pilhaAux.pop();
                        possiveis.add("fim");
                    }
                }
                if(token.equals("inteiro") || token.equals("quebrado") || token.equals("boleano") || token.equals("palavra") || token.equals("caracter"))
                {
                    String tokenAnt = pilhaAux.pop().toString();
                    if(tokenAnt.equals("logico"))
                    {
                        String tokenAntant = pilhaAux.pop().toString();
                        pilhaAux.push(tokenAntant);
                        //se o anterior era logico e agr recebeu inteiro ou quebrado
                        if(tokenAntant.equals("para"))
                        {
                            //se for dentro de um para
                            //o proximo é ;
                            pilhaAux.push("fim2");
                            possiveis.add("fim");
                        }
                        else
                            possiveis.add("s_a_chaves");//no caso do enquanto
                    }
                    else if(tokenAnt.equals("operador"))
                    {
                        possiveis.add("fim");
                    }
                    else if(tokenAnt.equals("atribui"))
                    {
                        possiveis.add("fim");
                    }else
                    {
                        pilhaAux.push(tokenAnt);
                        possiveis.add("fim"); //no caso de atribuicao
                    }
                }
                if(token.equals("s_igual"))
                {
                    //variaveis para atribuicao
                    possiveis.add("palavra");
                    possiveis.add("caracter");
                    possiveis.add("inteiro");
                    possiveis.add("quebrado");
                    possiveis.add("boleano");
                    
                    possiveis.add("id");
                }
                if(token.equals("logico"))
                {
                    //empilha o logico
                    pilhaAux.push("logico");
                    //os possiveis proximos sao
                    possiveis.add("id");
                    possiveis.add("inteiro");
                    possiveis.add("quebrado");
                }
                if(token.equals("s_a_chaves"))
                {
                    //se achou um abre e fecha chaves pode colocar mais comandos
                    possiveis.add("id");
                    possiveis.add("enquanto");
                    possiveis.add("para");
                    possiveis.add("se");
                    possiveis.add("s_f_chaves");
                }
                if(token.equals("fim"))
                {
                    String tokenAnt = pilhaAux.pop().toString();
                    pilhaAux.push(tokenAnt);
                    //se o anterior for tipo e agr tem fim os proximos sao
                    if(tokenAnt.equals("tipo"))
                    {
                        pilhaAux.pop();
                        //comandos
                        possiveis.add("id");
                        possiveis.add("enquanto");
                        possiveis.add("para");
                        possiveis.add("se");
                        possiveis.add("s_f_chaves");
                        //atribuicoes
                        possiveis.add("tipo");
                    }
                    if(tokenAnt.equals("para"))
                    {
                        //empilha fim1 para saber que está no primeiro ;
                        pilhaAux.push("fim1");
                        //se o anterior for o para o proximo possivel é id
                        possiveis.add("id");
                    }
                    if(tokenAnt.equals("fim2"))
                    {
                        //se o anterior for o fim2 o proximo possivel é id
                        possiveis.add("id");
                    }
                    if(tokenAnt.equals("inicio"))
                    {
                        possiveis.add("s_f_chaves");
                    }
                }
                if(token.equals("sentido"))
                {
                    possiveis.add("s_a_chaves");
                }
                if(token.equals("s_f_chaves"))
                {
                    String var = pilhaAux.pop().toString();
                    System.out.println("Variavel: "+var);
                    pilhaAux.push(var);
                    if(!pilhaAux.empty())
                    {
                        //desempilha toda vez que achar um fecha chaves
                        pilhaAux.pop();
                        if(!pilhaAux.empty())
                        {
                            //se ainda nao saiu do escopo do inicio pode colocar mais comandos os possiveis sao
                            possiveis.add("id");
                            possiveis.add("enquanto");
                            possiveis.add("para");
                            possiveis.add("se");
                            possiveis.add("s_f_chaves");
                        }   
                    }
                    else
                    {
                        pilhaAux.push("erro");
                    }
                }
                
            }
            else
            {
                possiveis = null;
            }
        }
        
        return possiveis;
    }
    
}
