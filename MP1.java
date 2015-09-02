
import java.io.*;
import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Comparator;

public class MP1 {

    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
        "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
        "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
        "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
        "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
        "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
        "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
        "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
        "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
        "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        //Apologies for the code. First time to program in Java. It has been fun!
        String[] ret = new String[20];
        String[] all = new String[500000];
        ArrayList<String> records = new ArrayList<String>();
        BufferedReader br = null;
        ArrayList<String> list = new ArrayList<String>();
        //read in the file
        try {
            String line;
            br = new BufferedReader(new FileReader(inputFileName));
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            System.err.println("unable to read from file");
            System.exit(1);
        }

        //iterate through the records array with the correct Indexes
        Integer[] myindex = getIndexes();
        String mys = null;
        for (Integer i : myindex) {
            mys = records.get(i);
            StringTokenizer st = new StringTokenizer(mys, delimiters);
            while (st.hasMoreElements()) {
                String myelements = st.nextElement().toString().toLowerCase().trim();
                boolean checkContains = false;
                for (String s : stopWordsArray) {
                    checkContains = myelements.equals(s);
                    if (checkContains) {
                        checkContains = true;
                        break;
                    }
                }
                if (!checkContains) {
                    list.add(myelements);
                }
                checkContains = false;

            }//Finish the delimeters and stopWordsArray

        }//finish my index loop
        //Count the frequency of each entry in list and store in my TreeHash.
        //Treehash sorts using natural ordering, hence alphabetic order.
        Map<String, Integer> map = new TreeMap<String, Integer>();
        for (String temp : list) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }

        //Create a linked list to save the order.
        List<Map.Entry<String, Integer>> entries = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        //Sort the frequencies
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        //return the top 20 values
        Set<Entry<String, Integer>> s = sortedMap.entrySet();
        Iterator it = s.iterator();
        int i = 0;
        while (it.hasNext() && i < 20) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = (String) pairs.getKey();
            int value = (int) pairs.getValue();
            ret[i] = key;
            i = i + 1;
        }
        //for (String temp: entries){
        //      System.out.println(entries);
        //}

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }
}
