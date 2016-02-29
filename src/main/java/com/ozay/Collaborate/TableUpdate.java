package com.ozay.Collaborate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by RGV Krushnan on 29-02-2016.
 This program shoukd run everytime some one takes a survey to update the count of number of people taking the survey
 */
public class TableUpdate {

    static String account = "pymqxayzqcupig";
    static String password = "bbfQfAnPae6Fk4jrJDChq4qOpN";
    static String server = "ec2-54-83-10-210.compute-1.amazonaws.com:5432";
    static String database = "d7iegu76smkaqb";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection( "jdbc:postgresql://" + server + "/" + database + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory", account, password);
            Statement stmt = con.createStatement();
            stmt.executeQuery("create trigger SurveyCount AFTER Insert on SurveyDetail");
            
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    }

}
