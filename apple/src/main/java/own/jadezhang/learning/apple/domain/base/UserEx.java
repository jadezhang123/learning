package own.jadezhang.learning.apple.domain.base;

import java.util.List;

/**
 * Created by Zhang Junwei on 2017/2/24 0024.
 */
public class UserEx extends User {
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
