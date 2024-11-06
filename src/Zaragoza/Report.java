package Zaragoza;

import java.util.Scanner;
import java.sql.*;

public class Report {
    Scanner input = new Scanner(System.in);
    ManageUser mu = new ManageUser();
    config conf = new config();

    public void report() {
        boolean exit = true;
        do {
            System.out.println("+------------------------------------------------------------+");
            System.out.printf("|%-20s%-30s%-20s|\n", "", "**Reports**", "");
            System.out.printf("|%-5s%-50s|\n", "", "1. General Report");
            System.out.printf("|%-5s%-50s|\n", "", "2. Individual Report");
            System.out.printf("|%-5s%-50s|\n", "", "3. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");

            int choice;
            while (true) {
                try {
                    choice = input.nextInt();
                    if (choice > 0 && choice < 4) {
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
                    general();
                    break;
                case 2:
                    individual();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }

    private void general() {
        mu.view();
        String tbl_view = "SELECT * FROM Test_History";
        String[] tbl_Headers = {"User ID", "Quiz Name", "Date Taken"};
        String[] tbl_Columns = {"U_Id", "Qz_Name", "H_Date"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }

    private void individual() {
        boolean exit = true;
        mu.view();
        int Uid;
        while (true) {
            System.out.print("|\tEnter User ID: ");
            try {
                Uid = input.nextInt();
                if (doesIDexists(Uid, conf)) {
                    break;
                } else if (Uid == 0) {
                    exit = false;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Enter Valid ID!");
                input.next();
            }
        }
        while(exit){
            try {
                String userSQL = "SELECT U_Fname, U_Mname, U_Lname, U_Birth_Date, U_Gender, U_Email FROM U_Users WHERE U_Id = ?";
                PreparedStatement userStmt = conf.connectDB().prepareStatement(userSQL);
                userStmt.setInt(1, Uid);
                ResultSet userRs = userStmt.executeQuery();

                if (userRs.next()) {
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "Individual User Information", "");
                    System.out.printf("|%-15s: %-60s|\n", "First Name", userRs.getString("U_Fname"));
                    System.out.printf("|%-15s: %-60s|\n", "Middle Name", userRs.getString("U_Mname"));
                    System.out.printf("|%-15s: %-60s|\n", "Last Name", userRs.getString("U_Lname"));
                    System.out.printf("|%-15s: %-60s|\n", "Birth Date", userRs.getString("U_Birth_Date"));
                    System.out.printf("|%-15s: %-60s|\n", "Gender", userRs.getString("U_Gender"));
                    System.out.printf("|%-15s: %-60s|\n", "Email", userRs.getString("U_Email"));
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String testHistorySQL = "SELECT Qz_Name, H_Score, H_Date FROM Test_History WHERE U_Id = ?";
                    PreparedStatement testHistoryStmt = conf.connectDB().prepareStatement(testHistorySQL);
                    testHistoryStmt.setInt(1, Uid);
                    ResultSet testHistoryRs = testHistoryStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Test History**", "");
                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");
                    System.out.printf("| %-28s | %-28s | %-28s |\n", "Quiz Name", "Score", "Date");
                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");

                    boolean hasHistory = false;
                    while (testHistoryRs.next()) {
                        hasHistory = true;
                        System.out.printf("| %-28s | %-28s | %-28s |\n", 
                            testHistoryRs.getString("Qz_Name"), 
                            testHistoryRs.getString("H_Score"), 
                            testHistoryRs.getString("H_Date"));
                    }

                    if (!hasHistory) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Test History Found!!", "");
                    }

                    System.out.println("+-------------------------------+-------------------------------+-------------------------------+");

                    testHistoryRs.close();
                    testHistoryStmt.close();
                } else {
                    System.out.println("|\tNo record found for ID: " + Uid + " |");
                }

                userRs.close();
                userStmt.close();
            } catch (Exception e) {
                System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
            }
            exit = false;
        }
    }
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM U_Users WHERE U_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking User ID: " + e.getMessage());
        }
        return false;
    }
    
}
