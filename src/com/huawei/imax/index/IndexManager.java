/**   
 * @Title: IndexManager.java 
 * @Package com.huawei.imax.index 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-8-29 下午4:40:30 
 * @version V1.0   
 */
package com.huawei.imax.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.huawei.imax.model.CommEntity;
import com.huawei.imax.util.Constans;
import com.huawei.imax.util.GetProperties;

/**
 * @ClassName: IndexManager
 * @Description: 索引管理
 * @author xubin
 * @date 2012-8-29 下午4:40:30
 * 
 */
public class IndexManager {
	
	static Logger logger = LoggerFactory.getLogger(IndexManager.class);
	/**
	 * 索引存储路径
	 */
	private static Directory fsDirect;

	/**
	 * 采用IKAnalyzer分词器(最细颗粒度切词)
	 */
	private static Analyzer analyzer = new IKAnalyzer();
//	private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	/**
	 * 采用IKAnalyzer分词器(智能切词)
	 */
	private static Analyzer aly = new IKAnalyzer(true);
	
	private static long startime;
	
	/**
	 * 搜索采用自带的WhitespaceAnalyzer分词器，当且仅当关键字中有空格才对关键切词
	 */
//	private static Analyzer aly = new WhitespaceAnalyzer(Version.LUCENE_36);

	private static String directory = "";
	/**
	 * @Desc: 初始化数据
	 * @throws IOException
	 * @return void
	 * @throws
	 */
	static {
		try {
			Properties porp = GetProperties.getInstance();
			directory = porp.getProperty("directory");
			// directory = "c:/lucene/doc";
			if (!new File("directory").exists()) {
				// 建立对应路径的目录
				new File("directory").mkdirs();
			}
			fsDirect = FSDirectory.open(new File(directory));
		} catch (IOException e) {
			logger.error("初始化数据异常：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException 
	 * @Title: saveorupdateIndexs
	 * @Description: 保存或更新索引文件
	 * @param @param info
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String saveorupdateIndexs(CommEntity info) throws IOException {
		startime = System.currentTimeMillis();
		IndexWriter writer = null;
		File file = new File(directory);
		if ("write.lock".equals(file.getName())) {
			file.delete();
		}
		try {
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,
					analyzer);
			// 每8个文件进行一次索引整合优化
			LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
			mergePolicy.setMergeFactor(1000);
			conf.setMergePolicy(mergePolicy);
			//内存中最大文档数
			conf.setMaxBufferedDocs(1000);
			writer = new IndexWriter(fsDirect, conf);
			IndexWriter.isLocked(fsDirect);
		} catch (Exception e) {
			logger.error("索引合并优化操作异常：" + e);
		}
		logger.debug(info.toString());
		if (info == null || "".equals(info.getHtmlPath())) {
			return Constans.INDEX_RESLUT_NULL;
		} else if (!"".equals(info.getHtmlPath())) {
			try {
				String htmlPath = info.getHtmlPath();
				if (htmlPath.endsWith(".html") || htmlPath.endsWith(".htm") || htmlPath.endsWith(".pdf")) {
					addDocument(writer, info);
				}
				writer.close();
				long timeDiff = (System.currentTimeMillis() - startime) / 1000;
				logger.debug("创建索引完成,共耗时" + timeDiff + "秒");
				logger.debug("================" + info.getHtmlPath() + "索引处理结束===============");
				return Constans.INDEX_RESLUT_SAVE_SUCCESS;
			} catch (IOException e) {
				logger.error("索引操作异常：" + e);
				e.printStackTrace();
				return e.getMessage();
			}
		}
		return Constans.INDEX_RESLUT_NO_STATE;
	}

	/**
	 * Add one document to the lucene index
	 */
	public static void addDocument(IndexWriter indexWriter, CommEntity info) {
		String path = info.getHtmlPath();
		String title = null;
		String content = null;
		org.jsoup.nodes.Document d = null;

		try {
			int n;
			if (path.indexOf("http") == -1) {
				if(path.endsWith(".html") || path.endsWith(".htm")){
					File input = new File(path);
					d = Jsoup.parse(input, "UTF-8");
					Element contentElement = d.body();
					title = d.title();
					content = contentElement.text();
				}else{
					File pdfFile = new File(path);
					FileInputStream stream = new FileInputStream(pdfFile);  
					PDDocument pdfDoc = new PDDocument();
					PDFParser parser = new PDFParser(stream);
					parser.parse();
					pdfDoc = parser.getPDDocument();
//					PDDocumentInformation ifo = doc.getDocumentInformation();
					PDFTextStripper stripper = new PDFTextStripper();
					content = stripper.getText(pdfDoc).toString().trim();
					n = path.lastIndexOf("\\");
					title = path.substring(n + 1).toString();
//					title = ifo.getTitle();
					pdfDoc.close();
					stream.close();
				}
			} else {
				if(path.endsWith(".html") || path.endsWith(".htm")){
					d = Jsoup.connect(path).get();
					Element contentElement = d.body();
					title = d.title();
					content = contentElement.text();
				}else{
					PDDocument pdfDoc = PDDocument.load(path);  
					PDFTextStripper stripper = new PDFTextStripper(); 
//					PDDocumentInformation ifo = doc.getDocumentInformation();
					content = stripper.getText(pdfDoc).toString().trim();
					n = path.lastIndexOf("/");
					title = path.substring(n + 1).toString();
//					title = ifo.getTitle();
					pdfDoc.close();
				}
			}
		} catch (IOException e1) {
			logger.error("解析操作异常：" + e1);
			e1.printStackTrace();
		}
		String time = info.getTime();

		Document doc = new Document();
		Field titleFile = new Field(Constans.FIELD_TITLE, title == null ? ""
				: title, Field.Store.YES, Field.Index.ANALYZED,
				Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field contentFile = new Field(Constans.FIELD_CONTENT,
				content == null ? "" : content, Field.Store.YES,
				Field.Index.ANALYZED);
		Field pathFile = new Field(Constans.FIELD_HTMLPATH, path == null ? ""
				: path, Field.Store.YES, Field.Index.NOT_ANALYZED);
		Field timeFile = new Field(Constans.FIELD_TIME, time == null ? ""
				: time, Field.Store.YES, Field.Index.NOT_ANALYZED);
		doc.add(titleFile);
		doc.add(contentFile);
		doc.add(pathFile);
		doc.add(timeFile);

		try {
			if (info.getFlag() == 1) {
				// 更新
				Term term = new Term(Constans.FIELD_HTMLPATH, path);
				indexWriter.updateDocument(term, doc);
			} else if (info.getFlag() == 0) {
				// 新增
				indexWriter.addDocument(doc);
			} else if (info.getFlag() == 2) {
				// 删除
				Term term = new Term(Constans.FIELD_HTMLPATH, path);
				indexWriter.deleteDocuments(term);
			}
		} catch (IOException e) {
			logger.error("索引增删改具体操作异常：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * @Title: queryDetailList
	 * @Description: 以关键字查询,标题或者内容匹配
	 * @param @param queryString
	 * @param @return 设定文件
	 * @return ArrayList<EnableInfo> 返回类型
	 * @throws
	 */
	public static List<CommEntity> queryDetailList(String queryString) {
		List<CommEntity> list = new ArrayList<CommEntity>();
		IndexReader ir = null;
		IndexSearcher searcher = null;
		try {
			ir = IndexReader.open(fsDirect);
			searcher = new IndexSearcher(ir);
			// 实现与或的复合搜索,标题或者内容中含有关键字
			BooleanClause.Occur[] clauses = new BooleanClause.Occur[] {
					BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD };
			String[] fields = { Constans.FIELD_TITLE, Constans.FIELD_CONTENT };// 在几个索引中查找

			// BooleanQuery aBooleanQuery = new BooleanQuery();
			// QueryParser qp_tit = new QueryParser(Version.LUCENE_36,
			// Constans.FIELD_TITLE ,aly);
			// QueryParser qp_con = new QueryParser(Version.LUCENE_36,
			// Constans.FIELD_CONTENT ,aly);
			// Query query_tit = qp_tit.parse(queryString);
			// Query query_con = qp_con.parse(queryString);
			// aBooleanQuery.add(query_tit, Occur.SHOULD);
			// aBooleanQuery.add(query_con, Occur.SHOULD);

			// 组装查询条件
			Query query = MultiFieldQueryParser.parse(Version.LUCENE_36,
					queryString, fields, clauses, aly);
			// 评分降序，评分一样时后索引的排前面
			Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE,
					new SortField(null, SortField.DOC, true) });

			// 进行查询
			TopDocs topDocs = searcher.search(query, 999999999, sort);

			// int totalNum = topDocs.totalHits;
			// 输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			list = forDataService(ir, searcher, query, scoreDocs);
		} catch (Exception e) {
			logger.error("搜索操作异常(在标题和内容内搜索)：" + e);
			e.printStackTrace();
		} finally {
			try {
				searcher.close();
				ir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * @Title: queryDetailList
	 * @Description: 以关键字查询,标题匹配,内容无所谓
	 * @param @param queryString
	 * @param @return 设定文件
	 * @return ArrayList<EnableInfo> 返回类型
	 * @throws
	 */
	public static List<CommEntity> queryDetailList_tit(String queryString) {
		List<CommEntity> list = new ArrayList<CommEntity>();
		IndexReader ir = null;
		IndexSearcher searcher = null;
		try {
			ir = IndexReader.open(fsDirect);
			searcher = new IndexSearcher(ir);
			// title中必须含关键字，内容中有没有无所谓
			BooleanClause.Occur[] clauses_tit = new BooleanClause.Occur[] {
					BooleanClause.Occur.MUST, BooleanClause.Occur.SHOULD };
			String[] fields = { Constans.FIELD_TITLE, Constans.FIELD_CONTENT };// 在几个索引中查找

			// 组装查询条件
			Query query_tit = MultiFieldQueryParser.parse(Version.LUCENE_36,
					queryString, fields, clauses_tit, aly);

			// 评分降序，评分一样时后索引的排前面
			Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE,
					new SortField(null, SortField.DOC, true) });

			// 进行查询
			TopDocs topDocs_tit = searcher.search(query_tit, 999999999, sort);

			// int totalNum_tit = topDocs_tit.totalHits;
			// 输出结果
			ScoreDoc[] scoreDocs_tit = topDocs_tit.scoreDocs;
			list = forDataService(ir, searcher, query_tit, scoreDocs_tit);
		} catch (Exception e) {
			logger.error("搜索操作异常(在标题内搜索)：" + e);
			e.printStackTrace();
		} finally {
			try {
				searcher.close();
				ir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * @Title: queryDetailList
	 * @Description: 以关键字查询,标题不匹配内容必须匹配
	 * @param @param queryString
	 * @param @return 设定文件
	 * @return ArrayList<EnableInfo> 返回类型
	 * @throws
	 */
	public static List<CommEntity> queryDetailList_conn(String queryString) {
		List<CommEntity> list = new ArrayList<CommEntity>();
		IndexReader ir = null;
		IndexSearcher searcher = null;
		try {
			ir = IndexReader.open(fsDirect);
			searcher = new IndexSearcher(ir);
			// title中无关键字，内容中有关键字
			BooleanClause.Occur[] clauses_con = new BooleanClause.Occur[] {
					BooleanClause.Occur.MUST_NOT, BooleanClause.Occur.MUST };
			// 在几个索引中查找
			String[] fields = { Constans.FIELD_TITLE, Constans.FIELD_CONTENT };

			// 组装查询条件
			Query query_con = MultiFieldQueryParser.parse(Version.LUCENE_36,
					queryString, fields, clauses_con, aly);

			// 评分降序，评分一样时后索引的排前面
			Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE,
					new SortField(null, SortField.DOC, true) });

			// 进行查询
			TopDocs topDocs_con = searcher.search(query_con, 999999999, sort);

			// int totalNum_con = topDocs_con.totalHits;
			// 输出结果
			ScoreDoc[] scoreDocs_con = topDocs_con.scoreDocs;
			list = forDataService(ir, searcher, query_con, scoreDocs_con);
		} catch (Exception e) {
			logger.error("搜索操作异常(在内容内搜索)：" + e);
			e.printStackTrace();
		} finally {
			try {
				searcher.close();
				ir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * @Title: forDataService
	 * @Description: 对搜索的结果集做排序高亮处理
	 * @param @param ir
	 * @param @param searcher
	 * @param @param query
	 * @param @param scoreDocs
	 * @param @return 设定文件
	 * @return List<CommEntity> 返回类型
	 * @throws
	 */
	public static List<CommEntity> forDataService(IndexReader ir,
			IndexSearcher searcher, Query query, ScoreDoc[] scoreDocs) {
		List<CommEntity> list = new ArrayList<CommEntity>();
		/**
		 * Formatter:设置高亮器的格式,无参构造函数表示使用<b>标签
		 * Scorer:Highlighter需要知道哪些关键词是需要高亮的,需要查询条件
		 * "<span class=\"highlighter\">", "</span>"
		 */
		Formatter formatter = new SimpleHTMLFormatter(
				"<font class=\"hightKeyWord\">", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		// 除了上面两个以外,还需要生成一段摘要,以便搜索的时候显示,指定摘要的大小为20个字符
		Fragmenter fragmenter = new SimpleFragmenter(130);
		highlighter.setTextFragmenter(fragmenter);
		try {
			for (int i = 0; i < scoreDocs.length; i++) {
				Document targetDoc = searcher.doc(scoreDocs[i].doc);
				String titleToBeHightlight = targetDoc.get("title");
				String contentToBeHightlight = targetDoc.get("content");
				if (titleToBeHightlight == null)
					titleToBeHightlight = "";
				if (contentToBeHightlight == null)
					contentToBeHightlight = "";
				TokenStream tokenStreamTitle = aly.tokenStream("title",
						new StringReader(titleToBeHightlight));
				TokenStream tokenStreamContent = aly.tokenStream(
						"content", new StringReader(contentToBeHightlight));
				String title = highlighter.getBestFragment(tokenStreamTitle,
						titleToBeHightlight);
				String content = highlighter.getBestFragment(
						tokenStreamContent, contentToBeHightlight);
				if (null == title) {
					title = titleToBeHightlight;
				}
				if (null == content) {
					content = contentToBeHightlight.substring(0, 20)
							.replaceAll(" ", "...");
				}
				CommEntity ce = new CommEntity();
				ce.setTitle(title);
				ce.setHtmlPath(targetDoc.get("htmlPath"));
				ce.setContent(content);
				ce.setTime(targetDoc.get("time"));
				list.add(ce);
			}
		} catch (Exception e) {
			logger.error("搜索结果高亮排序操作异常：" + e);
			e.printStackTrace();
		} finally {
			try {
				searcher.close();
				ir.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
