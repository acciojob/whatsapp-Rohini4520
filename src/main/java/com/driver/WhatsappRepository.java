package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("user already exist");
        }
        userMobile.add(mobile);
        return "user created successfully";
    }
public Group createGroup(List<User> users){
        if(users.size() == 2){
            return new Group(users.get(1).getName(), users);

        }
    customGroupCount++;
        return new Group("Group" + customGroupCount, users);
    }

public  int createMessage(String content){
      messageId++;
      return  messageId;
}

public int sendMessage(Message message, User user,Group group)throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("group does not exist");
        }
    if (!groupUserMap.get(group).contains(user)) {
        throw new Exception("You are not allowed to send message");
    }
    groupMessageMap.computeIfAbsent(group, k -> new ArrayList<>()).add(message);
    senderMap.put(message,user);
    return groupMessageMap.get(group).size();
}
    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if (!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }
        if (!adminMap.get(group).equals(approver)) {
            throw new Exception("Approver does not have rights");
        }
        if (!groupUserMap.get(group).contains(user)) {
            throw new Exception("User is not a participant");
        }
        adminMap.put(group, user);
        return "Admin changed successfully";
    }
    public int removeUser(User user) throws Exception{
        Group userGroup = null;
        for (Group group : groupUserMap.keySet()) {
            if (groupUserMap.get(group).contains(user)) {
                userGroup = group;
                break;
            }
        }
        if (userGroup == null) {
            throw new Exception("User not found");
        }
        if (adminMap.get(userGroup).equals(user)) {
            throw new Exception("Cannot remove admin");
        }
        groupUserMap.get(userGroup).remove(user);
        return groupUserMap.get(userGroup).size();
    }
    public String findMessage(Date start, Date end, int K) throws Exception {
        List<Message> messages = new ArrayList<>();
        for (List<Message> messageList : groupMessageMap.values()) {
            for (Message message : messageList) {
                if (message.getTimestamp().after(start) && message.getTimestamp().before(end)) {
                    messages.add(message);
                }
            }
        }
        if (messages.size() < K) {
            throw new Exception("K is greater than the number of messages");
        }
        messages.sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()));
        return messages.get(K - 1).getContent();
}
}
