package Zaragoza;

import java.util.Scanner;
import java.sql.*;

public class ManageQuestion {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    ManageQuiz mquiz = new ManageQuiz();

    public void manage_question() {
        boolean exit = true;
        do {
             System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Manage Questions**", "");
            System.out.printf("|%-5s%-50s|\n", "", "1. Add Question");
            System.out.printf("|%-5s%-50s|\n", "", "2. Edit Question");
            System.out.printf("|%-5s%-50s|\n", "", "3. Delete Question");
            System.out.printf("|%-5s%-50s|\n", "", "4. View Questions");
            System.out.printf("|%-5s%-50s|\n", "", "5. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    mquiz.view();
                    add();
                    break;
                case 2:
                    mquiz.view();
                    edit();
                    break;
                case 3:
                    mquiz.view();
                    delete();
                    break;
                case 4:
                    mquiz.view();
                    viewF();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }

    private void add() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-20s%-30s%-20s|\n", "", "**Make Questionare**", "");
        System.out.print("|\tEnter ID to of Quiz Name: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID to Edit Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Edit Again: ");
            }
        }
        while(exit){
            int qnums;
            while(true){
                System.out.print("|\tHow many Question? : ");
                try{
                    qnums = input.nextInt();
                    if(qnums>0){
                        break;
                    }
                }catch(Exception e){
                    input.next();
                }
            }
            input.nextLine();
            for(int x = 0; x<qnums; x++){
                System.out.println("+----------------------------------------------------------------------------------------------------+");
                System.out.printf("|%-20s%-16s%-34d%-20s|\n", "", "Question Number ",x+1, "");
                System.out.print("Enter Question Text: ");
                String text = input.nextLine();
                System.out.print("Enter Answer A: ");
                String A = input.nextLine();
                System.out.print("Enter Answer B: ");
                String B = input.nextLine();
                System.out.print("Enter Answer C: ");
                String C = input.nextLine();
                System.out.print("Enter Answer D: ");
                String D = input.nextLine();
                System.out.print("Enter Correct Answer: ");
                String Ans = input.nextLine();
                String SQL = "INSERT INTO Q_Questions (Qz_Id, Q_Text, Q_A, Q_B, Q_C, Q_D, Q_Answer) Values (?,?,?,?,?,?,?)";
                conf.addRecord(SQL, id, text, A, B, C, D, Ans);
            }
            exit = false;
        }
    }

    private void edit() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-20s%-30s%-20s|\n", "", "**Edit Questionare**", "");
        System.out.print("|\tEnter ID of Quiz Name: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            boolean exit2 = true;
            view(id);
             System.out.print("|\tEnter Question ID: ");
            int Qid;
            while(true){
                try{
                    Qid = input.nextInt();
                    if(doesIDexists2(Qid, id, conf)){
                        break;
                    }else if(Qid == 0){
                        exit2 = false;
                        break;
                    }else{
                        System.out.print("|\tEnter ID Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter ID Again: ");
                }
            }
            while(exit2){
                System.out.println("+----------------------------------------------------------------------------------------------------+");
                input.nextLine();
                System.out.print("Enter Question Text: ");
                String text = input.nextLine();
                System.out.print("Enter Answer A: ");
                String A = input.nextLine();
                System.out.print("Enter Answer B: ");
                String B = input.nextLine();
                System.out.print("Enter Answer C: ");
                String C = input.nextLine();
                System.out.print("Enter Answer D: ");
                String D = input.nextLine();
                System.out.print("Enter Correct Answer: ");
                String Ans = input.nextLine();
                String SQL = "UPDATE Q_Questions SET Q_Text, Q_A = ?, Q_B = ?, Q_C = ?, Q_D = ? Where Q_Id";
                conf.updateRecord(SQL, text, A, B, C, D, Qid);
                exit2 = false;
            }
            exit = false;
        }
    }

    private void delete() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-20s%-30s%-20s|\n", "", "**Delete Questionare**", "");
        System.out.print("|\tEnter ID of Quiz Name: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            String dsel;
            while(true){
                System.out.print("|\tDelete (All/Specific): ");
                try{
                    dsel = input.next();
                    if(dsel.equalsIgnoreCase("all")||dsel.equalsIgnoreCase("specific")){
                        break;
                    }
                }catch(Exception e){
                    System.out.println("\tSelect only (All/Specific)");
                }
            }
            boolean exit2 = true;
            if(dsel.equalsIgnoreCase("specific")){
                view(id);
                System.out.print("|\tEnter Question ID: ");
                int Qid;
                while(true){
                    try{
                        Qid = input.nextInt();
                        if(doesIDexists2(Qid, id, conf)){
                            break;
                        }else if(Qid == 0){
                            exit2 = false;
                            break;
                        }else{
                            System.out.print("|\tEnter ID Again: ");
                        }
                    }catch(Exception e){
                        input.next();
                        System.out.print("|\tEnter ID Again: ");
                    }
                }
                while(exit2){
                    String SQL = "DELETE FROM Q_Questions Where Qz_Id = ? AND Q_Id = ?";
                    conf.deleteRecord(SQL, id, Qid);
                    exit2 = false;
                }
            }else{
                String SQL = "DELETE FROM Q_Questions Where Qz_Id = ?";
                conf.deleteRecord(SQL, id);
            }
            exit = false;
        }
    }

    public void view(int id) {
        String tbl_view = "SELECT * FROM Q_Questions Where Qz_Id = "+id;
        String[] tbl_Headers = {"ID", "Question", "A", "B", "C", "D", "Answer"};
        String[] tbl_Columns = {"Qz_Id", "Q_Text", "Q_A", "Q_B", "Q_C", "Q_D", "Q_Answer"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    private void viewF(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-20s%-30s%-20s|\n", "", "**View Quizzes Questionare**", "");
        System.out.print("|\tEnter ID of Quiz Name: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            view(id);
            break;
        }
    }
    
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM Q_Quizzes Where Qz_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private boolean doesIDexists2(int Qid, int id, config conf) {
        String query = "SELECT COUNT(*) FROM Q_Questions Where Q_Id = ? AND Qz = "+id;
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, Qid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
}
