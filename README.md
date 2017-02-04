# nlp
自然语言处理 不断完善中......

句法分析。先初始化resources/nlp/nlp.sql，然后启动web服务（com.xb.ApplicationMain），输入http://localhost:8080/views/sytanx_tree.html  用户：admin 密码：admin 查看。<br/>
com.xb.business.hmm.builderImpl.TestCYK   

基于隐马尔科夫模型拼音输入转汉字<br/>
com.xb.business.hmm.builderImpl.TestPinyingToHanzi  

基于隐马尔科夫模型词性标注<br/>
com.xb.business.hmm.builderImpl.TestWordTag   

分词<br/>
com.xb.summary.TestKeywordExtraction  

文章摘要<br/>
com.xb.summary.TestSentencesExtraction  

新词发现<br/>
com.xb.newword.TestNewWord  

字符串的相似性<br/>
com.xb.text.TestCosineSimilar   

字符串的相似性<br/>
com.xb.text.TestSimHashTextSimilarity

词频统计<br/>
com.xb.text.TestTfidf

手写数字识别<br/>
com.xb.mnist.TestMnist
