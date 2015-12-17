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


    public static void main(String[] args) throws Exception {

//        Type could be : happy or words
        if(args.length < 3){
            System.err.println("Usage: tweet-reduce-1.0 <type> <search> <time> <out>");
            System.exit(2);
        }

        String type = args[0].toLowerCase();
        String searchTerms = args[1];
        long timeToUse = Long.valueOf(args[2]);


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
        if(args.length > 3){
            outPath = args[3];
        }



        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);


        if(type.equals("happy")){
            job.setMapperClass(HappyCount.TokenizerMapper.class);
            job.setCombinerClass(HappyCount.IntSumReducer.class);
            job.setReducerClass(HappyCount.IntSumReducer.class);
        } else if(type.equals("words")){
            job.setMapperClass(WordsCount.TokenizerMapper.class);
            job.setCombinerClass(WordsCount.IntSumReducer.class);
            job.setReducerClass(WordsCount.IntSumReducer.class);
        }




        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outPath));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
