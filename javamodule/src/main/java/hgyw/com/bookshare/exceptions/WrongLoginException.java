package hgyw.com.bookshare.exceptions;

/**
 * Created by Yoni on 3/27/2016.
 */
public class WrongLoginException extends Exception {
    private final Issue issue;

    public enum Issue{
        WRONG_USERNAME_OR_PWORD("Wrong username and password"),
        USERNAME_TAKEN("Username and password are taken.");

        String message;

        Issue(String message){
            this.message = message;
        }

        @Override
        public String toString(){
            return message;
        }

    }
    public WrongLoginException(Issue issue) {
        super(issue.toString());
        this.issue = issue;
    }
}
