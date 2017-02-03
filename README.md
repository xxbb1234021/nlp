# nlp
自然语言处理 不断完善中......

句法分析。先初始化resources/nlp/nlp.sql，然后启动web服务（com.xb.ApplicationMain），输入http://localhost:8080/views/sytanx_tree.html  用户：admin 密码：admin 查看。
com.xb.business.hmm.builderImpl.TestCYK   

基于隐马尔科夫拼音输入转汉字
com.xb.business.hmm.builderImpl.TestPinyingToHanzi  

基于隐马尔科夫词性标注
com.xb.business.hmm.builderImpl.TestWordTag   

分词
com.xb.summary.TestKeywordExtraction  

文章摘要
com.xb.summary.TestSentencesExtraction  

新词发现
com.xb.newword.TestNewWord  

字符串的相似性
com.xb.text.TestCosineSimilar   

 字符串的相似性
com.xb.text.TestSimHashTextSimilarity

com.xb.text.TestTfidf
