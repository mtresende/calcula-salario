import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject;

public class ValidaCEP {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String cep;
        boolean cepValido = false;
        
        System.out.print("Digite o CEP: ");
        while (!cepValido) {
            cep = sc.nextLine();

            
            try {
                URL url = new URL("https://viacep.com.br/ws/" + cep + "/json"); // Cria um objeto URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Estabelecendo uma conexão HTTP com a URL
                connection.setRequestMethod("GET"); // Define o método de requisição HTTP como GET para solicitar os dados
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)); // Cria um BufferedReader para ler a resposta da requisição
                String line;
                StringBuilder response = new StringBuilder();
                
                while((line = reader.readLine()) != null) { // // Lê linha por linha da resposta da requisição, até o conteúdo obtido ser diferente de nulo
                    response.append(line); // Cada resposta obtida é anexada ao StringBuilder response
                }
                reader.close();
                
                JSONObject jsonResponse = new JSONObject(response.toString()); //Converte a resposta de JSON para um objeto JSONObject
                
                if (jsonResponse.has("erro")){
                    System.out.print("CEP inválido, digite novamente: ");
                } else {
                    // Extrai os dados do JSON e os armazena em variáveis separadas
                    String logradouro = jsonResponse.getString("logradouro");
                    String bairro = jsonResponse.getString("bairro");
                    String cidade = jsonResponse.getString("localidade");
                    String estado = jsonResponse.getString("uf");
    
                    System.out.print("Endereço: " + logradouro + " - " + bairro + ", " + cidade + " - " + estado);
                    cepValido = true;
                }
                
            } catch (Exception e) {
                System.out.print("CEP inválido, digite novamente: ");
            }
        }
        sc.close();
    }
}
