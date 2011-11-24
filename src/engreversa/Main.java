package engreversa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static Connection conn;

    public static void main(String[] args) throws SQLException {

        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EngReversa", "postgres", "");

        ler_banco("EngReversa", "public", "itens");
    //    ler_banco("EngReversa", "public", "produto");
//        ler_banco("EngReversa", "public", "venda");

        conn.close();
    }

    static void ler_banco(String banco_, String schema_, String tabela_) throws SQLException {
        String saida = "";
        String sql = "SELECT c.column_name, "
                + "(select pg_catalog.col_description(oid, c.ordinal_position::int) from "
                + "pg_catalog.pg_class pc where pc.relname = c.table_name) as "
                + "column_comment, k2.table_name as fk_table, k2.column_name as fk_column, "
                + " * "
                /*+ tc.constraint_type, c.column_default, c.is_nullable, "
                + "c.data_type, c.character_maximum_length, k2.table_name as fk_table, "
                + "k2.column_name as fk_column, update_rule, delete_rule "*/
                + "FROM information_schema.columns as c "
                + "LEFT JOIN information_schema.key_column_usage as k "
                + "ON c.table_catalog = k.table_catalog "
                + "AND c.table_schema = k.table_schema AND c.table_name = k.table_name "
                + "AND c.column_name = k.column_name "
                + "LEFT JOIN information_schema.referential_constraints as rc "
                + "ON c.table_catalog = rc.constraint_catalog "
                + "AND c.table_schema = rc.constraint_schema "
                + "AND k.constraint_name = rc.constraint_name "
                + "LEFT JOIN information_schema.key_column_usage as k2 "
                + "ON rc.unique_constraint_catalog = k2.constraint_catalog "
                + "AND rc.unique_constraint_schema = k2.constraint_schema "
                + "AND rc.unique_constraint_name = k2.constraint_name "
                + "AND k.position_in_unique_constraint = k2.ordinal_position "
                + "LEFT JOIN information_schema.table_constraints as tc "
                + "ON c.table_catalog = tc.table_catalog "
                + "AND c.table_schema = tc.table_schema "
                + "AND c.table_name = tc.table_name "
                + "AND k.constraint_name = tc.constraint_name "
                + "AND tc.constraint_type <> 'CHECK' "
                + "WHERE "
                + "c.table_catalog = '" + banco_ + "' "
                + "AND c.table_schema = '" + schema_ + "' "
                + "AND c.table_name = '" + tabela_ + "' "
                + "ORDER BY c.ordinal_position, tc.constraint_type DESC;";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        Tabela tabela = new Tabela(tabela_);

        while (rs.next()) {

            Campo c = new Campo();
            c.nome = rs.getString("column_name");

            if (rs.getString("column_default") != null
                    && rs.getString("column_default").contains("nextval")) {
                c.tipo = "serial";
            } else {
                c.tipo = rs.getString("data_type");
            }

            c.tamanho = rs.getString("character_maximum_length");
            c.pkey = rs.getString("constraint_type") != null
                    && rs.getString("constraint_type").contains("PRIMARY");
            c.fkey = rs.getString("constraint_type") != null
                    && rs.getString("constraint_type").contains("FOREIGN");
            c.nao_nulo = rs.getString("is_nullable").equals("NO");
            c.coluna_fk = rs.getString("fk_column");
            c.tabela_fk = rs.getString("fk_table");
            c.atualizar = rs.getString("update_rule");
            c.deletar   = rs.getString("delete_rule");

            tabela.add(c);
        }

        saida = tabela.toString();

        stmt.close();
        System.out.println(saida);
    }
}
