# coding: utf-8
import os
from copy import copy

import pandas as pd
import re
import jieba
import jieba.posseg as psg
import numpy as np
import matplotlib.pyplot as plt
import joblib
import scipy.sparse
import xlutils.copy
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from scipy import sparse
import sys
import xlrd as xl
import xlwt


output_path = 'C:/Users/86198/Desktop/LDA_Code/testResult'
data=pd.read_excel("C:\\Users\\86198\\Desktop\\news\\data.xlsx")#content type
#data=pd.read_excel("C:\\Users\\86198\\Desktop\\OurProject\\NewsCrawler\\data.xlsx")#content type
os.chdir(output_path)
dic_file = "C:/Users/86198/Desktop/LDA_Code/stop_dic/dict.txt"
stop_file = "C:/Users/86198/Desktop/LDA_Code/stop_dic/stopwords.txt"

def chinese_word_cut(mytext):
    jieba.load_userdict(dic_file)
    jieba.initialize()
    try:
        stopword_list = open(stop_file, encoding='utf-8')
    except:
        stopword_list = []
        print("error in stop_file")
    stop_list = []
    flag_list = ['n', 'nz', 'vn']
    for line in stopword_list:
        line = re.sub(u'\n|\\r', '', line)
        stop_list.append(line)

    word_list = []
    # jieba分词
    seg_list = psg.cut(mytext)
    for seg_word in seg_list:
        word = re.sub(u'[^\u4e00-\u9fa5]', '', seg_word.word)
        # word = seg_word.word  #如果想要分析英语文本，注释这行代码，启动下行代码
        find = 0
        for stop_word in stop_list:
            if stop_word == word or len(word) < 2:  # this word is stopword
                find = 1
                break
        if find == 0 and seg_word.flag in flag_list:
            word_list.append(word)
    return (" ").join(word_list)



if __name__ == '__main__':

    data["content_cutted"] = data.text.apply(chinese_word_cut)
    tf_vectorizer = CountVectorizer(strip_accents='unicode', stop_words='english', max_features=1000)  # 0.5 10
    tf = tf_vectorizer.fit_transform(data.content_cutted)
    lda = joblib.load('C:\\Users\\86198\\Desktop\\LDA_Code\\testResult\\lda.pkl')


    # ### 2.2输出每篇文章对应主题
    topics=lda.transform(tf)

    topic = []
    for t in topics:
        topic.append("Topic #"+str(list(t).index(np.max(t))))
    data['概率最大的主题序号']=topic
    data['每个主题对应概率']=list(topics)
    data.to_excel("newdata.xlsx",index=False)

    print("end!")
