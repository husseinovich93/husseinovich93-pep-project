package DAO;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    //Adding a new account [## 1: Our API should be able to process new User registrations.]

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL 
            String sql = "INSERT INTO account (username,password) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Writting PrepareStatements : 
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generate_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generate_account_id, account.getUsername(), account.getPassword());
            }

            
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return null;
    }



    // Retrieving all Accounts from account table
    public List<Account>getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account>accounts = new ArrayList<>();
        try {
            String sql = "SELECT*FROM account";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public int getIDbyAccount(Account account){

        return account.getAccount_id();
    }

}
