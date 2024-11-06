package Zaragoza;

import java.util.Scanner;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TakeTest {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    ManageUser mu = new ManageUser();
    ManageQuiz mq = new ManageQuiz();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate cdate = LocalDate.now();

    public void test() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Test**", "");
        System.out.println("+----------------------------------------------------------------------------------------------------+");
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
        
        while (exit) {
            boolean exit2 = true;
            mq.view();
            
            int Qid;
            while (true) {
                System.out.print("|\tEnter Quiz ID: ");
                try {
                    Qid = input.nextInt();
                    if (doesIDexists2(Qid, conf)) {
                        break;
                    } else if (Qid == 0) {
                        exit2 = false;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Enter Valid Quiz ID!");
                    input.next();
                }
            }
            
            while (exit2) {
                String QuizName = getQuizName(Qid);
                LocalDate gdate;
                gdate = LocalDate.parse(getDate(Qid),dateFormat);
                if(gdate.isAfter(cdate)){
                    String questionQuery = "SELECT Q_Id, Q_Text, Q_A, Q_B, Q_C, Q_D, Q_Answer FROM Q_Questions WHERE Qz_Id = ?";
                    int correctAnswers = 0;
                    int incorrectAnswers = 0;

                    try (Connection conn = conf.connectDB();
                         PreparedStatement pstmt = conn.prepareStatement(questionQuery)) {

                        pstmt.setInt(1, Qid);
                        ResultSet rs = pstmt.executeQuery();

                        int questionNumber = 1;

                        while (rs.next()) {
                            System.out.println("\nQuestion " + questionNumber + ": " + rs.getString("Q_Text"));
                            System.out.println("A. " + rs.getString("Q_A"));
                            System.out.println("B. " + rs.getString("Q_B"));
                            System.out.println("C. " + rs.getString("Q_C"));
                            System.out.println("D. " + rs.getString("Q_D"));

                            System.out.print("Your answer (A, B, C, D): ");

                            String userAnswer = input.next().toUpperCase();

                            String correctAnswer = rs.getString("Q_Answer").toUpperCase();
                            if (userAnswer.equals(correctAnswer)) {
                                System.out.println("Correct!");
                                correctAnswers++;
                            } else {
                                System.out.println("Incorrect! The correct answer was " + correctAnswer);
                                incorrectAnswers++;
                            }

                            questionNumber++;
                        }
                        System.out.println("\n+----------------------------------------------------------------------------------------------------+");
                        System.out.println("Test Completed!");
                        System.out.printf("Correct Answers: %d\n", correctAnswers);
                        System.out.printf("Incorrect Answers: %d\n", incorrectAnswers);
                        String disScore = correctAnswers+"/"+(correctAnswers+incorrectAnswers);
                        System.out.println("+----------------------------------------------------------------------------------------------------+");

                        String SQL = "INSERT INTO Test_History (Qz_Id, U_Id, Qz_Name, H_Score, H_Date) Values (?,?,?,?,?)";
                        conf.addRecord(SQL, Qid, Uid, QuizName, disScore, cdate);
                    } catch (SQLException e) {
                        System.out.println("|\tError retrieving questions: " + e.getMessage());
                    }
                }else{
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Unable to Take the Test! Deadline is Over!**", "");
                }
                exit2 = false;
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
    
    private boolean doesIDexists2(int id, config conf) {
        String query = "SELECT COUNT(*) FROM Q_Quizzes WHERE Qz_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Quiz ID: " + e.getMessage());
        }
        return false;
    }
    private String getQuizName(int Qid) {
        String query = "SELECT Qz_Name FROM Q_Quizzes WHERE Qz_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Qid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Qz_Name");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving course name: " + e.getMessage());
        }
        return null;
    }
    private String getDate(int Qid) {
        String query = "SELECT Qz_Date FROM Q_Quizzes WHERE Qz_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Qid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Qz_Date");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving course name: " + e.getMessage());
        }
        return null;
    }
}
