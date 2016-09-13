# IR_System

This project consists of an implementation of an Information Retrieval system, in Java, using the Lucene library. 
The folder "npl" contains the following files :
  - common_words : the common words that should not be considered as valid words for the search (such as "a", "to", "from" etc)
  - doc-text : the documents (number and summary)
  - query-text : some example queries
  - rlv-ass : the relevant documents concerning the queries above
  
Apart from the standard word search (Google-like), which is based on a custom analyzer using the PorterStem filter (which is a 
build-in Lucene's EnglishAnalyzer), returning the number and summary of the documents as results, the user can check the 
recall-accuracy diagrams concerning the queries of the "query-text" file. Additionally the user can extend the query, in order 
to find the best query possible to get his/her desired results, but that functionality is incomplete and is to be implemented soon.
