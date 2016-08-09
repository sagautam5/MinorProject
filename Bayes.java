import java.io.*;
import java.util.*;
class WordDetails{
    ArrayList<String> nWord = new ArrayList<String>();
    ArrayList<Integer> nOccurance = new ArrayList<Integer>();
WordDetails()
{
}
void Display()
    {
    for(int k=0;k<nWord.size();k++)
        {   
        System.out.println(nWord.get(k)+""+nOccurance.get(k));
        }
    }
void makeDistinct()
    {
    for(int x=nWord.size()-1;x>=0;x--)
        for(int k=0;k<nWord.size()-1;k++)
            if(nWord.get(x).equals(nWord.get(k)))
                nWord.remove(x);
    }
WordDetails(WordDetails wordDetails){
        this.nWord = wordDetails.nWord;
        this.nOccurance = wordDetails.nOccurance;
}

}
class Data{
    int Wcounts = 0;
    int Vcounts = 0;
    int Lcounts = 0;
    WordDetails WD = new WordDetails();

    ArrayList<String> nTraindata = new ArrayList<String>();
    ArrayList<String> nNew1 = new ArrayList<String>();
    ArrayList<String> nNew2 = new ArrayList<String>();
    

Data(String fileName)
    {
    File file = new File(fileName);
    String Word;
    try{
        Scanner scanFile = new Scanner(new FileReader(file));
    while(scanFile.hasNext()){
    Word = scanFile.next();
    nTraindata.add(Word);
    Wcounts++;
    }
        }catch(Exception e){
         System.out.println("error while loading from file "+e.getMessage());
    }
//vocabulary counting
    nNew1 = nTraindata;
    nNew2 = nTraindata;
    for(int x=0;x<nNew1.size();x++){
        for(int y=x+1;y<nNew1.size();y++)
            {
            if(nNew1.get(x).equals(nNew1.get(y)))
                nNew1.remove(y);
            }
        for(int i=0;i<nNew1.size();i++)
            {
                WD.nWord.add(nNew1.get(i));
                WD.nOccurance.add(1);
            }
    }
           Vcounts = nNew1.size();
    //...
    //occurance count
    int occ = 1;
    int p=0,q=0;
        while(q<nNew1.size())
        {
             occ = Collections.frequency(nNew2 , nNew1.get(q));
             WD.nWord.set(q, nNew1.get(q));
             WD.nOccurance.set(q, occ);
            q++;
        }
   //calculating no of lines
    try{
        FileReader inputFile = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(inputFile);
    for(String X = bufferedReader.readLine();X!=null;X=bufferedReader.readLine())
        Lcounts++;
    }catch(Exception e1){
    System.out.println("Error while loading file "+e1.getMessage());
    }
    }
void Display()
    {
   // System.out.println("Word counted:"+Wcounts);
     //   System.out.println("Vocabulary counted: "+Vcounts);
       // System.out.println("Line Counts:"+Lcounts);
       // WD.Display();
      //  distinctWD.Display();
    }
}
class BayesProb
{
double cookPrior;
double managerPrior;
double cProb[];
double mProb[];
int cookVcounts,managerVcounts;
int cookWcounts,managerWcounts;
int tweetWcounts;
double condCook,condManager;
WordDetails cworddetails = new WordDetails();
WordDetails mworddetails = new WordDetails();
WordDetails tworddetails = new WordDetails();
WordDetails totaldetails = new WordDetails();
BayesProb(int nC,int nM)
    {
    cookPrior = (double)nC/(nC+nM);
    managerPrior= (double)nM/(nC+nM);
    }
void setVoc(int nCV,int nMV)
    {
    cookVcounts = nCV;
    managerVcounts = nMV;
    }
void setWord(int nCW,int nMW)
    {
    cookWcounts = nCW;
    managerWcounts = nMW;
    }
void setCWorddetails(WordDetails wd)
    {
   cworddetails = new WordDetails(wd);
    }
void setMWorddetails(WordDetails wd)
    {
    mworddetails = new WordDetails(wd);
    }
void setTweetdata(WordDetails Tw,int size)
    {
    tworddetails = new WordDetails(Tw);
    tweetWcounts = size;
    mProb = new double[tweetWcounts];
    cProb = new double[tweetWcounts];
    }
void setTotaldata(){
}
void Display()
    {
     //    System.out.println("Prior Probabilities are: ");
  //     System.out.println("cookPrior:"+cookPrior);
    //     System.out.println("managerPrior:"+managerPrior);
       /* for(int i=0;i<cookVcounts;i++) 
            System.out.println(cworddetails.Word[i]+":"+cworddetails.Occurance[i]);
        System.out.println();
        System.out.println();
        System.out.println();
        for(int i=0;i<managerVcounts;i++)
            System.out.println(mworddetails.Word[i]+":"+mworddetails.Occurance[i]);
     //   tworddetails.makeDistinct();*/
   //     tworddetails.Display();
    }
double Prob(String Str,char C){

double prob=1;
if(C=='M'){
        for(int i=0;i<managerWcounts;i++)
           if(Str.equals(mworddetails.nWord.get(i))){
                prob = mworddetails.nOccurance.get(i)+1;
           }
                prob = prob/(managerWcounts+managerVcounts+cookVcounts);
              //  System.out.println(prob);  
            }
else if(C=='C'){
            for(int j=0;j<cookWcounts;j++)
                if(Str.equals(cworddetails.nWord.get(j))){
                    prob = cworddetails.nOccurance.get(j)+1;
                }
                    prob = prob/(cookWcounts+cookVcounts+managerVcounts);
                //    System.out.println(prob);
                }
return prob;
}
int Conditional()
    {
        int classifier = 0 ;
for(int i=0;i<tweetWcounts;i++)
    {
        mProb[i] = this.Prob(tworddetails.nWord.get(i),'M');
        cProb[i] = this.Prob(tworddetails.nWord.get(i),'C');
    }
    
condCook    = cookPrior;
condManager = managerPrior;
//System.out.println(tweetWcounts);
for(int i=0;i<tweetWcounts;i++)
    {
    condCook = condCook*cProb[i];
    condManager = condManager*mProb[i];
    }
condCook = 10000*condCook;
condManager = 10000*condManager;
//System.out.println(condCook+"  "+condManager);
if(condCook>condManager){
    classifier = 1;
    }
else{
    classifier = 0;
    }
return classifier;
    }
}
class Bayes{
    public static void naiveBayes(Tokenizer[] T,int Size)
    { 
    String Mfile = "nmanagerdata.txt";
    String Cfile = "ncookdata.txt";
    Data managerData = new Data(Mfile);
    Data cookData = new Data(Cfile);
    managerData.Display();
   // System.out.println();
   // System.out.println();
   // System.out.println();
    cookData.Display();
    BayesProb BP = new BayesProb(cookData.Lcounts,managerData.Lcounts);
    BP.setVoc(cookData.Vcounts,managerData.Vcounts);
    BP.setWord(cookData.Wcounts,managerData.Wcounts);
    BP.setMWorddetails(managerData.WD);
    BP.setCWorddetails(cookData.WD);
    BP.setTotaldata();
    ArrayList<String>  nTweet = new ArrayList<String>();
    ArrayList<Integer> nCook  = new ArrayList<Integer>();
    int x = 0;
    int TWcounts = 0;
    int size = Size;
    WordDetails XD = new WordDetails();
    
    while(x<size){
    nTweet=T[x].nFinal;
    XD.nWord=nTweet;
    
    BP.setTweetdata(XD,nTweet.size());
    nCook.add(BP.Conditional());
    // BP.Display();
        if(nCook.get(x)==1){
            for(int p=0;p<T[x].nTweet.size();p++)
        System.out.print(T[x].nTweet.get(p)+" ");
        System.out.println();
        System.out.println(" : Attention Cook!!!");
         }
        else{
            for(int q=0;q<T[x].nTweet.size();q++)
        System.out.print(T[x].nTweet.get(q)+" ");
        System.out.println();
        System.out.println(" : Attention Manager!!!");
        }
        x++;
    }
    }
public static void main(String[] args){

	String stopword = "newstopwords.txt";
	StopWordRemove StopRemover = new StopWordRemove(stopword);
ArrayList<String> Tweets = new ArrayList<String>();
Tokenizer Twitter[] = new Tokenizer[50];
int z=0,n=0;
Scanner S = new Scanner(System.in);
System.out.println("Enter the number of Tweets !!");
z = S.nextInt();
while(n<z){
Twitter[n] = new Tokenizer();
Twitter[n].SpellingCorrect(Twitter[n].nWords);
int x;
x = Twitter[n].Remover(StopRemover.nSWords);
Twitter[n].Stemming(x);
System.out.println();
Twitter[n].SpellingCorrect(Twitter[n].nFinal);
Tweets = Twitter[n].nFinal;
//for(int i=0;i<Twitter[n].nFinal.size();i++)
//  System.out.println(Twitter[n].nFinal.get(i));
n++;
}
/*for(int i=0;i<z;i++){
    for(int j=0;j<Twitter[i].nFinal.size();j++)
System.out.println(Twitter[i].nFinal.get(j));
}*/
naiveBayes(Twitter,z);
}
}
