import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String dados = "dados.txt";
        String nome, cpf, cep = "0", endereco = "0";
        double salarioBruto, descontoInss;
        int dependentes;
        
        List<String[]> trabalhadores = new ArrayList<>(); // Criando lista de Array para armazenar os dados do trabalhador
        
        try (BufferedReader reader = new BufferedReader(new FileReader(dados))){
                String linha;
            while ((linha = reader.readLine()) != null) {
                    String[] trabalhador = linha.split(",");
                    trabalhadores.add(trabalhador);
            }
        } catch (IOException e) {
            System.err.println("Ocorreu um erro");
        }
    
        System.out.print("Digite seu nome: ");
        nome = sc.nextLine();
        
        System.out.print("Digite seu salário bruto: ");
        salarioBruto = sc.nextDouble();
        
        System.out.print("Digite o desconto de INSS: ");
        descontoInss = sc.nextDouble();
        
        sc.nextLine();
        
        System.out.print("Digte o número de dependentes: ");
        dependentes = sc.nextInt();
        
        sc.nextLine();

        double vlrDependentes = dependentes * 189.59;
        double baseIRRF = salarioBruto - descontoInss - vlrDependentes;
        double valorIRRF;
        double salarioLiquido;

        if (baseIRRF <= 2259.20) {
            valorIRRF = 0;

        } else if (baseIRRF >= 2259.21 && baseIRRF <= 2826.65) {
            valorIRRF = baseIRRF * 0.075 - 169.44;

        } else if (baseIRRF >= 2826.66 && baseIRRF <= 3751.05) {
            valorIRRF = baseIRRF * 0.15 - 381.44;

        } else if (baseIRRF >= 3751.06 && baseIRRF <= 4664.68) {
            valorIRRF = baseIRRF * 0.225 - 662.77;

        } else {
            valorIRRF = baseIRRF * 0.275 - 896.00;
        }

        DecimalFormat formato = new DecimalFormat("#,##00.00");

        if (valorIRRF == 0) {
            salarioLiquido = salarioBruto - descontoInss;
            System.out.println("\nSalario Líquido: " + formato.format(salarioLiquido) + "\nIsento de imposto de renda!\n");
        } else {
            salarioLiquido = salarioBruto - descontoInss - valorIRRF;
            System.out.println("\nSalario Líquido: " + formato.format(salarioLiquido) + "\nImposto de renda: " + formato.format(valorIRRF) + "\n");
        }
            
            System.out.print("Digite seu CPF: ");
            cpf = sc.nextLine();
            
            if (cpf.length() != 11) {
                do {
                    System.out.print("CPF inválido, digite novamente: ");
                    cpf = sc.nextLine();
                } while (cpf.length() != 11);
            }
            
            System.out.print("Digite o CEP: ");

            boolean cepValido = false;
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
    
                    endereco = logradouro + " - " + bairro + " " + cidade + " - " + estado;
                    System.out.println("Endereço: " + endereco);
                    cepValido = true;
                }
                
            } catch (Exception e) {
                System.out.print("CEP inválido, digite novamente: ");
            }
        }
        
        String brutoString = formato.format(salarioBruto);
        String liqString = formato.format(salarioLiquido);
        String depString = String.valueOf(dependentes);

        boolean cpfExistente = false;
        for (String[] trabalhador : trabalhadores) {
            if (trabalhador.length >= 2 && trabalhador[1].equals(cpf)) {
                trabalhador[0] = nome;
                trabalhador[2] = liqString;
                trabalhador[3] = depString;
                trabalhador[4] = brutoString;
                trabalhador[5] = cep;
                trabalhador[6] = endereco;
                cpfExistente = true;
                break;
            }
        }

        if (!cpfExistente){
            trabalhadores.add(new String[]{nome, cpf, liqString, depString, brutoString, cep, endereco}); // Armazenando os dados coletados no Array
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dados), StandardCharsets.UTF_8))){
            for (String[] trabalhador : trabalhadores) {
                for (int i = 0; i < trabalhador.length; i++) {
                    writer.write(trabalhador[i]);
                    if (i < trabalhador.length - 1) {
                        writer.write(",");
                    } else {
                        writer.write("\n");
                    }
                }
            }
        } catch(IOException e) {
            System.err.println("Ocorreu um erro");
        }

        sc.close();
    }
}