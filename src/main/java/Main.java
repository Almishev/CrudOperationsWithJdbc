

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String jdbcURL="jdbc:postgresql://localhost:5432/library";
    private static final String username="user1";
    private static final String password="asroma";
    private static final Scanner scanner = new Scanner(System.in);



    public static void main(String[] args) {
        System.out.println("Welcome to library application.");


        int input;

        do {

            System.out.println("1. Insert a new book");
            System.out.println("2. Update book");
            System.out.println("3. Delete book");
            System.out.println("4. See all books");
            System.out.println("5. Exit");
            input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1:
                    System.out.println("Insert a book.");
                    addNewBook(scanner);

                    break;
                case 2:
                    System.out.println("Update book released date.");
                     updateBook(scanner);

                    break;
                case 3:
                    System.out.println("Delete book from the table.");
                    deleteBook(scanner);

                    break;
                case 4:
                    System.out.println("Select all.");
                    selectAll(scanner);
                    break;
                case 5:
                    System.out.println("Exit.");
                    break;

                default:
                    System.out.println("Please enter a number between 1 and 5.");

            }
        }
        while(input !=5);

    }
    private static void selectAll(Scanner scanner){
        try {
            Connection con = DriverManager.getConnection(jdbcURL,username,password);


            String sql="select * from books";
            try ( PreparedStatement p_stmt = con.prepareStatement(sql)) {

                ResultSet resultSet= p_stmt.executeQuery();
                while (resultSet.next()){
                    System.out.println(resultSet.getInt(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                }






                resultSet.close();
                con.commit();
                con.setAutoCommit(true);

            } catch (SQLException e) {
                System.out.println(e);

                try {
                    System.out.println("rollback");
                    con.rollback();
                } catch (Exception ex) {
                    System.out.println(e);
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static void addNewBook(Scanner scanner) {
        try {
            Connection con = DriverManager.getConnection(jdbcURL,username,password);
            con.setAutoCommit(false);
            System.out.println("Connection to PostgreSql Server");
            System.out.println("Input a book authorID");
            int author = Integer.parseInt(scanner.nextLine());
            System.out.println("Input title of the book :");
            String title = scanner.nextLine();
            System.out.println("Input a book realised date");
            String date = scanner.nextLine();
            String sql="insert into books(title,released_date,author) values (?,?,?)";
            PreparedStatement p_stmt = con.prepareStatement(sql);
            p_stmt.setString(1,title);
            p_stmt.setInt(2,author);
            p_stmt.setString(3,date );


         int rows =   p_stmt.executeUpdate(sql);
         if (rows>0){
             System.out.println("A new book have been added.");
         }
         else {
             System.out.println("Operation failed");
         }
            p_stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error in connection to PostgreSql Server.");
            e.printStackTrace();
        }
    }

    private static void deleteBook(Scanner scanner){
        try ( Connection conn = DriverManager.getConnection(jdbcURL,username,password)) {

            conn.setAutoCommit(false);

            System.out.println("Input book ID :");
            int n2 = Integer.parseInt(scanner.nextLine());


            String sql="delete from books where bookId = ?" ;
            try ( PreparedStatement p_stmt = conn.prepareStatement(sql)) {

                p_stmt.setInt(1,n2);


                int rows=  p_stmt.executeUpdate();

                if(rows>0){
                    System.out.println("Successful deleting.");
                }else{
                    System.out.println("Deleting failed.");
                }




                p_stmt.close();
                conn.commit();
                conn.setAutoCommit(true);

            } catch (SQLException e) {
                System.out.println(e);

                try {
                    System.out.println("rollback");
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println(e);
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    private static void updateBook(Scanner scanner){
        try ( Connection conn = DriverManager.getConnection(jdbcURL,username,password);) {

            conn.setAutoCommit(false);
            String sql = "update books set released_date=? where bookId=?";

            PreparedStatement preparedStatement =
                    conn.prepareStatement(sql);
            System.out.println("Input changed released date :");
            String date = scanner.nextLine();
            preparedStatement.setString(1, date);
            System.out.println("Input bookId: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            preparedStatement.setInt(2, bookId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected>0){
                System.out.println("Success query.");
            }else{
                System.out.println("Query failed.");
            }

            preparedStatement.close();
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.out.println(e);

        }

    }
}
