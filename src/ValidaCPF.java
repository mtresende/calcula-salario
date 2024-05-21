import java.util.Scanner;

public class ValidaCPF {
    public static void main(String[] args) {
        String cpf;

        Scanner sc = new Scanner(System.in);

        System.out.print("Digite seu CPF: ");
        cpf = sc.nextLine();

        if (cpf.length() != 11) {
            do {
                System.out.print("CPF inválido, digite novamente: ");
                cpf = sc.nextLine();
            } while (cpf.length() != 11);
        }

        sc.close();
    }
}
