package Zaragoza;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

public class ManageUser {
    Scanner input = new Scanner(System.in);
    LocalDate cdate = LocalDate.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate bdate;
    config conf = new config();

    public void manage_user() {
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Manage Users**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Add User");
            System.out.printf("|%-5s%-95s|\n", "", "2. Edit User");
            System.out.printf("|%-5s%-95s|\n", "", "3. Delete User");
            System.out.printf("|%-5s%-95s|\n", "", "4. View Users");
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
                    view();
                    break;
                case 3:
                    view();
                    delete();
                    view();
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
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Add User**", "");
        System.out.print("|\tEnter First Name: ");
        String fname = input.next();
        System.out.print("|\tEnter Middle Name: ");
        String mname = input.next();
        System.out.print("|\tEnter Last Name: ");
        String lname = input.next();
        String bdate2;
            while(true){
                System.out.print("|\tEnter Birth Date (YYYY-MM-DD): ");
                try{
                    bdate2 = input.next();
                    bdate = LocalDate.parse(bdate2,dateFormat);
                    if(bdate.isBefore(cdate.minusYears(18))&&bdate.isAfter(cdate.minusYears(120))){
                        break;
                    }else{
                        System.out.printf("|%-10s%-80s%-10s|\n","","**User Must be 18 Years Old, and Should not be Older than 120**","");
                    }
                }catch(Exception e){
                    System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                }
            }
        String gender;
        while(true){
            System.out.print("|\tGender (Male/Female): ");
            try{
                gender = input.next();
                if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                    break;
                }
            }catch(Exception e){

            }
        }
        System.out.print("|\tEnter Email: ");
        String email = input.next();
        
        String SQL = "INSERT INTO U_Users (U_Fname, U_Mname, U_Lname, U_Birth_Date, U_Gender, U_Email) Values (?,?,?,?,?,?)";
        conf.addRecord(SQL, fname, mname, lname, bdate, gender, email);
    }

    private void edit() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-20s%-30s%-20s|\n", "", "**Edit User**", "");
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
            System.out.print("|\tEnter First Name: ");
            String fname = input.next();
            System.out.print("|\tEnter Middle Name: ");
            String mname = input.next();
            System.out.print("|\tEnter Last Name: ");
            String lname = input.next();
            String bdate2;
                while(true){
                    System.out.print("|\tEnter Birth Date (YYYY-MM-DD): ");
                    try{
                        bdate2 = input.next();
                        bdate = LocalDate.parse(bdate2,dateFormat);
                        if(bdate.isBefore(cdate.minusYears(18))&&bdate.isAfter(cdate.minusYears(120))){
                            break;
                        }else{
                            System.out.printf("|%-10s%-80s%-10s|\n","","**User Must be 18 Years Old, and Should not be Older than 120**","");
                        }
                    }catch(Exception e){
                        System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                    }
                }
            String gender;
            while(true){
                System.out.print("|\tGender (Male/Female): ");
                try{
                    gender = input.next();
                    if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                        break;
                    }
                }catch(Exception e){

                }
            }
            System.out.print("|\tEnter Email: ");
            String email = input.next();
            
            String SQL = "UPDATE U_Users SET U_Fname = ?, U_Mname = ?, U_Lname = ?, U_Birth_Date = ?, U_Gender = ?, U_Email = ? Where U_Id = ?";
            conf.updateRecord(SQL, fname, mname, lname, bdate, gender, email, id);
            exit = false;
        }
    }

    private void delete() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Delete User**","");
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
            String SQL = "DELETE FROM U_Users Where U_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
        }
    }

    public void view() {
        String tbl_view = "SELECT * FROM U_Users";
        String[] tbl_Headers = {"ID", "First Name", "Last Name"};
        String[] tbl_Columns = {"U_Id", "U_Fname", "U_Lname"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    
    
    
    
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM U_Users Where U_Id = ?";
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
