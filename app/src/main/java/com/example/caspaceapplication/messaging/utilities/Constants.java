package com.example.caspaceapplication.messaging.utilities;

import java.util.HashMap;

public class Constants {

    public static final String KEY_USER_COLLECTION = "UserAccounts";
    public static final String KEY_FIRST_NAME = "userFirstName";
    public static final String KEY_LAST_NAME = "userLastName";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_USERNAME = "userUsername";
    public static final String KEY_PASSWORD = "userPassword";
    public static final String KEY_ORGANIZATION = "userOrganization";
    public static final String KEY_POPULATION = "userPopulation";
    public static final String KEY_COMPANY = "userCompanyName";
    public static final String KEY_USER_ID = "userIDNum";

    //for cospace branches
    public static final String KEY_COLLECTION_BRANCH = "CospaceBranches";
    public static final String KEY_BRANCH_CITY_ADDRESS = "cospaceCityAddress";
    public static final String KEY_BRANCH_NAME = "cospaceName";
    public static final String KEY_BRANCH_IMAGE = "cospaceImage";

    //for owners
    public static final String KEY_COLLECTION_OWNER = "OwnerUserAccounts";
    public static final String KEY_OWNER_FIRSTNAME = "ownerFirstname";
    public static final String KEY_OWNER_LAST_NAME = "ownerLastName";
    public static final String KEY_OWNER_IMAGE = "ownerImage";
    //public static final String KEY_OWNER_COMPANY = "ownerCompanyName";
    public static final String KEY_OWNER_EMAIL = "ownerEmail";
    public static final String KEY_OWNER_USERNAME = "ownerUsername";
    public static final String KEY_OWNER_PASSWORD = "ownerPassword";
    public static final String KEY_OWNER_ACCOUNT_STATUS = "ownerAccountStatus";
    public static final String KEY_OWNER_USER_ID = "ownerIdNum";

    //customer
    public static final String KEY_COLLECTION_USERS = "CustomerUserAccounts";
    /*public static final String KEY_FIRST_NAME = "customersFirstName";
    public static final String KEY_LAST_NAME = "customersLastName";
    public static final String KEY_EMAIL = "customersEmail";
    public static final String KEY_USERNAME = "customersUsername";
    public static final String KEY_PASSWORD = "customersPassword";
    public static final String KEY_ORGANIZATION = "customersOrganization";
    public static final String KEY_POPULATION = "customersPopulation";*/
    public static final String KEY_CUSTOMER_ACCOUNT_STATUS = "customersAccountStatus";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    //public static final String KEY_USER_ID = "customersIDNum";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_FCM_TOKEN2 = "fcmToken2";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders()
    {
        if(remoteMsgHeaders == null)
        {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAJK8FWRk:APA91bHKO2g7DbF6_L0Pq0Q9mIYxprS18r5xeEuNAw_gCeOr1ONCXtJDOzqHNUZ2AJjTQVofct5TVALBcgC3Vy9yCJ0lkTapCf2vdDZHc4YU2Bt8oZPPzUaYr7j7tjybRheBPyXHpnLp");
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }

}