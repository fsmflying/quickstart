package com.fsmflying.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.*;

import java.io.IOException;
import java.nio.file.Paths;

public class Startup {
    public static void main(String[] args){
        String indexPath = "";
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            IndexWriter iw = new IndexWriter(dir,iwc);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
