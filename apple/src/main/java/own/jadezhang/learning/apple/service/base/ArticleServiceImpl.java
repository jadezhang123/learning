package own.jadezhang.learning.apple.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import own.jadezhang.common.service.AbstractServiceImpl;
import own.jadezhang.learning.apple.dao.base.IArticleDAO;
import own.jadezhang.learning.apple.domain.base.Article;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class ArticleServiceImpl extends AbstractServiceImpl<IArticleDAO, Article>implements IArticleService<IArticleDAO, Article> {
    @Autowired
    private IArticleDAO articleDAO;

    @Override
    public IArticleDAO getDao() {
        return articleDAO;
    }
}
