package engreversa;

import java.util.ArrayList;

public class Tabela extends ArrayList<Campo> {

    public String nome;

    public Tabela(String nome_) {
        nome = nome_;
    }

    @Override
    public String toString() {
        String saida = "CREATE TABLE `" + nome + "` (\n";

        for (Integer i = 0; i < this.size(); i++) {
            saida += "\t" + this.get(i).toString();

            if (i != this.size() - 1) {
                saida += ",";
            }
            saida += "\n";
        }

        saida += ");\n";

        return saida;
    }
}
