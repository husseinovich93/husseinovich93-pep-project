package Controller;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    List<Account>accounts = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageById);
        app.delete("/messages/{message_id}", this::delMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserID);
    
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    //## 1: Our API should be able to process new User registrations.

    private void registerAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);
        Account addedAccount;
        accounts = accountService.getAllAccounts();
        int counter = 0;

        for(Account acc : accounts){
            if(acc.getUsername().equals(account.getUsername())){
                counter++;
                break;
            }
        }
        System.out.println("Counter"+counter);
        if((account.getUsername().isEmpty() == false) && (account.getPassword().length() >=4) && (counter == 0)){
            System.out.println(counter);
            addedAccount = accountService.addAccount(account);
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
        
    }

    //## 2: Our API should be able to process User logins.

    private void loginAccountHandler(Context ctx) throws JsonProcessingException{
        int checker = 0;
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);
        accounts = accountService.getAllAccounts();
        for(Account acc : accounts){
            if((acc.getUsername().equals(account.getUsername())) && (acc.getPassword().equals(account.getPassword()))){
                ctx.json(mapper.writeValueAsString(acc));
                ctx.status(200);
                checker++;
                return;
            }
        }
        if(checker == 0){
            ctx.status(401);
        }
    }

    //## 3: Our API should be able to process the creation of new messages.

    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(),Message.class);
        Message addedMessage;
        int counter = 0;
        accounts = accountService.getAllAccounts();
        for(Account acc:accounts){
            if(message.getPosted_by() == acc.getAccount_id()){
                counter++;
                break;
            }

        
       }

        if((message.getMessage_text().isEmpty()== false) && (message.getMessage_text().length()<=255) && (counter>0 )){
            addedMessage = messageService.createMessage(message);
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }

    }

    //## 4: Our API should be able to retrieve all messages.

    private void getAllMessagesHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ctx.json(messageService.getAllMessages());
        ctx.status(200);
        
    }

    //## 5: Our API should be able to retrieve a message by its ID.

    private void getMessageById(Context ctx) throws JsonProcessingException{

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageById(message_id);
        for(Message msg : messageService.getAllMessages()){
            if(message_id == msg.getMessage_id()){
                ctx.json(mapper.writeValueAsString(message));
                ctx.status(200);
            }
        }
        

    }

    //## 6: Our API should be able to delete a message identified by a message ID.

    private void delMessageById(Context ctx) throws JsonProcessingException{

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        //Message message = 
        for(Message msg:messageService.getAllMessages()){
            if(message_id == msg.getMessage_id()){
                ctx.json(mapper.writeValueAsString(msg));
                ctx.status(200);
                messageService.delMessageByID(message_id);
            }
        }

    }

    //## 7: Our API should be able to update a message text identified by a message ID.

    private void updateMessageById(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        // System.out.println("Am i in 1. "+messageService.getMessageById(message_id));
        // System.out.println("Am i in 2. "+message);


        
        for(Message msg : messageService.getAllMessages()){
            if((msg.getMessage_id() == message_id) && (message.getMessage_text().isEmpty()==false) && (message.getMessage_text().length()<=255)){
                //System.out.println("Hello This is a Match"+(msg.getMessage_id()+".....>"+message_id));
                Message updatedMessage = messageService.updateMessageByID(msg.getMessage_id(), message);
                ctx.json(mapper.writeValueAsString(updatedMessage));
                System.out.println(updatedMessage);
                ctx.status(200);
                return;
            }
        }
        ctx.status(400);
    }

    public void getAllMessagesByUserID(Context ctx) throws JsonProcessingException{
        int user_id = Integer.parseInt(ctx.pathParam("account_id"));
        ObjectMapper mapper = new ObjectMapper();
        List<Message> userMessages = new ArrayList<>();
        for(Message msg : messageService.getAllMessages()){
            if(msg.getPosted_by() == user_id){
                userMessages.add(msg);
            }
        }
        ctx.json(mapper.writeValueAsString(userMessages));
        ctx.status(200);
        
        
    }


}