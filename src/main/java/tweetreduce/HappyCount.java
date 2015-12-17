package tweetreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by antoine on 12/17/15.
 */
public class HappyCount {
    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private LexicalWord lw = LexicalWord.getInstance();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());


            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());

                if(lw.getHappyWords().contains(word.toString().toLowerCase())){
                    context.write(new Text("Happy"), one);
                } else if(lw.getSadWords().contains(word.toString().toLowerCase())){
                    context.write(new Text("Sad"), one);

                }


//                if (word.equals(happy))// || word == "satisfied" || word == "joyful" || word == "joyous" || word == "chherful" || word == "contented" || word == "delighted" || word == "ecstatic" || word == "depressed" || word == "disturbed" || word == "sad" || word == "sadness" || word == "upset" || word == "unhappy" || word == "troubled" || word == "disappointed")
//                {
//                    context.write(new Text(":)"), one);
//                } else if (word.equals(sad)){
//                    context.write(new Text(":("), one);
//                }
            }
        }
    }


    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

}
