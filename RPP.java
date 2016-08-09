import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
class WagnerFischer
    {
     public int getLevenshteinDistance(String str1, String str2)
     {   
       //  System.out.println(str1+" "+str2);
         int len1;
         len1 = str1.length();
         int len2;
         len2 = str2.length();
         int[][] arr = new int[len1 + 1][len2 + 1];
             for (int i = 0; i <= len1; i++)
                   arr[i][0] = i;
             for (int i = 1; i <= len2; i++)
                   arr[0][i] = i;
             for (int i = 1; i <= len1; i++)
                   {
                for (int j = 1; j <= len2; j++)
                   {
                  int m = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0:1;                        
                   arr[i][j] = Math.min(Math.min(arr[i - 1][j] + 1, arr[i][j - 1] + 1), arr[i - 1][j - 1] + m);
                    }
                    }
    return arr[len1][len2];
     }
    }


class Tokenizer{
	int Wcounts = 0;
	int Lcounts = 0;
    int Fcounts = 0;
    int Tcounts = 0;
    ArrayList<String> nLines = new ArrayList<String>();
    ArrayList<String> nWords = new ArrayList<String>();
    ArrayList<String> nFinal = new ArrayList<String>();
    ArrayList<String> nTweet = new ArrayList<String>();
	String Symbols = " \t\n\r\f,.:;?![]'";
    Tokenizer()
        {
            String Tweet;
            Scanner in = new Scanner(System.in);
         try{
            System.out.println("Enter a Tweet");
                    Tweet = in.nextLine();
                    
            String[] tokens = Tweet.split(" ");
            for(String token : tokens)
            {
            nWords.add(token);
            nTweet.add(token);
            Tcounts++;
            Wcounts++;
            }
         }catch(Exception e){
         System.out.println("Error reading your Tweet "+e.getMessage());
         }
        }
    Tokenizer(String fileName)
        {

     try{

          FileReader inputFile = new FileReader(fileName);
          BufferedReader bufferedReader = new BufferedReader(inputFile);
        for(String X = bufferedReader.readLine();X!=null;X = bufferedReader.readLine())
        {
        nLines.add(X);
    	Lcounts++;
                
        String[] tokens = X.split(" ");
                    for (String token : tokens)
                    {
                        nWords.add(token);
			            Wcounts++;
                    }                

	    }	 
        }catch(Exception e){
	System.out.println("Error while reading line by line:"+e.getMessage());
	}
	}
void displayLine()
 	    {
        for(int i=0;i<Lcounts;i++)
            System.out.println(nLines.get(i));
	    }
void displayWord()
        {
	        for(int i=0;i<Fcounts;i++)
            System.out.println(nFinal.get(i));
	    }
public void Stemming(int x)
    {
	for(int i=0;i<x;i++)
	{
		Porter P = new Porter();
        nWords.set(i,P.stripAffixes(nWords.get(i)));
	}
    }
public int Remover(ArrayList<String> arr)
    {
    ArrayList<String> stopwords = new ArrayList<String>();
    stopwords = arr;
    ArrayList<String> filewords = new ArrayList<String>();
    filewords = nWords;
    Iterator<String> it = filewords.iterator();
    while(it.hasNext())
        if(stopwords.contains(it.next()))
            it.remove();
    int N = filewords.size();
    nFinal.clear();
    int x = filewords.size();
    nWords = filewords;
    return x;
    }
    
public void SpellingCorrect(ArrayList<String> arr)
    {
    ArrayList<String> nDictionary = new ArrayList<String>();
    int Dsize = 0;
    String dictName = "wordlist.txt";
    try{
        
        FileReader inputFile = new FileReader(dictName);
        BufferedReader bufferedReader = new BufferedReader(inputFile);
        for(String X = bufferedReader.readLine();X!=null;X=bufferedReader.readLine())
        {
        String[] tokens = X.split(" ");
        for(String token : tokens)
            {
                nDictionary.add(token);
                Dsize++;
            }
        }
    }catch(Exception e){
            System.out.println();
   // System.out.println("Error while reading from file"+e.getMessage());
    }
   
    ArrayList<String> Suggest = new ArrayList<String>();
    for(int p=0;p<arr.size();p++)
    {
    int editDist=100;
    WagnerFischer wr = new WagnerFischer();
    String temp = new String();
    for(int q=0;q<Dsize;q++)
        {
        
           int x = wr.getLevenshteinDistance(arr.get(p),nDictionary.get(q));
           if(x<2)
           {
     //      System.out.println("Suggestion:"+Dictionary[q]);
           Suggest.add(nDictionary.get(q));
           }
            if(x<editDist)
                {
                temp = nDictionary.get(q);
                editDist = x;
                }
        }
    arr.set(p,temp);
  //  System.out.println(arr.get(p));
    }
    nWords = arr;
    }
}
class StopWordRemove{
    int SWcounts = 0;
    ArrayList<String> nSWords = new ArrayList<String>();
    StopWordRemove(String fileName)
    {
    
        try{
       FileReader inputFile = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(inputFile);
        for(String Y = bufferedReader.readLine();Y!=null;Y=bufferedReader.readLine())
        {
        
        String[] tokens = Y.split(" ");
        for(String token : tokens)
             {
                nSWords.add(token);
                SWcounts++;
      //          System.out.println(token);
             }
        }
        }catch(Exception e){
            System.out.println();
       // System.out.println("Error while Reading From File"+ e.getMessage());
        }
    }
    void displaySWords()
    {
        System.out.println(SWcounts);
        for(int j=0;j<SWcounts;j++)
            System.out.println(nSWords.get(j));
    }
    ArrayList ReturnList()
	{
	return nSWords;
	}
}
public class RPP{ 
    
    public static void main(String[] args){

	String stopword = "newstopwords.txt";
	StopWordRemove StopRemover = new StopWordRemove(stopword);
    ArrayList<String> Tweets = new ArrayList<String>();
//	System.out.println("Reading File from Java code");
//	String fileName="sagar.txt";

   // Scanner in = new Scanner(System.in);
   // z = in.nextInt();
   // Tokenizer tokenizer = new Tokenizer(fileName);
   /* if(z==1){
	System.out.println();
	tokenizer.Stemming();
	tokenizer.Remover(StopRemover.nSWords);
    System.out.println();
    tokenizer.SpellingCorrect(tokenizer.nFinal);
	tokenizer.displayWord();
    }
    }*/
    Tokenizer Twitter[]=new Tokenizer[50];
    int z=0,n=0;
    Scanner S = new Scanner(System.in);
    z = S.nextInt();
    while(n<z){
        Twitter[n]= new Tokenizer();
    Twitter[n].SpellingCorrect(Twitter[n].nWords);
 int y = Twitter[n].Remover(StopRemover.nSWords);
	Twitter[n].Stemming(y);
    System.out.println();
    Twitter[n].SpellingCorrect(Twitter[n].nFinal);
    Tweets = Twitter[n].nFinal;
    for(int i=0;i<Twitter[n].nFinal.size();i++)
        System.out.println(Twitter[n].nFinal.get(i));
    n++;
    }
    }
    }
