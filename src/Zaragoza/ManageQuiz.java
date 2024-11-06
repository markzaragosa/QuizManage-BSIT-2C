package Zaragoza;

import java.time.LocalDate;
import java.util.Scanner;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class ManageQuiz {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    LocalDate cdate = LocalDate.now();
    LocalDate ddate;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public void manage_quiz() {
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Manage Quizzes**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Add Quiz");
            System.out.printf("|%-5s%-95s|\n", "", "2. Edit Quiz");
            System.out.printf("|%-5s%-95s|\n", "", "3. Delete Quiz");
            System.out.printf("|%-5s%-95s|\n", "", "4. View Quizzes");
            System.out.printf("|%-5s%-95s|\n", "", "5. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    add();
                    break;
                case 2:
                    view();
                    edit();
                    break;
                case 3:
                    view();
                    delete();
                    break;
                case 4:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }

    private void add() {
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Manage Quizzes**", "");
        input.nextLine();
        System.out.print("|\tQuiz Name: ");
        String qname = input.nextLine();
        String deadDate;
        while(true){
            System.out.print("|\tDeadline (YYYY-MM-DD): ");
            try{
                deadDate = input.next();
                ddate = LocalDate.parse(deadDate,dateFormat);
                if(ddate.isAfter(cdate)){
                    break;
                }else{
                    System.out.printf("|%-10s%-80s%-10s|\n","","**Deadline Must be 1 Day or More!**","");
                }
            }catch(Exception e){
                System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
            }
        }
        String SQL = "INSERT INTO Q_Quizzes (Qz_Name, Qz_Date) Values (?,?)";
        conf.addRecord(SQL, qname, ddate);
    }

    private void edit() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Edit Quizzes**", "");
        System.out.print("|\tEnter ID to Edit: ");
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
            input.nextLine();
            System.out.print("|\tQuiz Name: ");
            String qname = input.nextLine();
            String deadDate;
            while(true){
                System.out.print("|\tDeadline (YYYY-MM-DD): ");
                try{
                    deadDate = input.next();
                    ddate = LocalDate.parse(deadDate,dateFormat);
                    if(ddate.isAfter(cdate)){
                        break;
                    }else{
                        System.out.printf("|%-10s%-80s%-10s|\n","","**Deadline Must be 1 Day or More!**","");
                    }
                }catch(Exception e){
                    System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                }
            }
            String SQL = "UPDATE Q_Quizzes SET Qz_Name = ?, Qz_Date = ? Where Qz_Id";
            conf.updateRecord(SQL, qname, ddate);
        }
    }

    private void delete() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Delete Quiz**","");
        System.out.print("|\tEnter ID to Delete: ");
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
                    System.out.print("|\tEnter ID to Delete Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Delete Again: ");
            }
        }
        while(exit){
            String SQL = "DELETE FROM Q_Quizzes Where Qz_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
        }
    }

    public void view() {
        String tbl_view = "SELECT * FROM Q_Quizzes";
        String[] tbl_Headers = {"ID", "Quiz Name", "DeadLine"};
        String[] tbl_Columns = {"Qz_Id", "Qz_Name", "Qz_Date"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
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
}
