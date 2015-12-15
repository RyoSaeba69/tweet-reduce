package tweetreduce;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by antoine on 12/14/15.
 */

import org.apache.hadoop.io.Text;

public class LexicalWord {

        private static  LexicalWord lexicalWord;
        private ArrayList<String> happyWords;
    private ArrayList<String> sadWords;


        public LexicalWord() {

            happyWords = new ArrayList<String>();
            happyWords.add("happy");
            happyWords.add("great");
            happyWords.add("nice");
            happyWords.add("joy");
            happyWords.add("good");
            happyWords.add("happiness");

            sadWords = new ArrayList<String>();
            sadWords.add("sad");
            sadWords.add("bad");
            sadWords.add("ugly");
            sadWords.add("nothing");
            sadWords.add("desperate");
            sadWords.add("deception");


        }

        public static LexicalWord getInstance(){
            LexicalWord lw;
            if(lexicalWord != null){
                lw = lexicalWord;
            } else {
                lw = new LexicalWord();
            }
            return lw;
        }


    public ArrayList<String> getHappyWords() {
        return happyWords;
    }

    public ArrayList<String> getSadWords() {
        return this.sadWords;
    }
}
