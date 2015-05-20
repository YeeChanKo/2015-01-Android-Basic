package com.example.viz.nextagram;

/**
 * Created by viz on 2015. 3. 18..
 */
public class ArticleDTO {

    private int articleNumber;
    private String title;
    private String writerName;
    private String writerId;
    private String content;
    private String writeDate;
    private String imgName;

    public ArticleDTO(int articleNumber, String title, String writerName, String writerId, String content, String writeDate, String imgName) {
        this.articleNumber = articleNumber;
        this.title = title;
        this.writerName = writerName;
        this.writerId = writerId;
        this.content = content;
        this.writeDate = writeDate;
        this.imgName = imgName;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getWriterName() {
        return writerName;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getContent() {
        return content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public String getImgName() {
        return imgName;
    }
}
