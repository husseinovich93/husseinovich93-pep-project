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

    public Message delMessageByID(int id){
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageByID(int message_id, Message message){
        if(messageDAO.getMessageById(message_id) != null){
            messageDAO.updateMessageByID(message_id, message);
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
