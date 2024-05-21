import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        String idade;
        String nome;
        String cpf;
        String dados = "dados.txt"; // Nome do arquivo criado posteriormente

        List<String[]> pessoas = new ArrayList<>(); // Criando lista de Array para armazenar os dados da pessoa

        try (BufferedReader reader = new BufferedReader(new FileReader(dados))){
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] pessoa = linha.split(",");
                pessoas.add(pessoa);
            }
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        nome = sc.nextLine();

        System.out.print("Digite sua idade: ");
        idade = sc.nextLine();

        System.out.print("Digite seu CPF: ");
        cpf = sc.nextLine();

        boolean cpfExistente = false;
        for (String[] pessoa : pessoas) {
            if (pessoa[2].equals(cpf)) {
                pessoa[0] = nome;
                pessoa[1] = idade;
                cpfExistente = true;
                break;
            }
        }

        if (!cpfExistente){
            pessoas.add(new String[]{nome, idade, cpf}); // Armazenando os dados coletados no Array
        }

        sc.close();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dados))){ // Criando o aqruivo de texto
            for (String[] pessoa : pessoas) { // Atribuindo o Array "pessoas" à variável "pessoa"
                writer.write(String.join(",", pessoa)); // Separando os dados com ","
                writer.newLine(); // Adicionando uma linha
            }
        } catch(IOException e) {
            System.err.println("Ocorreu um erro");
        }
    }
}
