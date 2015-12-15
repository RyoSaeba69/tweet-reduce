package tweetreduce;


import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {

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

    public static void main(String[] args) throws Exception {

        if(args.length < 2){
            System.err.println("Usage: wordcount <search> <time> <out>");
            System.exit(2);
        }

        String searchTerms = args[0];
        long timeToUse = Long.valueOf(args[1]);

        TwitterStreamConsumer tsc = new TwitterStreamConsumer(searchTerms, timeToUse);

        tsc.start();
        tsc.join();

        Configuration conf = new Configuration();
//        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        FileSystem fs = FileSystem.newInstance(conf);

//        if (otherArgs.length != 2) {
//            System.err.println("Usage: wordcount <in> <out>");
//            System.exit(2);
//        }

        String inputPath = "res.txt";

        String outPath = "hadout";
        if(args.length > 2){
            outPath = args[2];
        }



        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
