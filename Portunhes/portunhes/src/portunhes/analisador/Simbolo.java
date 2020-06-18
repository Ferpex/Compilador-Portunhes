
package portunhes.analisador;


public class Simbolo
{
    String tipo;
    String valor;
    String token;
    String linha;
    String lexema;
    boolean usado;

    public String getTipo()
    {
        return tipo;
    }

    public String getValor()
    {
        return valor;
    }

    public String getToken()
    {
        return token;
    }

    public String getLinha()
    {
        return linha;
    }

    public String getLexema()
    {
        return lexema;
    }

    public boolean isUsado()
    {
        return usado;
    }
    
    public Simbolo(String tipo, String valor, String token, String linha, String lexema)
    {
        this.tipo = tipo;
        this.valor = valor;
        this.token = token;
        this.linha = linha;
        this.lexema = lexema;
        usado = true;
    }

    public Simbolo()
    {
        token = "nada";
        usado = false;
    }

    public Simbolo(String token, String linha, String lexema)
    {
        this.token = token;
        this.linha = linha;
        this.lexema = lexema;
        usado = false;
    }

    public Simbolo(String tipo, String token, String linha, String lexema)
    {
        this.tipo = tipo;
        this.token = token;
        this.linha = linha;
        this.lexema = lexema;
        usado = false;
    }
    
    
}
