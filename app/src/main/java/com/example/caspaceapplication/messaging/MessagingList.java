package com.example.caspaceapplication.messaging;

public class MessagingList {

    private String name, mobile, lastMessage, profilePic;
    private int unseenMessages;

    public MessagingList(String name, String mobile, String lastMessage, String profilePic, int unseenMessages) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
