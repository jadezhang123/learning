package own.jadezhang.learning.apple.domain.base;

import java.util.List;

/**
 * Created by Zhang Junwei on 2017/2/24 0024.
 */
public class UserEx extends User {

    private int rank;

    private int articleCount;

    private List<Article> articles;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
