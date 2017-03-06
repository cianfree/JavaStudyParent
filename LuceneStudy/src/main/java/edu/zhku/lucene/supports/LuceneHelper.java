package edu.zhku.lucene.supports;

import edu.zhku.lucene.model.Poem;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 夏集球
 * @time 2016/9/18$ 20:59$
 */
public class LuceneHelper {

    /**
     * 索引文件目录
     */
    public static final String INDEX_DIR = System.getProperty("user.dir") + File.separator + "index";

    /**
     * 分词器
     */
    private static Analyzer ANALYZER = new IKAnalyzer();

    /**
     * 当前索引目录
     */
    private volatile static Directory currentDirectory;
    /**
     * 当前Reader
     */
    private volatile static DirectoryReader currentReader;
    /**
     * 当前Searcher
     */
    private volatile static IndexSearcher currentSearcher;
    /**
     * 当前索引写入器
     */
    private volatile static IndexWriter currentWriter;

    /**
     * 初始化
     */
    static {
        try {
            currentDirectory = FSDirectory.open(new File(INDEX_DIR).toPath());

            IndexWriterConfig config = new IndexWriterConfig(ANALYZER);
            currentWriter = new IndexWriter(currentDirectory, config);
            currentWriter.commit();

            currentReader = DirectoryReader.open(currentDirectory);
            currentSearcher = new IndexSearcher(currentReader);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 分页查询
     *
     * @param keyword     关键字
     * @param currentPage 当前页
     * @param pageSize    每页多少记录
     */
    public static List<Poem> search(String keyword, int currentPage, int pageSize) {
        List<Poem> result = new ArrayList<>();
        QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content", "author"}, ANALYZER);
        try {
            final Query query = parser.parse(QueryParser.escape(keyword));
            final Sort sort = new Sort();
            TopFieldCollector collector = TopFieldCollector.create(sort, pageSize, false, false, false);
            currentSearcher.search(query, collector);

            int offset = (currentPage - 1) * pageSize;
            ScoreDoc[] hits = collector.topDocs(offset, pageSize).scoreDocs;

            if (null != hits && hits.length > 0) {
                for (ScoreDoc sd : hits) {
                    Document doc = currentReader.document(sd.doc);
                    result.add(new Poem(
                            Integer.parseInt(doc.get("id")),
                            doc.get("author"),
                            doc.get("title"),
                            doc.get("content")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 索引多个Poem
     *
     * @param poems 要索引的列表
     */
    public static void index(Collection<Poem> poems) {
        if (null != poems && !poems.isEmpty()) {
            for (Poem poem : poems) {
                Document doc = convertToDoc(poem);
                // 先删除该字段原来的索引
                try {
                    currentWriter.deleteDocuments(NumericRangeQuery.newIntRange("id", poem.getId(), poem.getId(), true, true));
                    // 重新索引
                    currentWriter.addDocument(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 索引诗词对象
     *
     * @param poem 诗词对象
     */
    public static void index(Poem poem) {
        Document doc = convertToDoc(poem);
        // 先删除该字段原来的索引
        try {
            currentWriter.deleteDocuments(NumericRangeQuery.newIntRange("id", poem.getId(), poem.getId(), true, true));
            // 重新索引
            currentWriter.addDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Poem对象转换成Document对象
     *
     * @param poem 要转换的诗词对象
     */
    private static Document convertToDoc(Poem poem) {
        String content = "";
        if (null != poem.getContent()) {
            content = poem.getContent().replaceAll("[\r\n]+", "");
        }
        String title = "";
        if (null != poem.getTitle()) {
            title = poem.getTitle().replaceAll("[\r\n]+", "");
        }

        Document doc = new Document();
        Field idField = new IntField("id", poem.getId(), IntField.TYPE_STORED);
        Field authorField = new StringField("author", poem.getAuthor(), Field.Store.YES);
        Field titleField = new StringField("title", title, Field.Store.YES);
        Field contentField = new StringField("content", content, Field.Store.YES);

        doc.add(idField);
        doc.add(authorField);
        doc.add(titleField);
        doc.add(contentField);

        return doc;
    }

    /**
     * 清空
     */
    public static void flushIndex() {
        try {
            currentWriter.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        exit();
    }

    /**
     * 退出
     */
    public static void exit() {
        try {
            if (currentReader != null) {
                currentReader.close();
                currentReader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (currentWriter != null) {
                currentWriter.close();
                currentWriter = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (currentDirectory != null) {
                currentDirectory.close();
                currentDirectory = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
