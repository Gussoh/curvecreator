

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gussoh
 */
public class test {
    public static void main(String[] args) {
        System.out.print("{");
        double sum = 1;
        System.out.print(sum + ", ");
        for (int i = 1; i < 50; i++) {
            sum *= i;
            System.out.print(sum + ", ");
            System.out.flush();
        }
    }
}

