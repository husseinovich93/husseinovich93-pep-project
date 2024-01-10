package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    public Message createMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages(); 
    }

    public Message getMessageById(int mesgID){
        return messageDAO.getMessageById(mesgID);
    }

    public void delMessageByID(int id){
         messageDAO.deleteMessageById(id);
    }

    public Message updateMessageByID(int message_id, Message message){
        if(messageDAO.getMessageById(message_id) != null){
            System.out.println("xHere:  "+messageDAO.getMessageById(message_id));
            messageDAO.updateMessageByID(message_id, message);
            System.out.println("yHere:  "+messageDAO.getMessageById(message_id));
            System.out.println("zHere:  "+messageDAO.getMessageById(message_id));
            return messageDAO.getMessageById(message_id);
        }
        else{
            return null;
        }
        
    }

    public List<Message>getMessagesByUserID(int UserID){
        return messageDAO.getAllMessagesByUserID(UserID);
    }

}
