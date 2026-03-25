package Concessionaria.helper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    // Formato de moeda para o padrão brasileiro (R$)
    private static final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    // Conversão String para Date
    public static Date stringParaData(String dataString) {
        try {
            return sdf.parse(dataString);
        } catch (Exception e) {
            System.out.println("Erro ao converter data.");
            return null;
        }
    }

    // Conversão Date para String
    public static String dataParaString(Date data) {
        return sdf.format(data);
    }

    // Conversão Double para String (Moeda R$)
    public static String doubleParaString(Double valor) {
        return nf.format(valor);
    }

    // Conversão String para Double (Removendo R$ se necessário)
    public static Double stringParaDouble(String valorString) {
        try {
            String limpo = valorString.replace("R$", "").replace(".", "").replace(",", ".").trim();
            return Double.parseDouble(limpo);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Pausar execução do console
    public static void pausar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Erro ao pausar.");
        }
    }
}