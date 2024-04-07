package DictionaryClient;



public class ServerReply {
    public RequestType requestType;
    public String replyData;
    public int replyCode;
    public String warning;

    @Override
    public String toString() {
        return STR."REPLY RECEIVE: \{replyCode}:\{replyData}:\{warning}";
    }

    public ServerReply(String serverReply) {
        String[] parts = serverReply.split(":", 3);

        if (parts.length != 3) {
            this.replyCode = 500;
            this.warning = "Wrong format rely from server. Must have the form ACTION:CODE:ReplyData ";
            return;
        }

        int replyCode;
        try {
            replyCode = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            this.replyCode = 500;
            this.warning = "Wrong format rely from server. CODE must be an INT";
            return;
        }

        RequestType requestType = switch (parts[0]) {
            case "SEARCH" -> RequestType.SEARCH;
            case "UPDATE" -> RequestType.UPDATE;
            case "DELETE" -> RequestType.DELETE;
            case "ADD" -> RequestType.ADD;
            case "ERROR" -> RequestType.ERROR;
            default -> {
                this.replyCode = 500;
                this.warning = "Wrong format rely from server. ACTION must be one of SEARCH, ADD, DELETE, UPDATE";
                yield RequestType.ERROR;
            }
        };
        this.requestType = requestType;

        if ((replyCode == 302) && (requestType == RequestType.SEARCH)){
            this.replyCode = replyCode;
            this.replyData = parts[2];
        } else if ((replyCode == 404) && (requestType == RequestType.SEARCH)) {
            this.replyCode = replyCode;
            this.warning = "Word can't be found";
        } else if((replyCode == 201) && (requestType == RequestType.ADD)) {
            this.replyCode = replyCode;
            this.warning = "Word is added to dictionary";
        } else if((replyCode == 409) && (requestType == RequestType.ADD)) {
            this.replyCode = replyCode;
            this.warning = "Word is already in the dictionary";
        } else if((replyCode == 200) && (requestType == RequestType.UPDATE)) {
            this.replyCode = replyCode;
            this.warning = "Word is successfully updated.";
        } else if((replyCode == 302) && (requestType == RequestType.UPDATE)) {
            this.replyCode = replyCode;
            this.warning = "Word is unsuccessfully updated";
        } else if((replyCode == 200) && (requestType == RequestType.DELETE)) {
            this.replyCode = replyCode;
            this.warning = "Word is successfully deleted";
        } else if((replyCode == 404) && (requestType == RequestType.DELETE)) {
            this.replyCode = replyCode;
            this.warning = "Word is unsuccessfully deleted";
        }
    }


}
