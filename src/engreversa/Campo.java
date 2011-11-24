
package engreversa;

public class Campo {
    public String nome;
    public String tipo;
    public String tamanho;
    public Boolean pkey;
    public Boolean nao_nulo;
    public Boolean fkey;
    public String tabela_fk;
    public String coluna_fk;
    public String atualizar;
    public String deletar;

    public Campo() { }

    @Override
    public String toString() {
        String saida = "`" + nome + "` " + tipo;

        if (tamanho != null && tamanho.length() > 0) {
            saida += "(" + tamanho + ")";
        }
        
        if (pkey) {
            saida += " PRIMARY KEY";
        }

        if (fkey) {
            saida += " REFERENCES `" + tabela_fk + "` (`" + coluna_fk + "`)";
            
            if (atualizar != null) {
                saida += " ON UPDATE " + atualizar;
            }

            if (deletar != null) {
                saida += " ON DELETE " + deletar;
            }

        }

        if (nao_nulo) {
            saida += " NOT NULL";
        }

        //if (atualizar != null || delet)



        return saida;
    }
}
