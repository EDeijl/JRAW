import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:58
 * Copyright (c) 2013 Erik Deijl
 */
public class Comment extends VotableThing {
    //region Constants
    private final String CommentUrl = "/api/comment";
    private final String DistinguishUrl = "/api/distinguish";

    //endregion

    //region Fields
    private Reddit Reddit;
    private String Author;
    private String Body;
    private String BodyHtml;
    private String ParentId;
    private String Subreddit;
    private String ApprovedBy;
    private String AuthorFlairCssClass;
    private String AuthorFlairText;
    private String RemovedBy;
    private String Gilded;
    private String LinkId;
    private String LinkTitle;
    private int NumReports;
    private DistinguishType Distinguished;
    private Comment[] Comments;
    //endregion

    public Comment(Reddit reddit, JsonToken token) {

        super(reddit, token);
        //TODO Constructor
    }

    public Comment Reply(String message) {
        //TODO
        return null;
    }

    public void Distinguish(DistinguishType distinguishType) {
        //TODO
    }

    public enum DistinguishType {
        Moderator,
        Admin,
        Special,
        None
    }

    class DistinghuishConverter extends TypeAdapter {
        @Override
        public void write(JsonWriter jsonWriter, Object o) throws IOException {
            //TODO
        }

        @Override
        public Object read(JsonReader jsonReader) throws IOException {
            return null;  //TODO
        }
    }


    //region Getters And Setters
    public Reddit getReddit() {
        return Reddit;
    }

    public void setReddit(Reddit reddit) {
        Reddit = reddit;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getBodyHtml() {
        return BodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        BodyHtml = bodyHtml;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getSubreddit() {
        return Subreddit;
    }

    public void setSubreddit(String subreddit) {
        Subreddit = subreddit;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String approvedBy) {
        ApprovedBy = approvedBy;
    }

    public String getAuthorFlairCssClass() {
        return AuthorFlairCssClass;
    }

    public void setAuthorFlairCssClass(String authorFlairCssClass) {
        AuthorFlairCssClass = authorFlairCssClass;
    }

    public String getAuthorFlairText() {
        return AuthorFlairText;
    }

    public void setAuthorFlairText(String authorFlairText) {
        AuthorFlairText = authorFlairText;
    }

    public String getRemovedBy() {
        return RemovedBy;
    }

    public void setRemovedBy(String removedBy) {
        RemovedBy = removedBy;
    }

    public String getGilded() {
        return Gilded;
    }

    public void setGilded(String gilded) {
        Gilded = gilded;
    }

    public String getLinkId() {
        return LinkId;
    }

    public void setLinkId(String linkId) {
        LinkId = linkId;
    }

    public String getLinkTitle() {
        return LinkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        LinkTitle = linkTitle;
    }

    public int getNumReports() {
        return NumReports;
    }

    public void setNumReports(int numReports) {
        NumReports = numReports;
    }

    public DistinguishType getDistinguished() {
        return Distinguished;
    }

    public void setDistinguished(DistinguishType distinguished) {
        Distinguished = distinguished;
    }

    public Comment[] getComments() {
        return Comments;
    }

    public void setComments(Comment[] comments) {
        Comments = comments;
    }
    //endregion
}
