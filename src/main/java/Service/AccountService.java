package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    // Adding a new Account
    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

    // get all accounts 
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
    
}
