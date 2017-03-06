package edu.zhku.lucene.model;

/**
 * 诗词
 *
 * @author 夏集球
 * @time 2016/9/18$ 20:56$
 */
public class Poem {

    private int id;

    private String author;

    private String title;

    private String content;

    public Poem() {
    }

    public Poem(int id, String author, String title, String content) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
