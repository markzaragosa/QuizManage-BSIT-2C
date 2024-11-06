package Zaragoza;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Quiz Management System**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Manage Users");
            System.out.printf("|%-5s%-95s|\n", "", "2. Manage Quizzes");
            System.out.printf("|%-5s%-95s|\n", "", "3. Manage Questions");
            System.out.printf("|%-5s%-95s|\n", "", "4. Take a Test");
            System.out.printf("|%-5s%-95s|\n", "", "5. Reports");
            System.out.printf("|%-5s%-95s|\n", "", "6. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
            int choice;
            while (true) {
                try {
                    choice = input.nextInt();
                    if (choice > 0 && choice < 7) {
                        break;
                    } else {
                        System.out.printf("|%-5sEnter Choice Again: ", "");
                    }
                } catch (Exception e) {
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ", "");
                }
            }
            switch (choice) {
                case 1:
                    ManageUser ms = new ManageUser();
                    ms.manage_user();
                    break;
                case 2:
                    ManageQuiz qm = new ManageQuiz();
                    qm.manage_quiz();
                    break;
                case 3:
                    ManageQuestion qm2 = new ManageQuestion();
                    qm2.manage_question();
                    break;
                case 4:
                    TakeTest tt = new TakeTest();
                    tt.test();
                    break;
                case 5:
                    Report r = new Report();
                    r.report();
                    break;
                default:
                    System.out.printf("|%-5sDo You Really Want to Exit (Yes/No): ", "");
                    String exit2;
                    while(true){
                        try{
                            exit2 = input.next();
                            if(exit2.equalsIgnoreCase("yes")){
                                exit = false;
                                break;
                            }else{
                                break;
                            }
                        }catch(Exception e){
                            System.out.print("|\tEnter (Yes/No) Again: ");
                        }
                    }
                    break;
            }
        } while (exit);
        input.close();
    }
}
